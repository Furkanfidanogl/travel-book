package com.furkanfidanoglu.travelbook.view;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.room.Room;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.furkanfidanoglu.travelbook.R;
import com.furkanfidanoglu.travelbook.model.Place;
import com.furkanfidanoglu.travelbook.roomdb.PlaceDao;
import com.furkanfidanoglu.travelbook.roomdb.PlaceDataBase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.furkanfidanoglu.travelbook.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    ActivityResultLauncher<String> permissionLauncher;
    LocationManager locationManager;
    LocationListener locationListener;
    SharedPreferences sharedPreferences;
    boolean info;
    Double targetLatitude, targetLongitude;
    Intent intent;
    Place listedPlace;
    PlaceDataBase db;
    PlaceDao placeDao;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        binding.saveBtn.setEnabled(false);

        intent = getIntent();

        sharedPreferences = this.getSharedPreferences("com.furkanfidanoglu.travelbook", MODE_PRIVATE);
        info = sharedPreferences.getBoolean("info", false);

        registerLauncher();

        db = Room.databaseBuilder(getApplicationContext(), PlaceDataBase.class, "Places").build();
        placeDao = db.placeDao();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
        mMap.clear();

        String isNewOrOld = intent.getStringExtra("isNewOrOld");
        //Menü'den geliyor
        if (isNewOrOld.equals("new")) {
            binding.saveBtn.setVisibility(View.VISIBLE);
            binding.deleteBtn.setVisibility(View.GONE);

            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

                    mMap.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(userLocation, 13),
                            3000,
                            null
                    );
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(userLocation).title("Buradasın"));
                }

                @Override
                public void onProviderEnabled(@NonNull String provider) {
                    Toast.makeText(MapsActivity.this, "Konum servisi açıldı", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onProviderDisabled(@NonNull String provider) {
                    Toast.makeText(MapsActivity.this, "Konum servisi kapalı", Toast.LENGTH_SHORT).show();
                    openLocationSettings();
                }
            };

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                checkLocationPermission();
            }
        }
        //Listeden geliyor
        else {
            listedPlace = (Place) intent.getSerializableExtra("place");
            LatLng userLocation = new LatLng(listedPlace.latitude, listedPlace.longitude);

            binding.saveBtn.setVisibility(View.GONE);
            binding.deleteBtn.setVisibility(View.VISIBLE);
            binding.placeName.setText(listedPlace.name);

            mMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(userLocation, 13),
                    3000,
                    null
            );
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(userLocation).title(listedPlace.name));
        }
    }

    private void openLocationSettings() {
        Snackbar.make(binding.getRoot(), "Konum servisi kapalı. Açmak ister misiniz?", Snackbar.LENGTH_INDEFINITE)
                .setAction("AÇ", v -> {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                })
                .show();
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Snackbar.make(binding.getRoot(), "Konum izni gerekli", Snackbar.LENGTH_INDEFINITE)
                        .setAction("İzin Ver", v -> permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION))
                        .show();
            } else {
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            }
        } else {
            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        mMap.clear();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            try {
                if (locationListener != null) {
                    locationManager.removeUpdates(locationListener);
                }

                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        4000,
                        10,
                        locationListener
                );

                Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastLocation != null) {
                    LatLng userLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 12));
                    mMap.addMarker(new MarkerOptions().position(userLocation).title("Buradasınnn"));
                } else {
                    LatLng defaultLatLng = new LatLng(0, 0);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLatLng, 1));
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Konum alınamadı", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void registerLauncher() {
        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                granted -> {
                    if (granted) {
                        startLocationUpdates();
                    } else {
                        Snackbar.make(binding.getRoot(),
                                        "Konum izni olmadan harita kullanılamaz",
                                        Snackbar.LENGTH_INDEFINITE)
                                .setAction("İzin Ver", v -> permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION))
                                .show();
                    }
                }
        );
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).title("Hedef Bölge"));

        targetLatitude = latLng.latitude;
        targetLongitude = latLng.longitude;

        binding.saveBtn.setEnabled(true);
    }

    public void save(View view) {
        if (binding.placeName.getText().toString().equals("")) {
            Toast.makeText(this, "Lütfen bir isim giriniz", Toast.LENGTH_SHORT).show();
        } else {
            Place place = new Place(binding.placeName.getText().toString(), targetLatitude, targetLongitude);

            compositeDisposable.add(
                    placeDao.insert(place)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(MapsActivity.this::handleResponse)
            );
        }
    }

    private void handleResponse() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void delete(View view) {
        compositeDisposable.add(
                placeDao.delete(listedPlace)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(MapsActivity.this::handleResponse)
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}










# Travel Book 🗺

## 🇬🇧 English

Travel Book is an Android application that allows users to save places they visit and view them later on a map.

Users can select a location on Google Maps, save it with a title, and create their own personal travel diary.

### Features

- Save locations on the map
- View saved places
- Add titles to locations
- Persistent storage using Room / SQLite
- Google Maps integration
- Location permissions support

### Tech Stack

- Kotlin / Java
- Android Studio
- Google Maps SDK
- Room / SQLite Database
- Location Services

### How It Works

Users can long-press on the map to select a location.  
After selecting the location, they can save it with a title.  

Saved locations are stored locally and can be viewed later.

### Google Maps API Key

To run this project, you must add your own Google Maps API key.

Add the following line to your `local.properties` file:

MAPS_API_KEY=YOUR_API_KEY

---

## 🇹🇷 Türkçe

Travel Book, kullanıcıların ziyaret ettikleri yerleri kaydedip daha sonra harita üzerinde görüntüleyebilecekleri bir Android uygulamasıdır.

Kullanıcılar Google Maps üzerinde bir konum seçebilir ve bu konumu bir başlık ile kaydederek kendi seyahat günlüğünü oluşturabilir.

### Özellikler

- Harita üzerinde konum kaydetme
- Kaydedilen yerleri görüntüleme
- Konumlara başlık ekleme
- Room / SQLite ile kalıcı veri saklama
- Google Maps entegrasyonu
- Konum izinleri desteği

### Kullanılan Teknolojiler

- Kotlin / Java
- Android Studio
- Google Maps SDK
- Room / SQLite veritabanı
- Location servisleri

### Nasıl Çalışır

Kullanıcı harita üzerinde uzun basarak bir konum seçer.  
Daha sonra bu konumu bir başlık ile kaydedebilir.

Kaydedilen konumlar cihazda saklanır ve daha sonra tekrar görüntülenebilir.

### Google Maps API Key

Projeyi çalıştırmak için kendi Google Maps API anahtarınızı eklemeniz gerekir.

`local.properties` dosyasına şu satırı ekleyin:

MAPS_API_KEY=YOUR_API_KEY

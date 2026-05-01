# 🗺️ Travel Book - Personal Location Diary

> A native Android application that empowers users to create a customized travel diary. By integrating Google Maps and local database storage, users can seamlessly pin, save, document, and revisit their favorite locations directly on their mobile device.

---

## 🎯 Key Features

*   📍 **Interactive Mapping:** Effortlessly drop a pin by long-pressing on the map to select and save specific coordinates.
*   💾 **Persistent Local Storage:** Reliable and efficient data management utilizing Room/SQLite database architecture.
*   🏷️ **Custom Bookmarks:** Personalize saved locations by assigning custom titles and descriptions for easy identification.
*   🔒 **Permission Management:** Gracefully handles Android location tracking permissions to ensure user privacy and app stability.
*   🗺️ **Seamless Retrieval:** Access and view previously saved locations seamlessly on the map interface at any time.

---

## 🛠️ Technologies & Architecture

| Technology | Role | Description |
| :--- | :--- | :--- |
| 📱 **Native Android** | Platform | Core application development using Kotlin/Java and Android Studio. |
| 🌍 **Google Maps SDK** | Maps Integration | Renders interactive maps and handles geolocation tracking. |
| 🗄️ **Room / SQLite** | Local Database | Robust offline data persistence and structural querying. |
| 📡 **Location Services** | Device Hardware | Utilizes Android's location services to pinpoint user coordinates. |

---

## 💻 Local Setup & Installation

To run this project locally, follow these steps:
```bash
# 1. Clone the repository
git clone [https://github.com/Furkanfidanogl/travel-book.git](https://github.com/Furkanfidanogl/travel-book.git)

# 2. Open the project in Android Studio


⚠️ Configuration Required:
This project requires a valid Google Maps API Key to render the map.

Add the following line to your local.properties file in the root directory:

Properties
MAPS_API_KEY=YOUR_API_KEY

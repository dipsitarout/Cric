# 🏏 CricWeather Chat App

<div align="center">

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)
![Firebase](https://img.shields.io/badge/firebase-%23039BE5.svg?style=for-the-badge&logo=firebase)
![Weather API](https://img.shields.io/badge/Weather%20API-FF6B35?style=for-the-badge&logo=openweathermap&logoColor=white)
![Google Maps](https://img.shields.io/badge/Google%20Maps-4285F4?style=for-the-badge&logo=googlemaps&logoColor=white)

**The Ultimate Cricket Companion with Weather Intelligence and Social Chat Features**

[![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=24)
[![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen.svg)](https://github.com/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

</div>

---

## 📱 Overview

CricWeather Chat App is a comprehensive cricket enthusiast's companion that seamlessly combines live cricket updates, real-time weather information, and social chat features. Built with modern Android technologies, this app delivers an immersive experience for cricket fans who want to stay connected with the game, weather conditions, and fellow enthusiasts.

<div align="center">
  
  <img src="https://img.icons8.com/fluency/96/partly-cloudy-day.png" alt="Weather" width="60"/>
  <img src="https://img.icons8.com/fluency/96/chat.png" alt="Chat" width="60"/>
  <img src="https://img.icons8.com/fluency/96/google-maps-new.png" alt="Maps" width="60"/>
</div>

---

## ✨ Features

### 🔐 **Authentication System**
<img src="https://img.icons8.com/fluency/48/google-logo.png" alt="Google Auth" align="left" width="40" style="margin-right: 10px;"/>

- **Google Authentication**: Seamless login with Google Play Services
- **Firebase Auth**: Secure user authentication and session management
- **Multi-Platform Login**: Email and social media authentication support

### 🏏 **Cricket Dashboard**


- **Live Scores**: Real-time cricket match updates and scores
- **Match Analytics**: Detailed match statistics and player performance
- **Tournament Tracking**: Follow your favorite teams and tournaments
- **Interactive UI**: Engaging cricket-themed user interface

### 🌤️ **Weather Intelligence**
<img src="https://img.icons8.com/fluency/48/weather.png" alt="Weather" align="left" width="40" style="margin-right: 10px;"/>

- **Current Weather**: Real-time weather conditions for any location
- **Location-Based**: Automatic weather updates based on current location
- **Weather API Integration**: Accurate weather data from reliable sources
- **Cricket Match Conditions**: Weather impact analysis for cricket matches

### 💬 **Chat Application**

- **Real-Time Messaging**: Instant chat with Firebase Firestore
- **Group Discussions**: Cricket fan communities and team discussions
- **Media Sharing**: Share images and videos via Cloudinary integration
- **Push Notifications**: Stay updated with message notifications

### 👤 **Profile Management**

- **User Profiles**: Personalized user profiles with preferences
- **Cricket Preferences**: Favorite teams, players, and match settings
- **Chat History**: Access to previous conversations and media
- **Weather Settings**: Customizable weather display preferences

---

## 🛠️ Technology Stack

<div align="center">

### **Core Technologies**

<img src="https://img.icons8.com/color/96/kotlin.png" alt="Kotlin" width="80"/>
<img src="https://img.icons8.com/color/96/android-studio--v3.png" alt="Android Studio" width="80"/>
<img src="https://img.icons8.com/color/96/firebase.png" alt="Firebase" width="80"/>
<img src="https://img.icons8.com/fluency/96/google-cloud.png" alt="Google Cloud" width="80"/>

</div>

### **Architecture & Languages**
- ![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=flat-square&logo=kotlin&logoColor=white) **Language**: Kotlin
- ![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=flat-square&logo=jetpackcompose&logoColor=white) **UI Framework**: Jetpack Compose
- ![Architecture](https://img.shields.io/badge/Architecture-MVVM-blue?style=flat-square) **Architecture**: MVVM (Model-View-ViewModel)
- ![Firebase](https://img.shields.io/badge/Database-Firebase%20Firestore-orange?style=flat-square) **Database**: Firebase Firestore

### **Key Libraries & Dependencies**

#### **🎨 UI & Navigation**
```kotlin
// Jetpack Compose
implementation(platform(libs.androidx.compose.bom))
implementation(libs.androidx.material3)              // Material Design 3
implementation("androidx.compose.ui:ui:1.5.0")       // Core UI components
implementation("androidx.compose.material:material:1.4.3")

// Navigation
implementation("androidx.navigation:navigation-compose:2.7.5")
```

#### **🔥 Firebase Services**
```kotlin
// Firebase Platform
implementation(platform("com.google.firebase:firebase-bom:33.14.0"))
implementation("com.google.firebase:firebase-analytics")
implementation("com.google.firebase:firebase-auth-ktx:22.1.1")
implementation("com.google.firebase:firebase-firestore-ktx:24.10.3")
implementation("com.google.firebase:firebase-messaging-ktx")
implementation("com.google.firebase:firebase-storage-ktx")
```

#### **🔐 Authentication**
```kotlin
// Google Play Services
implementation("com.google.android.gms:play-services-auth:20.7.0")
implementation("com.google.firebase:firebase-auth-ktx")
```

#### **🌐 Networking & APIs**
```kotlin
// HTTP Client - Retrofit & OkHttp
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")
implementation("com.squareup.okhttp3:okhttp:4.12.0")
implementation("com.google.code.gson:gson:2.10.1")
```

#### **🗺️ Maps & Location**
```kotlin
// Google Maps
implementation("com.google.maps.android:maps-compose:2.11.0")
implementation("com.google.android.gms:play-services-maps:18.1.0")
implementation("com.google.android.gms:play-services-location:21.0.1")
```

#### **📸 Media & Image Handling**
```kotlin
// Image Loading & Cloud Storage
implementation("io.coil-kt:coil-compose:2.4.0")
implementation("com.cloudinary:cloudinary-android:2.5.0")
implementation("androidx.activity:activity-compose:1.8.0")
```

#### **⚡ Lifecycle & ViewModel**
```kotlin
// Lifecycle Components
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
```

---

## 📋 Requirements

<div align="center">

![Min SDK](https://img.shields.io/badge/Min%20SDK-24-green?style=for-the-badge&logo=android)
![Target SDK](https://img.shields.io/badge/Target%20SDK-35-blue?style=for-the-badge&logo=android)
![Compile SDK](https://img.shields.io/badge/Compile%20SDK-35-orange?style=for-the-badge&logo=android)

</div>

### **System Requirements**
- <img src="https://img.icons8.com/fluency/24/android-os.png" alt="Android"/> **Minimum SDK**: Android 7.0 (API level 24)
- <img src="https://img.icons8.com/fluency/24/android-os.png" alt="Android"/> **Target SDK**: Android 15 (API level 35)
- <img src="https://img.icons8.com/fluency/24/android-os.png" alt="Android"/> **Compile SDK**: Android 15 (API level 35)

### **Development Environment**
- <img src="https://img.icons8.com/color/24/android-studio--v3.png" alt="Android Studio"/> **Android Studio**: Arctic Fox or later
- <img src="https://img.icons8.com/color/24/kotlin.png" alt="Kotlin"/> **Kotlin**: 1.9.0+
- <img src="https://img.icons8.com/color/24/java-coffee-cup-logo--v1.png" alt="Java"/> **Java**: Version 11
- <img src="https://img.icons8.com/fluency/24/software-installer.png" alt="Gradle"/> **Gradle**: 8.0+

---

## 🚀 Getting Started

### **Prerequisites**
<img src="https://img.icons8.com/fluency/32/checklist.png" alt="Checklist"/>

1. <img src="https://img.icons8.com/color/20/android-studio--v3.png" alt="Android Studio"/> Android Studio installed
2. <img src="https://img.icons8.com/color/20/firebase.png" alt="Firebase"/> Firebase project setup with Firestore enabled
3. <img src="https://img.icons8.com/color/20/google.png" alt="Google"/> Google Play Services configuration
4. <img src="https://img.icons8.com/fluency/20/api.png" alt="API"/> Weather API credentials
5. <img src="https://img.icons8.com/color/20/google-maps.png" alt="Maps"/> Google Maps API key

### **Installation Steps**

1. **🔄 Clone the Repository**
   ```bash
   git clone [repository-url]
   cd cricweather-chat-app
   ```

2. **🔥 Firebase Configuration**
   ```bash
   # Add google-services.json to the app/ directory
   # Enable Authentication, Firestore, Storage, and Messaging
   # Configure Google Sign-In in Firebase Console
   ```

3. **🗺️ Google Maps Setup**
   ```bash
   # Add Google Maps API key to local.properties
   MAPS_API_KEY=your_maps_api_key_here
   ```

4. **🌤️ Weather API Configuration**
   ```bash
   # Add Weather API credentials
   WEATHER_API_KEY=your_weather_api_key_here
   ```

5. **☁️ Cloudinary Setup**
   ```bash
   # Configure Cloudinary for media uploads
   CLOUDINARY_URL=cloudinary://api_key:api_secret@cloud_name
   ```

6. **🔨 Build the Project**
   ```bash
   ./gradlew build
   ```

7. **▶️ Run the Application**
   ```bash
   ./gradlew installDebug
   ```

---

## 📱 App Flow

<div align="center">

```mermaid
graph TD
    A[<img src='https://img.icons8.com/fluency/48/mobile-phone.png'/><br/>App Launch] --> B[<img src='https://img.icons8.com/fluency/48/cricket.png'/><br/>Cricket Splash]
    B --> C[<img src='https://img.icons8.com/fluency/48/google-logo.png'/><br/>Google Login]
    C --> D[<img src='https://img.icons8.com/fluency/48/dashboard-layout.png'/><br/>Cricket Dashboard<br/>Home Page]
    D --> E[<img src='https://img.icons8.com/fluency/48/user-account.png'/><br/>Profile Screen]
    E --> F[<img src='https://img.icons8.com/fluency/48/chat-room.png'/><br/>Chat Application]
    E --> G[<img src='https://img.icons8.com/fluency/48/partly-cloudy-day.png'/><br/>Weather App<br/>Current Location]
    
    style A fill:#e1f5fe
    style B fill:#fff3e0
    style C fill:#f3e5f5
    style D fill:#e8f5e8
    style E fill:#fff8e1
    style F fill:#fce4ec
    style G fill:#e0f2f1
```

</div>

---

## 🏗️ Architecture

<div align="center">
  <img src="https://img.icons8.com/fluency/96/module.png" alt="Architecture" width="80"/>
</div>

The app follows **MVVM (Model-View-ViewModel)** architecture pattern:

- **📊 Model**: Data layer with Firebase Firestore and API services
- **🎨 View**: UI layer built with Jetpack Compose
- **🔄 ViewModel**: Business logic and state management
- **🌐 Repository**: Data abstraction layer for APIs and Firebase

---

## 🔧 Configuration

### **🏏 Cricket API Integration**
-**Live Scores API**: Real-time cricket match data
- <img src="https://img.icons8.com/fluency/20/trophy.png" alt="Tournament"/> **Tournament API**: League and match information

### **🌤️ Weather API Setup**
- <img src="https://img.icons8.com/fluency/20/weather.png" alt="Weather"/> **OpenWeather API**: Current weather conditions
- <img src="https://img.icons8.com/fluency/20/location.png" alt="Location"/> **Location Services**: GPS-based weather updates

### **💬 Chat System**
- <img src="https://img.icons8.com/color/20/firebase.png" alt="Firebase"/> **Firebase Firestore**: Real-time messaging database
- **Firebase Messaging**: Push notifications for new messages
- <img src="https://img.icons8.com/fluency/20/cloud-storage.png" alt="Storage"/> **Firebase Storage**: Media file storage for chat

### **🗺️ Maps Integration**
- <img src="https://img.icons8.com/color/20/google-maps.png" alt="Maps"/> **Google Maps**: Location-based weather display
-  **Location Services**: Current location detection

---

## 🧪 Testing

<div align="center">

![JUnit](https://img.shields.io/badge/JUnit-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![Espresso](https://img.shields.io/badge/Espresso-6DB33F?style=for-the-badge&logo=android&logoColor=white)

</div>

The app includes comprehensive testing:

```kotlin
// Unit Tests
testImplementation(libs.junit)

// Android Instrumentation Tests
androidTestImplementation(libs.androidx.junit)
androidTestImplementation(libs.androidx.espresso.core)

// Jetpack Compose Tests
androidTestImplementation(platform(libs.androidx.compose.bom))
androidTestImplementation(libs.androidx.ui.test.junit4)

// Debug Tools
debugImplementation(libs.androidx.ui.tooling)
debugImplementation(libs.androidx.ui.test.manifest)
```

---

## 📦 Build Configuration

<div align="center">

![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)
![Version](https://img.shields.io/badge/Version-1.0-blue?style=for-the-badge)

</div>

- **📱 Application ID**: `com.example.cric`
- **🔢 Version Code**: 1
- **📝 Version Name**: 1.0
- **🔨 Build Tools**: Android Gradle Plugin 8.0+
- **⚡ Compose Compiler**: 1.4.3

---

## 🔒 Security Features

<div align="center">
  <img src="https://img.icons8.com/fluency/64/security-shield-green.png" alt="Security"/>
</div>

- <img src="https://img.icons8.com/color/20/google.png" alt="Google"/> Google OAuth 2.0 authentication
- <img src="https://img.icons8.com/color/20/firebase.png" alt="Firebase"/> Firebase Security Rules for Firestore
- <img src="https://img.icons8.com/fluency/20/ssl-security.png" alt="HTTPS"/> Secure API communication with HTTPS
- <img src="https://img.icons8.com/fluency/20/encrypt.png" alt="Encryption"/> End-to-end message encryption in chat
- <img src="https://img.icons8.com/fluency/20/privacy.png" alt="Privacy"/> User data privacy and GDPR compliance

---

## 🚀 Performance Optimizations

<div align="center">
  <img src="https://img.icons8.com/fluency/64/speed.png" alt="Performance"/>
</div>

- **🎨 Smooth UI**: Jetpack Compose with optimized recomposition
- **🌐 Network Efficiency**: Retrofit with caching and connection pooling
- **🔥 Real-time Sync**: Firebase real-time listeners for instant updates
- **📸 Image Optimization**: Coil for efficient image loading and caching
- **🧠 Memory Management**: Lifecycle-aware components and proper resource cleanup

---

## 📸 Features Breakdown

### **🏏 Cricket Dashboard Features**
- Live match scores and ball-by-ball updates
- Player statistics and team rankings
- Match schedules and tournament brackets
- Interactive cricket animations and UI elements

### **🌤️ Weather App Features**
- Current weather conditions with detailed metrics
- Location-based automatic weather detection
- Weather forecasts and alerts
- Cricket match weather impact analysis

### **💬 Chat Application Features**
- One-on-one and group conversations
- Image and media sharing via Cloudinary
- Real-time message delivery with read receipts
- Cricket-themed stickers and emojis

---

## 📞 Support & Contact

<div align="center">

![Email](https://img.shields.io/badge/Email-D14836?style=for-the-badge&logo=gmail&logoColor=white)
![Support](https://img.shields.io/badge/Support-Available-green?style=for-the-badge)

For technical support or inquiries related to the CricWeather Chat App, please contact the development team.

</div>

---

## 📄 License

<div align="center">

![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

</div>

---

<div align="center">

**Built with ❤️ for Cricket Enthusiasts**

*Connecting cricket fans through weather intelligence and social interaction*


<img src="https://img.icons8.com/fluency/48/partly-cloudy-day.png" alt="Weather"/>
<img src="https://img.icons8.com/fluency/48/chat.png" alt="Chat"/>


---

![Made with Love](https://img.shields.io/badge/Made%20with-❤️-red?style=for-the-badge)
![Android](https://img.shields.io/badge/Platform-Android-green?style=for-the-badge&logo=android)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple?style=for-the-badge&logo=kotlin)
![Firebase](https://img.shields.io/badge/Backend-Firebase-orange?style=for-the-badge&logo=firebase)

</div>
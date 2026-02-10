# 🥑 Pantry - Smart Kitchen Inventory Manager

![Kotlin](https://img.shields.io/badge/Kotlin-100%25-purple)
![UI](https://img.shields.io/badge/UI-Jetpack%20Compose-green)
![Database](https://img.shields.io/badge/Database-Room-blue)
![Status](https://img.shields.io/badge/Status-Thesis%20Project-orange)

## 📱 Project Overview
**Pantry** is a native Android application designed to minimize food waste and organize kitchen inventory efficiently.
Developed as a **Bachelor's Thesis in Computer Engineering**, this project explores modern Android architecture, local data persistence, and external API integration.

The app allows users to create virtual containers (e.g., Fridge, Freezer, Pantry) and track products, specifically focusing on expiration dates to notify users before food goes bad.

## ✨ Key Features

### 📦 Inventory Management
* **Custom Containers:** Users can create, edit, and delete storage locations (e.g., "Kitchen Cabinet", "Basement Freezer").
* **Product CRUD:** Full control to add, modify, or remove items within specific containers.

### 🔍 Smart Entry System
* **Barcode Scanner:** Integration with the device camera to scan product barcodes.
* **OpenFoodFacts API:** Automatically fetches product details (name, brand, image) from the open database upon scanning.
* **Manual Entry:** Fallback mode for products without barcodes.

### ⏰ Expiration Tracking
* **Priority Sorting:** Visual list of items sorted by expiration date (Ascending).
* **Smart Notifications:** Automated local push notifications alert the user when a product is approaching its expiration date.

## 🛠 Tech Stack & Architecture

This project is built using **modern Android development practices**:

* **Language:** Kotlin
* **User Interface:** **Jetpack Compose** (Declarative UI Toolkit)
* **Local Database:** **Room** (SQLite abstraction layer) for offline-first data persistence.
* **Network:** Integration with **OpenFoodFacts API** .
* **Async Operations:** Kotlin Coroutines & Flow.
* **Hardware Integration:** CameraX / Barcode Scanning library.

## 🚧 Roadmap & Future Improvements
As a thesis prototype, the current version focuses on core functionality and local storage. The following features are planned for future releases to make the app production-ready:

* [ ] **Cloud Synchronization:** Migrate from local Room DB to a cloud solution (e.g., Firebase/Supabase) to allow **family sharing** across multiple devices.
* [ ] **UI/UX Polishing:** Enhance the Jetpack Compose design with custom themes and animations.
* [ ] **User Settings:** Add a settings screen to customize notification timing (e.g., "Notify me 3 days before").
* [ ] **Recipe Suggestions:** Integration with recipe APIs based on available ingredients.

## 📷 Screenshots
<div style="display: flex; flex-direction: row;">
    <img src="Screenshot/Home.png" width="200" />
    <img src="path/to/screenshot2.png" width="200" />
</div>

## 👤 Author
**Luca Rizzo**
Computer Engineering Graduate
* [LinkedIn] https://www.linkedin.com/in/luca-rizzo-274367199/

---
*Developed for academic purposes at [Nome Università].*

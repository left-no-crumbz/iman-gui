# 🏨 IMAN CRUD App: Java GUI Hotel Booking System

This repository contains a Java GUI application for hotel room booking and management, developed as a final requirement for the Information Management (IMAN) subject. It showcases the implementation of a Create, Read, Update, Delete (CRUD) system using Java Swing, SQL, and the H2 embedded database. I only had two days to finish the application, and this included learning the tooling behind the NetBeans editor to create a GUI as well as finding an embedded database and learning how to connect to it. As such the application is quite rough on the edges. Nonetheless, it was still a great learning experience.

## Table of Contents

- [🚀 Project Overview](#-project-overview)
- [⚙️ Technologies Used](#%EF%B8%8F-technologies-used)
- [🚩 Getting Started](#-getting-started)
- [🛠️ Usage](#%EF%B8%8F-usage)
  - [👤 Lodger Management](#-lodger-management)
  - [🛏️ Room Management](#%EF%B8%8F-room-management)
  - [📅 Reservation Management](#-reservation-management)
- [📝 License](#-license)

## 🚀 Project Overview

The IMAN CRUD App is a comprehensive hotel booking system that allows users to manage lodger information, room details, and reservations. It provides a user-friendly interface for performing CRUD operations on three main entities: lodgers, rooms, and reservations.

Key features of the application include:

- **👤 Lodger Management**: Create, update, delete, and view lodger records with details such as name, contact information, and address.
- **🛏️ Room Management**: Add, modify, remove, and display room information, including room number, tier (e.g., Standard, Deluxe, Executive, Presidential), capacity, and pricing.
- **📅 Reservation Management**: Make new reservations, edit existing ones, cancel reservations, and view reservation details like check-in/check-out dates, duration, and total amount.
- **📂 Data Persistence**: All data is stored in an H2 embedded database, ensuring data integrity and persistence across application sessions.
- **🔒 Validation and Error Handling**: Input validation and error handling mechanisms are implemented to ensure data consistency and provide user-friendly error messages.

## ⚙️ Technologies Used

- 💻 Java
- 🖥️ Java Swing (GUI)
- 📊 SQL
- 🗄️ H2 Embedded Database

## 🚩 Getting Started

To run the IMAN CRUD App locally, follow these steps:

1. Clone the repository: `git clone https://github.com/left-no-crumbz/iman-gui.git)`
2. Open the project in your preferred Java IDE (e.g., NetBeans, IntelliJ IDEA).
3. Build and run the `Main` class.

## 🛠️ Usage

Upon launching the application, you'll be presented with the main window containing three tabs: "Lodger," "Room," and "Registration." Each tab provides a set of functionalities for managing the respective entities.

### 👤 Lodger Management

- Click the "Lodger" tab to access the lodger management features.
- Use the provided forms and buttons to create, update, delete, and search for lodger records.

### 🛏️ Room Management

- Navigate to the "Room" tab to manage room information.
- Create new rooms, modify existing room details, remove rooms, and search for specific rooms using the available controls.

### 📅 Reservation Management

- Switch to the "Registration" tab to handle reservations.
- Make new reservations by providing the required details, such as check-in/check-out dates, room selection, and mode of payment.
- Edit or cancel existing reservations by searching for the reservation ID and modifying the relevant fields.

## 📝 License

This project is licensed under the [MIT License](LICENSE).

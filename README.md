# Plant Care Assistant App

## Overview
The Plant Care Assistant is an Android application designed to help users manage and care for their plants. Utilizing Android Jetpack Compose for a seamless and intuitive user interface, the app offers features like plant identification and custom care reminders.

![image](https://github.com/angenue/Plant-Care-Assistant/assets/78523663/b26cb621-603d-46c4-9137-58f55eae090b) ![image](https://github.com/angenue/Plant-Care-Assistant/assets/78523663/f74c29d4-2535-46dc-8d7b-6cb86701ee76)


## Features
- **Plant Identification**: Uses Plant.id API for identifying plants from user-uploaded images.
  Plant information is retrieved from Perenual API.
- **Custom Care Reminders**: Allows users to set watering reminders for each plant.
- **User Customization**: Users can add custom names and images for their plants.
- **Recently Searched Plants**: Keeps a record of recently searched plants for quick reference.

## Technologies Used
- **Frontend**: Kotlin, Android Jetpack Compose
- **Backend**: Java, Spring Boot
- **Database**: MySQL
- **APIs**: Perenual API, Plant.id API
- **Testing**: JUnit (87% class coverage, 75% method coverage)

## Getting Started

### Prerequisites
- Android Studio
- JDK 11
- MySQL Server

### Setting Up the Project
1. **Clone the Repository**: `git clone https://github.com/angenue/Plant-Care-Assistant.git`
2. **Open the Project in Android Studio**: Navigate to the cloned directory and open the project.
3. **Backend Configuration**: 
   - Set up MySQL database and update `application.properties` with your database credentials.
   - Add your Perenual and Plant.id API keys to `application-secrets.properties`.
4. **Run the Backend**: Execute the Spring Boot application.
5. **Start the Android Emulator**: Choose an emulator in Android Studio and run the app.

## Usage
- **Add a Plant**: Click on 'Search' and enter the plant name or upload an image for identification.


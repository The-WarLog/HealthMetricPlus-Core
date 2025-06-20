```markdown
# HealthMetricPlus

## Description

HealthMetric+ is a next-generation health and fitness tracking application crafted to empower users with the tools they need for consistent and insightful physical progress tracking. From casual gym-goers to dedicated fitness enthusiasts, HealthMetric+ delivers a seamless, secure, and intuitive experience for logging key body measurements.

With Firebase Authentication at its core, the app ensures secure user onboarding and access via Google, email, and more. Backed by Firebase’s robust real-time database capabilities, user data is securely stored and instantly available across devices.

HealthMetric+ promotes a proactive and data-driven approach to physical wellness, enabling users to visualize progress, set informed goals, and stay committed to healthier living.

## Features

*   Effortless sign-in/sign-up via Firebase Authentication (Google, Email, etc.)
*   Secure real-time syncing with Firebase Firestore or Realtime Database
*   Visual analytics with intuitive graphs and trend charts for measurements like weight, biceps, chest, height, and more
*   Custom reminders to stay consistent with check-ins and logs
*   Tailored for fitness-first users, from personal growth trackers to professional gym trainers

## Technologies Used

*   Jetpack Compose: For building the User Interface
*   Material 3: For designing a beautiful and consistent UI.
*   Firebase Authentication: For securely managing user sign-ins, including Google Sign-In.
*   Firebase Firestore: For storing, syncing, and querying data in real-time.
*   Splash Screen: For creating a captivating splash screen.
*   Dagger Hilt: For managing dependency injection for cleaner, modular code.
*   Compose Navigation: For navigating between screens seamlessly.
*   Coil: For loading and displaying images effortlessly.

## Installation

Since this is an Android application using Jetpack Compose and Firebase, installation involves setting up the Android development environment and configuring Firebase.

1.  **Set up Android Studio:** Download and install the latest version of Android Studio.
2.  **Clone the repository:**
    ```bash
    git clone https://github.com/The-WarLog/HealthMetricPlus-Core
    ```
3.  **Open the project in Android Studio:**  Navigate to the cloned directory and open the project in Android Studio.
4.  **Configure Firebase:**
    *   Create a Firebase project in the Firebase Console.
    *   Register your Android app with Firebase.
    *   Download the `google-services.json` file and add it to your `app/` directory.
    *   Add Firebase dependencies to your `build.gradle` files (project-level and app-level).  Refer to the Firebase documentation for the specific dependencies.
5.  **Build and Run:**  Build the project in Android Studio and run it on an emulator or a physical device.

## Usage

1.  Launch the application.
2.  Sign up or sign in using Firebase Authentication (Google, Email, etc.).
3.  Start logging your body measurements (weight, biceps, chest, height, etc.).
4.  View visual analytics and track your progress using the intuitive graphs and trend charts.
5.  Set custom reminders to stay consistent with your check-ins.


```

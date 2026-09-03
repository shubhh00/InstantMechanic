# Instant Mechanic

A mini Android app that lets users browse nearby mechanics, view garage details, and submit vehicle service requests.

## Features

- Browse mechanics sorted by distance
- View rating, review count, location, services, and open/closed status
- View mechanic details and working hours
- Submit a vehicle service request
- Form validation
- Loading, success, and error states
- Graceful handling of missing mechanic data
- Firebase Realtime Database integration

## Tech Stack

- Kotlin
- Jetpack Compose
- MVVM
- Clean Architecture
- Retrofit + Gson
- Firebase Realtime Database
- Hilt
- KSP
- Kotlin Coroutines + StateFlow
- Navigation Compose
- JUnit
- JSON parsing
- REST API integration

## Architecture

The project uses a multi-module Clean Architecture setup:

```text
app     -> UI, Navigation, ViewModels, Dependency Injection
data    -> Retrofit, Firebase, DTOs, Mappers, Repository Implementations
domain  -> Domain Models, Repository Interfaces, Business Logic
```

The `domain` module does not depend on Android, Retrofit, or Firebase.

## API / Data

Mechanic data is hosted in Firebase Realtime Database and fetched using Retrofit through the Firebase REST API.

```http
GET /mechanics.json
```

Base URL:

```text
https://instant-mechanic-3baac-default-rtdb.firebaseio.com/
```

Service requests are submitted to Firebase Realtime Database under:

```text
serviceRequests/
```

Open/Closed status is calculated dynamically using each mechanic's opening and closing time rather than relying on a stored status value.

## Setup

1. Clone the repository.
2. Open the project in Android Studio.
3. Add `google-services.json` inside the `app/` directory.
4. Sync Gradle.
5. Run the app on an emulator or Android device with internet access.

Minimum SDK: Android 8.0 (API 26)

## Screenshots

## Screenshots

<p align="center">
  <img src="https://github.com/user-attachments/assets/cc343028-6385-407e-8714-d72c2cb1294d" width="15%" alt="Home Screen" />
  <img src="https://github.com/user-attachments/assets/e4311c50-b660-42fd-b830-a1972a540a61" width="15%" alt="Mechanic Details" />
  <img src="https://github.com/user-attachments/assets/8cb11ccc-d9ce-4c30-97cf-e22ad123193f" width="15%" alt="Request Service" />
  <img src="https://github.com/user-attachments/assets/62ea3c91-6552-447e-9242-0ba4a3ca835c" width="15%" alt="Screenshot_20260903_170312" />
  <img src="https://github.com/user-attachments/assets/95e39a4b-0ffd-4042-b530-a7ce074d178a" width="15%" alt="Network Error" />
  <img src="https://github.com/user-attachments/assets/61d206e6-aac3-47e6-ade4-95f1d6d05ee2" width="15%" alt="Screenshot_20260903_170353" />
</p>
<p align="center">
  <sub><i>Left to right: Home Screen, Mechanic Details, Request Service, Success Confirmation, Error State</i></sub>
</p>

## Notes

- Distance is currently provided by the backend and mechanics are displayed nearest-first.
- Live GPS-based distance calculation is outside the current assignment scope.
- Missing optional fields are handled gracefully in the UI.

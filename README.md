# Instant Mechanic

A mini Android application that allows users to browse nearby mechanics,
view garage details, and submit vehicle service requests.

## Features

- Browse mechanics sorted by distance
- View ratings, reviews, services and open/closed status
- View mechanic details
- Request a vehicle service
- Form validation
- Loading, success and error handling
- Firebase Realtime Database integration
- REST API integration using Retrofit
- Clean Architecture + MVVM
- Hilt dependency injection

## Tech Stack

- Kotlin
- Jetpack Compose
- MVVM
- Clean Architecture
- Retrofit
- Gson
- Firebase Realtime Database
- Hilt
- Coroutines + StateFlow
- Navigation Compose
- JUnit

## Architecture

The project follows a multi-module Clean Architecture structure:

app
- Presentation
- Navigation
- Dependency Injection

data
- Retrofit API
- Firebase data source
- DTOs
- Repository implementations
- Mappers

domain
- Business models
- Repository interfaces
- Business logic

Dependency direction:

app → data → domain
app → domain

The domain module has no dependency on Android, Firebase or Retrofit.

## API

Mechanic data is hosted in Firebase Realtime Database and accessed through
its REST API.

Endpoint:

GET /mechanics.json

Retrofit parses the JSON response into DTOs which are then mapped to domain
models.

## Service Requests

Service requests are stored in Firebase Realtime Database under:

serviceRequests/

Each request contains:

- Mechanic ID
- Customer name
- Phone number
- Vehicle number
- Selected service
- Problem description

## Error Handling

The app handles:

- API loading state
- Network failures
- Firebase request timeout
- Invalid form fields
- Missing mechanic data
- Empty service lists

## Setup

1. Clone the repository
2. Open the project in Android Studio
3. Add your `google-services.json` file inside the `app/` directory
4. Sync Gradle
5. Run the application

## Screenshots

Add screenshots of:

- Home screen
- Mechanic details
- Request service form
- Success confirmation
- Error state

## Assumptions

- Distance values are currently provided by the API rather than calculated from live GPS.
- Open/closed status is calculated from mechanic opening and closing hours.

## Future Improvements

- Live user location and distance calculation
- Search and filtering
- Nearby / Open Now filters
- Authentication
- Offline caching
- Pagination
- Mechanic/admin onboarding

<div align="center">

# Instant Mechanic

**Find nearby garages, book a service, and talk to a mechanic on video — even with no network.**

An offline-first Android app built to explore production concerns: durable local data, background sync, real-time communication, modular architecture, and measurable performance work.

<br>

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Hilt](https://img.shields.io/badge/Hilt-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Room](https://img.shields.io/badge/Room-FF6F00?style=for-the-badge&logo=sqlite&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)
![Agora](https://img.shields.io/badge/Agora%20RTC-099DFD?style=for-the-badge&logo=webrtc&logoColor=white)

![Min SDK](https://img.shields.io/badge/min%20SDK-26-3DDC84?style=flat-square)
![Architecture](https://img.shields.io/badge/architecture-MVVM%20%2B%20Clean-blue?style=flat-square)
![Modules](https://img.shields.io/badge/modules-app%20%7C%20data%20%7C%20domain-lightgrey?style=flat-square)
![Tests](https://img.shields.io/badge/tests-JUnit%20%2B%20MockK-success?style=flat-square)

</div>

---

<div align="center">

### At a glance

| 📦 APK size | ⚡ Cold start | 🧱 Architecture | 📴 Offline |
|:---:|:---:|:---:|:---:|
| **155 MB → 69 MB** | **3,701 ms → 3,423 ms** | **3 Gradle modules** | **Full browse + submit** |
| 55.5% smaller | 7.5% faster to full display | Android-free domain layer | Queued, auto-retried |

</div>

---

## Screenshots

<div align="center">

| Home | Mechanic details | Service request |
|:---:|:---:|:---:|
| <img src="https://github.com/user-attachments/assets/cc343028-6385-407e-8714-d72c2cb1294d" width="230" alt="Home screen" /> | <img src="https://github.com/user-attachments/assets/e4311c50-b660-42fd-b830-a1972a540a61" width="230" alt="Mechanic details" /> | <img src="https://github.com/user-attachments/assets/8cb11ccc-d9ce-4c30-97cf-e22ad123193f" width="230" alt="Service request form" /> |
| Mechanics ordered by distance | Ratings, hours, and live open/closed status | Validated request form |

| Success | Network error | Missing data |
|:---:|:---:|:---:|
| <img src="https://github.com/user-attachments/assets/62ea3c91-6552-447e-9242-0ba4a3ca835c" width="230" alt="Request success confirmation" /> | <img src="https://github.com/user-attachments/assets/95e39a4b-0ffd-4042-b530-a7ce074d178a" width="230" alt="Network error state" /> | <img src="https://github.com/user-attachments/assets/61d206e6-aac3-47e6-ade4-95f1d6d05ee2" width="230" alt="Graceful missing-data handling" /> |
| Confirmation after submission | Cached data survives a failed refresh | Graceful degradation on partial records |

</div>

---

## Features

**Discovery**
- Browse mechanics ordered by distance
- Ratings, reviews, address, working hours, services, and calculated open/closed status
- Cached listings remain browsable with no network connection

**Service requests**
- Submit requests online or offline
- Pending requests retry automatically when connectivity returns
- Form input validation

**Video consultation**
- One-to-one call with camera, microphone, camera-switching, and call controls

**State handling**
- Loading, refresh, offline, empty, success, and error states throughout

---

## Architecture

MVVM and Clean Architecture across three Gradle modules:

| Module | Responsibility |
|:---|:---|
| **`app`** | Compose UI, navigation, ViewModels, DI, Agora integration, WorkManager workers |
| **`data`** | Room, Firebase, Retrofit, DTOs, mappers, data sources, repository implementations |
| **`domain`** | Platform-independent models and repository contracts |

> [!NOTE]
> The `domain` module has **no dependency** on Android, Room, Retrofit, Firebase, or Compose — it compiles as pure Kotlin.

```mermaid
flowchart TD
    UI["🖥️ Compose UI"] --> VM["🔄 ViewModels · StateFlow"]
    VM --> Contracts["📄 Domain repository contracts"]
    Contracts --> Repositories["🗂️ Data repositories"]
    Repositories --> Room[("💾 Room database")]
    Repositories --> Remote["☁️ Firebase remote data"]
    Worker["⏱️ WorkManager sync"] --> Repositories

    classDef ui fill:#4285F4,stroke:#1a1a1a,color:#fff
    classDef domain fill:#7F52FF,stroke:#1a1a1a,color:#fff
    classDef data fill:#FF6F00,stroke:#1a1a1a,color:#fff
    class UI,VM ui
    class Contracts domain
    class Repositories,Room,Remote,Worker data
```

---

## Offline-first behavior

**Mechanic listings.** Room is the local source of truth. The UI observes database changes as a `Flow`, while a refresh fetches the latest Firebase data through Retrofit and updates the cache. Cached mechanics remain available when the refresh fails.

**Service requests.** A request is saved locally before remote submission is attempted. Failed or timed-out requests stay queued with their sync status, and a network-constrained WorkManager job retries them once connectivity is available.

---

## Video consultation

Agora RTC powers the consultation flow:

- Local camera preview and remote participant rendering
- Microphone and camera toggles, front/rear switching
- Join, leave, error, and remote-user lifecycle events
- RTC engine cleanup when the call ViewModel is cleared

> [!WARNING]
> The project uses a fixed demonstration channel and a temporary Agora token. A production implementation would issue tokens from a secure backend and add mechanic matching/signaling.

---

## Performance

### APK size

| Configuration | APK size |
|:---|---:|
| Full Agora SDK, no release optimization | 155 MB |
| **R8 + Agora Lite SDK** | **69 MB** |

**≈55.5% smaller**, via R8 shrinking and migration from the full Agora package to the Lite SDK.

### Startup

Compared against an eager-initialization build that opened Room synchronously and initialized Agora, WorkManager, and service-request dependencies during application launch.

| Implementation | Median time to full display |
|:---|---:|
| Eager initialization | 3,701 ms |
| **Deferred initialization** | **3,423 ms** |

**≈278 ms (7.5%) faster** at the median.

<details>
<summary><b>Measurement methodology</b></summary>

<br>

- 10 process-cold launches per implementation
- Same physical Android device and persisted app data
- Same debug build configuration and branded splash duration
- Launched through ADB using `am start -S`
- Meaningful UI completion reported with Compose `ReportDrawnWhen`

Absolute timings are device- and build-dependent; the percentage represents the controlled comparison on the test device.

</details>

---

## Tech stack

| Layer | Technologies |
|:---|:---|
| **Language** | Kotlin, Coroutines |
| **UI** | Jetpack Compose, Navigation Compose |
| **Architecture** | MVVM, Clean Architecture, StateFlow |
| **DI** | Hilt with KSP |
| **Persistence** | Room |
| **Background** | WorkManager |
| **Network** | Retrofit, Gson, Firebase Realtime Database |
| **Real-time** | Agora RTC |
| **Testing** | JUnit, MockK, kotlinx-coroutines-test |
| **Build** | R8 |

---

## Testing

Unit tests cover:

- Mechanic DTO-to-domain mapping
- Service-request entity and DTO mapping
- Repository submission and synchronization outcomes
- Online success, offline queuing, and retry behavior

---

## Setup

<details open>
<summary><b>Requirements</b></summary>

<br>

- Android Studio with JDK 11 support
- Android SDK 26 or newer
- Firebase Android configuration
- Agora project credentials

</details>

<details>
<summary><b>Configuration steps</b></summary>

<br>

**1.** Clone the repository and open it in Android Studio.

**2.** Add your Firebase `google-services.json` file under `app/`.

**3.** Add the following to the root `local.properties`:

```properties
AGORA_APP_ID=your_agora_app_id
AGORA_RTC_UID=1001
AGORA_TEMP_TOKEN=your_temporary_token
```

**4.** Sync Gradle and run the `app` configuration on an emulator or physical device.

> `local.properties` must not be committed. Agora temporary tokens expire and should be replaced by server-generated tokens before production use.

</details>

---

## Current limitations

| Area | Status |
|:---|:---|
| **Distance** | Supplied by the backend rather than calculated from live GPS |
| **Video** | Shared demonstration channel without production signaling or mechanic assignment |
| **Credentials** | Firebase and Agora configured for development, not a deployed production backend |

---

<div align="center">
<sub>Built to explore production Android concerns end to end — architecture, offline resilience, and measurable performance.</sub>
</div>

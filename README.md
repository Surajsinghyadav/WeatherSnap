# WeatherSnap

WeatherSnap is an Android app built with Kotlin and Jetpack Compose that lets users search live weather for a city, capture a custom photo, add notes, and save weather reports locally using Room. It is built with a modern MVVM architecture and uses a custom CameraX screen instead of the system camera.

## Tech Stack

- Kotlin, Jetpack Compose, Material 3
- MVVM, ViewModel, StateFlow, Coroutines
- Koin for dependency injection
- Retrofit + Gson + OkHttp logging interceptor
- Room Database (reports + cached city suggestions)
- Navigation 3 (Compose)
- CameraX custom camera
- Coil for image loading

## Features

- City search using Open‑Meteo geocoding API (no API key required)
- Type‑ahead city suggestions with local Room caching
- Current weather: city, temperature, condition, humidity, wind speed, pressure
- “Create Report” flow with:
  - Weather snapshot
  - Custom CameraX capture
  - Image compression with original vs compressed size
  - Notes input
- Saved reports screen showing:
  - Captured image
  - Weather details at save time
  - Notes and sizes
  - Saved timestamp

## Project Structure (high level)

- `WeatherSnapApplication` – sets up Koin modules (network, database, app).
- `MainActivity` – hosts the Compose UI and navigation.
- `navigation/` – Navigation 3 routes and animated transitions.
- `data/local/` – Room database, entities, and DAOs.
- `data/remote/` – Retrofit services for Open‑Meteo APIs.
- `data/repository/WeatherRepository` – local‑first city search + weather fetching.
- `Presentation/` – `WeatherViewModel` and all Compose screens.
- `koin/modules/` – network, database, and app DI modules.
- `ui/theme/` – colors, typography, and Material 3 theme.

## Setup & Run

1. **Clone the repository**

   ```bash
   https://github.com/Surajsinghyadav/WeatherSnap
   ```

2. **Open in Android Studio**

   - Use Android Studio Giraffe or newer.
   - Open the project folder as an existing Android project.

3. **Sync Gradle**

   - Gradle wrapper is included.
   - Project config:
     - `minSdk = 29`
     - `targetSdk = 37`
     - `compileSdk = 37`

4. **Run the app**

   - Connect a device or start an emulator (API 29+).
   - Select the `app` run configuration and click **Run**.
   - No API keys or extra configuration are required.

## Main Flow (for reviewers)

1. Type a city name (≥ 3 letters) to see suggestions.
2. Select a city to load current weather.
3. Tap **Create Report**:
   - Capture/retake photo via the **Custom Camera** screen.
   - See original and compressed image sizes.
   - Add notes and save the report.
4. Open **Saved Reports** to see the stored list with images, weather snapshot, notes, and sizes.

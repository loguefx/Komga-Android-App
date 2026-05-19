# Komga Android App

A clean Android client for [Komga](https://komga.org/), the self-hosted manga/comic server.

## Features

- **Login** — Connect to your Komga server with URL, email, and password
- **Home** — "Continue Reading" (on-deck books) and "Recently Added" series
- **Library** — Browse all series in your Komga library with search
- **Favorites** — Save series locally for quick access
- **Series Detail** — Cover art, metadata, book list, and favorite toggle
- **Reader** — Full-screen page viewer with pinch-to-zoom and progress tracking

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Navigation | Compose Navigation |
| Networking | Retrofit 2 + OkHttp |
| Images | Coil |
| DI | Hilt |
| Local DB | Room (favorites) |
| Preferences | DataStore |
| Build | Gradle Kotlin DSL + Version Catalog |

## Building

1. Open the project in Android Studio Iguana or newer
2. Let Gradle sync
3. Run on a device or emulator (API 26+)

## Setup

1. Launch the app
2. Enter your Komga server URL (e.g. `http://192.168.1.100:25600`)
3. Enter your Komga email and password
4. Tap **Connect**

## Notes

- `android:usesCleartextTraffic="true"` is enabled to support local HTTP servers
- Favorites are stored locally in Room (not synced to the server)
- Reading progress is synced to the Komga server via `PATCH /api/v1/books/{id}/read-progress`

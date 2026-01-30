# CryptoTracker - Cryptocurrency Price Tracker

## Overview
A real-time cryptocurrency tracking Android application built with Kotlin and Jetpack Compose, showcasing Clean Architecture, API integration with offline caching, and reactive data streams. Users can browse market data, track favorite coins, view interactive price charts, and set price alerts.

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin |
| UI Framework | Jetpack Compose + Material Design 3 |
| Architecture | MVVM + Clean Architecture |
| DI | Hilt |
| Networking | Retrofit + OkHttp + Moshi |
| Database | Room (offline cache + watchlist) |
| Async | Kotlin Coroutines + Flow |
| Navigation | Jetpack Compose Navigation + Bottom Nav |
| Charts | Vico (Compose-native charting library) |
| Background Work | WorkManager (price alerts) |
| Image Loading | Coil (Compose integration) |
| Testing | JUnit 5, Mockk, Compose UI Tests |
| Build | Gradle (Kotlin DSL), Version Catalog |

---

## API: CoinGecko (Free, No API Key Required)

| Endpoint | Purpose |
|----------|---------|
| `GET /coins/markets?vs_currency=usd&order=market_cap_desc&per_page=100` | Market listing with prices |
| `GET /coins/{id}` | Coin detail info |
| `GET /coins/{id}/market_chart?vs_currency=usd&days=7` | Price chart data |
| `GET /search?query=bitcoin` | Search coins |
| `GET /simple/supported_vs_currencies` | Supported fiat currencies |

Base URL: `https://api.coingecko.com/api/v3/`

> Note: Free tier has rate limits (~10-30 calls/min). App implements caching and throttling to stay within limits.

---

## Features

### 1. Market Screen (Home)
- Top 100 cryptocurrencies ranked by market cap
- Each row shows: rank, icon, name, symbol, price, 24h % change, sparkline
- Pull-to-refresh for latest data
- Sort by: Market Cap, Price, 24h Change
- Color-coded price change (green for +, red for -)
- Tap to navigate to coin detail

### 2. Search Screen
- Real-time search with debounce (300ms)
- Search results with coin icon, name, symbol, market cap rank
- Recent searches (persisted locally)
- Tap result to navigate to coin detail

### 3. Coin Detail Screen
- Coin header: icon, name, symbol, current price, 24h change
- Interactive price chart with period selector (24h, 7d, 30d, 90d, 1y)
- Market stats: market cap, volume, circulating supply, ATH, ATL
- Price in selected fiat currency
- Add/Remove from watchlist (heart icon toggle)
- Set price alert button

### 4. Watchlist Screen
- User's favorited coins with live prices
- Swipe-to-remove from watchlist
- Empty state with prompt to browse market
- Persisted in Room database
- Quick access to coin detail

### 5. Price Alerts
- Set target price (above/below current price)
- WorkManager periodic checks (every 15 min)
- Local notification when alert triggers
- Manage active alerts list
- Delete/edit existing alerts

### 6. Settings Screen
- Dark/Light theme toggle
- Default fiat currency (USD, EUR, GBP, INR, JPY)
- Cache duration setting
- Clear cache option
- App version info

---

## Architecture & Package Structure

```
com.sai.cryptotracker/
│
├── data/
│   ├── remote/
│   │   ├── CoinGeckoApi.kt             -- Retrofit API interface
│   │   ├── dto/
│   │   │   ├── CoinMarketDto.kt        -- Market listing response
│   │   │   ├── CoinDetailDto.kt        -- Coin detail response
│   │   │   ├── MarketChartDto.kt       -- Price chart response
│   │   │   └── SearchResultDto.kt      -- Search response
│   │   └── interceptor/
│   │       └── RateLimitInterceptor.kt  -- Throttle API calls
│   ├── local/
│   │   ├── CryptoDatabase.kt           -- Room database
│   │   ├── dao/
│   │   │   ├── CoinDao.kt              -- Cached coin data queries
│   │   │   ├── WatchlistDao.kt         -- Watchlist CRUD
│   │   │   └── AlertDao.kt             -- Price alerts CRUD
│   │   ├── entity/
│   │   │   ├── CoinEntity.kt           -- Cached coin market data
│   │   │   ├── WatchlistEntity.kt      -- Watchlist coin reference
│   │   │   ├── AlertEntity.kt          -- Price alert config
│   │   │   └── SearchHistoryEntity.kt  -- Recent searches
│   │   └── converter/
│   │       └── Converters.kt
│   ├── repository/
│   │   ├── CoinRepositoryImpl.kt       -- API + Room caching logic
│   │   ├── WatchlistRepositoryImpl.kt
│   │   └── AlertRepositoryImpl.kt
│   └── mapper/
│       ├── CoinMapper.kt               -- DTO <-> Entity <-> Domain
│       ├── ChartMapper.kt
│       └── AlertMapper.kt
│
├── domain/
│   ├── model/
│   │   ├── Coin.kt                     -- Domain model for market listing
│   │   ├── CoinDetail.kt               -- Full coin detail
│   │   ├── ChartData.kt                -- Price chart points
│   │   ├── PriceAlert.kt               -- Alert configuration
│   │   └── SearchResult.kt
│   ├── repository/
│   │   ├── CoinRepository.kt           -- Interface
│   │   ├── WatchlistRepository.kt
│   │   └── AlertRepository.kt
│   └── usecase/
│       ├── market/
│       │   ├── GetMarketListUseCase.kt
│       │   └── GetCoinDetailUseCase.kt
│       ├── chart/
│       │   └── GetChartDataUseCase.kt
│       ├── search/
│       │   ├── SearchCoinsUseCase.kt
│       │   └── GetSearchHistoryUseCase.kt
│       ├── watchlist/
│       │   ├── ToggleWatchlistUseCase.kt
│       │   ├── GetWatchlistUseCase.kt
│       │   └── IsInWatchlistUseCase.kt
│       └── alert/
│           ├── SetPriceAlertUseCase.kt
│           ├── GetAlertsUseCase.kt
│           └── DeleteAlertUseCase.kt
│
├── presentation/
│   ├── MainActivity.kt
│   ├── CryptoTrackerApp.kt             -- Root composable with NavHost
│   ├── navigation/
│   │   ├── Screen.kt                   -- Sealed class for routes
│   │   ├── BottomNavItem.kt
│   │   └── NavGraph.kt
│   ├── theme/
│   │   ├── Color.kt                    -- Crypto-themed colors (greens, reds)
│   │   ├── Theme.kt                    -- M3 theming
│   │   ├── Type.kt
│   │   └── Shape.kt
│   ├── common/
│   │   ├── CoinListItem.kt             -- Reusable coin row composable
│   │   ├── PriceChangeChip.kt          -- Green/red % change chip
│   │   ├── SparklineChart.kt           -- Mini inline chart
│   │   ├── CoinIcon.kt                 -- Async coin image with Coil
│   │   ├── ErrorView.kt                -- Error state with retry
│   │   ├── EmptyStateView.kt
│   │   └── ShimmerLoading.kt           -- Shimmer placeholder loading
│   ├── market/
│   │   ├── MarketScreen.kt
│   │   ├── MarketViewModel.kt
│   │   └── components/
│   │       ├── MarketHeader.kt          -- Sort options row
│   │       └── CoinMarketList.kt        -- LazyColumn of coins
│   ├── search/
│   │   ├── SearchScreen.kt
│   │   ├── SearchViewModel.kt
│   │   └── components/
│   │       ├── SearchBar.kt
│   │       ├── SearchResults.kt
│   │       └── RecentSearches.kt
│   ├── detail/
│   │   ├── CoinDetailScreen.kt
│   │   ├── CoinDetailViewModel.kt
│   │   └── components/
│   │       ├── CoinHeader.kt            -- Name, price, change
│   │       ├── PriceChart.kt            -- Interactive Vico chart
│   │       ├── ChartPeriodSelector.kt   -- 24h/7d/30d/90d/1y tabs
│   │       ├── MarketStats.kt           -- Grid of market data
│   │       └── SetAlertDialog.kt
│   ├── watchlist/
│   │   ├── WatchlistScreen.kt
│   │   ├── WatchlistViewModel.kt
│   │   └── components/
│   │       └── WatchlistItem.kt
│   └── settings/
│       ├── SettingsScreen.kt
│       └── SettingsViewModel.kt
│
├── di/
│   ├── AppModule.kt                    -- Database, OkHttp, Retrofit providers
│   ├── NetworkModule.kt                -- API service, interceptors
│   ├── RepositoryModule.kt
│   └── UseCaseModule.kt
│
├── worker/
│   └── PriceAlertWorker.kt             -- WorkManager for checking alerts
│
└── util/
    ├── NumberFormatter.kt               -- Price & percentage formatting
    ├── DateUtils.kt
    ├── NetworkResult.kt                 -- Sealed class: Success/Error/Loading
    └── Constants.kt
```

---

## Dependencies (build.gradle.kts)

```kotlin
// Compose BOM
val composeBom = "2024.12.01"

// Core
implementation("androidx.core:core-ktx:1.15.0")
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
implementation("androidx.activity:activity-compose:1.9.3")

// Compose
implementation(platform("androidx.compose:compose-bom:$composeBom"))
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.ui:ui-graphics")
implementation("androidx.compose.ui:ui-tooling-preview")
implementation("androidx.compose.material3:material3")
implementation("androidx.compose.material:material-icons-extended")

// Navigation
implementation("androidx.navigation:navigation-compose:2.8.5")

// Hilt
implementation("com.google.dagger:hilt-android:2.53.1")
kapt("com.google.dagger:hilt-compiler:2.53.1")
implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

// Retrofit + Moshi
implementation("com.squareup.retrofit2:retrofit:2.11.0")
implementation("com.squareup.retrofit2:converter-moshi:2.11.0")
implementation("com.squareup.moshi:moshi-kotlin:1.15.1")
kapt("com.squareup.moshi:moshi-kotlin-codegen:1.15.1")
implementation("com.squareup.okhttp3:okhttp:4.12.0")
implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

// Room
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
kapt("androidx.room:room-compiler:2.6.1")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")

// Coil (image loading)
implementation("io.coil-kt:coil-compose:2.7.0")

// Charts - Vico
implementation("com.patrykandpatrick.vico:compose-m3:2.0.1")

// WorkManager
implementation("androidx.work:work-runtime-ktx:2.10.0")
implementation("androidx.hilt:hilt-work:1.2.0")
kapt("androidx.hilt:hilt-compiler:1.2.0")

// DataStore Preferences
implementation("androidx.datastore:datastore-preferences:1.1.1")

// Testing
testImplementation("junit:junit:4.13.2")
testImplementation("io.mockk:mockk:1.13.13")
testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")
androidTestImplementation("androidx.compose.ui:ui-test-junit4")
```

---

## Implementation Order

### Phase 1: Project Setup
1. Create Android project with Kotlin DSL Gradle
2. Configure dependencies and version catalog
3. Set up Hilt application class
4. Create theme (crypto-themed colors, dark/light)
5. Set up navigation shell with bottom nav bar
6. Create NetworkResult sealed class for API state handling

### Phase 2: Data Layer - Network
7. Define Retrofit API interface (CoinGeckoApi)
8. Create DTO classes for all API responses
9. Build OkHttp client with logging + rate limit interceptor
10. Set up Retrofit instance in Hilt NetworkModule

### Phase 3: Data Layer - Local
11. Define Room entities (CoinEntity, WatchlistEntity, AlertEntity)
12. Create DAOs with Flow-returning queries
13. Build Room database
14. Implement DTO <-> Entity <-> Domain mappers

### Phase 4: Domain Layer
15. Define domain models (Coin, CoinDetail, ChartData, PriceAlert)
16. Define repository interfaces
17. Implement repository classes with caching strategy:
    - Fetch from API -> Save to Room -> Return Flow from Room
    - On error -> Return cached data from Room
18. Implement all use cases

### Phase 5: Presentation - Market & Search
19. Market screen with ViewModel (coin list, sorting, pull-to-refresh)
20. Shimmer loading placeholders
21. CoinListItem composable with price change coloring
22. Search screen with debounced search + recent history

### Phase 6: Presentation - Detail & Charts
23. Coin detail screen with ViewModel
24. Price chart with Vico (period selector)
25. Market stats grid
26. Watchlist toggle (heart icon)

### Phase 7: Watchlist & Alerts
27. Watchlist screen with live prices
28. Set price alert dialog
29. WorkManager periodic alert checker
30. Notification channel + alert notifications

### Phase 8: Settings & Polish
31. Settings screen (theme, currency, cache)
32. Error states with retry buttons
33. Empty states
34. Swipe-to-remove from watchlist
35. Pull-to-refresh animations
36. Shimmer loading effects

### Phase 9: Testing
37. Unit tests for repositories (API + caching logic)
38. Unit tests for use cases
39. Unit tests for ViewModels
40. Compose UI tests for key screens

---

## Offline Caching Strategy

```
User opens app
    │
    ├── Has network?
    │   ├── YES: Fetch from CoinGecko API
    │   │         ├── Success: Save to Room DB → Emit data from Room Flow
    │   │         └── Error: Emit cached data from Room + show error snackbar
    │   │
    │   └── NO: Emit cached data from Room DB
    │            └── No cache? → Show offline empty state
    │
    └── Cache TTL: 5 minutes for market data
                   15 minutes for coin detail
                   30 minutes for chart data
```

---

## Database Schema

### coins (cached market data)
| Column | Type | Description |
|--------|------|-------------|
| id | String (PK) | CoinGecko coin ID (e.g., "bitcoin") |
| symbol | String | "btc" |
| name | String | "Bitcoin" |
| image | String | URL to coin icon |
| currentPrice | Double | Current price in fiat |
| marketCap | Long | Market capitalization |
| marketCapRank | Int | Rank by market cap |
| priceChangePercent24h | Double | 24h price change % |
| high24h | Double | 24h high |
| low24h | Double | 24h low |
| totalVolume | Double | 24h trading volume |
| circulatingSupply | Double | Circulating supply |
| sparklineData | String | JSON array of 7d prices |
| lastUpdated | Long | Cache timestamp |

### watchlist
| Column | Type | Description |
|--------|------|-------------|
| coinId | String (PK) | Reference to coin ID |
| addedAt | Long | When user added to watchlist |

### alerts
| Column | Type | Description |
|--------|------|-------------|
| id | Long (PK, auto) | Unique ID |
| coinId | String | Coin to watch |
| coinName | String | Display name |
| targetPrice | Double | Alert trigger price |
| isAbove | Boolean | Alert when price goes above (true) or below (false) |
| isActive | Boolean | Whether alert is enabled |
| createdAt | Long | Creation timestamp |

### search_history
| Column | Type | Description |
|--------|------|-------------|
| query | String (PK) | Search term |
| timestamp | Long | When searched |

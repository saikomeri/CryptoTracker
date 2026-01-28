# CryptoTracker

A real-time cryptocurrency tracking Android application built with Kotlin and Jetpack Compose, featuring Clean Architecture with MVVM pattern, CoinGecko API integration, offline caching, and price alerts.

## Features

- **Market Overview** - Top 100 cryptocurrencies with live prices, 24h changes, and sparkline charts
- **Search** - Real-time debounced search across all cryptocurrencies
- **Coin Detail** - Interactive animated price charts (24h/7d/30d/90d/1y), market stats, ATH/ATL
- **Watchlist** - Favorite coins with persisted watchlist and live price updates
- **Price Alerts** - WorkManager-powered background price monitoring with notifications
- **Pull-to-Refresh** - Swipe down to refresh market data
- **Shimmer Loading** - Polished loading states with shimmer placeholders
- **Dark/Light Theme** - Full Material Design 3 theming support
- **Offline Caching** - Room database caching with TTL-based refresh strategy

## Tech Stack

| Technology | Purpose |
|-----------|---------|
| **Kotlin** | Primary language |
| **Jetpack Compose** | Declarative UI with Material Design 3 |
| **MVVM + Clean Architecture** | Separation of concerns with data/domain/presentation layers |
| **Hilt** | Dependency Injection |
| **Retrofit + Moshi** | REST API communication with CoinGecko |
| **OkHttp** | HTTP client with rate limiting interceptor |
| **Room** | Local database for offline caching and watchlist |
| **Kotlin Coroutines + Flow** | Reactive async data streams |
| **Jetpack Navigation** | Type-safe Compose navigation with bottom nav |
| **Coil** | Async image loading for coin icons |
| **WorkManager** | Background price alert checking |
| **Canvas API** | Custom animated sparkline and price charts |

## Architecture

```
com.sai.cryptotracker/
├── data/
│   ├── remote/      # Retrofit API, DTOs, rate limit interceptor
│   ├── local/       # Room database, DAOs, entities
│   ├── repository/  # Repository implementations with caching
│   └── mapper/      # DTO <-> Entity <-> Domain mappers
├── domain/
│   ├── model/       # Business models (Coin, CoinDetail, ChartData, PriceAlert)
│   └── repository/  # Repository interfaces
├── presentation/
│   ├── market/      # Market listing with sorting and pull-to-refresh
│   ├── search/      # Debounced coin search
│   ├── detail/      # Coin detail with interactive price charts
│   ├── watchlist/   # User's favorite coins
│   ├── settings/    # Theme toggle and app info
│   ├── common/      # Shared composables (CoinListItem, SparklineChart, ShimmerLoading)
│   ├── navigation/  # Navigation graph and bottom nav
│   └── theme/       # M3 color scheme and theming
├── di/              # Hilt modules (App, Network, Repository)
├── worker/          # WorkManager price alert checker
└── util/            # Number formatting, constants, NetworkResult sealed class
```

## API

Uses the [CoinGecko API](https://www.coingecko.com/en/api) (free tier, no API key required):

- `GET /coins/markets` - Market listing with sparkline data
- `GET /coins/{id}` - Detailed coin information
- `GET /coins/{id}/market_chart` - Historical price data for charts
- `GET /search` - Search coins by name/symbol

## Offline Strategy

```
App Request → Check Cache TTL → Valid? → Return Room data
                                  ↓ Invalid
                          Fetch from CoinGecko API
                                  ↓
                          Save to Room DB → Return data
                                  ↓ On Error
                          Return cached data + show error
```

## Setup

1. Clone the repository
2. Open in Android Studio (Hedgehog or newer)
3. Sync Gradle and run on emulator/device (API 26+)
4. No API key needed — uses CoinGecko free tier

## Author

**Sai Sampurna Komeri** - Android Mobile Developer

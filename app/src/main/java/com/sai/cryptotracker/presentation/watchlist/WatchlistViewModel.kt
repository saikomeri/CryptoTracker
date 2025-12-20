package com.sai.cryptotracker.presentation.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sai.cryptotracker.domain.model.Coin
import com.sai.cryptotracker.domain.repository.CoinRepository
import com.sai.cryptotracker.domain.repository.WatchlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WatchlistUiState(
    val coins: List<Coin> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val coinRepository: CoinRepository,
    private val watchlistRepository: WatchlistRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WatchlistUiState())
    val uiState: StateFlow<WatchlistUiState> = _uiState.asStateFlow()

    init {
        loadWatchlist()
    }

    fun removeFromWatchlist(coinId: String) {
        viewModelScope.launch {
            watchlistRepository.removeFromWatchlist(coinId)
        }
    }

    private fun loadWatchlist() {
        viewModelScope.launch {
            coinRepository.getWatchlistCoins().collect { coins ->
                _uiState.value = WatchlistUiState(
                    coins = coins,
                    isLoading = false
                )
            }
        }
    }
}

package com.sai.cryptotracker.presentation.market

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sai.cryptotracker.domain.model.Coin
import com.sai.cryptotracker.domain.repository.CoinRepository
import com.sai.cryptotracker.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MarketUiState(
    val coins: List<Coin> = emptyList(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val sortBy: SortOption = SortOption.MARKET_CAP
)

enum class SortOption { MARKET_CAP, PRICE, CHANGE_24H }

@HiltViewModel
class MarketViewModel @Inject constructor(
    private val coinRepository: CoinRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MarketUiState())
    val uiState: StateFlow<MarketUiState> = _uiState.asStateFlow()

    init {
        loadMarketData()
    }

    fun refresh() {
        _uiState.value = _uiState.value.copy(isRefreshing = true)
        loadMarketData(forceRefresh = true)
    }

    fun setSortOption(option: SortOption) {
        _uiState.value = _uiState.value.copy(sortBy = option)
        sortCoins()
    }

    private fun loadMarketData(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            coinRepository.getMarketData(forceRefresh).collect { result ->
                when (result) {
                    is NetworkResult.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                    }
                    is NetworkResult.Success -> {
                        _uiState.value = _uiState.value.copy(
                            coins = result.data,
                            isLoading = false,
                            isRefreshing = false,
                            error = null
                        )
                        sortCoins()
                    }
                    is NetworkResult.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = result.message
                        )
                    }
                }
            }
        }
    }

    private fun sortCoins() {
        val sorted = when (_uiState.value.sortBy) {
            SortOption.MARKET_CAP -> _uiState.value.coins.sortedBy { it.marketCapRank }
            SortOption.PRICE -> _uiState.value.coins.sortedByDescending { it.currentPrice }
            SortOption.CHANGE_24H -> _uiState.value.coins.sortedByDescending { it.priceChangePercent24h }
        }
        _uiState.value = _uiState.value.copy(coins = sorted)
    }
}

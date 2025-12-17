package com.sai.cryptotracker.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sai.cryptotracker.domain.model.ChartData
import com.sai.cryptotracker.domain.model.CoinDetail
import com.sai.cryptotracker.domain.model.PriceAlert
import com.sai.cryptotracker.domain.repository.AlertRepository
import com.sai.cryptotracker.domain.repository.CoinRepository
import com.sai.cryptotracker.domain.repository.WatchlistRepository
import com.sai.cryptotracker.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CoinDetailUiState(
    val coinDetail: CoinDetail? = null,
    val chartData: ChartData? = null,
    val isInWatchlist: Boolean = false,
    val selectedPeriod: ChartPeriod = ChartPeriod.SEVEN_DAYS,
    val isLoading: Boolean = true,
    val isChartLoading: Boolean = true,
    val error: String? = null
)

enum class ChartPeriod(val days: String, val label: String) {
    ONE_DAY("1", "24h"),
    SEVEN_DAYS("7", "7d"),
    THIRTY_DAYS("30", "30d"),
    NINETY_DAYS("90", "90d"),
    ONE_YEAR("365", "1y")
}

@HiltViewModel
class CoinDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val coinRepository: CoinRepository,
    private val watchlistRepository: WatchlistRepository,
    private val alertRepository: AlertRepository
) : ViewModel() {

    private val coinId: String = savedStateHandle.get<String>("coinId") ?: ""

    private val _uiState = MutableStateFlow(CoinDetailUiState())
    val uiState: StateFlow<CoinDetailUiState> = _uiState.asStateFlow()

    init {
        loadCoinDetail()
        loadChartData()
        observeWatchlist()
    }

    fun selectPeriod(period: ChartPeriod) {
        _uiState.value = _uiState.value.copy(selectedPeriod = period, isChartLoading = true)
        loadChartData()
    }

    fun toggleWatchlist() {
        viewModelScope.launch {
            if (_uiState.value.isInWatchlist) {
                watchlistRepository.removeFromWatchlist(coinId)
            } else {
                watchlistRepository.addToWatchlist(coinId)
            }
        }
    }

    fun setAlert(targetPrice: Double, isAbove: Boolean) {
        viewModelScope.launch {
            val coinName = _uiState.value.coinDetail?.name ?: coinId
            alertRepository.addAlert(
                PriceAlert(
                    coinId = coinId,
                    coinName = coinName,
                    targetPrice = targetPrice,
                    isAbove = isAbove
                )
            )
        }
    }

    private fun loadCoinDetail() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            when (val result = coinRepository.getCoinDetail(coinId)) {
                is NetworkResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        coinDetail = result.data,
                        isLoading = false
                    )
                }
                is NetworkResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    private fun loadChartData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isChartLoading = true)
            when (val result = coinRepository.getChartData(coinId, _uiState.value.selectedPeriod.days)) {
                is NetworkResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        chartData = result.data,
                        isChartLoading = false
                    )
                }
                is NetworkResult.Error -> {
                    _uiState.value = _uiState.value.copy(isChartLoading = false)
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    private fun observeWatchlist() {
        viewModelScope.launch {
            watchlistRepository.isInWatchlist(coinId).collect { isInWatchlist ->
                _uiState.value = _uiState.value.copy(isInWatchlist = isInWatchlist)
            }
        }
    }
}

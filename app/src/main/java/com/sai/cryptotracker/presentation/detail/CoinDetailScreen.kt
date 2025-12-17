package com.sai.cryptotracker.presentation.detail

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.sai.cryptotracker.domain.model.ChartPoint
import com.sai.cryptotracker.presentation.theme.ChartGradientEnd
import com.sai.cryptotracker.presentation.theme.ChartGradientStart
import com.sai.cryptotracker.presentation.theme.ChartLine
import com.sai.cryptotracker.presentation.theme.PriceDown
import com.sai.cryptotracker.presentation.theme.PriceUp
import com.sai.cryptotracker.util.NumberFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinDetailScreen(
    onNavigateBack: () -> Unit,
    viewModel: CoinDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.coinDetail?.name ?: "Loading...") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = viewModel::toggleWatchlist) {
                        Icon(
                            imageVector = if (uiState.isInWatchlist) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Watchlist",
                            tint = if (uiState.isInWatchlist) PriceDown else MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }
        } else if (uiState.coinDetail == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) { Text("Failed to load coin details") }
        } else {
            val coin = uiState.coinDetail!!

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = coin.image,
                            contentDescription = coin.name,
                            modifier = Modifier.size(48.dp).clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = NumberFormatter.formatPrice(coin.currentPrice),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                            val isPositive = coin.priceChangePercent24h >= 0
                            Text(
                                text = NumberFormatter.formatPercentage(coin.priceChangePercent24h),
                                style = MaterialTheme.typography.titleMedium,
                                color = if (isPositive) PriceUp else PriceDown,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                // Price Chart
                item {
                    Card(elevation = CardDefaults.cardElevation(1.dp)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Period Selector
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                ChartPeriod.entries.forEach { period ->
                                    FilterChip(
                                        selected = uiState.selectedPeriod == period,
                                        onClick = { viewModel.selectPeriod(period) },
                                        label = { Text(period.label, style = MaterialTheme.typography.labelSmall) }
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))

                            if (uiState.isChartLoading) {
                                Box(
                                    modifier = Modifier.fillMaxWidth().height(200.dp),
                                    contentAlignment = Alignment.Center
                                ) { CircularProgressIndicator() }
                            } else {
                                uiState.chartData?.let { chartData ->
                                    PriceChart(
                                        points = chartData.prices,
                                        modifier = Modifier.fillMaxWidth().height(200.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Market Stats
                item {
                    Card(elevation = CardDefaults.cardElevation(1.dp)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Market Stats",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            StatRow("Market Cap", NumberFormatter.formatMarketCap(coin.marketCap))
                            StatRow("24h Volume", NumberFormatter.formatVolume(coin.totalVolume))
                            StatRow("24h High", NumberFormatter.formatPrice(coin.high24h))
                            StatRow("24h Low", NumberFormatter.formatPrice(coin.low24h))
                            StatRow("Circulating Supply", NumberFormatter.formatSupply(coin.circulatingSupply) + " ${coin.symbol.uppercase()}")
                            if (coin.totalSupply > 0) {
                                StatRow("Total Supply", NumberFormatter.formatSupply(coin.totalSupply) + " ${coin.symbol.uppercase()}")
                            }
                            StatRow("All-Time High", NumberFormatter.formatPrice(coin.ath))
                            StatRow("All-Time Low", NumberFormatter.formatPrice(coin.atl))
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(32.dp)) }
            }
        }
    }
}

@Composable
private fun PriceChart(
    points: List<ChartPoint>,
    modifier: Modifier = Modifier
) {
    if (points.size < 2) return

    var animPlayed by remember { mutableStateOf(false) }
    val animProgress by animateFloatAsState(
        targetValue = if (animPlayed) 1f else 0f,
        animationSpec = tween(1000),
        label = "chart"
    )
    LaunchedEffect(points) { animPlayed = true }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val prices = points.map { it.price }
        val min = prices.min()
        val max = prices.max()
        val range = if (max - min > 0) max - min else 1.0
        val visibleCount = (points.size * animProgress).toInt().coerceAtLeast(2)

        val path = Path()
        val fillPath = Path()

        for (i in 0 until visibleCount) {
            val x = (i.toFloat() / (points.size - 1)) * width
            val y = height - ((prices[i] - min) / range * height).toFloat()
            if (i == 0) {
                path.moveTo(x, y)
                fillPath.moveTo(x, y)
            } else {
                path.lineTo(x, y)
                fillPath.lineTo(x, y)
            }
        }

        // Fill gradient
        val lastX = ((visibleCount - 1).toFloat() / (points.size - 1)) * width
        fillPath.lineTo(lastX, height)
        fillPath.lineTo(0f, height)
        fillPath.close()

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(ChartGradientStart, ChartGradientEnd)
            )
        )

        drawPath(
            path = path,
            color = ChartLine,
            style = Stroke(width = 2.5f)
        )
    }
}

@Composable
private fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

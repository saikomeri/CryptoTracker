package com.sai.cryptotracker.presentation.common

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import com.sai.cryptotracker.presentation.theme.PriceDown
import com.sai.cryptotracker.presentation.theme.PriceUp

@Composable
fun SparklineChart(
    data: List<Double>,
    isPositive: Boolean,
    modifier: Modifier = Modifier
) {
    if (data.size < 2) return

    val color = if (isPositive) PriceUp else PriceDown

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val min = data.min()
        val max = data.max()
        val range = if (max - min > 0) max - min else 1.0

        val path = Path()
        data.forEachIndexed { index, value ->
            val x = (index.toFloat() / (data.size - 1)) * width
            val y = height - ((value - min) / range * height).toFloat()
            if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }

        drawPath(
            path = path,
            color = color,
            style = Stroke(width = 1.5f)
        )
    }
}

package com.yummy.bread.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.glassPanel(
    shape: Shape = RoundedCornerShape(24.dp),
    opacity: Float = 0.4f,
    borderOpacity: Float = 0.1f,
    padding: Dp = 0.dp
): Modifier = this
    .clip(shape)
    .background(
        Color(0xFF171F33).copy(alpha = opacity)
    )
    .border(
        width = 1.dp,
        brush = Brush.verticalGradient(
            colors = listOf(
                Color.White.copy(alpha = borderOpacity + 0.05f),
                Color.White.copy(alpha = borderOpacity)
            )
        ),
        shape = shape
    )
    .padding(padding)

fun Modifier.glassPanelHeavy(
    shape: Shape = RoundedCornerShape(32.dp),
    opacity: Float = 0.6f,
    borderOpacity: Float = 0.08f
): Modifier = this
    .clip(shape)
    .background(
        Color(0xFF0B1326).copy(alpha = opacity)
    )
    .border(
        width = 1.dp,
        brush = Brush.verticalGradient(
            colors = listOf(
                Color.White.copy(alpha = borderOpacity + 0.02f),
                Color.White.copy(alpha = borderOpacity)
            )
        ),
        shape = shape
    )

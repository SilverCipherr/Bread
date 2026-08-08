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
import androidx.compose.ui.geometry.Offset
import com.yummy.bread.ui.theme.GlassColor

import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.RenderEffect
import androidx.compose.ui.graphics.asComposeRenderEffect
import android.graphics.RenderEffect as AndroidRenderEffect
import android.os.Build

fun Modifier.glassPanel(
    shape: Shape = RoundedCornerShape(24.dp),
    opacity: Float = 0.12f,
    borderOpacity: Float = 0.3f,
    blur: Float = 25f
): Modifier = this
    .then(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Modifier.graphicsLayer {
                renderEffect = AndroidRenderEffect.createBlurEffect(blur, blur, android.graphics.Shader.TileMode.MIRROR).asComposeRenderEffect()
            }
        } else this
    )
    .background(
        brush = Brush.linearGradient(
            colors = listOf(
                GlassColor.copy(alpha = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) opacity else opacity + 0.15f),
                GlassColor.copy(alpha = (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) opacity else opacity + 0.15f) * 0.4f)
            ),
            start = Offset(0f, 0f),
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
        ),
        shape = shape
    )
    .border(
        width = 1.dp,
        brush = Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = borderOpacity),
                Color.White.copy(alpha = borderOpacity * 0.1f),
                Color.White.copy(alpha = borderOpacity * 0.6f)
            ),
            start = Offset(0f, 0f),
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
        ),
        shape = shape
    )
    .clip(shape)

fun Modifier.glassPanelHeavy(
    shape: Shape = RoundedCornerShape(32.dp),
    opacity: Float = 0.28f,
    borderOpacity: Float = 0.4f,
    blur: Float = 40f
): Modifier = this
    .then(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Modifier.graphicsLayer {
                renderEffect = AndroidRenderEffect.createBlurEffect(blur, blur, android.graphics.Shader.TileMode.MIRROR).asComposeRenderEffect()
            }
        } else this
    )
    .background(
        brush = Brush.linearGradient(
            colors = listOf(
                GlassColor.copy(alpha = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) opacity else opacity + 0.2f),
                GlassColor.copy(alpha = (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) opacity else opacity + 0.2f) * 0.5f)
            ),
            start = Offset(0f, 0f),
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
        ),
        shape = shape
    )
    .border(
        width = 1.dp,
        brush = Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = borderOpacity),
                Color.White.copy(alpha = borderOpacity * 0.05f),
                Color.White.copy(alpha = borderOpacity * 0.7f)
            ),
            start = Offset(0f, 0f),
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
        ),
        shape = shape
    )
    .clip(shape)

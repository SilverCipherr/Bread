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
import com.yummy.bread.ui.theme.GlassColor

import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.RenderEffect
import androidx.compose.ui.graphics.asComposeRenderEffect
import android.graphics.RenderEffect as AndroidRenderEffect
import android.os.Build

fun Modifier.glassPanel(
    shape: Shape = RoundedCornerShape(24.dp),
    opacity: Float = 0.1f,
    borderOpacity: Float = 0.2f,
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
        GlassColor.copy(alpha = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) opacity else opacity + 0.15f),
        shape = shape
    )
    .border(
        width = 0.5.dp,
        brush = Brush.verticalGradient(
            colors = listOf(
                GlassColor.copy(alpha = borderOpacity),
                GlassColor.copy(alpha = borderOpacity * 0.5f)
            )
        ),
        shape = shape
    )
    .clip(shape)

fun Modifier.glassPanelHeavy(
    shape: Shape = RoundedCornerShape(32.dp),
    opacity: Float = 0.25f,
    borderOpacity: Float = 0.3f,
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
        GlassColor.copy(alpha = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) opacity else opacity + 0.2f),
        shape = shape
    )
    .border(
        width = 0.5.dp,
        brush = Brush.verticalGradient(
            colors = listOf(
                GlassColor.copy(alpha = borderOpacity),
                GlassColor.copy(alpha = borderOpacity * 0.4f)
            )
        ),
        shape = shape
    )
    .clip(shape)

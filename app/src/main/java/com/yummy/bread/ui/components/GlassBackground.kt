package com.yummy.bread.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.yummy.bread.ui.theme.*

@Composable
fun GlassBackground(
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "background")
    
    val phase1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(25000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "phase1"
    )
    
    val phase2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(30000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "phase2"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val maxRadius = size.maxDimension * 1.8f
            
            // Layer 1: Indigo
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(Glow1.copy(alpha = 0.6f), Color.Transparent),
                    center = Offset(
                        x = size.width * (-0.2f + 1.4f * phase1),
                        y = size.height * (-0.2f + 1.4f * phase2)
                    ),
                    radius = maxRadius
                )
            )
            
            // Layer 2: Sky
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(Glow2.copy(alpha = 0.5f), Color.Transparent),
                    center = Offset(
                        x = size.width * (1.2f - 1.4f * phase2),
                        y = size.height * (-0.2f + 1.4f * phase1)
                    ),
                    radius = maxRadius * 1.2f
                )
            )
            
            // Layer 3: Rose
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(Glow3.copy(alpha = 0.5f), Color.Transparent),
                    center = Offset(
                        x = size.width * (-0.2f + 1.4f * phase2),
                        y = size.height * (1.2f - 1.4f * phase1)
                    ),
                    radius = maxRadius * 1.1f
                )
            )
            
            // Layer 4: Green
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(Glow4.copy(alpha = 0.4f), Color.Transparent),
                    center = Offset(
                        x = size.width * (1.2f - 1.4f * phase1),
                        y = size.height * (1.2f - 1.4f * phase2)
                    ),
                    radius = maxRadius * 0.9f
                )
            )
        }
        
        content()
    }
}

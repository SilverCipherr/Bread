package com.yummy.bread.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yummy.bread.data.CategorySpend
import com.yummy.bread.data.TrendPoint
import com.yummy.bread.ui.theme.Primary
import com.yummy.bread.ui.theme.Tertiary

@Composable
fun DonutChart(
    breakdown: List<CategorySpend>,
    totalText: String,
    symbol: String,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(220.dp)) {
            var startAngle = -90f
            if (breakdown.isEmpty()) {
                drawArc(
                    color = Color.White.copy(alpha = 0.1f),
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = 30f, cap = StrokeCap.Round)
                )
            } else {
                breakdown.forEach { item ->
                    val sweepAngle = item.percentage * 360f
                    drawArc(
                        color = item.color,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        style = Stroke(width = 30f, cap = StrokeCap.Round)
                    )
                    startAngle += sweepAngle
                }
            }
        }
        
        // Inner circle for text
        Box(
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .background(Color(0xFF060E20).copy(alpha = 0.8f))
                .border(1.dp, Color.White.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Total",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    "$symbol$totalText",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun BalanceTrendChart(
    points: List<TrendPoint>,
    modifier: Modifier = Modifier
) {
    if (points.size < 2) return

    Box(modifier = modifier.height(180.dp).fillMaxWidth()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val maxVal = points.maxOf { it.value }.coerceAtLeast(1f)
            val minVal = points.minOf { it.value }
            val range = (maxVal - minVal).coerceAtLeast(1f)

            val path = Path()
            val fillPath = Path()
            
            // Generate coordinates
            val coords = points.mapIndexed { i, point ->
                Offset(
                    x = (i.toFloat() / (points.size - 1)) * width,
                    y = height - ((point.value - minVal) / range) * (height * 0.8f) - (height * 0.1f)
                )
            }

            if (coords.isNotEmpty()) {
                path.moveTo(coords[0].x, coords[0].y)
                fillPath.moveTo(coords[0].x, height)
                fillPath.lineTo(coords[0].x, coords[0].y)

                for (i in 0 until coords.size - 1) {
                    val from = coords[i]
                    val to = coords[i + 1]
                    
                    // Bezier curve points
                    val control1 = Offset(from.x + (to.x - from.x) / 2f, from.y)
                    val control2 = Offset(from.x + (to.x - from.x) / 2f, to.y)
                    
                    path.cubicTo(control1.x, control1.y, control2.x, control2.y, to.x, to.y)
                    fillPath.cubicTo(control1.x, control1.y, control2.x, control2.y, to.x, to.y)
                }
                
                fillPath.lineTo(coords.last().x, height)
                fillPath.close()
            }

            // Draw Fill
            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(Primary.copy(alpha = 0.3f), Color.Transparent)
                )
            )

            // Draw Line
            drawPath(
                path = path,
                color = Primary,
                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
            )
            
            // Draw Data Points (Last two or all)
            coords.forEach { coord ->
                drawCircle(
                    color = Primary,
                    radius = 5.dp.toPx(),
                    center = coord
                )
                drawCircle(
                    color = Color.White,
                    radius = 3.dp.toPx(),
                    center = coord
                )
            }
        }
        
        // X-Axis Labels
        Row(
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).padding(bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            points.forEach { point ->
                Text(
                    text = point.label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun TimeToggle(
    selectedRange: String,
    onRangeSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf("Weekly", "Monthly", "Yearly").forEach { range ->
            val isSelected = selectedRange == range
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .padding(horizontal = 4.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) {
                            Brush.verticalGradient(
                                listOf(Primary.copy(alpha = 0.2f), Primary.copy(alpha = 0.5f))
                            )
                        } else {
                            Brush.linearGradient(listOf(Color.White.copy(alpha = 0.05f), Color.White.copy(alpha = 0.05f)))
                        }
                    )
                    .border(
                        1.dp,
                        if (isSelected) Primary.copy(alpha = 0.3f) else Color.White.copy(alpha = 0.05f),
                        CircleShape
                    )
                    .clickable { onRangeSelected(range) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = range,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

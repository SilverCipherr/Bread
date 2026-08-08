package com.yummy.bread.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yummy.bread.data.CategoryBudget
import com.yummy.bread.ui.theme.Primary
import com.yummy.bread.ui.theme.PrimaryContainer
import com.yummy.bread.ui.theme.Tertiary

@Composable
fun TotalBudgetSummaryCard(
    spent: Double,
    total: Double,
    symbol: String,
    modifier: Modifier = Modifier
) {
    val progress = if (total > 0) (spent / total).toFloat().coerceIn(0f, 1f) else 0f
    
    GlassCard(modifier = modifier.fillMaxWidth(), shape = RoundedCornerShape(32.dp)) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "TOTAL MONTHLY BUDGET",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    "$symbol${spent.toInt()}",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    " / $symbol${total.toInt()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Large Progress Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.05f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progress)
                        .clip(CircleShape)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Primary, Tertiary)
                            )
                        )
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "${(progress * 100).toInt()}% Spent",
                    style = MaterialTheme.typography.labelSmall,
                    color = Primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "$symbol${(total - spent).coerceAtLeast(0.0).toInt()} Left",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun CategoryBudgetCard(
    budget: CategoryBudget,
    symbol: String,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icon = when (budget.icon) {
        "shopping_cart" -> Icons.Default.ShoppingCart
        "movie" -> Icons.Default.Movie
        "flight" -> Icons.Default.Flight
        "restaurant" -> Icons.Default.Restaurant
        "directions_car" -> Icons.Default.DirectionsCar
        "shopping_bag" -> Icons.Default.ShoppingBag
        "electric_bolt" -> Icons.Default.ElectricBolt
        "trending_up" -> Icons.AutoMirrored.Filled.TrendingUp
        "card_giftcard" -> Icons.Default.CardGiftcard
        "payments" -> Icons.Default.Payments
        "category" -> Icons.Default.Category
        else -> Icons.Default.Category
    }
    
    val progressColor = if (budget.isOverBudget) Color(0xFFFFB4AB) else Tertiary
    
    GlassCard(modifier = modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(icon, contentDescription = null, tint = progressColor, modifier = Modifier.size(20.dp))
                    }
                    Text(budget.category, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    Text("$symbol${budget.spent.toInt()}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                    Text("of $symbol${budget.limit.toInt()}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                }
            }
            
            // Progress Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.05f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(budget.progress)
                        .clip(CircleShape)
                        .background(
                            if (budget.isOverBudget) {
                                Brush.horizontalGradient(listOf(Primary, Color(0xFFFFB4AB)))
                            } else {
                                Brush.horizontalGradient(listOf(Primary, Tertiary))
                            }
                        )
                )
            }
            
            if (budget.isOverBudget) {
                Text(
                    "Over budget by $symbol${(budget.spent - budget.limit).toInt()}",
                    color = Color(0xFFFFB4AB),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDeleteClick, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Budget", tint = Color.Gray.copy(alpha = 0.6f), modifier = Modifier.size(16.dp))
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(onClick = onEditClick, modifier = Modifier.height(32.dp)) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Edit", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

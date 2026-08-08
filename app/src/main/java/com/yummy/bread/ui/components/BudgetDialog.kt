package com.yummy.bread.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.yummy.bread.data.CategoryBudget
import com.yummy.bread.ui.theme.Background
import com.yummy.bread.ui.theme.Primary
import com.yummy.bread.ui.theme.Tertiary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetDialog(
    onDismiss: () -> Unit,
    onConfirm: (category: String, limit: Double, icon: String) -> Unit,
    initialBudget: CategoryBudget? = null,
    isEditing: Boolean = false
) {
    var selectedCategory by remember { mutableStateOf(initialBudget?.category ?: "Food") }
    var limit by remember { mutableStateOf(initialBudget?.limit?.toString() ?: "") }

    val categories = listOf(
        BudgetCategoryItem("Food", Icons.Default.Restaurant, "restaurant"),
        BudgetCategoryItem("Transport", Icons.Default.DirectionsCar, "directions_car"),
        BudgetCategoryItem("Shopping", Icons.Default.ShoppingBag, "shopping_bag"),
        BudgetCategoryItem("Utilities", Icons.Default.ElectricBolt, "electric_bolt"),
        BudgetCategoryItem("Invest", Icons.AutoMirrored.Filled.TrendingUp, "trending_up"),
        BudgetCategoryItem("Gift", Icons.Default.CardGiftcard, "card_giftcard"),
        BudgetCategoryItem("Salary", Icons.Default.Payments, "payments"),
        BudgetCategoryItem("Groceries", Icons.Default.ShoppingCart, "shopping_cart"),
        BudgetCategoryItem("Entertainment", Icons.Default.Movie, "movie"),
        BudgetCategoryItem("Other", Icons.Default.Category, "category")
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(32.dp)),
            color = Background
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    text = if (isEditing) "Edit Budget" else "Set New Budget",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Primary
                )

                if (!isEditing) {
                    // Category Picker
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text("SELECT CATEGORY", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        Spacer(modifier = Modifier.height(16.dp))
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(4),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.height(200.dp)
                        ) {
                            items(categories) { cat ->
                                val isSelected = selectedCategory == cat.name
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.clickable { selectedCategory = cat.name }
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(CircleShape)
                                            .background(if (isSelected) Primary else Color.White.copy(alpha = 0.05f))
                                            .border(1.dp, if (isSelected) Primary else Color.White.copy(alpha = 0.1f), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            cat.icon,
                                            contentDescription = null,
                                            tint = if (isSelected) Color.Black else Color.White,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        cat.name, 
                                        style = MaterialTheme.typography.labelSmall, 
                                        color = if (isSelected) Primary else Color.Gray,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // Show current category icon and name
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Primary.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            val cat = categories.find { it.name == selectedCategory }
                            Icon(cat?.icon ?: Icons.Default.Category, contentDescription = null, tint = Primary)
                        }
                        Text(selectedCategory, style = MaterialTheme.typography.headlineSmall)
                    }
                }

                // Limit Input
                TextField(
                    value = limit,
                    onValueChange = { if (it.all { c -> c.isDigit() || it == "." }) limit = it },
                    label = { Text("Monthly Limit") },
                    prefix = { Text("$ ") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.05f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.05f),
                        focusedIndicatorColor = Primary,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel", color = Color.Gray)
                    }
                    Button(
                        onClick = {
                            val limitVal = limit.toDoubleOrNull() ?: 0.0
                            if (limitVal > 0) {
                                val icon = categories.find { it.name == selectedCategory }?.iconName ?: "category"
                                onConfirm(selectedCategory, limitVal, icon)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Primary),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Confirm", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

data class BudgetCategoryItem(val name: String, val icon: ImageVector, val iconName: String)

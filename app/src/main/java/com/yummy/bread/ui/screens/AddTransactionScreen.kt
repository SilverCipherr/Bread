package com.yummy.bread.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.yummy.bread.BreadViewModel
import com.yummy.bread.data.Transaction
import com.yummy.bread.data.TransactionType
import com.yummy.bread.ui.components.GlassCard
import com.yummy.bread.ui.components.MoltenButton
import com.yummy.bread.ui.theme.Background
import com.yummy.bread.ui.theme.Primary
import java.util.*

import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.automirrored.filled.Notes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    viewModel: BreadViewModel,
    onDismiss: () -> Unit
) {
    var type by remember { mutableStateOf(TransactionType.EXPENSE) }
    var amount by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Food") }
    var note by remember { mutableStateOf("") }

    val categories = listOf(
        CategoryItem("Food", Icons.Default.Restaurant),
        CategoryItem("Transport", Icons.Default.DirectionsCar),
        CategoryItem("Shopping", Icons.Default.ShoppingBag),
        CategoryItem("Utilities", Icons.Default.ElectricBolt),
        CategoryItem("Invest", Icons.AutoMirrored.Filled.TrendingUp),
        CategoryItem("Gift", Icons.Default.CardGiftcard),
        CategoryItem("Salary", Icons.Default.Payments),
        CategoryItem("Groceries", Icons.Default.ShoppingCart),
        CategoryItem("Entertainment", Icons.Default.Movie),
        CategoryItem("Other", Icons.Default.Category)
    )

    val focusManager = androidx.compose.ui.platform.LocalFocusManager.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("New Transaction", style = MaterialTheme.typography.headlineMedium, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color.Transparent
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Type Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(Color.White.copy(alpha = 0.05f))
                        .padding(4.dp)
                ) {
                    TransactionType.entries.forEach { t ->
                        val isSelected = type == t
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(CircleShape)
                                .background(if (isSelected) Primary else Color.Transparent)
                                .clickable { type = t },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                t.name.lowercase().replaceFirstChar { it.uppercase() },
                                color = if (isSelected) Color.Black else Color.Gray,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Amount Input
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Enter Amount", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("$", style = MaterialTheme.typography.displayLarge, color = Primary)
                            TextField(
                                value = amount,
                                onValueChange = { newValue ->
                                    if (newValue.all { it.isDigit() || it == '.' } && newValue.count { it == '.' } <= 1) {
                                        amount = newValue
                                    }
                                },
                                textStyle = MaterialTheme.typography.displayLarge.copy(textAlign = androidx.compose.ui.text.style.TextAlign.Center),
                                placeholder = { Text("0.00", style = MaterialTheme.typography.displayLarge, color = Color.DarkGray) },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                    keyboardType = KeyboardType.Decimal,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                                    onDone = { focusManager.clearFocus() }
                                ),
                                modifier = Modifier.width(200.dp)
                            )
                        }
                    }
                }

                // Category Selection
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("CATEGORY", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    categories.chunked(4).forEach { rowCategories ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            rowCategories.forEach { cat ->
                                val isSelected = selectedCategory == cat.name
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { selectedCategory = cat.name }
                                        .padding(vertical = 8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(CircleShape)
                                            .background(if (isSelected) Primary else Color.White.copy(alpha = 0.05f))
                                            .border(1.dp, if (isSelected) Primary else Color.White.copy(alpha = 0.1f), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            cat.icon,
                                            contentDescription = null,
                                            tint = if (isSelected) Color.Black else Color.White,
                                            modifier = Modifier.size(24.dp)
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
                            repeat(4 - rowCategories.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                // Note
                TextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Add a note...") },
                    leadingIcon = { Icon(Icons.AutoMirrored.Filled.Notes, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.05f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.05f),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    )
                )
                
                Spacer(modifier = Modifier.height(24.dp))
            }

            MoltenButton(
                text = "Save Transaction",
                onClick = {
                    val amountVal = amount.toDoubleOrNull() ?: 0.0
                    if (amountVal > 0) {
                        viewModel.addTransaction(
                            Transaction(
                                id = UUID.randomUUID().toString(),
                                title = selectedCategory,
                                category = selectedCategory,
                                amount = amountVal,
                                date = "Today",
                                type = type,
                                note = note,
                                timestamp = System.currentTimeMillis()
                            )
                        )
                        onDismiss()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            )
        }
    }
}

data class CategoryItem(val name: String, val icon: ImageVector)

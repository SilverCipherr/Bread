package com.yummy.bread.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.yummy.bread.BreadViewModel
import com.yummy.bread.R
import com.yummy.bread.data.Transaction
import com.yummy.bread.ui.components.*
import com.yummy.bread.ui.navigation.Screen
import com.yummy.bread.ui.theme.*

@Composable
fun DashboardContent(viewModel: BreadViewModel, navController: NavHostController) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 24.dp, bottom = 100.dp, start = 20.dp, end = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Total Balance Card
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(48.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Total Balance",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        Text(
                            "${uiState.currency.split(" ").last().removeSurrounding("(", ")")}${uiState.totalBalance}",
                            style = MaterialTheme.typography.displayLarge,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }

            // Bento Grid
            item {
                val symbol = uiState.currency.split(" ").last().removeSurrounding("(", ")")
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    GlassCard(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Monthly Income", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.7f))
                                Text("$symbol${uiState.monthlyIncome}", style = MaterialTheme.typography.headlineMedium, color = Color.White)
                            }
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .border(1.dp, VibrantGreen.copy(alpha = 0.5f), CircleShape)
                                    .background(VibrantGreen.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowUpward,
                                    contentDescription = null,
                                    tint = VibrantGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                    GlassCard(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Monthly Spend", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.7f))
                                Text("$symbol${uiState.monthlySpend}", style = MaterialTheme.typography.headlineMedium, color = Color.White)
                            }
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .border(1.dp, VibrantRed.copy(alpha = 0.5f), CircleShape)
                                    .background(VibrantRed.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowDownward,
                                    contentDescription = null,
                                    tint = VibrantRed,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Progress Bar
            item {
                val symbol = uiState.currency.split(" ").last().removeSurrounding("(", ")")
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    LiquidProgressBar(
                        progress = uiState.budget.progress,
                        remainingText = "Remaining Spend: $symbol${uiState.budget.remaining}"
                    )
                }
            }

            // Transactions Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Recent Transactions", style = MaterialTheme.typography.headlineSmall, color = Color.White)
                     MoltenButton(text = "See All", onClick = {
                         navController.navigate(Screen.History.route) {
                             popUpTo(Screen.Dashboard.route) { saveState = true }
                             launchSingleTop = true
                             restoreState = true
                         }
                     })
                }
            }

            // Transactions List
            items(uiState.recentTransactions) { transaction ->
                TransactionItem(transaction)
            }
        }
    }
}

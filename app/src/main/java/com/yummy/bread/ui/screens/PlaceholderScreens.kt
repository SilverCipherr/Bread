package com.yummy.bread.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import androidx.navigation.NavHostController
import com.yummy.bread.data.Transaction
import com.yummy.bread.data.TransactionType
import com.yummy.bread.ui.components.GlassCard
import com.yummy.bread.ui.components.MoltenButton
import com.yummy.bread.ui.components.TransactionItem
import com.yummy.bread.ui.theme.Background
import com.yummy.bread.ui.theme.Primary
import com.yummy.bread.ui.theme.Secondary
import com.yummy.bread.ui.theme.Tertiary

import com.yummy.bread.BreadViewModel

@Composable
fun TransactionHistoryScreen(navController: NavHostController) {
    val transactions = listOf(
        Transaction("1", "Sweetgreen", "Food & Dining", -14.50, "12:30 PM", TransactionType.EXPENSE),
        Transaction("2", "Uber", "Transport", -22.40, "9:15 AM", TransactionType.EXPENSE),
        Transaction("3", "Tech Corp Inc.", "Salary", 3250.00, "Yesterday", TransactionType.INCOME),
        Transaction("4", "Whole Foods", "Groceries", -142.80, "Yesterday", TransactionType.EXPENSE),
        Transaction("5", "Apartment Management", "Rent", -1800.00, "Oct 24", TransactionType.EXPENSE)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            "History",
            style = MaterialTheme.typography.displayLarge,
            color = Primary
        )
        Spacer(modifier = Modifier.height(20.dp))
        
        // Search bar placeholder
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Search transactions...", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        
        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            items(transactions) { transaction ->
                TransactionItem(transaction)
            }
        }
    }
}

@Composable
fun BudgetPlannerScreen() {
    PlaceholderScreen("Budget Planner")
}

@Composable
fun AnalyticsScreen() {
    PlaceholderScreen("Analytics Insights")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetupScreen(viewModel: BreadViewModel, onSetupComplete: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()
    
    var name by remember { mutableStateOf(uiState.userName) }
    var balance by remember { mutableStateOf(if (uiState.totalBalance == 0.0) "" else uiState.totalBalance.toString()) }
    var goal by remember { mutableStateOf(if (uiState.monthlySavingsGoal == 0.0) "" else uiState.monthlySavingsGoal.toString()) }
    var selectedCurrency by remember { mutableStateOf(uiState.currency) }
    var selectedImageUri by remember { mutableStateOf<android.net.Uri?>(uiState.profilePictureUri) }
    
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri }
    )
    
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Set Up Your Profile",
                style = MaterialTheme.typography.headlineLarge,
                color = Primary,
                modifier = Modifier.padding(top = 40.dp)
            )
            Text(
                "Let's personalize your experience.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Avatar with Upload Photo
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.DarkGray)
                    .clickable { 
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                if (selectedImageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(selectedImageUri),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(60.dp), tint = Color.Gray)
                }
                
                // Upload Overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.AddAPhoto, contentDescription = null, tint = Primary, modifier = Modifier.size(24.dp))
                        Text("Upload", color = Primary, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Full Name") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                        )
                    )
                    TextField(
                        value = balance,
                        onValueChange = { newValue ->
                            if (newValue.all { it.isDigit() || it == '.' }) {
                                balance = newValue
                            }
                        },
                        label = { Text("Current Balance") },
                        leadingIcon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Next
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                        )
                    )
                    TextField(
                        value = goal,
                        onValueChange = { newValue ->
                            if (newValue.all { it.isDigit() || it == '.' }) {
                                goal = newValue
                            }
                        },
                        label = { Text("Monthly Savings Goal") },
                        leadingIcon = { Icon(Icons.Default.Savings, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                        )
                    )

                    Text(
                        "Primary Currency",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("USD ($)", "EUR (€)", "GBP (£)", "BDT (৳)").forEach { currency ->
                            val isSelected = selectedCurrency == currency
                            Surface(
                                onClick = { selectedCurrency = currency },
                                shape = CircleShape,
                                color = if (isSelected) Primary.copy(alpha = 0.2f) else Color.Transparent,
                                border = if (isSelected) BorderStroke(1.dp, Primary.copy(alpha = 0.5f)) else BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
                                modifier = Modifier.height(40.dp)
                            ) {
                                Box(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        currency, 
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (isSelected) Primary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            MoltenButton(
                text = "Complete Setup",
                onClick = {
                    viewModel.updateProfile(
                        name = name,
                        balance = balance.toDoubleOrNull() ?: 0.0,
                        goal = goal.toDoubleOrNull() ?: 0.0,
                        currency = selectedCurrency,
                        photoUri = selectedImageUri
                    )
                    onSetupComplete()
                },
                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
            )
        }
    }
}

@Composable
private fun PlaceholderScreen(title: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Coming Soon", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import androidx.navigation.NavHostController
import com.yummy.bread.BreadViewModel
import com.yummy.bread.data.CategoryBudget
import com.yummy.bread.data.Transaction
import com.yummy.bread.data.TransactionType
import com.yummy.bread.ui.components.*
import com.yummy.bread.ui.theme.Background
import com.yummy.bread.ui.theme.Primary
import com.yummy.bread.ui.theme.Secondary
import com.yummy.bread.ui.theme.Tertiary

@Composable
fun TransactionHistoryScreen(viewModel: BreadViewModel, navController: NavHostController) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    
    val filteredTransactions = remember(searchQuery, uiState.recentTransactions) {
        uiState.recentTransactions.filter { 
            it.category.contains(searchQuery, ignoreCase = true) || 
            it.note.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            "History",
            style = MaterialTheme.typography.displayLarge,
            color = Primary
        )
        Spacer(modifier = Modifier.height(20.dp))
        
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search transactions...", color = MaterialTheme.colorScheme.onSurfaceVariant) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White.copy(alpha = 0.05f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.05f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(20.dp))

        if (filteredTransactions.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    if (searchQuery.isEmpty()) "No transactions yet" else "No matching transactions",
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                items(filteredTransactions) { transaction ->
                    TransactionItem(transaction)
                }
            }
        }
    }
}

@Composable
fun BudgetPlannerScreen(viewModel: BreadViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val symbol = uiState.currency.split(" ").last().removeSurrounding("(", ")")
    
    val totalBudget = uiState.monthlyIncome - uiState.monthlySavingsGoal

    var showDialog by remember { mutableStateOf(false) }
    var editingBudget by remember { mutableStateOf<CategoryBudget?>(null) }

    if (showDialog) {
        BudgetDialog(
            onDismiss = { 
                showDialog = false
                editingBudget = null
            },
            onConfirm = { category, limit, icon ->
                if (editingBudget != null) {
                    viewModel.updateCategoryBudget(category, limit)
                } else {
                    viewModel.addCategoryBudget(category, limit, icon)
                }
                showDialog = false
                editingBudget = null
            },
            initialBudget = editingBudget,
            isEditing = editingBudget != null
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            "Budget",
            style = MaterialTheme.typography.displayLarge,
            color = Primary
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        TotalBudgetSummaryCard(
            spent = uiState.monthlySpend,
            total = totalBudget,
            symbol = symbol
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            "Categories",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(uiState.categoryBudgets) { budget ->
                CategoryBudgetCard(
                    budget = budget, 
                    symbol = symbol,
                    onEditClick = {
                        editingBudget = budget
                        showDialog = true
                    },
                    onDeleteClick = {
                        viewModel.deleteCategoryBudget(budget.category)
                    }
                )
            }
            
            item {
                OutlinedButton(
                    onClick = { showDialog = true },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = CircleShape,
                    border = BorderStroke(1.dp, Primary.copy(alpha = 0.3f))
                ) {
                    Icon(Icons.Default.AddCircle, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Set New Budget", color = Primary)
                }
            }
        }
    }
}

@Composable
fun AnalyticsScreen(viewModel: BreadViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val symbol = uiState.currency.split(" ").last().removeSurrounding("(", ")")
    
    val totalSpent = uiState.spendingBreakdown.sumOf { it.amount }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        
        // Header
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "Spending Overview",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                "Your financial breakdown for this period.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        TimeToggle(
            selectedRange = uiState.selectedTimeRange,
            onRangeSelected = { viewModel.updateTimeRange(it) }
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Donut Chart Card
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            DonutChart(
                breakdown = uiState.spendingBreakdown,
                totalText = totalSpent.toInt().toString(),
                symbol = symbol
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Legend
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                uiState.spendingBreakdown.chunked(2).forEach { rowItems ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        rowItems.forEach { item ->
                            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(item.color)
                                        .shadow(8.dp, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(item.category, style = MaterialTheme.typography.labelMedium, color = Color.White)
                                    Text("${(item.percentage * 100).toInt()}%", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Balance Trend Card
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Balance Trend", style = MaterialTheme.typography.labelLarge, color = Color.White, fontWeight = FontWeight.Bold)
                Surface(
                    color = Tertiary.copy(alpha = 0.1f),
                    shape = CircleShape,
                    border = BorderStroke(1.dp, Tertiary.copy(alpha = 0.2f))
                ) {
                    Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, tint = Tertiary, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(uiState.trendPercentage, style = MaterialTheme.typography.labelSmall, color = Tertiary)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            BalanceTrendChart(points = uiState.balanceTrend)
        }
        
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetupScreen(viewModel: BreadViewModel, isNew: Boolean = false, onSetupComplete: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()
    val isEditing = uiState.activeProfileId != null && !isNew
    
    // Use isNew as a key for remember to force reset when creating a new account
    var name by remember(uiState.activeProfileId, isNew) { mutableStateOf(if (isNew) "" else uiState.userName) }
    var balance by remember(uiState.activeProfileId, isNew) { mutableStateOf(if (isNew || uiState.totalBalance == 0.0) "" else uiState.totalBalance.toString()) }
    var income by remember(uiState.activeProfileId, isNew) { mutableStateOf(if (isNew || uiState.monthlyIncome == 0.0) "" else uiState.monthlyIncome.toString()) }
    var goal by remember(uiState.activeProfileId, isNew) { mutableStateOf(if (isNew || uiState.monthlySavingsGoal == 0.0) "" else uiState.monthlySavingsGoal.toString()) }
    var pin by remember(uiState.activeProfileId, isNew) { mutableStateOf("") }
    var selectedCurrency by remember(uiState.activeProfileId, isNew) { mutableStateOf(if (isNew) "USD ($)" else uiState.currency) }
    var selectedImageUri by remember(uiState.activeProfileId, isNew) { mutableStateOf<android.net.Uri?>(if (isNew) null else uiState.profilePictureUri) }
    
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri }
    )
    
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                if (isEditing) "Edit Profile" else "Set Up Your Profile",
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
                            if (newValue.all { it.isDigit() || it == '.' }) balance = newValue
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
                        value = income,
                        onValueChange = { newValue ->
                            if (newValue.all { it.isDigit() || it == '.' }) income = newValue
                        },
                        label = { Text("Monthly Income") },
                        leadingIcon = { Icon(Icons.Default.Payments, contentDescription = null) },
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
                            if (newValue.all { it.isDigit() || it == '.' }) goal = newValue
                        },
                        label = { Text("Monthly Savings Goal") },
                        leadingIcon = { Icon(Icons.Default.Savings, contentDescription = null) },
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

                    // PIN Setup
                    TextField(
                        value = pin,
                        onValueChange = { if (it.length <= 4 && it.all { c -> c.isDigit() }) pin = it },
                        label = { Text(if (isEditing) "New Security PIN (Optional)" else "Set 4-Digit Security PIN") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.NumberPassword,
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

            Spacer(modifier = Modifier.height(32.dp))

            MoltenButton(
                text = if (isEditing) "Save Changes" else "Complete Setup",
                onClick = {
                    val balanceVal = balance.toDoubleOrNull() ?: 0.0
                    val incomeVal = income.toDoubleOrNull() ?: 0.0
                    val goalVal = goal.toDoubleOrNull() ?: 0.0
                    
                    if (isEditing) {
                        viewModel.updateProfile(name, balanceVal, incomeVal, goalVal, selectedCurrency, selectedImageUri)
                        if (pin.length == 4) viewModel.updatePin(pin)
                    } else {
                        if (pin.length == 4 && name.isNotBlank()) {
                            viewModel.createProfile(name, balanceVal, incomeVal, goalVal, selectedCurrency, selectedImageUri, pin)
                        } else {
                            // Show error toast
                            return@MoltenButton
                        }
                    }
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

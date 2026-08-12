package com.yummy.bread

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import com.yummy.bread.data.*
import com.yummy.bread.ui.theme.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.*

data class BreadUiState(
    val userName: String = "",
    val totalBalance: Double = 0.0,
    val monthlyIncome: Double = 0.0,
    val monthlySpend: Double = 0.0,
    val monthlySavingsGoal: Double = 0.0,
    val currency: String = "USD ($)",
    val profilePictureUri: Uri? = null,
    val recentTransactions: List<Transaction> = emptyList(),
    val categoryBudgets: List<CategoryBudget> = emptyList(),
    val spendingBreakdown: List<CategorySpend> = emptyList(),
    val balanceTrend: List<TrendPoint> = emptyList(),
    val selectedTimeRange: String = "Monthly",
    val trendPercentage: String = "+0.0%",
    val isDarkMode: Boolean? = null,
    
    // Multi-profile
    val profiles: List<Profile> = emptyList(),
    val activeProfileId: String? = null,
    val lastActiveProfileId: String? = null,
    val isLocked: Boolean = false
) {
    val budget: Budget get() = Budget(monthlyIncome, monthlySavingsGoal, monthlySpend)
    val activeProfile: Profile? get() = profiles.find { it.id == activeProfileId }
    val currencySymbol: String get() = currency.split(" ").last().removeSurrounding("(", ")")
}

class BreadViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = BreadRepository(application)
    
    private val _uiState = MutableStateFlow(
        BreadUiState(
            profiles = repository.loadProfiles(),
            lastActiveProfileId = repository.loadLastActiveProfileId()
        )
    )
    val uiState: StateFlow<BreadUiState> = _uiState.asStateFlow()

    init {
        recalculateBreakdown()
    }

    private val categoryColors = mapOf(
        "Food" to Color(0xFFBAB0FF),
        "Transport" to Color(0xFFADC6FF),
        "Shopping" to Color(0xFF4EDEA3),
        "Utilities" to Color(0xFFFFA726),
        "Invest" to Color(0xFF66BB6A),
        "Gift" to Color(0xFFEC407A),
        "Salary" to Color(0xFF26C6DA),
        "Groceries" to Color(0xFFAB47BC),
        "Entertainment" to Color(0xFFFF7043),
        "Other" to Color(0xFF90A4AE)
    )

    fun createProfile(
        name: String,
        balance: Double,
        income: Double,
        goal: Double,
        currency: String,
        photoUri: Uri?,
        pin: String
    ) {
        val newProfile = Profile(
            id = UUID.randomUUID().toString(),
            name = name,
            pictureUri = photoUri,
            pin = pin,
            initialBalance = balance,
            monthlyIncome = income,
            monthlySavingsGoal = goal,
            currency = currency
        )
        
        takeUriPermission(photoUri)
        
        val updatedProfiles = _uiState.value.profiles + newProfile
        repository.saveProfiles(updatedProfiles)
        
        _uiState.update { it.copy(profiles = updatedProfiles) }
        login(newProfile.id)
    }

    fun updateProfile(
        name: String,
        balance: Double,
        income: Double,
        goal: Double,
        currency: String,
        photoUri: Uri?
    ) {
        val activeId = _uiState.value.activeProfileId ?: return
        
        takeUriPermission(photoUri)
        
        val updatedProfiles = _uiState.value.profiles.map {
            if (it.id == activeId) {
                it.copy(
                    name = name,
                    pictureUri = photoUri,
                    initialBalance = balance,
                    monthlyIncome = income,
                    monthlySavingsGoal = goal,
                    currency = currency
                )
            } else it
        }
        
        repository.saveProfiles(updatedProfiles)
        
        _uiState.update {
            it.copy(
                profiles = updatedProfiles,
                userName = name,
                totalBalance = balance,
                monthlyIncome = income,
                monthlySavingsGoal = goal,
                currency = currency,
                profilePictureUri = photoUri
            ).also { newState -> repository.saveActiveProfileState(activeId, newState) }
        }
        recalculateBreakdown()
    }
    
    fun updatePin(newPin: String) {
        val activeId = _uiState.value.activeProfileId ?: return
        val updatedProfiles = _uiState.value.profiles.map {
            if (it.id == activeId) it.copy(pin = newPin) else it
        }
        repository.saveProfiles(updatedProfiles)
        _uiState.update { it.copy(profiles = updatedProfiles) }
    }

    fun login(profileId: String) {
        val profile = _uiState.value.profiles.find { it.id == profileId } ?: return
        val profileState = repository.loadActiveProfileState(profile)
        
        repository.saveLastActiveProfileId(profileId)
        
        _uiState.update {
            profileState.copy(
                profiles = it.profiles,
                activeProfileId = profileId,
                lastActiveProfileId = profileId,
                isLocked = false
            )
        }
        recalculateBreakdown()
    }

    fun logout() {
        _uiState.update {
            BreadUiState(
                profiles = it.profiles,
                isDarkMode = it.isDarkMode,
                lastActiveProfileId = it.lastActiveProfileId
            )
        }
    }

    fun lock() {
        _uiState.update { it.copy(isLocked = true) }
    }

    private fun takeUriPermission(uri: Uri?) {
        uri?.let { u ->
            try {
                val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                getApplication<Application>().contentResolver.takePersistableUriPermission(u, flags)
            } catch (_: Exception) {}
        }
    }

    fun setDarkMode(enabled: Boolean?) {
        _uiState.update { it.copy(isDarkMode = enabled) }
        repository.saveGlobalDarkMode(enabled)
    }

    fun addTransaction(transaction: Transaction) {
        val activeId = _uiState.value.activeProfileId ?: return
        _uiState.update { currentState ->
            val updatedTransactions = (listOf(transaction) + currentState.recentTransactions).take(100)
            val balanceChange = if (transaction.type == TransactionType.INCOME) transaction.amount else -transaction.amount
            val spendIncrease = if (transaction.type == TransactionType.EXPENSE) transaction.amount else 0.0
            
            val updatedBudgets = if (transaction.type == TransactionType.EXPENSE) {
                currentState.categoryBudgets.map { catBudget ->
                    if (catBudget.category == transaction.category) {
                        catBudget.copy(spent = catBudget.spent + transaction.amount)
                    } else catBudget
                }
            } else currentState.categoryBudgets

            currentState.copy(
                recentTransactions = updatedTransactions,
                totalBalance = currentState.totalBalance + balanceChange,
                monthlySpend = currentState.monthlySpend + spendIncrease,
                categoryBudgets = updatedBudgets
            ).also { newState -> repository.saveActiveProfileState(activeId, newState) }
        }
        recalculateBreakdown()
    }
    
    fun updateCategoryBudget(category: String, newLimit: Double) {
        val activeId = _uiState.value.activeProfileId ?: return
        _uiState.update { currentState ->
            val updatedBudgets = currentState.categoryBudgets.map { 
                if (it.category == category) it.copy(limit = newLimit) else it
            }
            currentState.copy(categoryBudgets = updatedBudgets)
                .also { repository.saveActiveProfileState(activeId, it) }
        }
    }

    fun addCategoryBudget(category: String, limit: Double, icon: String) {
        val activeId = _uiState.value.activeProfileId ?: return
        _uiState.update { currentState ->
            if (currentState.categoryBudgets.any { it.category == category }) return@update currentState
            
            val initialSpent = currentState.recentTransactions
                .filter { it.category == category && it.type == TransactionType.EXPENSE }
                .sumOf { it.amount }

            val newBudget = CategoryBudget(category, limit, initialSpent, icon)
            val updatedBudgets = currentState.categoryBudgets + newBudget
            currentState.copy(categoryBudgets = updatedBudgets)
                .also { repository.saveActiveProfileState(activeId, it) }
        }
    }

    fun deleteCategoryBudget(category: String) {
        val activeId = _uiState.value.activeProfileId ?: return
        _uiState.update { currentState ->
            val updatedBudgets = currentState.categoryBudgets.filter { it.category != category }
            currentState.copy(categoryBudgets = updatedBudgets)
                .also { repository.saveActiveProfileState(activeId, it) }
        }
    }

    fun deleteProfile(profile: Profile) {
        repository.deleteProfileData(profile.id)
        
        _uiState.update { currentState ->
            val updatedProfiles = currentState.profiles.filter { it.id != profile.id }
            repository.saveProfiles(updatedProfiles)
            
            val isLastActive = currentState.lastActiveProfileId == profile.id
            if (isLastActive) {
                repository.saveLastActiveProfileId(null)
            }
            
            currentState.copy(
                profiles = updatedProfiles,
                lastActiveProfileId = if (isLastActive) null else currentState.lastActiveProfileId
            )
        }
    }

    fun updateTimeRange(range: String) {
        _uiState.update { it.copy(selectedTimeRange = range) }
        recalculateBreakdown()
    }

    private fun recalculateBreakdown() {
        _uiState.update { currentState ->
            val now = System.currentTimeMillis()
            val startTime = when (currentState.selectedTimeRange) {
                "Weekly" -> now - 7 * 24 * 60 * 60 * 1000L
                "Monthly" -> now - 30 * 24 * 60 * 60 * 1000L
                "Yearly" -> now - 365 * 24 * 60 * 60 * 1000L
                else -> 0L
            }

            val filteredTransactions = currentState.recentTransactions.filter { it.timestamp >= startTime }
            val expenses = filteredTransactions.filter { it.type == TransactionType.EXPENSE }
            val totalExpense = expenses.sumOf { it.amount }
            
            val breakdown = expenses.groupBy { it.category }
                .map { (category, transactions) ->
                    val categoryAmount = transactions.sumOf { it.amount }
                    CategorySpend(
                        category = category,
                        amount = categoryAmount,
                        percentage = if (totalExpense > 0) (categoryAmount / totalExpense).toFloat() else 0f,
                        color = categoryColors[category] ?: Color.Gray
                    )
                }
                .sortedByDescending { it.amount }

            val pointsCount = 5
            val period = now - startTime
            val trend = mutableListOf<TrendPoint>()
            
            for (i in (pointsCount - 1) downTo 0) {
                val pointTime = startTime + (i.toFloat() / (pointsCount - 1) * period).toLong()
                val label = when (currentState.selectedTimeRange) {
                    "Weekly" -> "Day ${i + 1}"
                    "Monthly" -> if (i == 0) "1st" else if (i == 2) "15th" else if (i == 4) "30th" else ""
                    "Yearly" -> "Q${i + 1}"
                    else -> ""
                }
                
                val transactionsAfterPoint = currentState.recentTransactions.filter { it.timestamp > pointTime }
                val balanceChangeAfter = transactionsAfterPoint.sumOf { 
                    if (it.type == TransactionType.INCOME) it.amount else -it.amount 
                }
                
                trend.add(0, TrendPoint(label, (currentState.totalBalance - balanceChangeAfter).toFloat()))
            }

            val firstVal = trend.firstOrNull()?.value ?: 0f
            val lastVal = trend.lastOrNull()?.value ?: 0f
            val diff = if (firstVal != 0f) ((lastVal - firstVal) / firstVal) * 100 else 0f
            val trendPct = (if (diff >= 0) "+" else "") + String.format("%.1f%%", diff)

            currentState.copy(
                spendingBreakdown = breakdown,
                balanceTrend = trend,
                trendPercentage = trendPct
            )
        }
    }
}

package com.yummy.bread

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import com.yummy.bread.data.BreadRepository
import com.yummy.bread.data.Budget
import com.yummy.bread.data.Transaction
import com.yummy.bread.data.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class BreadUiState(
    val userName: String = "",
    val totalBalance: Double = 0.0,
    val monthlyIncome: Double = 0.0,
    val monthlySpend: Double = 0.0,
    val monthlySavingsGoal: Double = 0.0,
    val currency: String = "USD ($)",
    val profilePictureUri: Uri? = null,
    val recentTransactions: List<Transaction> = emptyList()
) {
    val budget: Budget get() = Budget(monthlyIncome, monthlySavingsGoal, monthlySpend)
}

class BreadViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = BreadRepository(application)
    
    private val _uiState = MutableStateFlow(repository.loadState())
    val uiState: StateFlow<BreadUiState> = _uiState.asStateFlow()

    fun updateProfile(
        name: String,
        balance: Double,
        income: Double,
        goal: Double,
        currency: String,
        photoUri: Uri?
    ) {
        photoUri?.let { uri ->
            try {
                val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                getApplication<Application>().contentResolver.takePersistableUriPermission(uri, flags)
            } catch (e: Exception) {
                // Ignore if not a content URI or permission cannot be taken
            }
        }
        _uiState.update {
            it.copy(
                userName = name,
                totalBalance = balance,
                monthlyIncome = income,
                monthlySavingsGoal = goal,
                currency = currency,
                profilePictureUri = photoUri
            ).also { newState -> repository.saveState(newState) }
        }
    }

    fun addTransaction(transaction: Transaction) {
        _uiState.update { currentState ->
            val updatedTransactions = (listOf(transaction) + currentState.recentTransactions).take(10)
            val balanceChange = if (transaction.type == TransactionType.INCOME) transaction.amount else -transaction.amount
            
            val spendIncrease = if (transaction.type == TransactionType.EXPENSE) transaction.amount else 0.0
            
            currentState.copy(
                recentTransactions = updatedTransactions,
                totalBalance = currentState.totalBalance + balanceChange,
                monthlySpend = currentState.monthlySpend + spendIncrease
            ).also { newState -> repository.saveState(newState) }
        }
    }
}

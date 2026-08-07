package com.yummy.bread

import android.net.Uri
import androidx.lifecycle.ViewModel
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
    val monthlyIncome: Double = 5240.0, // Mock for now
    val monthlySpend: Double = 3120.0, // Mock for now
    val monthlySavingsGoal: Double = 0.0,
    val currency: String = "USD ($)",
    val profilePictureUri: Uri? = null,
    val recentTransactions: List<Transaction> = listOf(
        Transaction("1", "Sweetgreen", "Food & Dining", -18.50, "Today", TransactionType.EXPENSE),
        Transaction("2", "Uber Ride", "Transport", -24.00, "Yesterday", TransactionType.EXPENSE),
        Transaction("3", "Target", "Shopping", -112.30, "Mon, 14th", TransactionType.EXPENSE),
        Transaction("4", "Salary", "Work", 5000.00, "Fri, 11th", TransactionType.INCOME)
    )
) {
    val budget: Budget get() = Budget("Overall", monthlySavingsGoal, monthlySpend)
}

class BreadViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(BreadUiState())
    val uiState: StateFlow<BreadUiState> = _uiState.asStateFlow()

    fun updateProfile(
        name: String,
        balance: Double,
        goal: Double,
        currency: String,
        photoUri: Uri?
    ) {
        _uiState.update {
            it.copy(
                userName = name,
                totalBalance = balance,
                monthlySavingsGoal = goal,
                currency = currency,
                profilePictureUri = photoUri
            )
        }
    }
}

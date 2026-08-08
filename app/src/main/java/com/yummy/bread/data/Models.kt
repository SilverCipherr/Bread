package com.yummy.bread.data

import android.net.Uri

data class Transaction(
    val id: String,
    val title: String,
    val category: String,
    val amount: Double,
    val date: String,
    val type: TransactionType,
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

enum class TransactionType {
    INCOME, EXPENSE
}

data class Budget(
    val monthlyIncome: Double,
    val savingsGoal: Double,
    val monthlySpend: Double
) {
    val spendableLimit: Double get() = (monthlyIncome - savingsGoal).coerceAtLeast(0.0)
    val progress: Float get() = if (spendableLimit > 0) (monthlySpend / spendableLimit).toFloat().coerceIn(0f, 1f) else 1f
    val remaining: Double get() = (spendableLimit - monthlySpend).coerceAtLeast(0.0)
}

data class CategoryBudget(
    val category: String,
    val limit: Double,
    val spent: Double,
    val icon: String
) {
    val progress: Float get() = if (limit > 0) (spent / limit).toFloat().coerceIn(0f, 1f) else 0f
    val remaining: Double get() = (limit - spent).coerceAtLeast(0.0)
    val isOverBudget: Boolean get() = spent > limit
}

data class CategorySpend(
    val category: String,
    val amount: Double,
    val percentage: Float,
    val color: androidx.compose.ui.graphics.Color
)

data class TrendPoint(
    val label: String,
    val value: Float
)

data class Profile(
    val id: String,
    val name: String,
    val pictureUri: Uri? = null,
    val pin: String, // 4-digit hashed/encrypted PIN
    val initialBalance: Double = 0.0,
    val monthlyIncome: Double = 0.0,
    val monthlySavingsGoal: Double = 0.0,
    val currency: String = "USD ($)"
)

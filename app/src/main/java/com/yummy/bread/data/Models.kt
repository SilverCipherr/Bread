package com.yummy.bread.data

data class Transaction(
    val id: String,
    val title: String,
    val category: String,
    val amount: Double,
    val date: String,
    val type: TransactionType,
    val note: String = ""
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

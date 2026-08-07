package com.yummy.bread.data

data class Transaction(
    val id: String,
    val title: String,
    val category: String,
    val amount: Double,
    val date: String,
    val type: TransactionType
)

enum class TransactionType {
    INCOME, EXPENSE
}

data class Budget(
    val category: String,
    val targetAmount: Double,
    val currentAmount: Double
) {
    val progress: Float get() = (currentAmount / targetAmount).toFloat().coerceIn(0f, 1f)
    val remaining: Double get() = (targetAmount - currentAmount).coerceAtLeast(0.0)
}

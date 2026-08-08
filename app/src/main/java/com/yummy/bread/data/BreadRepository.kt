package com.yummy.bread.data

import android.content.Context
import android.net.Uri
import com.yummy.bread.BreadUiState
import org.json.JSONArray
import org.json.JSONObject

class BreadRepository(context: Context) {
    private val prefs = context.getSharedPreferences("bread_prefs", Context.MODE_PRIVATE)

    fun saveState(state: BreadUiState) {
        prefs.edit().apply {
            putString("user_name", state.userName)
            putFloat("total_balance", state.totalBalance.toFloat())
            putFloat("monthly_income", state.monthlyIncome.toFloat())
            putFloat("monthly_spend", state.monthlySpend.toFloat())
            putFloat("monthly_savings_goal", state.monthlySavingsGoal.toFloat())
            putString("currency", state.currency)
            putString("profile_picture_uri", state.profilePictureUri?.toString())
            
            val transactionsArray = JSONArray()
            state.recentTransactions.forEach { transaction ->
                val json = JSONObject().apply {
                    put("id", transaction.id)
                    put("title", transaction.title)
                    put("category", transaction.category)
                    put("amount", transaction.amount)
                    put("date", transaction.date)
                    put("type", transaction.type.name)
                    put("note", transaction.note)
                    put("timestamp", transaction.timestamp)
                }
                transactionsArray.put(json)
            }
            putString("transactions", transactionsArray.toString())

            val budgetsArray = JSONArray()
            state.categoryBudgets.forEach { budget ->
                val json = JSONObject().apply {
                    put("category", budget.category)
                    put("limit", budget.limit)
                    put("spent", budget.spent)
                    put("icon", budget.icon)
                }
                budgetsArray.put(json)
            }
            putString("category_budgets", budgetsArray.toString())
            apply()
        }
    }

    fun loadState(): BreadUiState {
        val transactionsJson = prefs.getString("transactions", null)
        val transactions = mutableListOf<Transaction>()
        if (transactionsJson != null) {
            val array = JSONArray(transactionsJson)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val id = obj.getString("id")
                if (id in listOf("1", "2", "3", "4")) continue // Skip stale mock data
                transactions.add(
                    Transaction(
                        id = id,
                        title = obj.getString("title"),
                        category = obj.getString("category"),
                        amount = obj.getDouble("amount"),
                        date = obj.getString("date"),
                        type = TransactionType.valueOf(obj.getString("type")),
                        note = obj.optString("note", ""),
                        timestamp = obj.optLong("timestamp", System.currentTimeMillis())
                    )
                )
            }
        }

        val budgetsJson = prefs.getString("category_budgets", null)
        val categoryBudgets = mutableListOf<CategoryBudget>()
        if (budgetsJson != null) {
            val array = JSONArray(budgetsJson)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                categoryBudgets.add(
                    CategoryBudget(
                        category = obj.getString("category"),
                        limit = obj.getDouble("limit"),
                        spent = obj.getDouble("spent"),
                        icon = obj.getString("icon")
                    )
                )
            }
        }

        return BreadUiState(
            userName = prefs.getString("user_name", "") ?: "",
            totalBalance = prefs.getFloat("total_balance", 0f).toDouble(),
            monthlyIncome = prefs.getFloat("monthly_income", 0f).toDouble(),
            monthlySpend = prefs.getFloat("monthly_spend", 0f).toDouble(),
            monthlySavingsGoal = prefs.getFloat("monthly_savings_goal", 0f).toDouble(),
            currency = prefs.getString("currency", "USD ($)") ?: "USD ($)",
            profilePictureUri = prefs.getString("profile_picture_uri", null)?.let { Uri.parse(it) },
            recentTransactions = transactions,
            categoryBudgets = categoryBudgets
        )
    }
}

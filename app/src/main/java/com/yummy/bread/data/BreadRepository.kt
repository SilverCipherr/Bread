package com.yummy.bread.data

import android.content.Context
import android.net.Uri
import com.yummy.bread.BreadUiState
import org.json.JSONArray
import org.json.JSONObject

class BreadRepository(context: Context) {
    private val prefs = context.getSharedPreferences("bread_prefs", Context.MODE_PRIVATE)

    fun saveProfiles(profiles: List<Profile>) {
        val array = JSONArray()
        profiles.forEach { profile ->
            array.put(JSONObject().apply {
                put("id", profile.id)
                put("name", profile.name)
                put("pictureUri", profile.pictureUri?.toString())
                put("pin", profile.pin)
                put("initialBalance", profile.initialBalance)
                put("monthlyIncome", profile.monthlyIncome)
                put("monthlySavingsGoal", profile.monthlySavingsGoal)
                put("currency", profile.currency)
            })
        }
        prefs.edit().putString("profiles", array.toString()).apply()
    }

    fun saveLastActiveProfileId(id: String?) {
        prefs.edit().putString("last_active_profile_id", id).apply()
    }

    fun loadLastActiveProfileId(): String? {
        return prefs.getString("last_active_profile_id", null)
    }

    fun loadProfiles(): List<Profile> {
        val json = prefs.getString("profiles", null) ?: return emptyList()
        val array = JSONArray(json)
        val profiles = mutableListOf<Profile>()
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            profiles.add(Profile(
                id = obj.getString("id"),
                name = obj.getString("name"),
                pictureUri = obj.optString("pictureUri", null)?.takeIf { it != "null" }?.let { Uri.parse(it) },
                pin = obj.getString("pin"),
                initialBalance = obj.optDouble("initialBalance", 0.0),
                monthlyIncome = obj.optDouble("monthlyIncome", 0.0),
                monthlySavingsGoal = obj.optDouble("monthlySavingsGoal", 0.0),
                currency = obj.optString("currency", "USD ($)")
            ))
        }
        return profiles
    }

    fun saveActiveProfileState(profileId: String, state: BreadUiState) {
        prefs.edit().apply {
            putFloat("${profileId}_total_balance", state.totalBalance.toFloat())
            putFloat("${profileId}_monthly_spend", state.monthlySpend.toFloat())
            
            val transactionsArray = JSONArray()
            state.recentTransactions.forEach { transaction ->
                transactionsArray.put(JSONObject().apply {
                    put("id", transaction.id)
                    put("title", transaction.title)
                    put("category", transaction.category)
                    put("amount", transaction.amount)
                    put("date", transaction.date)
                    put("type", transaction.type.name)
                    put("note", transaction.note)
                    put("timestamp", transaction.timestamp)
                })
            }
            putString("${profileId}_transactions", transactionsArray.toString())

            val budgetsArray = JSONArray()
            state.categoryBudgets.forEach { budget ->
                budgetsArray.put(JSONObject().apply {
                    put("category", budget.category)
                    put("limit", budget.limit)
                    put("spent", budget.spent)
                    put("icon", budget.icon)
                })
            }
            putString("${profileId}_category_budgets", budgetsArray.toString())
            apply()
        }
    }

    fun loadActiveProfileState(profile: Profile): BreadUiState {
        val profileId = profile.id
        val transactionsJson = prefs.getString("${profileId}_transactions", null)
        val transactions = mutableListOf<Transaction>()
        if (transactionsJson != null) {
            val array = JSONArray(transactionsJson)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                transactions.add(Transaction(
                    id = obj.getString("id"),
                    title = obj.getString("title"),
                    category = obj.getString("category"),
                    amount = obj.getDouble("amount"),
                    date = obj.getString("date"),
                    type = TransactionType.valueOf(obj.getString("type")),
                    note = obj.optString("note", ""),
                    timestamp = obj.optLong("timestamp", System.currentTimeMillis())
                ))
            }
        }

        val budgetsJson = prefs.getString("${profileId}_category_budgets", null)
        val categoryBudgets = mutableListOf<CategoryBudget>()
        if (budgetsJson != null) {
            val array = JSONArray(budgetsJson)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                categoryBudgets.add(CategoryBudget(
                    category = obj.getString("category"),
                    limit = obj.getDouble("limit"),
                    spent = obj.getDouble("spent"),
                    icon = obj.getString("icon")
                ))
            }
        }

        return BreadUiState(
            userName = profile.name,
            totalBalance = prefs.getFloat("${profileId}_total_balance", profile.initialBalance.toFloat()).toDouble(),
            monthlyIncome = profile.monthlyIncome,
            monthlySpend = prefs.getFloat("${profileId}_monthly_spend", 0f).toDouble(),
            monthlySavingsGoal = profile.monthlySavingsGoal,
            currency = profile.currency,
            profilePictureUri = profile.pictureUri,
            recentTransactions = transactions,
            categoryBudgets = categoryBudgets,
            isDarkMode = if (prefs.contains("is_dark_mode")) prefs.getBoolean("is_dark_mode", true) else null
        )
    }
    
    fun saveGlobalDarkMode(enabled: Boolean?) {
        if (enabled != null) {
            prefs.edit().putBoolean("is_dark_mode", enabled).apply()
        } else {
            prefs.edit().remove("is_dark_mode").apply()
        }
    }

    fun deleteProfileData(profileId: String) {
        prefs.edit().apply {
            remove("${profileId}_total_balance")
            remove("${profileId}_monthly_spend")
            remove("${profileId}_transactions")
            remove("${profileId}_category_budgets")
            apply()
        }
    }
}

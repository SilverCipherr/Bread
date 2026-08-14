package com.yummy.bread.ui

import java.text.SimpleDateFormat
import java.util.*
import android.text.format.DateUtils

object DateUtils {
    fun formatTransactionDate(timestamp: Long): String {
        val now = System.currentTimeMillis()
        return when {
            DateUtils.isToday(timestamp) -> "Today"
            DateUtils.isToday(timestamp + DateUtils.DAY_IN_MILLIS) -> "Yesterday"
            else -> {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = timestamp
                val year = calendar.get(Calendar.YEAR)
                val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                
                val pattern = if (year == currentYear) "d MMM" else "d MMM yyyy"
                SimpleDateFormat(pattern, Locale.getDefault()).format(Date(timestamp))
            }
        }
    }
}

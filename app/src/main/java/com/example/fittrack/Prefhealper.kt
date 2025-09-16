package com.example.fittrack

import android.content.Context
import android.content.SharedPreferences

class Prefhealper(context: Context) {

    companion object {
        private const val PREFS_NAME = "ExpensePrefs"
        private const val KEY_RESET = "reset_gauge"
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // ✅ Gauge reset flag set karne ka method
    fun setGaugeReset(reset: Boolean) {
        prefs.edit().putBoolean(KEY_RESET, reset).apply()
    }

    // ✅ Check karne ka method
    fun shouldResetGauge(): Boolean {
        return prefs.getBoolean(KEY_RESET, false)
    }

    // ✅ Reset flag clear karne ka method
    fun clearResetFlag() {
        prefs.edit().putBoolean(KEY_RESET, false).apply()
    }
}

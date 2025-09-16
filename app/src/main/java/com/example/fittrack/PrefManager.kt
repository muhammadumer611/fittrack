package com.example.fittrack

import android.content.Context

object PrefManager {
    private const val PREF_NAME = "onboarding_pref"
    private const val IS_FIRST_TIME = "is_first_time"

    fun isFirstTimeLaunch(context: Context): Boolean {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return pref.getBoolean(IS_FIRST_TIME, true) // default true
    }


    fun setFirstTimeLaunch(context: Context, isFirstTime: Boolean) {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean(IS_FIRST_TIME, isFirstTime)
        editor.apply()
    }
}

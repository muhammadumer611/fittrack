package com.example.fittrack

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MySharedPref(context: Context) {

    private val sharedPref = context.getSharedPreferences("com.example.fittrack", Context.MODE_PRIVATE)
    private val editor = sharedPref.edit()
    private val gson = Gson()

    // ✅ Keys
    private val defaultKey = "fittrack"          // History list
    private val totalKey = "total_amount"        // Total expenses
    private val incomeKey = "total_income"       // Total income
    private val dateKey = "Date"                 // Selected date
    private val timeKey = "Time"                 // Selected time

    // ✅ Save single card item (append in list)
    fun saveCardItem(item: carditem) {
        val currentList = getCardItemList().toMutableList()
        currentList.add(item)
        val json = gson.toJson(currentList)
        editor.putString(defaultKey, json).apply()
    }

    // ✅ Get complete list
    fun getCardItemList(): List<carditem> {
        val json = sharedPref.getString(defaultKey, null)

        return if (!json.isNullOrEmpty()) {
            try {
                val type = object : TypeToken<List<carditem>>() {}.type
                gson.fromJson<List<carditem>>(json, type) ?: emptyList()
            } catch (e: Exception) {
                emptyList() // agar parsing fail ho jaye to crash na ho
            }
        } else {
            emptyList()
        }
    }


    // ✅ Save full card item list (overwrite)
    fun saveCardItemList(list: List<carditem>) {
        val json = gson.toJson(list)
        editor.putString(defaultKey, json).apply()
    }

    // ✅ Save total income
    fun saveTotalIncome(income: Long) {
        editor.putLong(incomeKey, income).apply()
    }

    // ✅ Get total income
    fun getTotalIncome(): Long {
        return sharedPref.getLong(incomeKey, 0L)
    }

    // ✅ Calculate total income from list
    fun calculateTotalIncome(): Long {
        val list = getCardItemList()
        return list.sumOf { it.income }
    }

    // ✅ Save total amount (expenses)
    fun saveTotalAmount(amount: Long) {
        editor.putLong(totalKey, amount).apply()
    }

    // ✅ Get total amount (expenses)
    fun getTotalAmount(): Long {
        return sharedPref.getLong(totalKey, 0L)
    }

    // ✅ Calculate total amount from list
    fun calculateTotalAmount(): Long {
        val list = getCardItemList()
        return list.sumOf { it.amount }
    }

    // ✅ Date
    fun saveDate(date: String) {
        editor.putString(dateKey, date).apply()
    }

    fun getdate(): String {
        return sharedPref.getString(dateKey, "") ?: ""
    }

    // ✅ Clear only history
    fun clearAllHistory() {
        saveCardItemList(emptyList())
    }

    // ✅ Save full history (overwrite)
    fun saveHistory(list: List<carditem>) {
        val json = gson.toJson(list)
        editor.putString(defaultKey, json).apply()
    }

    // ✅ Clear expenses + income + history (full reset)
    fun clearExpenseData() {
        editor.remove(defaultKey)       // History
        editor.remove(totalKey)         // Total expenses
        editor.remove(incomeKey)        // Total income
        editor.apply()
    }

    // ✅ Time
    fun saveTime(time: String) {
        editor.putString(timeKey, time).apply()
    }

    fun getTime(): String {
        return sharedPref.getString(timeKey, "") ?: ""
    }
}


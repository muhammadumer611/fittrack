package com.example.fittrack

import java.util.Date

data class carditem(
    val cardImage: Int = 0,
    var title: String ="",
    var catageorytype: String = "",
    var date:  String = " ",   // default current date
    var type: String = "",
    var note: String = "",
    var time: String = "",
    var amount: Long = 0,
    var totalAmount: Long = 0,
    var income: Long = 0,
    var totalIncome: Long = 0,
)


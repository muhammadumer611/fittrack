package com.example.fittrack

import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fittrack.databinding.ActivityCountExpenseBinding
import java.util.*

class ActivityCountExpense : AppCompatActivity(), CardAdapter.CardInterfaceListener {

    private lateinit var binding: ActivityCountExpenseBinding
    private lateinit var sharedPref: MySharedPref
    private lateinit var carditem: carditem
    private lateinit var adapter: CardAdapter
    private var isExpense : Boolean=false

    private var selection = ""
    private var selectedDate: String = ""
    private var selectedTime: String = ""
    private var note: String = ""

    private val cardsList = arrayListOf(
        carditem(R.drawable.food, "Food", "Expense"),
        carditem(R.drawable.buss, "Transport", "Expense"),
        carditem(R.drawable.shopping, "Shopping", "Expense"),
        carditem(R.drawable.health, "Health", "Expense"),
        carditem(R.drawable.utilities, "Utilities", "Expense"),
        carditem(R.drawable.fee_receipt__1_, "Fees", "Expense"),
        carditem(R.drawable.personal, "Personal", "Expense"),
        carditem(R.drawable.bnous, "Bonus", "Income"),
        carditem(R.drawable.alounce, "Allowance", "Income"),
        carditem(R.drawable.salary, "Salary", "Income"),
        carditem(R.drawable.royality, "Royalty", "Income"),
        carditem(R.drawable.growth, "Growth", "Income"),
        carditem(R.drawable.lease, "Lease", "Income")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCountExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = MySharedPref(this@ActivityCountExpense)
        carditem = carditem()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }


        binding.btnExpense.setOnClickListener {
            binding.btnExpense.background = ContextCompat.getDrawable(this, R.drawable.selected_background)
            binding.btnIncome.background = ContextCompat.getDrawable(this, R.drawable.default_background)

            isExpense=true
            selection = "Expense"
            toggleSelection(isExpense = true)
            showTopText(isExpense = true)
            showCards()
            Toast.makeText(this, "Expense Selected", Toast.LENGTH_SHORT).show()
        }

        binding.btnIncome.setOnClickListener {
            binding.btnExpense.background = ContextCompat.getDrawable(this, R.drawable.default_background)
            binding.btnIncome.background = ContextCompat.getDrawable(this, R.drawable.selected_background)
            isExpense=false
            selection = "Income"
            toggleSelection(isExpense = false)
            showTopText(isExpense = false)
            showCards()
            Toast.makeText(this, "Income Selected", Toast.LENGTH_SHORT).show()
        }

        binding.btnTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                this@ActivityCountExpense,
                { _, selectedHour, selectedMinute ->
                    selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                    binding.btnTime.text = selectedTime
                },
                hour,
                minute,
                true
            )
            timePickerDialog.show()
        }

        binding.btnDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    binding.btnDate.text = selectedDate
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        binding.btnsave.setOnClickListener {
            val amount = binding.tvAmount.text.toString().toLongOrNull() ?: 0L
            note = binding.Note.text.toString().trim()

            if (selection.isEmpty()) {
                Toast.makeText(this, "Please select Expense or Income", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (selectedDate.isEmpty() || selectedTime.isEmpty()) {
                Toast.makeText(this, "Please select Date and Time", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (note.isEmpty()) {
                Toast.makeText(this, "Please fill the note", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            carditem.amount = if (selection == "Expense") amount else 0L
            carditem.income = if (selection == "Income") amount else 0L
            carditem.type = selection
            carditem.date = selectedDate
            carditem.time = selectedTime
            carditem.note = note

            if (selection == "Expense") {
                val previousAmount = sharedPref.getTotalAmount()
                val totalAmount = amount + previousAmount
                sharedPref.saveTotalAmount(totalAmount)
                carditem.totalAmount = totalAmount

                // 🔹 Check 80% income used
                val totalIncome = sharedPref.getTotalIncome()
                if (totalIncome > 0) {
                    val percentUsed = (totalAmount.toDouble() / totalIncome.toDouble()) * 100
                    if (percentUsed >= 80) {
                      showNotification(
                          "⚠️ Budget Alert",
                          "Dear User\uD83C\uDF39 You have used you 80% of Income!"
                      )
                  }
                }

            }
                else {
                val previousIncome = sharedPref.getTotalIncome()
                val totalIncome = amount + previousIncome
                sharedPref.saveTotalIncome(totalIncome)
                carditem.totalIncome = totalIncome
            }

            sharedPref.saveCardItem(carditem)
            Toast.makeText(this@ActivityCountExpense, "Data saved successfully!", Toast.LENGTH_SHORT).show()
        }

        // 🔹 Default Expense filter apply kar diya
        selection = "Expense"
        toggleSelection(isExpense = true)
        showTopText(isExpense = true)
        showCards()
    }

    override fun onItemclick(type: String) {
        Toast.makeText(this@ActivityCountExpense, "Category: $type", Toast.LENGTH_SHORT).show()
        carditem.title = type
    }

    private fun showCards() {
        val filteredList = if (selection == "Expense") {
            filterCards("Expense")
        } else {
            filterCards("Income")
        }

        adapter = CardAdapter(filteredList, this@ActivityCountExpense)
        binding.recyclerview.layoutManager = GridLayoutManager(this@ActivityCountExpense, 4)
        binding.recyclerview.adapter = adapter
        binding.recyclerview.visibility = View.VISIBLE
    }

    private fun showTopText(isExpense: Boolean) {
        if (isExpense) {
            binding.AddExpense.visibility = View.VISIBLE
            binding.AddIncome.visibility = View.GONE
        } else {
            binding.AddIncome.visibility = View.VISIBLE
            binding.AddExpense.visibility = View.GONE
        }
    }

    private fun toggleSelection(isExpense: Boolean) {

    }

    private fun filterCards(type: String): List<carditem> {
        return cardsList.filter { it.catageorytype == type }
    }

    // 🔹 Notification Function Added
    private fun showNotification(title: String, message: String) {
        val channelId = "fittrack_channel"
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "FitTrack Notifications", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // apna custom icon use karein
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1001, notification)
    }
}

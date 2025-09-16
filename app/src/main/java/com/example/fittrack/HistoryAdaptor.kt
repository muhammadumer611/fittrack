package com.example.fittrack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter(private val historyList: List<carditem>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtType: TextView = itemView.findViewById(R.id.txtType)
        val txtCategory: TextView = itemView.findViewById(R.id.txtCategory)
        val txtIncome: TextView = itemView.findViewById(R.id.txtIncome)
        val txtExpense: TextView = itemView.findViewById(R.id.txtExpense)
        val txtDate: TextView = itemView.findViewById(R.id.txtDate)
        val txtTime: TextView = itemView.findViewById(R.id.txtTime)
        val txtNote: TextView = itemView.findViewById(R.id.txtNote)
        val txtTotalIncome: TextView = itemView.findViewById(R.id.txtTotalIncome)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = historyList[position]

        holder.txtType.text = "Type: ${item.type}"
        holder.txtCategory.text = "Category: ${item.title}"

        if (item.type == "Income") {
            holder.txtIncome.visibility = View.VISIBLE
            holder.txtTotalIncome.visibility = View.VISIBLE
            holder.txtExpense.visibility = View.GONE
            holder.txtIncome.text = "Income: ${item.income}"
            holder.txtTotalIncome.text = "Total Income: ${item.totalIncome}"
        }
        else {
            holder.txtExpense.visibility = View.VISIBLE
            holder.txtTotalIncome.visibility = View.GONE
            holder.txtIncome.visibility = View.GONE
            holder.txtExpense.text = "Expense: ${item.amount}"
        }

        holder.txtDate.text = "Date: ${item.date}"
        holder.txtTime.text = "Time: ${item.time}"
        holder.txtNote.text = "Note: ${item.note}"
    }

    override fun getItemCount(): Int = historyList.size
}

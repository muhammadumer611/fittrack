package com.example.fittrack

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import androidx.recyclerview.widget.RecyclerView

class CardAdapter(
    private val fullList: List<carditem>,   // ✅ Original full list
    private val listener: CardInterfaceListener
) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    private var selectedPosition = -1
    private var filteredList: List<carditem> = fullList  // ✅ Filtered list

    interface CardInterfaceListener {
        fun onItemclick(type: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.itemcard, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val items = filteredList[position]
        holder.image.setImageResource(items.cardImage)
        holder.text.text = items.title

        // ✅ Selection stroke logic
        if (position == selectedPosition) {
            holder.laymain.setCardBackgroundColor(Color.parseColor("#FFE082"))
            holder.laymain.strokeWidth = 6
            holder.laymain.strokeColor = Color.YELLOW
        } else {
            holder.laymain.setCardBackgroundColor(Color.WHITE)
            holder.laymain.strokeWidth = 0
        }

        holder.laymain.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                selectedPosition = pos
                notifyDataSetChanged()
                listener.onItemclick(items.title)
            }
        }
    }

    override fun getItemCount(): Int = filteredList.size

    inner class CardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.cardImage)
        val text: TextView = view.findViewById(R.id.cardText)
        val laymain: MaterialCardView = view.findViewById(R.id.laymain)
    }

    // ✅ Filter function
    fun filter(type: String) {
        filteredList = if (type == "All") {
            fullList
        } else {
            fullList.filter { it.catageorytype.equals(type, ignoreCase = true) }
        }
        selectedPosition = -1 // filter ke baad reset selection
        notifyDataSetChanged()
    }
}

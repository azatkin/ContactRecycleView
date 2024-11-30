package com.example.customview2

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val contactName: TextView = itemView.findViewById(R.id.contactName)
    val contactNumber: TextView = itemView.findViewById(R.id.contactNumber)

}
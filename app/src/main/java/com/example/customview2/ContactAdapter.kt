package com.example.customview2
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ContactAdapter(private var contacts: List<Contact>) : RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.contact_fragment_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val contact = contacts[position]
        holder.contactName.text = contact.name
        holder.contactNumber.text = contact.number
    }

    override fun getItemCount(): Int = contacts.size

    // Метод для обновления данных
    fun updateContacts(newContacts: List<Contact>) {
        contacts = newContacts
        notifyDataSetChanged()
    }
}
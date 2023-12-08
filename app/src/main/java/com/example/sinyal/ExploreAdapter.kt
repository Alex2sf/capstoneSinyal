package com.example.sinyal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// ExploreAdapter.kt
class ExploreAdapter(private val dataList: List<YourDataModel>) : RecyclerView.Adapter<ExploreAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView: TextView = itemView.findViewById(R.id.hel)
        // Tambahkan referensi ke elemen UI lainnya sesuai kebutuhan
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_item_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = dataList[position]

        // Set nilai-nilai ke dalam elemen UI
        holder.dateTextView.text = currentItem.date
        // ...

        // Tambahkan event onClick atau apapun yang Anda perlukan di sini
        holder.itemView.setOnClickListener {
            // Lakukan sesuatu saat item diklik
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}

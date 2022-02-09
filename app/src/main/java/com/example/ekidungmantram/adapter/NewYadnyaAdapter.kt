package com.example.ekidungmantram.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ekidungmantram.R
import com.example.ekidungmantram.model.NewYadnyaModel

class NewYadnyaAdapter(val results: ArrayList<NewYadnyaModel.Data>)
    : RecyclerView.Adapter<NewYadnyaAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.grid_layout_list_item, parent, false)
    )

    override fun getItemCount() = results.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.view.findViewById<TextView>(R.id.title_yadnya_baru).setText(result.nama_post)
        holder.view.findViewById<TextView>(R.id.jenis_yadnya_baru).setText(result.kategori)
    }

    class ViewHolder (val view: View) : RecyclerView.ViewHolder(view)

    fun setData (data: List<NewYadnyaModel.Data>) {
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }
}
package com.example.ekidungmantram.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ekidungmantram.R
import com.example.ekidungmantram.model.NewKidungModel

class NewKidungAdapter(val results:ArrayList<NewKidungModel.DataK>, val listener: OnAdapterKidungListener)
    : RecyclerView.Adapter<NewKidungAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.layout_list_kidung, parent, false)
    )

    class ViewHolder (val view: View) : RecyclerView.ViewHolder(view){
        val title: TextView = view.findViewById(R.id.title_kidung_baru)
        val jenis: TextView = view.findViewById(R.id.jenis_kidung_baru)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.title.setText(result.nama_post)
        holder.jenis.setText("Kidung "+result.kategori)
        holder.view.setOnClickListener{
            listener.onClick(result)
        }
    }

    override fun getItemCount() = results.size

    fun setData (data: List<NewKidungModel.DataK>) {
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }

    interface OnAdapterKidungListener {
        fun onClick(result: NewKidungModel.DataK)
    }
}
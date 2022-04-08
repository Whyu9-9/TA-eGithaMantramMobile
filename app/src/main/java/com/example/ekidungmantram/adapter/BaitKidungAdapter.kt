package com.example.ekidungmantram.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.model.AllKidungModel
import com.example.ekidungmantram.model.DetailBaitKidungModel

class BaitKidungAdapter(val results: ArrayList<DetailBaitKidungModel.Data>)
    : RecyclerView.Adapter<BaitKidungAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.layout_list_detail_kidung, parent, false)
    )

    override fun getItemCount() = results.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.bait.setText(result.urutan)
        holder.isibait.setText(result.bait)
    }

    class ViewHolder (val view: View) : RecyclerView.ViewHolder(view){
        val bait: TextView    = view.findViewById(R.id.bait)
        val isibait: TextView = view.findViewById(R.id.isiBait)
    }

    fun setData (data: List<DetailBaitKidungModel.Data>) {
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }
}
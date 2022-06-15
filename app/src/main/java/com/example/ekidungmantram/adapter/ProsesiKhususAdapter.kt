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
import com.example.ekidungmantram.model.MantramProsesiModel
import com.example.ekidungmantram.model.ProsesiKhususModel

class ProsesiKhususAdapter(val results:ArrayList<ProsesiKhususModel.Data>, val listener: OnAdapterProsesiKhususListener)
    : RecyclerView.Adapter<ProsesiKhususAdapter.ViewHolder>(){

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title_prosesi_khusus)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.layout_list_prosesi_khusus, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.title.setText(result.nama_post)
        holder.view.setOnClickListener{
            listener.onClick(result)
        }
    }

    override fun getItemCount() = results.size

    fun setData (data: List<ProsesiKhususModel.Data>) {
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }

    interface OnAdapterProsesiKhususListener {
        fun onClick(result: ProsesiKhususModel.Data)
    }
}
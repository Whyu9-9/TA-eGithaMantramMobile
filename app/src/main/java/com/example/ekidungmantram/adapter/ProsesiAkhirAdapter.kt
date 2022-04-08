package com.example.ekidungmantram.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ekidungmantram.R
import com.example.ekidungmantram.model.ProsesiAkhirModel

class ProsesiAkhirAdapter(val results:ArrayList<ProsesiAkhirModel.Data>, val listener: OnAdapterAkhirListener)
    : RecyclerView.Adapter<ProsesiAkhirAdapter.ViewHolder>(){

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title_prosesi_akhir)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.layout_list_prosesi_akhir, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.title.setText(result.nama_post)
        holder.view.setOnClickListener{
            listener.onClick(result)
        }
    }

    override fun getItemCount() = results.size

    fun setData (data: List<ProsesiAkhirModel.Data>) {
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }

    interface OnAdapterAkhirListener {
        fun onClick(result: ProsesiAkhirModel.Data)
    }
}
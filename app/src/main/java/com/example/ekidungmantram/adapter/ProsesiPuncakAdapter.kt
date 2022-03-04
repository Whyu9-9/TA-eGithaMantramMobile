package com.example.ekidungmantram.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ekidungmantram.R
import com.example.ekidungmantram.model.ProsesiAwalModel
import com.example.ekidungmantram.model.ProsesiPuncakModel

class ProsesiPuncakAdapter(val results:ArrayList<ProsesiPuncakModel.Data>, val listener: OnAdapterPuncakListener)
    : RecyclerView.Adapter<ProsesiPuncakAdapter.ViewHolder>(){

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title_prosesi_puncak)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.layout_list_prosesi_puncak, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.title.setText(result.nama_post)
    }

    override fun getItemCount() = results.size

    fun setData (data: List<ProsesiPuncakModel.Data>) {
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }

    interface OnAdapterPuncakListener {
        fun onClick(result: ProsesiPuncakModel.Data)
    }
}
package com.example.ekidungmantram.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ekidungmantram.R
import com.example.ekidungmantram.model.NewKidungModel
import com.example.ekidungmantram.model.ProsesiAwalModel

class ProsesiAwalAdapter(val results:ArrayList<ProsesiAwalModel.Data>, val listener: OnAdapterAwalListener)
    : RecyclerView.Adapter<ProsesiAwalAdapter.ViewHolder>(){

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title_prosesi_awal)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.layout_list_prosesi_awal, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.title.setText(result.nama_post)
    }

    override fun getItemCount() = results.size

    fun setData (data: List<ProsesiAwalModel.Data>) {
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }

    interface OnAdapterAwalListener {
        fun onClick(result: ProsesiAwalModel.Data)
    }
}
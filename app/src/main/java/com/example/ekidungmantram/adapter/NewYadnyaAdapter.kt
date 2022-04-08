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
import com.example.ekidungmantram.model.NewYadnyaModel

class NewYadnyaAdapter(val results: ArrayList<NewYadnyaModel.Data>, val listener: OnAdapterListener)
    : RecyclerView.Adapter<NewYadnyaAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.grid_layout_list_item, parent, false)
    )

    override fun getItemCount() = results.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.title.setText(result.nama_post)
        holder.jenis.setText(result.kategori)
        Glide.with(holder.view).load(Constant.IMAGE_URL+result.gambar).into(holder.gambar)
        holder.view.setOnClickListener{
            listener.onClick(result)
        }
    }

    class ViewHolder (val view: View) : RecyclerView.ViewHolder(view){
        val title: TextView   = view.findViewById(R.id.title_yadnya_baru)
        val jenis: TextView   = view.findViewById(R.id.jenis_yadnya_baru)
        val gambar: ImageView = view.findViewById(R.id.yadnya_baru)
    }

    fun setData (data: List<NewYadnyaModel.Data>) {
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }

    interface OnAdapterListener{
        fun onClick(result: NewYadnyaModel.Data)
    }
}
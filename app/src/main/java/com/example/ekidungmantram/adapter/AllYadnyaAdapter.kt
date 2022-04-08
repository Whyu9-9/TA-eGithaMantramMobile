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
import com.example.ekidungmantram.model.AllYadnyaModel

class AllYadnyaAdapter(private var results: ArrayList<AllYadnyaModel>, val listener: OnAdapterAllYadnyaListener)
    : RecyclerView.Adapter<AllYadnyaAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView = layoutInflater.inflate(R.layout.layout_list_all_yadnya, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.bindItem(result)
        holder.itemView.setOnClickListener{
            listener.onClick(result)
        }
    }

    override fun getItemCount(): Int = results.size


    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private var title : TextView = view.findViewById(R.id.title_yadnya_all)
        private var jenis : TextView = view.findViewById(R.id.jenis_yadnya_all)
        private var gambar : ImageView = view.findViewById(R.id.yadnyaAll)
        fun bindItem(data: AllYadnyaModel) {
            title.text = data.nama_post
            jenis.text = data.kategori
            Glide.with(itemView).load(Constant.IMAGE_URL + data.gambar).into(gambar)
        }

    }

    interface OnAdapterAllYadnyaListener {
        fun onClick(result: AllYadnyaModel)
    }
}
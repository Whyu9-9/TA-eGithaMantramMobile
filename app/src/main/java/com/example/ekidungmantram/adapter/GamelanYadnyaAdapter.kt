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
import com.example.ekidungmantram.model.GamelanYadnyaModel
import com.example.ekidungmantram.model.ProsesiAkhirModel
import com.example.ekidungmantram.model.ProsesiAwalModel
import com.example.ekidungmantram.model.ProsesiPuncakModel

class GamelanYadnyaAdapter(val results:ArrayList<GamelanYadnyaModel.Data>, val listener: OnAdapterGamelanYadnyaListener)
    : RecyclerView.Adapter<GamelanYadnyaAdapter.ViewHolder>(){

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title_gamelan_yadnya)
        val image: ImageView = view.findViewById(R.id.yadnyaGamelanDetail)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.layout_gamelan_detail_yadnya, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.title.setText(result.nama_post)
        Glide.with(holder.view).load(Constant.IMAGE_URL +result.gambar).into(holder.image)
    }

    override fun getItemCount() = results.size

    fun setData (data: List<GamelanYadnyaModel.Data>) {
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }

    interface OnAdapterGamelanYadnyaListener {
        fun onClick(result: GamelanYadnyaModel.Data)
    }
}
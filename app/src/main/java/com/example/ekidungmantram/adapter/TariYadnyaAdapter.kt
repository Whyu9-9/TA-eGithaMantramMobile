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
import com.example.ekidungmantram.model.*

class TariYadnyaAdapter(val results:ArrayList<TariYadnyaModel.Data>, val listener: OnAdapterTariYadnyaListener)
    : RecyclerView.Adapter<TariYadnyaAdapter.ViewHolder>(){

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title_tari_yadnya)
        val image: ImageView = view.findViewById(R.id.yadnyaTariDetail)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.layout_tari_detail_yadnya, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.title.setText(result.nama_post)
        if(result.gambar != null){
            Glide.with(holder.view).load(Constant.IMAGE_URL +result.gambar).into(holder.image)
        }else{
            holder.image.setImageResource(R.drawable.music)
        }
    }

    override fun getItemCount() = results.size

    fun setData (data: List<TariYadnyaModel.Data>) {
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }

    interface OnAdapterTariYadnyaListener {
        fun onClick(result: TariYadnyaModel.Data)
    }
}
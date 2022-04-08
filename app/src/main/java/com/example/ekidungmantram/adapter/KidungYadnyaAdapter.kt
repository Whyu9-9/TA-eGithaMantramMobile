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

class KidungYadnyaAdapter(val results:ArrayList<KidungYadnyaModel.Data>, val listener: OnAdapterKidungYadnyaListener)
    : RecyclerView.Adapter<KidungYadnyaAdapter.ViewHolder>(){

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title_kidung_yadnya)
        val image: ImageView = view.findViewById(R.id.yadnyaKidungDetail)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.layout_kidung_detail_yadnya, parent, false)
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

    fun setData (data: List<KidungYadnyaModel.Data>) {
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }

    interface OnAdapterKidungYadnyaListener {
        fun onClick(result: KidungYadnyaModel.Data)
    }
}
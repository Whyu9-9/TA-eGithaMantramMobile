package com.example.ekidungmantram.adapter.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.model.AllMantramModel
import com.example.ekidungmantram.model.adminmodel.*

class KidungYadnyaAdminAdapter(private var results: ArrayList<DetailAllKidungOnYadnyaAdminModel>)
    : RecyclerView.Adapter<KidungYadnyaAdminAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView = layoutInflater.inflate(R.layout.layout_kidung_detail_yadnya_admin, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.bindItem(result)
    }

    override fun getItemCount(): Int = results.size

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private var title : TextView = view.findViewById(R.id.title_kidung_yadnya_admin)
        private var gambar : ImageView = view.findViewById(R.id.yadnyaKidungDetailAdmin)
        fun bindItem(data: DetailAllKidungOnYadnyaAdminModel) {
            title.text = data.nama_post
            if(data.gambar != null){
                Glide.with(itemView).load(Constant.IMAGE_URL + data.gambar).into(gambar)
            }else{
                gambar.setImageResource(R.drawable.music)
            }
        }

    }
}
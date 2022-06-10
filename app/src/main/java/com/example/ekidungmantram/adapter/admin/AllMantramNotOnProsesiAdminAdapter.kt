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
import com.example.ekidungmantram.model.adminmodel.*

class AllMantramNotOnProsesiAdminAdapter(private var results: ArrayList<AllMantramAdminModel>)
    : RecyclerView.Adapter<AllMantramNotOnProsesiAdminAdapter.ViewHolder>() {
    private var onclickAdd: ((AllMantramAdminModel)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView = layoutInflater.inflate(R.layout.layout_all_mantram_not_on_prosesi_admin, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.bindItem(result)
        holder.add.setOnClickListener {
            onclickAdd?.invoke(result)
        }
    }

    override fun getItemCount(): Int = results.size

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var add : ImageView = view.findViewById(R.id.addMantramToProsesi)
        private var title : TextView = view.findViewById(R.id.titleNP_mantram_admin)
        private var jenis : TextView = view.findViewById(R.id.jenisNP_mantram_admin)
        private var gambar : ImageView = view.findViewById(R.id.mantramNP_imgs_admin)
        fun bindItem(data: AllMantramAdminModel) {
            if(data.nama_post.length > 20){
                title.textSize = 15F
            }
            title.text = data.nama_post
            if(data.kategori != null){
                jenis.text = "Mantram "+data.kategori
            }else{
                jenis.text = "Mantram Hindu"
            }
            if(data.gambar != null){
                Glide.with(itemView).load(Constant.IMAGE_URL + data.gambar).into(gambar)
            }else{
                gambar.setImageResource(R.drawable.meditation)
            }
        }

    }

    fun setOnClickAdd(callback: ((AllMantramAdminModel)->Unit)){
        this.onclickAdd = callback
    }
}
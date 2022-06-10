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

class AllMantramOnProsesiAdminAdapter(private var results: ArrayList<DetailAllMantramOnProsesiAdminModel>)
    : RecyclerView.Adapter<AllMantramOnProsesiAdminAdapter.ViewHolder>() {
    private var onclickDelete: ((DetailAllMantramOnProsesiAdminModel)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView = layoutInflater.inflate(R.layout.layout_all_mantram_on_prosesi_admin, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.bindItem(result)
        holder.delete.setOnClickListener {
            onclickDelete?.invoke(result)
        }
    }

    override fun getItemCount(): Int = results.size

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var delete : ImageView = view.findViewById(R.id.deleteMantramOnProsesi)
        private var title : TextView = view.findViewById(R.id.titleOP_mantram_admin)
        private var jenis : TextView = view.findViewById(R.id.jenisOP_mantram_admin)
        private var gambar : ImageView = view.findViewById(R.id.mantramOP_imgs_admin)
        fun bindItem(data: DetailAllMantramOnProsesiAdminModel) {
            if(data.nama_post.length > 20){
                title.textSize = 15F
            }
            title.text = data.nama_post
            if(data.nama_kategori != null){
                jenis.text = "Mantram "+data.nama_kategori
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

    fun setOnClickDelete(callback: ((DetailAllMantramOnProsesiAdminModel)->Unit)){
        this.onclickDelete = callback
    }
}
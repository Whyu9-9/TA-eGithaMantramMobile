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

class AllTariOnYadnyaAdminAdapter(private var results: ArrayList<DetailAllTariOnYadnyaAdminModel>)
    : RecyclerView.Adapter<AllTariOnYadnyaAdminAdapter.ViewHolder>() {
    private var onclickDelete: ((DetailAllTariOnYadnyaAdminModel)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView = layoutInflater.inflate(R.layout.layout_all_tari_on_yadnya_admin, parent, false)
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
        var delete : ImageView = view.findViewById(R.id.deleteTariOnYadnya)
        private var title : TextView = view.findViewById(R.id.titleOY_tari_admin)
        private var jenis : TextView = view.findViewById(R.id.jenisOY_tari_admin)
        private var gambar : ImageView = view.findViewById(R.id.tariOY_imgs_admin)
        fun bindItem(data: DetailAllTariOnYadnyaAdminModel) {
            if(data.nama_post.length > 20){
                title.textSize = 15F
            }
            title.text = data.nama_post
            jenis.text = "Tari Bali"
            if(data.gambar != null){
                Glide.with(itemView).load(Constant.IMAGE_URL + data.gambar).into(gambar)
            }else{
                gambar.setImageResource(R.drawable.sample_image_tari)
            }
        }

    }

    fun setOnClickDelete(callback: ((DetailAllTariOnYadnyaAdminModel)->Unit)){
        this.onclickDelete = callback
    }
}
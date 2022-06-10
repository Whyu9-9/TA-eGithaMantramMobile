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

class AllTabuhOnProsesiAdminAdapter(private var results: ArrayList<DetailAllTabuhOnProsesiAdminModel>)
    : RecyclerView.Adapter<AllTabuhOnProsesiAdminAdapter.ViewHolder>() {
    private var onclickDelete: ((DetailAllTabuhOnProsesiAdminModel)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView = layoutInflater.inflate(R.layout.layout_all_tabuh_on_prosesi_admin, parent, false)
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
        var delete : ImageView = view.findViewById(R.id.deleteTabuhOnProsesi)
        private var title : TextView = view.findViewById(R.id.titleOP_tabuh_admin)
        private var jenis : TextView = view.findViewById(R.id.jenisOP_tabuh_admin)
        private var gambar : ImageView = view.findViewById(R.id.tabuhOP_imgs_admin)
        fun bindItem(data: DetailAllTabuhOnProsesiAdminModel) {
            if(data.nama_post.length > 20){
                title.textSize = 15F
            }
            title.text = data.nama_post
            jenis.text = "Tabuh Bali"
            if(data.gambar != null){
                Glide.with(itemView).load(Constant.IMAGE_URL + data.gambar).into(gambar)
            }else{
                gambar.setImageResource(R.drawable.sample_image_tabuh)
            }
        }

    }

    fun setOnClickDelete(callback: ((DetailAllTabuhOnProsesiAdminModel)->Unit)){
        this.onclickDelete = callback
    }
}
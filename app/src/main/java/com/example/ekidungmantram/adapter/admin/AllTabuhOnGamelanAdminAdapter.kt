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
import com.example.ekidungmantram.model.adminmodel.AllLirikKidungAdminModel
import com.example.ekidungmantram.model.adminmodel.AllMantramAdminModel
import com.example.ekidungmantram.model.adminmodel.AllTabuhAdminModel
import com.example.ekidungmantram.model.adminmodel.DetailAllTabuhOnGamelanAdminModel

class AllTabuhOnGamelanAdminAdapter(private var results: ArrayList<DetailAllTabuhOnGamelanAdminModel>)
    : RecyclerView.Adapter<AllTabuhOnGamelanAdminAdapter.ViewHolder>() {
    private var onclickDelete: ((DetailAllTabuhOnGamelanAdminModel)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView = layoutInflater.inflate(R.layout.layout_all_tabuh_on_gamelan_admin, parent, false)
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
        var delete : ImageView = view.findViewById(R.id.deleteTabuhOnGamelan)
        private var title : TextView = view.findViewById(R.id.titleOG_tabuh_admin)
        private var jenis : TextView = view.findViewById(R.id.jenisOG_tabuh_admin)
        private var gambar : ImageView = view.findViewById(R.id.tabuhOG_imgs_admin)
        fun bindItem(data: DetailAllTabuhOnGamelanAdminModel) {
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

    fun setOnClickDelete(callback: ((DetailAllTabuhOnGamelanAdminModel)->Unit)){
        this.onclickDelete = callback
    }
}
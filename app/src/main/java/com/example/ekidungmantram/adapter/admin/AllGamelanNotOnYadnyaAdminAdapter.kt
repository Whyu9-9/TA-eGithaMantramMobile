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
import com.example.ekidungmantram.model.adminmodel.AllGamelanAdminModel
import com.example.ekidungmantram.model.adminmodel.AllLirikKidungAdminModel
import com.example.ekidungmantram.model.adminmodel.AllMantramAdminModel
import com.example.ekidungmantram.model.adminmodel.AllTabuhAdminModel

class AllGamelanNotOnYadnyaAdminAdapter(private var results: ArrayList<AllGamelanAdminModel>)
    : RecyclerView.Adapter<AllGamelanNotOnYadnyaAdminAdapter.ViewHolder>() {
    private var onclickAdd: ((AllGamelanAdminModel)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView = layoutInflater.inflate(R.layout.layout_all_gamelan_not_on_yadnya_admin, parent, false)
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
        var add : ImageView = view.findViewById(R.id.addGamelanToYadnya)
        private var title : TextView = view.findViewById(R.id.titleNY_gamelan_admin)
        private var jenis : TextView = view.findViewById(R.id.jenisNY_gamelan_admin)
        private var gambar : ImageView = view.findViewById(R.id.gamelanNY_imgs_admin)
        fun bindItem(data: AllGamelanAdminModel) {
            if(data.nama_post.length > 20){
                title.textSize = 15F
            }
            title.text = data.nama_post
            jenis.text = "Gamelan Bali"
            if(data.gambar != null){
                Glide.with(itemView).load(Constant.IMAGE_URL + data.gambar).into(gambar)
            }else{
                gambar.setImageResource(R.drawable.sample_image_gamelan)
            }
        }

    }

    fun setOnClickAdd(callback: ((AllGamelanAdminModel)->Unit)){
        this.onclickAdd = callback
    }
}
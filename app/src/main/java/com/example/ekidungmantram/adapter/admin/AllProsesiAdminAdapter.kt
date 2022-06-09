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
import com.example.ekidungmantram.model.adminmodel.AllMantramAdminModel
import com.example.ekidungmantram.model.adminmodel.AllProsesiAdminModel
import com.example.ekidungmantram.model.adminmodel.AllTariAdminModel

class AllProsesiAdminAdapter(private var results: ArrayList<AllProsesiAdminModel>, val listener: OnAdapterAllProsesiAdminListener)
    : RecyclerView.Adapter<AllProsesiAdminAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView = layoutInflater.inflate(R.layout.layout_all_prosesi_admin, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.bindItem(result)
        holder.itemView.setOnClickListener{
            listener.onClick(result)
        }
    }

    override fun getItemCount(): Int = results.size

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private var title : TextView = view.findViewById(R.id.title_prosesi_admin)
        private var jenis : TextView = view.findViewById(R.id.jenis_prosesi_admin)
        private var gambar : ImageView = view.findViewById(R.id.prosesi_imgs_admin)
        fun bindItem(data: AllProsesiAdminModel) {
            if(data.nama_post.length > 20){
                title.textSize = 15F
            }
            title.text = data.nama_post
            jenis.text = "Prosesi Upacara"

            if(data.gambar != null){
                Glide.with(itemView).load(Constant.IMAGE_URL + data.gambar).into(gambar)
            }else{
                gambar.setImageResource(R.drawable.sample_image_yadnya)
            }
        }

    }

    interface OnAdapterAllProsesiAdminListener {
        fun onClick(result: AllProsesiAdminModel)
    }
}
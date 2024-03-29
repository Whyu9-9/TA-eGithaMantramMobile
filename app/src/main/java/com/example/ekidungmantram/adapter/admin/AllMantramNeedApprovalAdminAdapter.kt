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
import com.example.ekidungmantram.model.adminmodel.AllMantramAdminModel

class AllMantramNeedApprovalAdminAdapter(private var results: ArrayList<AllMantramAdminModel>, val listener: OnAdapterAllMantramNAAdminListener)
    : RecyclerView.Adapter<AllMantramNeedApprovalAdminAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView = layoutInflater.inflate(R.layout.layout_all_mantram_need_approval_admin, parent, false)
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
        private var title : TextView = view.findViewById(R.id.titleNA_mantram_admin)
        private var jenis : TextView = view.findViewById(R.id.jenisNA_mantram_admin)
        private var gambar : ImageView = view.findViewById(R.id.mantramNA_img_admin)
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

    interface OnAdapterAllMantramNAAdminListener {
        fun onClick(result: AllMantramAdminModel)
    }
}
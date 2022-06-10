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

class AllKidungNotOnProsesiAdminAdapter(private var results: ArrayList<AllKidungAdminModel>)
    : RecyclerView.Adapter<AllKidungNotOnProsesiAdminAdapter.ViewHolder>() {
    private var onclickAdd: ((AllKidungAdminModel)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView = layoutInflater.inflate(R.layout.layout_all_kidung_not_on_prosesi_admin, parent, false)
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
        var add : ImageView = view.findViewById(R.id.addKidungToProsesi)
        private var title : TextView = view.findViewById(R.id.titleNP_kidung_admin)
        private var jenis : TextView = view.findViewById(R.id.jenisNP_kidung_admin)
        private var gambar : ImageView = view.findViewById(R.id.kidungNP_imgs_admin)
        fun bindItem(data: AllKidungAdminModel) {
            if(data.nama_post.length > 20){
                title.textSize = 15F
            }
            title.text = data.nama_post
            jenis.text = "Kidung "+data.kategori
            if(data.gambar != null){
                Glide.with(itemView).load(Constant.IMAGE_URL + data.gambar).into(gambar)
            }else{
                gambar.setImageResource(R.drawable.sample_image_yadnya)
            }
        }

    }

    fun setOnClickAdd(callback: ((AllKidungAdminModel)->Unit)){
        this.onclickAdd = callback
    }
}
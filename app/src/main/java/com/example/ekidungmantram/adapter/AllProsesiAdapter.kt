package com.example.ekidungmantram.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.model.AllProsesiModel

class AllProsesiAdapter(private var results: ArrayList<AllProsesiModel>, val listener: OnAdapterAllProsesiListener)
    : RecyclerView.Adapter<AllProsesiAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView = layoutInflater.inflate(R.layout.layout_all_prosesi, parent, false)
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
        private var title : TextView = view.findViewById(R.id.title_prosesi)
        private var jenis : TextView = view.findViewById(R.id.jenis_prosesi)
        private var gambar : ImageView = view.findViewById(R.id.prosesi_imgs)
        fun bindItem(data: AllProsesiModel) {
            title.text = data.nama_post
            jenis.text = "Prosesi Upacara"
            if(data.nama_post.length > 30){
                title.textSize = 15F
            }
            if(data.gambar != null){
                Glide.with(itemView).load(Constant.IMAGE_URL + data.gambar).into(gambar)
            }else{
                gambar.setImageResource(R.drawable.sample_image_yadnya)
            }
        }
    }

    interface OnAdapterAllProsesiListener {
        fun onClick(result: AllProsesiModel)
    }
}
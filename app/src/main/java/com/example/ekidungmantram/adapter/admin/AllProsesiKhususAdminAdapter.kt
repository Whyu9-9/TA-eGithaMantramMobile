package com.example.ekidungmantram.adapter.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ekidungmantram.R
import com.example.ekidungmantram.model.adminmodel.*

class AllProsesiKhususAdminAdapter(private var results: ArrayList<DetailAllProsesiKhususAdminModel>)
    : RecyclerView.Adapter<AllProsesiKhususAdminAdapter.ViewHolder>() {
    private var onclickDelete: ((DetailAllProsesiKhususAdminModel)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView = layoutInflater.inflate(R.layout.layout_all_prosesi_khusus_admin, parent, false)
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
        var delete : ImageView = view.findViewById(R.id.hapusProsesiKhusus)
        private var title : TextView = view.findViewById(R.id.namaProsesiKhusus)
        fun bindItem(data: DetailAllProsesiKhususAdminModel) {
            if(data.nama_post.length > 20){
                title.textSize = 15F
            }

            title.text = data.nama_post
        }

    }

    fun setOnClickDelete(callback: ((DetailAllProsesiKhususAdminModel)->Unit)){
        this.onclickDelete = callback
    }
}
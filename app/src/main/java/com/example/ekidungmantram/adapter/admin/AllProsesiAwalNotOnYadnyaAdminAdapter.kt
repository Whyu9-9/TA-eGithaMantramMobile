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

class AllProsesiAwalNotOnYadnyaAdminAdapter(private var results: ArrayList<AllProsesiAdminModel>)
    : RecyclerView.Adapter<AllProsesiAwalNotOnYadnyaAdminAdapter.ViewHolder>() {
    private var onclickAdd: ((AllProsesiAdminModel)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView = layoutInflater.inflate(R.layout.layout_all_prosesi_awal_not_on_yadnya_admin, parent, false)
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
        var add : ImageView = view.findViewById(R.id.addProsesiAwalYadnya)
        private var title : TextView = view.findViewById(R.id.nama_prosesi_awal_yadnya)
        fun bindItem(data: AllProsesiAdminModel) {
            if(data.nama_post.length > 20){
                title.textSize = 15F
            }
            title.text = data.nama_post
        }

    }

    fun setOnClickAdd(callback: ((AllProsesiAdminModel)->Unit)){
        this.onclickAdd = callback
    }
}
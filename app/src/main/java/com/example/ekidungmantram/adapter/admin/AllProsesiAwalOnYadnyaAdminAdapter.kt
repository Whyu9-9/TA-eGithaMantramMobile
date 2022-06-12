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
import com.example.ekidungmantram.model.adminmodel.*

class AllProsesiAwalOnYadnyaAdminAdapter(private var results: ArrayList<DetailAllProsesiAwalOnYadnyaAdminModel>)
    : RecyclerView.Adapter<AllProsesiAwalOnYadnyaAdminAdapter.ViewHolder>() {
    private var onclickDelete: ((DetailAllProsesiAwalOnYadnyaAdminModel)->Unit)? = null
    private var onclickUp: ((DetailAllProsesiAwalOnYadnyaAdminModel)->Unit)? = null
    private var onclickDown: ((DetailAllProsesiAwalOnYadnyaAdminModel)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView = layoutInflater.inflate(R.layout.layout_all_prosesi_awal_on_yadnya_admin, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.bindItem(result)
        holder.delete.setOnClickListener {
            onclickDelete?.invoke(result)
        }
        holder.up.setOnClickListener {
            onclickUp?.invoke(result)
        }
        holder.down.setOnClickListener {
            onclickDown?.invoke(result)
        }
    }

    override fun getItemCount(): Int = results.size

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var delete : ImageView = view.findViewById(R.id.deleteProsesiAwalYadnya)
        var up : ImageView = view.findViewById(R.id.upAwal)
        var down : ImageView = view.findViewById(R.id.downAwal)
        private var title : TextView = view.findViewById(R.id.title_prosesi_awal_yadnya)
        fun bindItem(data: DetailAllProsesiAwalOnYadnyaAdminModel) {
            if(data.nama_post.length > 20){
                title.textSize = 15F
            }
            if(data.index.equals("first")){
                up.visibility = View.GONE
            }
            if(data.index.equals("last")){
                down.visibility = View.GONE
            }

            if(data.index.equals("none")){
                up.visibility   = View.GONE
                down.visibility = View.GONE
            }

            title.text = data.nama_post
        }

    }

    fun setOnClickDelete(callback: ((DetailAllProsesiAwalOnYadnyaAdminModel)->Unit)){
        this.onclickDelete = callback
    }

    fun setOnClickUp(callback: ((DetailAllProsesiAwalOnYadnyaAdminModel)->Unit)){
        this.onclickUp = callback
    }

    fun setOnClickDown(callback: ((DetailAllProsesiAwalOnYadnyaAdminModel)->Unit)){
        this.onclickDown = callback
    }
}
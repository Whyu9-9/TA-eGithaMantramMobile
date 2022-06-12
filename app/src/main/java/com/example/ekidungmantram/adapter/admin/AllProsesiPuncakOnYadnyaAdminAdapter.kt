package com.example.ekidungmantram.adapter.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ekidungmantram.R
import com.example.ekidungmantram.model.adminmodel.*

class AllProsesiPuncakOnYadnyaAdminAdapter(private var results: ArrayList<DetailAllProsesiPuncakOnYadnyaAdminModel>)
    : RecyclerView.Adapter<AllProsesiPuncakOnYadnyaAdminAdapter.ViewHolder>() {
    private var onclickDelete: ((DetailAllProsesiPuncakOnYadnyaAdminModel)->Unit)? = null
    private var onclickUp: ((DetailAllProsesiPuncakOnYadnyaAdminModel)->Unit)? = null
    private var onclickDown: ((DetailAllProsesiPuncakOnYadnyaAdminModel)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView = layoutInflater.inflate(R.layout.layout_all_prosesi_puncak_on_yadnya_admin, parent, false)
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
        var delete : ImageView = view.findViewById(R.id.deleteProsesiPuncakYadnya)
        var up : ImageView = view.findViewById(R.id.upPuncak)
        var down : ImageView = view.findViewById(R.id.downPuncak)
        private var title : TextView = view.findViewById(R.id.title_prosesi_puncak_yadnya)
        fun bindItem(data: DetailAllProsesiPuncakOnYadnyaAdminModel) {
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

    fun setOnClickDelete(callback: ((DetailAllProsesiPuncakOnYadnyaAdminModel)->Unit)){
        this.onclickDelete = callback
    }

    fun setOnClickUp(callback: ((DetailAllProsesiPuncakOnYadnyaAdminModel)->Unit)){
        this.onclickUp = callback
    }

    fun setOnClickDown(callback: ((DetailAllProsesiPuncakOnYadnyaAdminModel)->Unit)){
        this.onclickDown = callback
    }
}
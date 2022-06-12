package com.example.ekidungmantram.adapter.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ekidungmantram.R
import com.example.ekidungmantram.model.adminmodel.*

class ProsesiAkhirYadnyaAdminAdapter(private var results: ArrayList<DetailAllProsesiAkhirOnYadnyaAdminModel>, val listener: OnAdapterProsesiAkhirYadnyaAdminListener)
    : RecyclerView.Adapter<ProsesiAkhirYadnyaAdminAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView = layoutInflater.inflate(R.layout.layout_list_prosesi_akhir_admin, parent, false)
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
        private var title : TextView = view.findViewById(R.id.title_prosesi_akhir_admin)
        fun bindItem(data: DetailAllProsesiAkhirOnYadnyaAdminModel) {
            title.text = data.nama_post
        }
    }

    interface OnAdapterProsesiAkhirYadnyaAdminListener {
        fun onClick(result: DetailAllProsesiAkhirOnYadnyaAdminModel)
    }
}
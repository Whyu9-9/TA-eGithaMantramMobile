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

class BaitKidungListAdminAdapter(private var results: ArrayList<DetailAllLirikKidungAdminModel>)
    : RecyclerView.Adapter<BaitKidungListAdminAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView = layoutInflater.inflate(R.layout.layout_list_detail_kidung_admin, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.bindItem(result)
    }

    override fun getItemCount(): Int = results.size

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private var nomor : TextView = view.findViewById(R.id.baitAdmin)
        private var bait : TextView = view.findViewById(R.id.isiBaitAdmin)
        fun bindItem(data: DetailAllLirikKidungAdminModel) {
            nomor.text = data.urutan
            bait.text = data.bait
        }
    }
}
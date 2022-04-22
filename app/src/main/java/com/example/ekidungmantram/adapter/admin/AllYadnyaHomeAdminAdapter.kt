package com.example.ekidungmantram.adapter.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ekidungmantram.R
import com.example.ekidungmantram.model.adminmodel.AllYadnyaHomeAdminModel

class AllYadnyaHomeAdminAdapter(private var results: ArrayList<AllYadnyaHomeAdminModel>, val listener: OnAdapterAllYadnyaHomeAdminListener)
    : RecyclerView.Adapter<AllYadnyaHomeAdminAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView   = layoutInflater.inflate(R.layout.layout_list_all_yadnya_admin_home, parent, false)
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
        private var title  : TextView = view.findViewById(R.id.nama_yadnya_admin)
        private var jumlah : TextView = view.findViewById(R.id.jumlah_yadnya_admin)

        fun bindItem(data: AllYadnyaHomeAdminModel) {
            title.text = data.nama_kategori
            jumlah.text = data.jumlah.toString()
        }

    }

    interface OnAdapterAllYadnyaHomeAdminListener {
        fun onClick(result: AllYadnyaHomeAdminModel)
    }
}
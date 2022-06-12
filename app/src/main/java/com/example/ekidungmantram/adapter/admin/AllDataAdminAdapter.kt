package com.example.ekidungmantram.adapter.admin

import android.view.LayoutInflater
import android.view.ScrollCaptureCallback
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.model.AllMantramModel
import com.example.ekidungmantram.model.adminmodel.AllDataAdminModel
import com.example.ekidungmantram.model.adminmodel.AllMantramAdminModel

class AllDataAdminAdapter(private var results: ArrayList<AllDataAdminModel>, val listener: OnAdapterAllDataAdminListener)
    : RecyclerView.Adapter<AllDataAdminAdapter.ViewHolder>() {
    private var onclickName: ((AllDataAdminModel)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView = layoutInflater.inflate(R.layout.layout_all_admin, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.bindItem(result)
        holder.itemView.setOnClickListener{
            listener.onClick(result)
        }
        holder.name.setOnClickListener {
            onclickName?.invoke(result)
        }
    }

    override fun getItemCount(): Int = results.size

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var name : TextView = view.findViewById(R.id.admin_name)
        private var email : TextView = view.findViewById(R.id.admin_email)
        fun bindItem(data: AllDataAdminModel) {
            if(data.name.length > 20){
                name.textSize = 15F
            }
            name.text = data.name
            email.text = data.email
        }

    }

    fun setOnClickName(callback: ((AllDataAdminModel)->Unit)){
        this.onclickName = callback
    }

    interface OnAdapterAllDataAdminListener {
        fun onClick(result: AllDataAdminModel)
    }
}
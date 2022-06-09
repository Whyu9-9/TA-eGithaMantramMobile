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
import com.example.ekidungmantram.model.adminmodel.AllLirikKidungAdminModel
import com.example.ekidungmantram.model.adminmodel.AllMantramAdminModel

class AllDataLirikKidungAdminAdapter(private var results: ArrayList<AllLirikKidungAdminModel> )
    : RecyclerView.Adapter<AllDataLirikKidungAdminAdapter.ViewHolder>() {
    private var onclickEdit: ((AllLirikKidungAdminModel)->Unit)? = null
    private var onclickDelete: ((AllLirikKidungAdminModel)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView = layoutInflater.inflate(R.layout.layout_list_lirik_kidung, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.bindItem(result)
        holder.editl.setOnClickListener {
            onclickEdit?.invoke(result)
        }
        holder.delete.setOnClickListener {
            onclickDelete?.invoke(result)
        }
    }

    override fun getItemCount(): Int = results.size

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private var urutan : TextView = view.findViewById(R.id.urutan_list_lirik)
        private var lirik : TextView = view.findViewById(R.id.lirik_kidung)
        var editl : ImageView = view.findViewById(R.id.goToEditLirik)
        var delete : ImageView = view.findViewById(R.id.deleteLirikKidung)
        fun bindItem(data: AllLirikKidungAdminModel) {
            urutan.text = data.urutan.toString() + "."
            lirik.text = data.bait
        }

    }

    fun setOnClickEdit(callback: ((AllLirikKidungAdminModel)->Unit)){
        this.onclickEdit = callback
    }

    fun setOnClickDelete(callback: ((AllLirikKidungAdminModel)->Unit)){
        this.onclickDelete = callback
    }
}
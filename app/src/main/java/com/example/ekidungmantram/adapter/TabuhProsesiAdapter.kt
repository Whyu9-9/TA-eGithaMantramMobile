package com.example.ekidungmantram.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.model.TabuhProsesiModel

class TabuhProsesiAdapter(val results:ArrayList<TabuhProsesiModel.Data>, val listener: OnAdapterTabuhProsesiListener)
    : RecyclerView.Adapter<TabuhProsesiAdapter.ViewHolder>(){

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title_tabuh_prosesi)
        val image: ImageView = view.findViewById(R.id.prosesiTabuhDetail)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.layout_tabuh_detail_prosesi, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.title.setText(result.nama_post)
        Glide.with(holder.view).load(Constant.IMAGE_URL +result.gambar).into(holder.image)
        holder.view.setOnClickListener{
            listener.onClick(result)
        }
    }

    override fun getItemCount() = results.size

    fun setData (data: List<TabuhProsesiModel.Data>) {
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }

    interface OnAdapterTabuhProsesiListener {
        fun onClick(result: TabuhProsesiModel.Data)
    }
}
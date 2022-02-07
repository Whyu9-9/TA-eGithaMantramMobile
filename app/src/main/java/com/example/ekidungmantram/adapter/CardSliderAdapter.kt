package com.example.ekidungmantram.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ekidungmantram.data.CardSliderData
import com.example.ekidungmantram.databinding.ItemSlideBinding
import com.example.ekidungmantram.user.YadnyaActivity


class CardSliderAdapter(private val items: List<CardSliderData>) :RecyclerView.Adapter<CardSliderAdapter.CardViewHolder>() {
    inner class CardViewHolder(itemView: ItemSlideBinding) : RecyclerView.ViewHolder(itemView.root){
        private val binding = itemView
        fun bind(data: CardSliderData){
            with(binding){
                namaYadnya.setText(data.yadnyaName)
                cardYadnya.setOnClickListener {
                    val intent = Intent(itemView.context, YadnyaActivity::class.java)
                    val bundle = Bundle()
                    bundle.putString("nama_yadnya", data.yadnyaName)
                    intent.putExtras(bundle)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder(ItemSlideBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(
            items[position]
        )
    }

    override fun getItemCount(): Int = items.size
}
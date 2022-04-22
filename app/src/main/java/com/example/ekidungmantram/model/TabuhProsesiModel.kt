package com.example.ekidungmantram.model

data class TabuhProsesiModel(val data: ArrayList<Data>){
    data class Data(
        val id_post: Int,
        val nama_post: String,
        val gambar: String,
    )
}
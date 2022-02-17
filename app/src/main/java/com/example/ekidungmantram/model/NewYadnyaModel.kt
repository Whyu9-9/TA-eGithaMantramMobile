package com.example.ekidungmantram.model

data class NewYadnyaModel(val data: ArrayList<Data>){
    data class Data(
        val id_post: Int,
        val id_kategori: Int,
        val kategori: String,
        val nama_post: String,
        val gambar: String
    )
}

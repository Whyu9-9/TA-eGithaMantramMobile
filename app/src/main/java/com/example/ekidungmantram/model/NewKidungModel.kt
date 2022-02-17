package com.example.ekidungmantram.model

data class NewKidungModel(val data: ArrayList<DataK>) {
    data class DataK(
        val id_post: Int,
        val id_kategori: Int,
        val id_tag: Int,
        val kategori: String,
        val nama_post: String
    )
}
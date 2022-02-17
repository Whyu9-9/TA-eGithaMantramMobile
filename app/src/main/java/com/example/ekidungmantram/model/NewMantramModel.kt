package com.example.ekidungmantram.model

data class NewMantramModel(val data: ArrayList<DataM>) {
    data class DataM(
        val id_post: Int,
        val id_kategori: Int,
        val id_tag: Int,
        val kategori: String,
        val nama_post: String
    )
}
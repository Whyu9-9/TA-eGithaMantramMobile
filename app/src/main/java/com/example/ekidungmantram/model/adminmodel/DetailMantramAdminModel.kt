package com.example.ekidungmantram.model.adminmodel

data class DetailMantramAdminModel(
    val id_post: Int,
    val nama_post: String,
    val jenis_mantram: String,
    val bait_mantra: String,
    val video: String,
    val gambar: String,
    val deskripsi: String,
    val nama_kategori: String,
    val arti_mantra: String,
)

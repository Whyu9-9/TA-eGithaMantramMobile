package com.example.ekidungmantram.model.adminmodel

data class AllMantramAdminModel (
    val id_post: Int,
    val id_kategori: Int,
    val kategori: String,
    val nama_post: String,
    val gambar: String,
    val is_approved: Int
    )

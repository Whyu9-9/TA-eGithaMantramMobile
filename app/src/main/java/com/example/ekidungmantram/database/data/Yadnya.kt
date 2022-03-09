package com.example.ekidungmantram.database.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "yadnyabookmarked", indices = [Index(value = ["id_yadnya"], unique = true)])
data class Yadnya (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val id_yadnya: Int,
    val id_kategori: Int,
    val kategori: String,
    val nama_post: String,
    val gambar: String
){
    constructor() : this(0, 0 , 0, "", "", "")
}

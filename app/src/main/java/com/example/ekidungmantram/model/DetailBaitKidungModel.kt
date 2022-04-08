package com.example.ekidungmantram.model

data class DetailBaitKidungModel(val data: ArrayList<Data>){
    data class Data(
        val urutan: String,
        val bait: String,
    )
}
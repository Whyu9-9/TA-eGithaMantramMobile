package com.example.ekidungmantram.model

data class DetailYadnyaModel(val data: ArrayList<Data>){
    data class Data(
        val id_status: Int,
        val nama_status: String,
        val det_proses: ArrayList<Detail>
    ){
        data class Detail(
            val id_status: Int,
            val nama_post: String,
            val id_post: Int,
            val id_parent_post: Int,
            val id_tag: Int
        )
    }
}

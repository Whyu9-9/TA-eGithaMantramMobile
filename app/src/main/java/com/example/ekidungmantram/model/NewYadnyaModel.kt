package com.example.ekidungmantram.model

//"id_post": 222,
//"kategorinya": "Bhuta Yadnya",
//"nama_post": "Caru Kesanga",
//"gambar": "1605538411_tawur.jpg"
data class NewYadnyaModel(val data: ArrayList<Data>){
    data class Data(
        val id_post: Int,
        val kategori: String,
        val nama_post: String,
        val gambar: String
    )
}

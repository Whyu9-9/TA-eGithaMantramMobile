package com.example.ekidungmantram.api

import com.example.ekidungmantram.model.HomeModel
import com.example.ekidungmantram.model.NewYadnyaModel
import retrofit2.Call
import retrofit2.http.GET

interface ApiEndpoint {
    @GET("listyadnya")
    fun getYadnyaMasterList(): Call<List<HomeModel>>

    @GET("listyadnyaterbaru")
    fun getYadnyaNewList(): Call<NewYadnyaModel>
}
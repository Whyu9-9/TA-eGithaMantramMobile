package com.example.ekidungmantram.api

import com.example.ekidungmantram.model.HomeModel
import retrofit2.Call
import retrofit2.http.GET

interface ApiEndpoint {

    @GET("listyadnya")
    fun getYadnyaMasterList(): Call<List<HomeModel>>
}
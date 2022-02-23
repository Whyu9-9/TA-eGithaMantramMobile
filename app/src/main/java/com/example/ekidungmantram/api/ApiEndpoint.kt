package com.example.ekidungmantram.api

import com.example.ekidungmantram.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiEndpoint {
    @GET("listyadnya")
    fun getYadnyaMasterList(): Call<List<HomeModel>>

    @GET("listyadnyaterbaru")
    fun getYadnyaNewList(): Call<NewYadnyaModel>

    @GET("listkidungterbaru")
    fun getKidungNewList(): Call<NewKidungModel>

    @GET("listmantramterbaru")
    fun getMantramNewList(): Call<NewMantramModel>

    @GET("listallyadnya")
    fun getYadnyaAllList(): Call<ArrayList<AllYadnyaModel>>

    @GET("detailyadnya/{id_post}")
    fun getDetailYadnya(@Path("id_post") id: Int) : Call<DetailYadnyaModel>
}
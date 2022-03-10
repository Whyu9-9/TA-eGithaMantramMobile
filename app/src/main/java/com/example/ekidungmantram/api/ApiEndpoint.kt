package com.example.ekidungmantram.api

import com.example.ekidungmantram.model.*
import retrofit2.Call
import retrofit2.http.*

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

    @GET("detailawal/{id_post}")
    fun getDetailAwal(@Path("id_post") id: Int) : Call<ProsesiAwalModel>

    @GET("detailpuncak/{id_post}")
    fun getDetailPuncak(@Path("id_post") id: Int) : Call<ProsesiPuncakModel>

    @GET("detailakhir/{id_post}")
    fun getDetailAkhir(@Path("id_post") id: Int) : Call<ProsesiAkhirModel>

    @GET("detailgamelan/{id_post}")
    fun getDetailGamelanYadnya(@Path("id_post") id: Int) : Call<GamelanYadnyaModel>

    @GET("detailtari/{id_post}")
    fun getDetailTariYadnya(@Path("id_post") id: Int) : Call<TariYadnyaModel>

    @GET("detailkidung/{id_post}")
    fun getDetailKidungYadnya(@Path("id_post") id: Int) : Call<KidungYadnyaModel>

    @FormUrlEncoded
    @POST("login")
    fun loginAdmin (
        @Field("username") username:String,
        @Field("password") password:String
    ):Call<AdminModel>
}
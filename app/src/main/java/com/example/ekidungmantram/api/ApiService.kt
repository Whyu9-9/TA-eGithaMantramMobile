package com.example.ekidungmantram.api

import com.example.ekidungmantram.Constant.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ApiService {
    val endpoint: ApiEndpoint
    get() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiEndpoint::class.java)
    }
}
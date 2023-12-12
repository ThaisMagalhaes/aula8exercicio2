package com.example.aula8exercicio2

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UserService {
    @GET("/api/")
    fun getUsers(@Query("results") count: Int): Call<ApiResponse>
}

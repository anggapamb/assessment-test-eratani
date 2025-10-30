package com.anggapamb.assessmenttesteratani.data.source.remote

import com.anggapamb.assessmenttesteratani.data.model.UserDto
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("public/v2/users")
    suspend fun getUsers(): List<UserDto>

    @FormUrlEncoded
    @POST("public/v2/users")
    suspend fun createUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("gender") gender: String,
        @Field("status") status: String
    ): UserDto
}

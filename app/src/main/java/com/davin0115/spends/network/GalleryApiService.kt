package com.davin0115.spends.network

import com.davin0115.spends.model.Gallery
import com.davin0115.spends.model.OpStatus
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://spends-api.bagasaldianata.my.id/api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface GalleryApiService {
    @GET("spends")
    suspend fun getGallery(
            @Header("Authorization") userId: String
    ): List<Gallery>

    @Multipart
    @POST("spends")
    suspend fun postGallery(
        @Header("Authorization") userId: String,
        @Part("judul") judul: RequestBody,
        @Part("keterangan") keterangan: RequestBody,
        @Part image: MultipartBody.Part
    ): OpStatus

    @Multipart
    @POST("spends")
    suspend fun updateGallery(
        @Header("Authorization") email: String,
        @Query("id") id: String,
        @Part("_method") method: RequestBody,
        @Part("judul") judul: RequestBody,
        @Part("keterangan") keterangan: RequestBody,
        @Part image: MultipartBody.Part?
    ): Gallery

    @DELETE("spends")
    suspend fun deleteGallery(
        @Header("Authorization") userId: String,
        @Query("id") id: String // Gunakan Query untuk ID saat delete
    ): OpStatus
}

object GalleryApi {
    val service: GalleryApiService by lazy {
        retrofit.create(GalleryApiService::class.java)
    }

    fun getGalleryUrl(gambar: String): String {
        return "${BASE_URL}image?id=$gambar"
    }
}

enum class ApiStatus { LOADING, SUCCESS, FAILED }
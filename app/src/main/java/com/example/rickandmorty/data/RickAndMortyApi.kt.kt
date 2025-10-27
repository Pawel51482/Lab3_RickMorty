package com.example.rickandmorty.data


import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface RickAndMortyService {
    @GET("character")
    suspend fun getCharacters(@Query("page") page: Int? = null): CharacterResponse


    @GET("character/{id}")
    suspend fun getCharacterById(@Path("id") id: Int): Character
}


object ApiClient {
    private const val BASE_URL = "https://rickandmortyapi.com/api/"

    // Twój SPKI hash (wklejony z openssl)
    private const val SPKI_HASH = "sha256/j5ykQyFYmMqTg6SQtogIS35T9z/exVTI0HRAyG7RmeM="

    private val certificatePinner = CertificatePinner.Builder()
        // użyj dokładnej nazwy hosta jak w URL (bez protokołu, bez slash)
        .add("rickandmortyapi.com", SPKI_HASH)
        .build()

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }
    private val http = OkHttpClient.Builder()
        .certificatePinner(certificatePinner)
        .addInterceptor(logging)
        .build()


    val service: RickAndMortyService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(http)
            .build()
            .create(RickAndMortyService::class.java)
    }
}
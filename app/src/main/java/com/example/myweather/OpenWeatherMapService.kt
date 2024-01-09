package com.example.myweather

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherMapService {
    @GET("weather")
    fun getCurrentWeatherData(@Query("q") location: String, @Query("appid") apiKey: String): Call<WeatherData>
}

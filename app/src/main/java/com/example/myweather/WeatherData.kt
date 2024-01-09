package com.example.myweather

data class WeatherData(
    var name: String?,
    var main: MainData?,
    var weather: List<WeatherInfo>?
)

data class MainData(
    var temp: Double?
)

data class WeatherInfo(
    var description: String?,
    var icon: String?
)






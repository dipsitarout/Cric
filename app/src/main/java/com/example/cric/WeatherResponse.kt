package com.example.cric

data class WeatherResponse(
    val request: Request,
    val location: Location,
    val current: Current
)

data class Request(
    val type: String,
    val query: String,
    val language: String,
    val unit: String
)

data class Location(
    val name: String,
    val country: String,
    val region: String,
    val localtime: String
)

data class Current(
    val observation_time: String,
    val temperature: Int,
    val weather_icons: List<String>,
    val weather_descriptions: List<String>,
    val wind_speed: Int,
    val humidity: Int,
    val cloudcover: Int,
    val uv_index: Int,
    val feelslike: Int,
    val visibility: Int,
    val is_day: String
)

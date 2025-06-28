package com.example.cric

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {


    private val api: WeatherApiService = WeatherApiService.create()

    private val _weatherState = MutableStateFlow<WeatherResponse?>(null)
    val weatherState: StateFlow<WeatherResponse?> = _weatherState

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState

    fun fetchWeather(city: String) {
        if (city.isBlank()) {
            _errorState.value = "Please enter a city name"
            _weatherState.value = null
            return
        }
        viewModelScope.launch {
            try {
                _errorState.value = null
                val response = api.getCurrentWeather(
                    accessKey = "2f2cbbb90ac844c0ebd80a8bd2cc596a", // Your real key
                    query = city
                )
                _weatherState.value = response
            } catch (e: Exception) {
                _errorState.value = "Failed to fetch weather: ${e.message}"
                _weatherState.value = null
            }
        }
    }
}

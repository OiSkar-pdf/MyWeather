package com.example.myweather

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.EditText
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var cityNameTextView: TextView
    private lateinit var temperatureTextView: TextView
    private lateinit var weatherDescriptionTextView: TextView
    private lateinit var weatherIconImageView: ImageView
    private lateinit var cityInputEditText: EditText
    private lateinit var updateWeatherButton: Button

    private lateinit var openWeatherMapService: OpenWeatherMapService
    private val apiKey = "e54099862c10667e067e77952984c554"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cityNameTextView = findViewById(R.id.city_name_text_view)
        temperatureTextView = findViewById(R.id.temperature_text_view)
        weatherDescriptionTextView = findViewById(R.id.weather_description_text_view)
        weatherIconImageView = findViewById(R.id.weather_icon_image_view)
        cityInputEditText = findViewById(R.id.city_input_edit_text)
        updateWeatherButton = findViewById(R.id.update_weather_button)

        openWeatherMapService = ApiClient.getInstance().create(OpenWeatherMapService::class.java)

        updateWeatherButton.setOnClickListener {
            val inputCity = cityInputEditText.text.toString()
            if (inputCity.isNotEmpty()) {
                loadWeatherData(inputCity)
            } else {
                Toast.makeText(this, "Please enter a city", Toast.LENGTH_SHORT).show()
            }
        }


        loadWeatherData("Scotch Plains")
    }

    private fun loadWeatherData(location: String) {
        openWeatherMapService.getCurrentWeatherData(location, apiKey).enqueue(object : Callback<WeatherData> {
            override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                if (response.isSuccessful) {
                    val weatherData: WeatherData? = response.body()
                    weatherData?.let {
                        updateUI(it)
                        println("Received data: $it")
                    }
                } else {
                    showErrorToast()
                }
            }


            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                showErrorToast()
            }
        })
    }

    private fun updateUI(weatherData: WeatherData) {
        println("Debug message: ${weatherData.main?.temp}")
        println("City: ${weatherData.name}")
        println("Weather Description: ${weatherData.weather?.get(0)?.description}")

        // Convert temperature from Kelvin to Fahrenheit
        val temperatureInKelvin = weatherData.main?.temp ?: 0.0
        val temperatureInFahrenheit = (temperatureInKelvin - 273.15) * 9 / 5 + 32

        cityNameTextView.text = weatherData.name ?: "N/A"
        temperatureTextView.text = "${String.format("%.2f", temperatureInFahrenheit)}°F"
        weatherDescriptionTextView.text = weatherData.weather?.get(0)?.description ?: "N/A"

        val iconUrl = "https://openweathermap.org/img/wn/${weatherData.weather?.get(0)?.icon}.png"
        Picasso.get().load(iconUrl).into(weatherIconImageView)
    }




    private fun showErrorToast() {
        Toast.makeText(this@MainActivity, "Failed to load weather data", Toast.LENGTH_SHORT).show()
    }
}

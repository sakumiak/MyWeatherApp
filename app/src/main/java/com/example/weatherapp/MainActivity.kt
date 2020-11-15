package com.example.weatherapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.coroutines.*
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cityButton : Button = findViewById(R.id.cityButton)
        val cityText : EditText = findViewById(R.id.cityName)
        cityButton.setOnClickListener {
            weatherRun(cityText.text.toString())
        }
        weatherRun("Wroclaw")
    }

    //Ta metoda tworzy osobny wątek, który pobiera dane z OpenWeather API
    fun weatherRun(city:String) {
        Thread(Runnable {
            val jsonStr = WeatherConnection().getJSONString(city)
            Log.d("HTTP_JSON",jsonStr)
            val json = JSONObject(jsonStr)
            val main = JSONObject(json.get("main").toString())
            Log.d("HTTP_JSON_main",main.toString())
            val temp = main.get("temp").toString()
            Log.d("HTTP_JSON_temp",temp)
            val city = json.get("name").toString()
            Log.d("HTTP_JSON_city",city)
            val temp_min = main.get("temp_min").toString()
            val temp_max = main.get("temp_max").toString()

            //UI może nadpisywać tylko specjalny wątek dla UI. Właśnie to tutaj robimy
            runOnUiThread {
                findViewById<TextView>(R.id.tempText).text = temp + "°C"
                findViewById<TextView>(R.id.cityText).text = city
                findViewById<TextView>(R.id.tempMinText).text = temp_min + "°C"
                findViewById<TextView>(R.id.tempMaxText).text = temp_max + "°C"

            }
        }).start()
    }

}
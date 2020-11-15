package com.example.weatherapp

import android.util.Log
import org.json.JSONException
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class WeatherConnection {
    //Zmienna przechowujący ApiKey do OpenWeather API
    private val APIKEY = "a630aa78ba98c7c8962008bca141b8d3"

    //Pobieramy dane w formie ByteArray z połączenia typu Https
    fun getUrlBytes(urlSpec: String):ByteArray{
        val url = URL(urlSpec)
        val connection = url.openConnection() as HttpsURLConnection

        try {
            val out = ByteArrayOutputStream()
            val input = connection.inputStream

            if(connection.responseCode != HttpsURLConnection.HTTP_OK)
                throw IOException(connection.responseMessage)
            var bytesRead: Int
            val buffer = ByteArray(1024)

            do{
                bytesRead = input.read(buffer)
                out.write(buffer, 0, bytesRead)

            }while (input.read(buffer) > 0)
            out.close()
            return out.toByteArray()
        }catch (e : IOException) {
            Log.e("ERROR_HTTP_CONNECTION", e.message.toString())
            return ByteArray(0)
        }
        finally {
            connection.disconnect()
        }
    }

    //Ta metoda z pobranego BytesArray zwróci nam String
    fun getUrlString(urlSpec:String):String {
        return String(getUrlBytes(urlSpec))
    }

    //Tworzymy url i zwracamy JSON w formie Stringa
    fun getJSONString(city:String):String {
        var jsonString = "Something went wrong"
        try {
            val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&units=metric&appid=$APIKEY"
            jsonString = getUrlString(url)

        }catch (je:JSONException){

        }
        return jsonString
    }
}
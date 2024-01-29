package com.example.mobile_app

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.os.FileUtils.ProgressListener
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class WeatherPage : AppCompatActivity() {
    val CITY: String = "Hong Kong"
    val API: String = "11a3b0f620605e835fb7fd6a0b6ec6da"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weatherpage);

        checkWeather().execute()

        val buttonClick = findViewById<Button>(R.id.btn_saveData)
        buttonClick.setOnClickListener {
            goSaveDataPage()
        }
    }

    inner class checkWeather() : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            //findViewById<ProgressBar>(R.id.load).visibility = View.VISIBLE
            findViewById<RelativeLayout>(R.id.home_Container).visibility = View.GONE
            findViewById<TextView>(R.id.error).visibility = View.GONE
        }

        override fun doInBackground(vararg params: String?): String? {
            var response:String?
            try{
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API").readText(
                    Charsets.UTF_8
                )
            }catch (e: Exception){
                response = null
                Log.e(TAG, "Exception: "+Log.getStackTraceString(e));
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            try {
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val weatherDesc = weather.getString("description")
                val updated:Long = jsonObj.getLong("dt")
                val updatedAt = "Updated at: "+ SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(
                    Date(updated*1000)
                )
                val temp = main.getString("temp")+"°C"
                val tempMin = "Min Temp: "+main.getString("temp_min")+"°C"
                val tempMax = "Max Temp: "+main.getString("temp_max")+"°C"
                val address = jsonObj.getString("name")+", "+sys.getString("country")

                findViewById<TextView>(R.id.address).text = address
                findViewById<TextView>(R.id.updated).text = updatedAt
                findViewById<TextView>(R.id.status).text = weatherDesc
                findViewById<TextView>(R.id.temp).text = temp
                findViewById<TextView>(R.id.minTemp).text = tempMin
                findViewById<TextView>(R.id.maxTemp).text = tempMax

                //findViewById<ProgressBar>(R.id.load).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.home_Container).visibility = View.VISIBLE
            }
            catch (e: Exception) {
                //findViewById<ProgressBar>(R.id.load).visibility = View.GONE
                findViewById<TextView>(R.id.error).visibility = View.VISIBLE
                Log.e(TAG, "Exception: "+Log.getStackTraceString(e));
            }
        }
    }

    fun goSaveDataPage() {
        val intent = Intent(this, SaveWeatherDataPage::class.java)
        startActivity(intent)
    }
}
package com.example.mobile_app

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.os.FileUtils.ProgressListener
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class SaveWeatherDataPage : AppCompatActivity() {
    companion object {
        const val FILE_NAME = "contacts.txt"
    }
    private var editTextLocation: EditText? = null
    private var editTextWeather: EditText? = null
    private var editTextTemp: EditText? = null
    private var file: File? = null
    private var outputStream: FileOutputStream? = null
    private var inputStream: FileInputStream? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weatherdatapage);
        editTextLocation = findViewById<View>(R.id.location_Text) as EditText
        editTextWeather = findViewById<View>(R.id.weather_Desc_Text) as EditText
        editTextTemp = findViewById<View>(R.id.temp_Text) as EditText
        file = File(this.filesDir, FILE_NAME)

        val buttonClick = findViewById<Button>(R.id.btn_back)
        buttonClick.setOnClickListener {
            goWeatherPage()
        }
    }

    fun save(v: View?) {
        val data = editTextLocation!!.text.toString() + "|" + editTextWeather!!.text.toString() + "|" + editTextTemp!!.text.toString()
        try {
            outputStream = FileOutputStream(file)
            outputStream!!.write(data.toByteArray())
            outputStream!!.close()
            Toast.makeText(this, "data saved", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun load(v: View?) {
        val length = file!!.length().toInt()
        val bytes = ByteArray(length)
        try {
            inputStream = FileInputStream(file)
            inputStream!!.read(bytes)
            inputStream!!.close()
            val data = String(bytes)
            editTextLocation!!.setText(data.split("|").toTypedArray()[0])
            editTextWeather!!.setText(data.split("|").toTypedArray()[1])
            editTextTemp!!.setText(data.split("|").toTypedArray()[2])
            Toast.makeText(baseContext, "data loaded", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun goWeatherPage() {
        val intent = Intent(this, WeatherPage::class.java)
        startActivity(intent)
    }
}
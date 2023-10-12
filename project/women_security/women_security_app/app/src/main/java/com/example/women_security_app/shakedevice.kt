package com.example.women_security_app

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.IOException
import java.util.*

class shakedevice : AppCompatActivity() {

    var fusedLocationProviderClient: FusedLocationProviderClient? = null

    var lat: String? = null
    var log: String? = null
    var address: String? =null
    var counter=1

    private var sensorManager: SensorManager? = null
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shakedevice)

        val btn = findViewById<Button>(R.id.btn)
        btn.setOnClickListener {
            val intent = Intent(applicationContext, saferoute::class.java)
            startActivity(intent)
        }
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        Objects.requireNonNull(sensorManager)!!.registerListener(sensorListener, sensorManager!!
            .getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
        acceleration = 10f
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)


    }

    private val sensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            lastAcceleration = currentAcceleration
            currentAcceleration = Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta: Float = currentAcceleration - lastAcceleration
            acceleration = acceleration * 0.9f + delta
            if (acceleration > 12) {
                getlocation()
            }
        }
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}


    }


    private fun getlocation() {

        //Toast.makeText(applicationContext,"location", Toast.LENGTH_LONG).show()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            return
        }

        fusedLocationProviderClient!!.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val geocoder = Geocoder(this@shakedevice, Locale.getDefault())
                try {
                    val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)


                    lat = addresses?.get(0)!!.latitude.toString()
                    log = addresses[0]!!.longitude.toString()
                    address = addresses.get(0).getAddressLine(0)
//                    Toast.makeText(applicationContext,address.toString(),Toast.LENGTH_LONG).show()

                    val sb = StringBuffer()
                    sb.append("Current location latitude").append(lat)
                    sb.append(System.getProperty("line.separator"))
                    sb.append("current location longitutde").append(log)
                    sb.append(System.getProperty("line.separator"))
                    sb.append("Current Address").append(address)
                    val msg = sb.toString()



//                    Toast.makeText(applicationContext,lat.toString(),Toast.LENGTH_LONG).show()
//                    Toast.makeText(applicationContext,log.toString(),Toast.LENGTH_LONG).show()
//                    Toast.makeText(applicationContext,addresses.get(0).getAddressLine(0),Toast.LENGTH_LONG).show()

//                    val s = send(applicationContext, "madhurioza2@gmail.com", "Call Log ", msg)
//                       s.execute()
                    val MY_PREFS_NAME = "MyPrefsFile"
                    val prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE)
                    val mail = prefs.getString("mail", "No name defined")
                    val number = prefs.getString("number", "")

//                    Toast.makeText(applicationContext,mail.toString(),Toast.LENGTH_LONG).show()
                    val se = send(applicationContext,mail.toString(),"Current Location",msg)
                    se.execute()



                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }



    }

    override fun onResume() {
        sensorManager?.registerListener(sensorListener, sensorManager!!.getDefaultSensor(
            Sensor .TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL
        )
        super.onResume()
    }

    override fun onPause() {
        sensorManager!!.unregisterListener(sensorListener)
        super.onPause()
    }


}

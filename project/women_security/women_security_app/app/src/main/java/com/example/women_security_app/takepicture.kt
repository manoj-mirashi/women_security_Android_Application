package com.example.women_security_app

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

class takepicture : AppCompatActivity() {

    val MY_PREFS_NAME = "MyPrefsFile"
    val PERMISSION_CODE :Int=1000
    var imageuri: Uri?=null
    var IMAGE_CAPTURE_CODE:Int=1001
    var img: ImageView?=null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_takepicture)

        val btn = findViewById<Button>(R.id.button2)
        val btnserach = findViewById<Button>(R.id.btn)


        img = findViewById<ImageView>(R.id.imageView)

        btn.setOnClickListener {
            val intent = Intent(applicationContext, autocapture::class.java)
            startActivity(intent)
        }

        btnserach.setOnClickListener {
            val intent = Intent(applicationContext, dashboard::class.java)
            startActivity(intent)
        }

    }

}
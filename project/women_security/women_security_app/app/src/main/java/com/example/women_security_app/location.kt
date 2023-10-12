package com.example.women_security_app

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.Camera
import android.hardware.Camera.PictureCallback
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.util.Log
import android.view.SurfaceView
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.sun.mail.imap.Utility
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

import java.util.*
import kotlin.Exception

class location : AppCompatActivity() {

    private var camera: Camera? = null
    private val cameraId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        try {
            captureImage()
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    private fun captureImage() {
        if (!packageManager
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
        ) {
            Toast.makeText(this, "No camera on this device", Toast.LENGTH_LONG)
                .show()
        } else {

//            cameraId = Utility.getFrontFacingCamera();
            if (cameraId < 0) {
                Toast.makeText(
                    this, "No front facing camera found.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
//                safeCameraOpen(cameraId)
            }
        }
        val view = SurfaceView(this)
        try {
            camera!!.setPreviewDisplay(view.holder)
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            Log.e("captureImage","setPreviewDisplay");
            Toast.makeText(this, "set preview display", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
        camera!!.startPreview()
        val params = camera!!.parameters
        params.jpegQuality = 100
        camera!!.parameters = params
        camera!!.takePicture(null, null, mCall)


    }

    var mCall = Camera.PictureCallback { data, camera ->
        Toast.makeText(applicationContext,"PictureCallback",Toast.LENGTH_LONG).show()
        //decode the data obtained by the camera into a Bitmap
            Log.e("PictureCallback", "PictureCallback")
        println(data.toString())
            val bitmapPicture = BitmapFactory.decodeByteArray(data, 0, data.size)
//            val dis = findViewById<ImageView>(R.id.imageView)
//            dis.setImageBitmap(bitmapPicture)


            try {
//                SaveImage(bitmapPicture)
            } catch (e: Exception) {
            }
        }

    private fun SaveImage(bitmapPicture: Bitmap?) {
        Toast.makeText(this, "save image", Toast.LENGTH_LONG).show()
        val root = Environment.getExternalStorageDirectory().toString()
        Toast.makeText(applicationContext,root.toString(),Toast.LENGTH_LONG).show()
        val myDir = File("$root/ChatSecure")
        myDir.mkdirs()
        val generator = Random()
        var n = 10000
        n = generator.nextInt(n)
        val fname = "Image-$n.jpg"
        val file = File(myDir, fname)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            bitmapPicture?.compress(Bitmap.CompressFormat.JPEG, 90, out)
            val mSharedPreference = PreferenceManager.getDefaultSharedPreferences(
                baseContext
            )
            val value = mSharedPreference.getString("name", "DEFAULT")
            val s = capturesend(applicationContext, value.toString(), "Call Log ", file.absolutePath)
            s.execute()

            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.e("SaveImage", "Path: " + file.absolutePath)

    }

    override fun onPause() {
        releaseCamera()
        super.onPause()
    }

    override fun onDestroy() {
        releaseCamera()
        super.onDestroy()
    }

    private fun safeCameraOpen(id: Int): Boolean {
        var qOpened = false
        try {
            Log.e("safeCameraOpen","safeCameraOpen");
            Toast.makeText(applicationContext, "save camera open", Toast.LENGTH_LONG).show()
            releaseCamera()
            camera = Camera.open(id)
            qOpened = camera != null
        } catch (e: Exception) {
            Log.e(getString(R.string.app_name), "failed to open Camera")
            e.printStackTrace()
        }
        return qOpened
    }
    //
    private fun releaseCamera() {
        if (camera != null) {
            camera!!.stopPreview()
            camera!!.release()
            camera = null
        }
    }
}




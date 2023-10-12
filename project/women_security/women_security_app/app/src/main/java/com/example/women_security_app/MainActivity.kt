package com.example.women_security_app

import android.app.Activity
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.speech.RecognizerIntent
import android.telephony.SmsManager
import android.view.KeyEvent
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import java.util.*


class MainActivity : AppCompatActivity() {

    var MY_PREFS_NAME = "MyPrefsFile"

    private val REQUEST_CODE_SPEECH_INPUT = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        val i = Intent(applicationContext,takepicture::class.java)
        val myi = Intent(applicationContext,shakedevice::class.java)

        when(keyCode)
        {
            KeyEvent.KEYCODE_VOLUME_UP ->  record()
            KeyEvent.KEYCODE_VOLUME_DOWN -> startActivity(i)
            KeyEvent.KEYCODE_BACK -> startActivity(myi)
        }
        return true
    }

    private fun record() {

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        );
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            Locale.getDefault()
        );
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (e: Exception) {
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int,
        @Nullable data: Intent?
    ) {
//        val txt = findViewById<TextView>(R.id.tv_speech_to_text)
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
//                txt.setText(Objects.requireNonNull(result)?.get(0))
                val msg:String = Objects.requireNonNull(result)?.get(0).toString()
                //Toast.makeText(applicationContext,Objects.requireNonNull(result)?.get(0).toString(),Toast.LENGTH_LONG).show()
                val prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE)
               val mail = prefs.getString("mail", "No name defined")
                val number = prefs.getString("number", "")
                val mymsg = prefs.getString("msg", "")

//                Toast.makeText(applicationContext,value.toString(),Toast.LENGTH_LONG).show()
                val se = send(applicationContext,mail.toString(),"audio",msg)
                se.execute()
                System.out.println(number)
                val smsManager = SmsManager.getDefault() as SmsManager
                smsManager.sendTextMessage(number,null,msg,null,null)

            }
        }
    }
}




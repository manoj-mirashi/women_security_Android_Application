package com.example.women_security_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class register : AppCompatActivity() {

    lateinit var auth : FirebaseAuth
    val MY_PREFS_NAME = "MyPrefsFile"

    var databaseReference: DatabaseReference? = null
    var database: FirebaseDatabase? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val name = findViewById<EditText>(R.id.username)
        val mail = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val number = findViewById<EditText>(R.id.number)
        val btn = findViewById<Button>(R.id.btn_register)
        auth = FirebaseAuth.getInstance()

        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("User")


        btn.setOnClickListener {
            if(name.text.isEmpty())
                {
                    name.setError("Enter Username")
                    return@setOnClickListener
                }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(mail.text.toString()).matches())
                {
                    mail.setError("Enter Email id")
                    return@setOnClickListener
                }else if(password.text.isEmpty())
                {
                    password.setError("Enter Password ")
                    return@setOnClickListener
                }else if (number.length() > 1 && number.length() <1)
                {
                    number.setError("Enter Contact Number")
                    return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(mail.text.toString(), password.text.toString())
                .addOnCompleteListener {
                    if(it.isSuccessful)
                    {
                        val currentuser = auth.currentUser
                        val currentUserdb = databaseReference?.child((currentuser?.uid!!))
                        currentUserdb?.child("name")?.setValue(name.text.toString())
                        currentUserdb?.child("number")?.setValue(number.text.toString())
                        Toast.makeText(applicationContext,"success", Toast.LENGTH_LONG).show()
                    }
                    else
                    {
                        Toast.makeText(applicationContext,"failed", Toast.LENGTH_LONG).show()
                    }
                }

            val editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit()
            editor.putString("mail", mail.text.toString())

            editor.putString("number", number.text.toString())

            editor.apply()



            editor.apply()
            editor.commit()

        }


    }
}
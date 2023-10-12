package com.example.women_security_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class login : AppCompatActivity() {

    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val usernamelogin = findViewById<EditText>(R.id.userloginname)
        val userpasswordlogin = findViewById<EditText>(R.id.userloginpassword)
        val btnlogin = findViewById<Button>(R.id.btn_login)
        auth = FirebaseAuth.getInstance()

        //goto register
        val btnre = findViewById<Button>(R.id.btn_register)
        btnre.setOnClickListener {
            val intent = Intent(applicationContext,register::class.java)
            startActivity(intent)


            //skip code





        }


        //login
    btnlogin.setOnClickListener {

        if(usernamelogin.text.isEmpty() ) {
            usernamelogin.setError("Enter Email Id")
            return@setOnClickListener
        }
        else if (userpasswordlogin.text.isEmpty()){
            userpasswordlogin.setError("Enter Password")
            return@setOnClickListener
        }
        auth.signInWithEmailAndPassword(usernamelogin.text.toString(),userpasswordlogin.text.toString())
            .addOnCompleteListener {
                if(it.isSuccessful)
                {
                    Toast.makeText(applicationContext,"Login successfull", Toast.LENGTH_LONG).show()
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                }
                else
                {
                    Toast.makeText(applicationContext,"Login Failed", Toast.LENGTH_LONG).show()
                }
            }


    }






    }
}
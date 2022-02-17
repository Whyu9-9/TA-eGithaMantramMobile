package com.example.ekidungmantram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.ekidungmantram.user.MainActivity
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    lateinit var back: TextView
    lateinit var submit: Button
    lateinit var username: TextInputEditText
    lateinit var password: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar!!.hide()
        back     = findViewById(R.id.back)
        submit   = findViewById(R.id.login_button)
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)

        back.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        })

        submit.setOnClickListener(View.OnClickListener {
            val inputusername = username.text.toString()
            val inputpassword = password.text.toString()
            if(inputpassword=="123" && inputusername=="wahyu"){
                Toast.makeText(this, "Login Berhasil" ,Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Login Gagal" ,Toast.LENGTH_SHORT).show()
            }
        })

    }
}
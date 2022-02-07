package com.example.ekidungmantram.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ekidungmantram.R

class YadnyaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yadnya)

        val bundle :Bundle ?=intent.extras
        if (bundle!=null){
            val message = bundle.getString("nama_yadnya") // 1

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        }
    }
}
package com.example.ekidungmantram.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ekidungmantram.R

class DetailYadnyaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_yadnya)
        supportActionBar!!.setTitle("Detail Yadnya")
        val bundle :Bundle ?=intent.extras
        if (bundle!=null){
            val message = bundle.getInt("id_yadnya")
            val message2 = bundle.getInt("id_kategori")
            val concat = message.toString() + "-" + message2.toString()
            Toast.makeText(this, concat, Toast.LENGTH_SHORT).show()

        }
    }

}
package com.example.ekidungmantram

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.ekidungmantram.admin.HomeAdminActivity
import com.example.ekidungmantram.user.MainActivity

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var handler: Handler
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        handler = Handler()
        handler.postDelayed({
            sharedPreferences = getSharedPreferences("is_logged", Context.MODE_PRIVATE)
            val check         = sharedPreferences.getString("ID_ADMIN", null)
            if(check != null){
                val intent = Intent(this, HomeAdminActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 2000)
    }
}
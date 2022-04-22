package com.example.ekidungmantram

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.ekidungmantram.admin.HomeAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.AdminModel
import com.example.ekidungmantram.user.MainActivity
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class LoginActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var back: TextView
    private lateinit var submit: Button
    private lateinit var username: TextInputEditText
    private lateinit var password: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar!!.hide()
        back     = findViewById(R.id.back)
        submit   = findViewById(R.id.login_button)
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)

        back.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        submit.setOnClickListener{
            val inputusername = username.text.toString()
            val inputpassword = password.text.toString()
            if(validateInput()){
                login(inputusername, inputpassword)
            }
        }

    }

    private fun login(inputusername: String, inputpassword: String) {
        ApiService.endpoint.loginAdmin(inputusername, inputpassword)
            .enqueue(object: Callback<AdminModel>{
                override fun onResponse(
                    call: Call<AdminModel>,
                    response: Response<AdminModel>
                ) {
                    if(!response.body()?.error!!){
                        saveData(response.body()?.id_admin, response.body()?.nama)
                    }else{
                        Toast.makeText(this@LoginActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<AdminModel>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun validateInput(): Boolean {
        if(username.text.toString().isEmpty()){
            usernameLayout.isErrorEnabled = true
            usernameLayout.error = "Email tidak boleh kosong!"
            return false
        }

        if(password.text.toString().isEmpty()){
            passwordLayout.isErrorEnabled = true
            passwordLayout.error = "Password tidak boleh kosong!"
            return false
        }

        return true
    }

    private fun saveData(idAdmin: Int?, nama: String?) {
        sharedPreferences = getSharedPreferences("is_logged", Context.MODE_PRIVATE)
        val editor        = sharedPreferences.edit()
        editor.apply{
            putString("ID_ADMIN", idAdmin?.toString())
            putString("NAMA", nama)
        }.apply()
        Toast.makeText(this, "Log In Sukses", Toast.LENGTH_SHORT).show()
        goToAdmin()
    }

    private fun goToAdmin() {
        val intent = Intent(this, HomeAdminActivity::class.java)
        startActivity(intent)
        finish()
    }
}
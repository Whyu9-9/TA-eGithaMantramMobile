package com.example.ekidungmantram.admin.adminmanager

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ekidungmantram.R
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import kotlinx.android.synthetic.main.activity_add_admin.*
import kotlinx.android.synthetic.main.activity_add_mantram_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddAdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_admin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Tambah Admin"

        submitAdmin.setOnClickListener {
            val nama = namaAdmin.text.toString()
            val email = emailAdmin.text.toString()
            val password = passwordAdmin.text.toString()
            if(validateInput()){
                postAdmin(nama, email, password)
            }
        }

        cancelSubmitAddAdmin.setOnClickListener {
            goBack()
        }
    }

    private fun postAdmin(nama: String, email: String, password: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.createDataAdmin(nama, email, password)
            .enqueue(object: Callback<CrudModel> {
                override fun onResponse(
                    call: Call<CrudModel>,
                    response: Response<CrudModel>
                ) {
                    if(response.body()?.status == 200){
                        progressDialog.dismiss()
                        Toast.makeText(this@AddAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        goBack()
                    }else{
                        progressDialog.dismiss()
                        Toast.makeText(this@AddAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(this@AddAdminActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun validateInput(): Boolean {
        if(namaAdmin.text.toString().isEmpty()){
            layoutNamaAdmin.isErrorEnabled = true
            layoutNamaAdmin.error = "Nama Admin tidak boleh kosong!"
            return false
        }

        if(emailAdmin.text.toString().isEmpty()){
            layoutEmailAdmin.isErrorEnabled = true
            layoutEmailAdmin.error = "Email Admin tidak boleh kosong!"
            return false
        }

        if (!emailAdmin.text.toString().isValidEmail()) {
            layoutEmailAdmin.isErrorEnabled = true
            layoutEmailAdmin.error = "Email Admin harus email yang valid!"
            return false
        }

        if(passwordAdmin.text.toString().length < 8){
            layoutPasswordAdmin.isErrorEnabled = true
            layoutPasswordAdmin.error = "Password Admin harus berisi minimal 8 karakter!"
            return false
        }

        if(konfirmasiPassword.text.toString() != passwordAdmin.text.toString()){
            layoutKonfirmasiPassword.isErrorEnabled = true
            layoutKonfirmasiPassword.error = "Konfirmasi Password Admin harus sama dengan Password Admin yang dimasukan!"
            return false
        }

        return true
    }

    private fun goBack() {
        val intent = Intent(this, AllAdminActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun String.isValidEmail() = isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

}
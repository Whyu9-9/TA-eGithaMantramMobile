package com.example.ekidungmantram.admin.adminmanager

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.ekidungmantram.R
import com.example.ekidungmantram.admin.mantram.DetailMantramAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailDataAdminModel
import kotlinx.android.synthetic.main.activity_add_admin.*
import kotlinx.android.synthetic.main.activity_detail_admin.*
import kotlinx.android.synthetic.main.activity_edit_admin.*
import kotlinx.android.synthetic.main.activity_edit_arti.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditAdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_admin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Edit Admin"

        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val userID = bundle.getInt("id_user")
            getDetailData(userID)

            submitEditAdmin.setOnClickListener {
                val nama = editNamaAdmin.text.toString()
                val email = editEmailAdmin.text.toString()
                val password = passwordAdminBaru.text.toString()
                if(validateInput()){
                    editAdmin(userID, nama, email, password!!)
                }
            }

            cancelSubmitEditAdmin.setOnClickListener {
                goBack(userID)
            }
        }
    }

    private fun editAdmin(id:Int , nama: String, email: String, password: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.updateDataAdmin(id ,nama, email, password)
            .enqueue(object: Callback<CrudModel> {
                override fun onResponse(
                    call: Call<CrudModel>,
                    response: Response<CrudModel>
                ) {
                    if(response.body()?.status == 200){
                        progressDialog.dismiss()
                        Toast.makeText(this@EditAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        goBack(id)
                    }else{
                        progressDialog.dismiss()
                        Toast.makeText(this@EditAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(this@EditAdminActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun getDetailData(userID: Int) {
        ApiService.endpoint.getDetailAdmin(userID).enqueue(object: Callback<DetailDataAdminModel> {
            override fun onResponse(
                call: Call<DetailDataAdminModel>,
                response: Response<DetailDataAdminModel>
            ) {
                val result = response.body()!!
                result.let {
                    editNamaAdmin.setText(result.name)
                    editEmailAdmin.setText(result.email)
                }
            }

            override fun onFailure(call: Call<DetailDataAdminModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun validateInput(): Boolean {
        if(editNamaAdmin.text.toString().isEmpty()){
            layoutEditNamaAdmin.isErrorEnabled = true
            layoutEditNamaAdmin.error = "Nama Admin tidak boleh kosong!"
            return false
        }

        if(editEmailAdmin.text.toString().isEmpty()){
            layoutEditEmailAdmin.isErrorEnabled = true
            layoutEditEmailAdmin.error = "Email Admin tidak boleh kosong!"
            return false
        }

        if (!editEmailAdmin.text.toString().isValidEmail()) {
            layoutEditEmailAdmin.isErrorEnabled = true
            layoutEditEmailAdmin.error = "Email Admin harus email yang valid!"
            return false
        }

        if(passwordAdminBaru.text.toString().isNotEmpty() && passwordAdminBaru.text.toString().length < 8){
            layoutPasswordAdminBaru.isErrorEnabled = true
            layoutPasswordAdminBaru.error = "Password Admin harus berisi minimal 8 karakter!"
            return false
        }

        if(passwordAdminBaru.text.toString().isNotEmpty() && konfirmasiPasswordBaru.text.toString().isNotEmpty() && konfirmasiPasswordBaru.text.toString() != passwordAdminBaru.text.toString()){
            layoutKonfirmasiPasswordBaru.isErrorEnabled = true
            layoutKonfirmasiPasswordBaru.error = "Konfirmasi Password Admin harus sama dengan Password Admin yang dimasukan!"
            return false
        }

        return true
    }

    private fun goBack(id: Int) {
        val bundle = Bundle()
        val intent = Intent(this, DetailAdminActivity::class.java)
        bundle.putInt("id_user", id)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    fun String.isValidEmail() = isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}
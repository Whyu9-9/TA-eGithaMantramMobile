package com.example.ekidungmantram.admin.mantram

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ekidungmantram.R
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import kotlinx.android.synthetic.main.activity_add_arti.*
import kotlinx.android.synthetic.main.activity_add_bait.*
import kotlinx.android.synthetic.main.activity_edit_mantram_admin.*
import retrofit2.Call
import retrofit2.Response

class AddArtiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_arti)
        supportActionBar!!.title = "Tambah Arti Mantram"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_mantram")

            submitArtiMantram.setOnClickListener {
                val arti_mantra     = artiMantramForm.text.toString()
                if(validateInput()){
                    postArti(postID, arti_mantra)
                }
            }

            cancelSubmitArtiMantram.setOnClickListener {
                goBack(postID)
            }
        }
    }

    private fun postArti(postID: Int, artiMantra: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.editArtiMantram(postID, artiMantra).enqueue(object: retrofit2.Callback<CrudModel>{
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AddArtiActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    goBack(postID)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AddArtiActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AddArtiActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun validateInput(): Boolean {
        if(artiMantramForm.text.toString().isEmpty()){
            layoutAddArtiMantram.isErrorEnabled = true
            layoutAddArtiMantram.error = "Arti Mantram tidak boleh kosong!"
            return false
        }

        return true
    }

    private fun goBack(id: Int) {
        val bundle = Bundle()
        val intent = Intent(this, DetailMantramAdminActivity::class.java)
        bundle.putInt("id_mantram", id)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}
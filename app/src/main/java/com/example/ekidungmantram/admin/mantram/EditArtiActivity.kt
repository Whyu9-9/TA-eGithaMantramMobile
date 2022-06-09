package com.example.ekidungmantram.admin.mantram

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ekidungmantram.R
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailMantramAdminModel
import kotlinx.android.synthetic.main.activity_edit_arti.*
import kotlinx.android.synthetic.main.activity_edit_bait.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditArtiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_arti)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Edit Arti Mantram"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_mantram")
            getDetailData(postID)

            submitEditedArti.setOnClickListener {
                val arti_mantra = artiEditedMantram.text.toString()
                if(validateInput()){
                    editArti(postID, arti_mantra)
                }
            }

            cancelSubmitEditedArti.setOnClickListener {
                goBack(postID)
            }
        }
    }

    private fun getDetailData(postID: Int) {
        ApiService.endpoint.getDetailMantramAdmin(postID).enqueue(object:
            Callback<DetailMantramAdminModel> {
            override fun onResponse(
                call: Call<DetailMantramAdminModel>,
                response: Response<DetailMantramAdminModel>
            ) {
                val result = response.body()!!
                result.let {
                    artiEditedMantram.setText(result.arti_mantra)
                }
            }

            override fun onFailure(call: Call<DetailMantramAdminModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun editArti(postID: Int, artiMantra: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.editArtiMantram(postID, artiMantra).enqueue(object: retrofit2.Callback<CrudModel>{
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@EditArtiActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    goBack(postID)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@EditArtiActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@EditArtiActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun validateInput(): Boolean {
        if(artiEditedMantram.text.toString().isEmpty()){
            layoutEditedArtiMantram.isErrorEnabled = true
            layoutEditedArtiMantram.error = "Arti Mantram tidak boleh kosong!"
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
package com.example.ekidungmantram.admin.kidung

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ekidungmantram.R
import com.example.ekidungmantram.admin.mantram.DetailMantramAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import kotlinx.android.synthetic.main.activity_add_bait.*
import kotlinx.android.synthetic.main.activity_add_lirik_kidung_admin.*
import retrofit2.Call
import retrofit2.Response

class AddLirikKidungAdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_lirik_kidung_admin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Tambah Lirik Kidung"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_kidung")
            val namaPost = bundle.getString("nama_kidung")

            submitLirikKidung.setOnClickListener {
                val lirikKidung     = lirikKidungForm.text.toString()
                if(validateInput()){
                    postLirik(postID, lirikKidung, namaPost!!)
                }
            }

            cancelSubmitLirikKidung.setOnClickListener {
                goBack(postID, namaPost!!)
            }
        }
    }

    private fun postLirik(postID: Int, lirikKidung: String, nama:String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.createDataLirikKidungAdmin(postID, lirikKidung).enqueue(object: retrofit2.Callback<CrudModel>{
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AddLirikKidungAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    goBack(postID, nama)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AddLirikKidungAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AddLirikKidungAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun validateInput(): Boolean {
        if(lirikKidungForm.text.toString().isEmpty()){
            layoutAddLirikKidung.isErrorEnabled = true
            layoutAddLirikKidung.error = "Lirik Kidung tidak boleh kosong!"
            return false
        }

        return true
    }

    private fun goBack(id: Int, nama: String) {
        val bundle = Bundle()
        val intent = Intent(this, DetailKidungAdminActivity::class.java)
        bundle.putInt("id_kidung", id)
        bundle.putString("nama_kidung", nama)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }
}
package com.example.ekidungmantram.admin.kidung

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ekidungmantram.R
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailLirikKidungAdminModel
import com.example.ekidungmantram.model.adminmodel.DetailMantramAdminModel
import kotlinx.android.synthetic.main.activity_add_lirik_kidung_admin.*
import kotlinx.android.synthetic.main.activity_edit_arti.*
import kotlinx.android.synthetic.main.activity_edit_lirik_kidung_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditLirikKidungAdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_lirik_kidung_admin)
        supportActionBar!!.title = "Edit Arti Mantram"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val lirikID = bundle.getInt("id_lirik_kidung")
            val postID = bundle.getInt("id_kidung")
            val namaPost = bundle.getString("nama_kidung")
            getDetailData(lirikID)

            submitEditedLirikKidung.setOnClickListener {
                val lirikKidung = lirikEditedKidung.text.toString()
                if(validateInput()){
                    editLirik(postID, lirikID, lirikKidung, namaPost!!)
                }
            }

            cancelSubmitEditedLirikKidung.setOnClickListener {
                goBack(postID, namaPost!!)
            }
        }
    }

    private fun editLirik(postId:Int, lirikID: Int, lirikKidung: String, nama: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.updateDataLirikKidungAdmin(lirikID, lirikKidung).enqueue(object: retrofit2.Callback<CrudModel>{
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@EditLirikKidungAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    goBack(postId, nama)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@EditLirikKidungAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@EditLirikKidungAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun validateInput(): Boolean {
        if(lirikEditedKidung.text.toString().isEmpty()){
            layoutEditedLirikKidung.isErrorEnabled = true
            layoutEditedLirikKidung.error = "Lirik Kidung tidak boleh kosong!"
            return false
        }

        return true
    }

    private fun getDetailData(lirikID: Int) {
        ApiService.endpoint.getShowLirikKidungAdmin(lirikID).enqueue(object:
            Callback<DetailLirikKidungAdminModel> {
            override fun onResponse(
                call: Call<DetailLirikKidungAdminModel>,
                response: Response<DetailLirikKidungAdminModel>
            ) {
                val result = response.body()!!
                result.let {
                    lirikEditedKidung.setText(result.bait_kidung)
                }
            }

            override fun onFailure(call: Call<DetailLirikKidungAdminModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
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
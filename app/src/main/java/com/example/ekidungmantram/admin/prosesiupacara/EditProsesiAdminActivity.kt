package com.example.ekidungmantram.admin.prosesiupacara

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.admin.gamelan.AllGamelanAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailGamelanAdminModel
import com.example.ekidungmantram.model.adminmodel.DetailProsesiAdminModel
import kotlinx.android.synthetic.main.activity_edit_gamelan_admin.*
import kotlinx.android.synthetic.main.activity_edit_prosesi_admin.*
import retrofit2.Call
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

@Suppress("DEPRECATION")
class EditProsesiAdminActivity : AppCompatActivity() {
    private val REQUEST_CODE     = 100
    private var bitmap: Bitmap?  = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_prosesi_admin)
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_prosesi")

            setFormData(postID)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = "Edit Prosesi Upacara"

            selectEditedImageProsesi.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE)
            }

            submitEditedProsesi.setOnClickListener {
                val nama_post     = namaEditedProsesi.text.toString()
                val video         = linkEditedProsesi.text.toString()
                val deskripsi     = deskripsiEditedProsesiAdmin.text.toString()
                val gambar        = bitmapToString(bitmap).toString()
                if(validateInput()){
                    postEditedProsesi(postID, nama_post, video, deskripsi, gambar)
                }
            }

            cancelSubmitEditedProsesi.setOnClickListener {
                goBack()
            }
        }
    }

    private fun setFormData(postID: Int) {
        ApiService.endpoint.getShowProsesiAdmin(postID).enqueue(object:
            retrofit2.Callback<DetailProsesiAdminModel> {
            override fun onResponse(
                call: Call<DetailProsesiAdminModel>,
                response: Response<DetailProsesiAdminModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskripsiEditedProsesiAdmin.setText(result.deskripsi)
                    namaEditedProsesi.setText(result.nama_post)
                    linkEditedProsesi.setText(result.video)

                    if(result.gambar != null) {
                        Glide.with(this@EditProsesiAdminActivity)
                            .load(Constant.IMAGE_URL+result.gambar).into(submitEditedImgProsesi)
                    }
                }
            }

            override fun onFailure(call: Call<DetailProsesiAdminModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun postEditedProsesi(postID: Int, namaPost: String, video: String, deskripsi: String, gambar: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.updateDataProsesiAdmin(postID ,namaPost, video, deskripsi, gambar)
            .enqueue(object: retrofit2.Callback<CrudModel> {
                override fun onResponse(
                    call: Call<CrudModel>,
                    response: Response<CrudModel>
                ) {
                    if(response.body()?.status == 200){
                        progressDialog.dismiss()
                        Toast.makeText(this@EditProsesiAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        goBack()
                    }else{
                        progressDialog.dismiss()
                        Toast.makeText(this@EditProsesiAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(this@EditProsesiAdminActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun goBack() {
        val intent = Intent(this, AllProsesiAdminActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun validateInput(): Boolean {
        if(namaEditedProsesi.text.toString().isEmpty()){
            layoutEditedNamaProsesi.isErrorEnabled = true
            layoutEditedNamaProsesi.error = "Nama Prosesi tidak boleh kosong!"
            return false
        }

        if(linkEditedProsesi.text.toString().isEmpty()){
            layoutEditedLinkProsesi.isErrorEnabled = true
            layoutEditedLinkProsesi.error = "Link Youtube tidak boleh kosong!"
            return false
        }

        if(deskripsiEditedProsesiAdmin.text.toString().isEmpty()){
            layoutEditedDeskripsiProsesi.isErrorEnabled = true
            layoutEditedDeskripsiProsesi.error = "Deskripsi Prosesi tidak boleh kosong!"
            return false
        }

        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            val imgUri: Uri? = data?.data
            submitEditedImgProsesi.setImageURI(imgUri) // handle chosen image
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver,imgUri)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun bitmapToString(bitmap: Bitmap?): String? {
        if (bitmap != null) {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val array = byteArrayOutputStream.toByteArray()
            return Base64.getEncoder().encodeToString(array)
        }
        return ""
    }
}
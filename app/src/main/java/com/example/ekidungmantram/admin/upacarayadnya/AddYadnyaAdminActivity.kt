package com.example.ekidungmantram.admin.upacarayadnya

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
import com.example.ekidungmantram.R
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import kotlinx.android.synthetic.main.activity_add_yadnya_admin.*
import kotlinx.android.synthetic.main.activity_detail_yadnya_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

@Suppress("DEPRECATION")
class AddYadnyaAdminActivity : AppCompatActivity() {
    private val REQUEST_CODE     = 100
    private var bitmap: Bitmap?  = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_yadnya_admin)
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_yadnya")
            val namaPost = bundle.getString("nama_yadnya")

            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = "Tambah " + namaPost
            tambahYadnyaText.text = "Tambah $namaPost Baru"

            selectImageYadnya.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE)
            }

            submitYadnya.setOnClickListener {
                val nama_post     = namaYadnya.text.toString()
                val video         = linkYadnya.text.toString()
                val deskripsi     = deskripsiYadnyaAdmin.text.toString()
                val gambar        = bitmapToString(bitmap).toString()
                val kategori      = namaPost
                if(validateInput()){
                    postYadnya(postID, namaPost!!, nama_post, video, deskripsi, gambar, kategori!!)
                }
            }

            cancelSubmitAddYadnya.setOnClickListener {
                goBack(postID, namaPost!!)
            }
        }
    }

    private fun postYadnya(postID: Int, namaPost: String, namaPost1: String, video: String, deskripsi: String, gambar: String, kategori: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.createDataYadnyaAdmin(namaPost1, video, deskripsi, gambar, kategori!!)
            .enqueue(object: Callback<CrudModel> {
                override fun onResponse(
                    call: Call<CrudModel>,
                    response: Response<CrudModel>
                ) {
                    if(response.body()?.status == 200){
                        progressDialog.dismiss()
                        Toast.makeText(this@AddYadnyaAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        goBack(postID, namaPost!!)
                    }else{
                        progressDialog.dismiss()
                        Toast.makeText(this@AddYadnyaAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(this@AddYadnyaAdminActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }


    private fun goBack(postID: Int, namaPost: String) {
        val bundle = Bundle()
        val intent = Intent(this, SelectedAllYadnyaAdminActivity::class.java)
        bundle.putInt("id_yadnya", postID)
        bundle.putString("nama_yadnya", namaPost)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

    private fun validateInput(): Boolean {
        if(namaYadnya.text.toString().isEmpty()){
            layoutNamaYadnya.isErrorEnabled = true
            layoutNamaYadnya.error = "Nama Yadnya tidak boleh kosong!"
            return false
        }

        if(linkYadnya.text.toString().isEmpty()){
            layoutLinkYadnya.isErrorEnabled = true
            layoutLinkYadnya.error = "Link Youtube tidak boleh kosong!"
            return false
        }

        if(deskripsiYadnyaAdmin.text.toString().isEmpty()){
            layoutDeskripsiYadnya.isErrorEnabled = true
            layoutDeskripsiYadnya.error = "Deskripsi Yadnya tidak boleh kosong!"
            return false
        }

        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            val imgUri: Uri? = data?.data
            submitImgYadnya.setImageURI(imgUri) // handle chosen image
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
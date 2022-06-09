package com.example.ekidungmantram.admin.tabuh

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
import com.example.ekidungmantram.admin.mantram.AllMantramAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailMantramAdminModel
import com.example.ekidungmantram.model.adminmodel.DetailTabuhAdminModel
import kotlinx.android.synthetic.main.activity_edit_mantram_admin.*
import kotlinx.android.synthetic.main.activity_edit_tabuh_admin.*
import retrofit2.Call
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

@Suppress("DEPRECATION")
class EditTabuhAdminActivity : AppCompatActivity() {
    private val REQUEST_CODE     = 100
    private var bitmap: Bitmap?  = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_tabuh_admin)
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_tabuh")

            setFormData(postID)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = "Edit Tabuh"

            selectEditedImageTabuh.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE)
            }

            submitEditedTabuh.setOnClickListener {
                val nama_post     = namaEditedTabuh.text.toString()
                val video         = linkEditedTabuh.text.toString()
                val deskripsi     = deskripsiEditedTabuhAdmin.text.toString()
                val gambar        = bitmapToString(bitmap).toString()
                if(validateInput()){
                    postEditedTabuh(postID, nama_post, video, deskripsi, gambar)
                }
            }

            cancelSubmitEditedTabuh.setOnClickListener {
                goBack()
            }
        }
    }

    private fun setFormData(postID: Int) {
        ApiService.endpoint.getShowTabuhAdmin(postID).enqueue(object:
            retrofit2.Callback<DetailTabuhAdminModel> {
            override fun onResponse(
                call: Call<DetailTabuhAdminModel>,
                response: Response<DetailTabuhAdminModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskripsiEditedTabuhAdmin.setText(result.deskripsi)
                    namaEditedTabuh.setText(result.nama_post)
                    linkEditedTabuh.setText(result.video)

                    if(result.gambar != null) {
                        Glide.with(this@EditTabuhAdminActivity)
                            .load(Constant.IMAGE_URL+result.gambar).into(submitEditedImgTabuh)
                    }
                }
            }

            override fun onFailure(call: Call<DetailTabuhAdminModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun postEditedTabuh(postID: Int, namaPost: String, video: String, deskripsi: String, gambar: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.updateDataTabuhAdmin(postID ,namaPost, video, deskripsi, gambar)
            .enqueue(object: retrofit2.Callback<CrudModel> {
                override fun onResponse(
                    call: Call<CrudModel>,
                    response: Response<CrudModel>
                ) {
                    if(response.body()?.status == 200){
                        progressDialog.dismiss()
                        Toast.makeText(this@EditTabuhAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        goBack()
                    }else{
                        progressDialog.dismiss()
                        Toast.makeText(this@EditTabuhAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(this@EditTabuhAdminActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun goBack() {
        val intent = Intent(this, AllTabuhAdminActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun validateInput(): Boolean {
        if(namaEditedTabuh.text.toString().isEmpty()){
            layoutEditedNamaTabuh.isErrorEnabled = true
            layoutEditedNamaTabuh.error = "Nama Tabuh tidak boleh kosong!"
            return false
        }

        if(linkEditedTabuh.text.toString().isEmpty()){
            layoutEditedLinkTabuh.isErrorEnabled = true
            layoutEditedLinkTabuh.error = "Link Youtube tidak boleh kosong!"
            return false
        }

        if(deskripsiEditedTabuhAdmin.text.toString().isEmpty()){
            layoutEditedDeskripsiTabuh.isErrorEnabled = true
            layoutEditedDeskripsiTabuh.error = "Deskripsi Tabuh tidak boleh kosong!"
            return false
        }

        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            val imgUri: Uri? = data?.data
            submitEditedImgTabuh.setImageURI(imgUri) // handle chosen image
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
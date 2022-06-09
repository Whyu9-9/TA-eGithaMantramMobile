package com.example.ekidungmantram.admin.gamelan

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
import com.example.ekidungmantram.admin.tabuh.AllTabuhAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailGamelanAdminModel
import com.example.ekidungmantram.model.adminmodel.DetailTabuhAdminModel
import kotlinx.android.synthetic.main.activity_edit_gamelan_admin.*
import kotlinx.android.synthetic.main.activity_edit_tabuh_admin.*
import retrofit2.Call
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

@Suppress("DEPRECATION")
class EditGamelanAdminActivity : AppCompatActivity() {
    private val REQUEST_CODE     = 100
    private var bitmap: Bitmap?  = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_gamelan_admin)
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_gamelan")

            setFormData(postID)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = "Edit Gamelan Bali"

            selectEditedImageGamelan.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE)
            }

            submitEditedGamelan.setOnClickListener {
                val nama_post     = namaEditedGamelan.text.toString()
                val video         = linkEditedGamelan.text.toString()
                val deskripsi     = deskripsiEditedGamelanAdmin.text.toString()
                val gambar        = bitmapToString(bitmap).toString()
                if(validateInput()){
                    postEditedGamelan(postID, nama_post, video, deskripsi, gambar)
                }
            }

            cancelSubmitEditedGamelan.setOnClickListener {
                goBack()
            }
        }
    }

    private fun setFormData(postID: Int) {
        ApiService.endpoint.getShowGamelanAdmin(postID).enqueue(object:
            retrofit2.Callback<DetailGamelanAdminModel> {
            override fun onResponse(
                call: Call<DetailGamelanAdminModel>,
                response: Response<DetailGamelanAdminModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskripsiEditedGamelanAdmin.setText(result.deskripsi)
                    namaEditedGamelan.setText(result.nama_post)
                    linkEditedGamelan.setText(result.video)

                    if(result.gambar != null) {
                        Glide.with(this@EditGamelanAdminActivity)
                            .load(Constant.IMAGE_URL+result.gambar).into(submitEditedImgGamelan)
                    }
                }
            }

            override fun onFailure(call: Call<DetailGamelanAdminModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun postEditedGamelan(postID: Int, namaPost: String, video: String, deskripsi: String, gambar: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.updateDataGamelanAdmin(postID ,namaPost, video, deskripsi, gambar)
            .enqueue(object: retrofit2.Callback<CrudModel> {
                override fun onResponse(
                    call: Call<CrudModel>,
                    response: Response<CrudModel>
                ) {
                    if(response.body()?.status == 200){
                        progressDialog.dismiss()
                        Toast.makeText(this@EditGamelanAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        goBack()
                    }else{
                        progressDialog.dismiss()
                        Toast.makeText(this@EditGamelanAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(this@EditGamelanAdminActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun goBack() {
        val intent = Intent(this, AllGamelanAdminActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun validateInput(): Boolean {
        if(namaEditedGamelan.text.toString().isEmpty()){
            layoutEditedNamaGamelan.isErrorEnabled = true
            layoutEditedNamaGamelan.error = "Nama Gamelan tidak boleh kosong!"
            return false
        }

        if(linkEditedGamelan.text.toString().isEmpty()){
            layoutEditedLinkGamelan.isErrorEnabled = true
            layoutEditedLinkGamelan.error = "Link Youtube tidak boleh kosong!"
            return false
        }

        if(deskripsiEditedGamelanAdmin.text.toString().isEmpty()){
            layoutEditedDeskripsiGamelan.isErrorEnabled = true
            layoutEditedDeskripsiGamelan.error = "Deskripsi Gamelan tidak boleh kosong!"
            return false
        }

        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            val imgUri: Uri? = data?.data
            submitEditedImgGamelan.setImageURI(imgUri) // handle chosen image
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
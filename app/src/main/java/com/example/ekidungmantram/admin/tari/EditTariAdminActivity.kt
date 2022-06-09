package com.example.ekidungmantram.admin.tari

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
import com.example.ekidungmantram.model.adminmodel.DetailTariAdminModel
import kotlinx.android.synthetic.main.activity_edit_gamelan_admin.*
import kotlinx.android.synthetic.main.activity_edit_tari_admin.*
import retrofit2.Call
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

@Suppress("DEPRECATION")
class EditTariAdminActivity : AppCompatActivity() {
    private val REQUEST_CODE     = 100
    private var bitmap: Bitmap?  = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_tari_admin)
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_tari")

            setFormData(postID)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = "Edit Tari Bali"

            selectEditedImageTari.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE)
            }

            submitEditedTari.setOnClickListener {
                val nama_post     = namaEditedTari.text.toString()
                val video         = linkEditedTari.text.toString()
                val deskripsi     = deskripsiEditedTariAdmin.text.toString()
                val gambar        = bitmapToString(bitmap).toString()
                if(validateInput()){
                    postEditedTari(postID, nama_post, video, deskripsi, gambar)
                }
            }

            cancelSubmitEditedTari.setOnClickListener {
                goBack()
            }
        }
    }

    private fun setFormData(postID: Int) {
        ApiService.endpoint.getShowTariAdmin(postID).enqueue(object:
            retrofit2.Callback<DetailTariAdminModel> {
            override fun onResponse(
                call: Call<DetailTariAdminModel>,
                response: Response<DetailTariAdminModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskripsiEditedTariAdmin.setText(result.deskripsi)
                    namaEditedTari.setText(result.nama_post)
                    linkEditedTari.setText(result.video)

                    if(result.gambar != null) {
                        Glide.with(this@EditTariAdminActivity)
                            .load(Constant.IMAGE_URL+result.gambar).into(submitEditedImgTari)
                    }
                }
            }

            override fun onFailure(call: Call<DetailTariAdminModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun postEditedTari(postID: Int, namaPost: String, video: String, deskripsi: String, gambar: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.updateDataTariAdmin(postID ,namaPost, video, deskripsi, gambar)
            .enqueue(object: retrofit2.Callback<CrudModel> {
                override fun onResponse(
                    call: Call<CrudModel>,
                    response: Response<CrudModel>
                ) {
                    if(response.body()?.status == 200){
                        progressDialog.dismiss()
                        Toast.makeText(this@EditTariAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        goBack()
                    }else{
                        progressDialog.dismiss()
                        Toast.makeText(this@EditTariAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(this@EditTariAdminActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun goBack() {
        val intent = Intent(this, AllTariAdminActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun validateInput(): Boolean {
        if(namaEditedTari.text.toString().isEmpty()){
            layoutEditedNamaTari.isErrorEnabled = true
            layoutEditedNamaTari.error = "Nama Tari tidak boleh kosong!"
            return false
        }

        if(linkEditedTari.text.toString().isEmpty()){
            layoutEditedLinkTari.isErrorEnabled = true
            layoutEditedLinkTari.error = "Link Youtube tidak boleh kosong!"
            return false
        }

        if(deskripsiEditedTariAdmin.text.toString().isEmpty()){
            layoutEditedDeskripsiTari.isErrorEnabled = true
            layoutEditedDeskripsiTari.error = "Deskripsi Tari tidak boleh kosong!"
            return false
        }

        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            val imgUri: Uri? = data?.data
            submitEditedImgTari.setImageURI(imgUri) // handle chosen image
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
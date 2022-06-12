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
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailGamelanAdminModel
import com.example.ekidungmantram.model.adminmodel.DetailYadnyaAdminModel
import kotlinx.android.synthetic.main.activity_add_yadnya_admin.*
import kotlinx.android.synthetic.main.activity_edit_gamelan_admin.*
import kotlinx.android.synthetic.main.activity_edit_yadnya_admin.*
import retrofit2.Call
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

@Suppress("DEPRECATION")
class EditYadnyaAdminActivity : AppCompatActivity() {
    private val REQUEST_CODE     = 100
    private var bitmap: Bitmap?  = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_yadnya_admin)
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val katID = bundle.getInt("id_kategori")
            val postID = bundle.getInt("id_yadnya")
            val namaPost = bundle.getString("nama_yadnya")

            setFormData(postID)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = "Edit " + namaPost
            editYadnyaText.text = "Edit $namaPost"

            selectEditedImageYadnya.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE)
            }

            submitEditedYadnya.setOnClickListener {
                val nama_post     = namaEditedYadnya.text.toString()
                val video         = linkEditedYadnya.text.toString()
                val deskripsi     = deskripsiEditedYadnyaAdmin.text.toString()
                val gambar        = bitmapToString(bitmap).toString()
                if(validateInput()){
                    postEditedYadnya(katID, namaPost, postID, nama_post, video, deskripsi, gambar)
                }
            }

            cancelSubmitEditedYadnya.setOnClickListener {
                goBack(katID, namaPost)
            }
        }
    }

    private fun setFormData(postID: Int) {
        ApiService.endpoint.getShowYadnyaAdmin(postID).enqueue(object:
            retrofit2.Callback<DetailYadnyaAdminModel> {
            override fun onResponse(
                call: Call<DetailYadnyaAdminModel>,
                response: Response<DetailYadnyaAdminModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskripsiEditedYadnyaAdmin.setText(result.deskripsi)
                    namaEditedYadnya.setText(result.nama_post)
                    linkEditedYadnya.setText(result.video)

                    if(result.gambar != null) {
                        Glide.with(this@EditYadnyaAdminActivity)
                            .load(Constant.IMAGE_URL+result.gambar).into(submitEditedImgYadnya)
                    }
                }
            }

            override fun onFailure(call: Call<DetailYadnyaAdminModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun postEditedYadnya(katID: Int, namaPost: String?, postID: Int, namaPost1: String, video: String, deskripsi: String, gambar: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.updateDataYadnyaAdmin(postID ,namaPost1, video, deskripsi, gambar)
            .enqueue(object: retrofit2.Callback<CrudModel> {
                override fun onResponse(
                    call: Call<CrudModel>,
                    response: Response<CrudModel>
                ) {
                    if(response.body()?.status == 200){
                        progressDialog.dismiss()
                        Toast.makeText(this@EditYadnyaAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        goBack(katID, namaPost!!)
                    }else{
                        progressDialog.dismiss()
                        Toast.makeText(this@EditYadnyaAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(this@EditYadnyaAdminActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun goBack(katID: Int, namaPost: String?) {
        val bundle = Bundle()
        val intent = Intent(this, SelectedAllYadnyaAdminActivity::class.java)
        bundle.putInt("id_yadnya", katID)
        bundle.putString("nama_yadnya", namaPost)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

    private fun validateInput(): Boolean {
        if(namaEditedYadnya.text.toString().isEmpty()){
            layoutEditedNamaYadnya.isErrorEnabled = true
            layoutEditedNamaYadnya.error = "Nama Yadnya tidak boleh kosong!"
            return false
        }

        if(linkEditedYadnya.text.toString().isEmpty()){
            layoutEditedLinkYadnya.isErrorEnabled = true
            layoutEditedLinkYadnya.error = "Link Youtube tidak boleh kosong!"
            return false
        }

        if(deskripsiEditedYadnyaAdmin.text.toString().isEmpty()){
            layoutEditedDeskripsiYadnya.isErrorEnabled = true
            layoutEditedDeskripsiYadnya.error = "Deskripsi Yadnya tidak boleh kosong!"
            return false
        }

        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            val imgUri: Uri? = data?.data
            submitEditedImgYadnya.setImageURI(imgUri) // handle chosen image
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
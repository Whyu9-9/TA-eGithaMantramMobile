package com.example.ekidungmantram.admin.kidung

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.admin.mantram.AllMantramAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailKidungAdminModel
import com.example.ekidungmantram.model.adminmodel.DetailMantramAdminModel
import kotlinx.android.synthetic.main.activity_edit_kidung_admin.*
import kotlinx.android.synthetic.main.activity_edit_mantram_admin.*
import retrofit2.Call
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

@Suppress("DEPRECATION")
class EditKidungAdminActivity : AppCompatActivity(), AdapterView.OnItemClickListener {
    private val REQUEST_CODE     = 100
    private var bitmap: Bitmap?  = null
    private var yadnya : String? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_kidung_admin)
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_kidung")

            setFormData(postID)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = "Edit Kidung"

            selectEditedImageKidung.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE)
            }

            submitEditedKidung.setOnClickListener {
                val nama_post     = namaEditedKidung.text.toString()
                val kategori      = yadnya
                val video         = linkEditedKidung.text.toString()
                val deskripsi     = deskripsiEditedKidung.text.toString()
                val gambar        = bitmapToString(bitmap).toString()
                if(validateInput()){
                    postEditedKidung(postID, nama_post, kategori!!, video, deskripsi, gambar)
                }
            }

            cancelSubmitEditedKidung.setOnClickListener {
                goBack()
            }
        }
    }

    private fun setFormData(postID: Int) {
        ApiService.endpoint.getShowKidungAdmin(postID).enqueue(object:
            retrofit2.Callback<DetailKidungAdminModel> {
            override fun onResponse(
                call: Call<DetailKidungAdminModel>,
                response: Response<DetailKidungAdminModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskripsiEditedKidung.setText(result.deskripsi)
                    namaEditedKidung.setText(result.nama_post)
                    linkEditedKidung.setText(result.video)
                    setupSpinnerYadnya(result.nama_kategori)
                    Glide.with(this@EditKidungAdminActivity).load(Constant.IMAGE_URL+result.gambar).into(submitEditedImgKidung)
                }
            }

            override fun onFailure(call: Call<DetailKidungAdminModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun postEditedKidung(postID: Int, namaPost: String, kategori: String, video: String, deskripsi: String, gambar: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.updateDataKidungAdmin(postID ,namaPost, video, kategori, deskripsi, gambar)
            .enqueue(object: retrofit2.Callback<CrudModel> {
                override fun onResponse(
                    call: Call<CrudModel>,
                    response: Response<CrudModel>
                ) {
                    if(response.body()?.status == 200){
                        progressDialog.dismiss()
                        Toast.makeText(this@EditKidungAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        goBack()
                    }else{
                        progressDialog.dismiss()
                        Toast.makeText(this@EditKidungAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(this@EditKidungAdminActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val item = p0?.getItemAtPosition(p2).toString()
        yadnya = item
    }

    private fun goBack() {
        val intent = Intent(this, AllKidungAdminActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setupSpinnerYadnya(data: String) {
        val options = listOf("Dewa Yadnya", "Pitra Yadnya", "Manusa Yadnya", "Rsi Yadnya", "Bhuta Yadnya")
        val adapter = ArrayAdapter(this, R.layout.spinner_yadnya, options)
        with(jenisEditedKidung){
            setText(data, false)
            if(jenisEditedKidung.text.toString().isNotEmpty())
                yadnya = jenisEditedKidung.text.toString()
            onItemClickListener = this@EditKidungAdminActivity
            setAdapter(adapter)
        }
    }

    private fun validateInput(): Boolean {
        if(namaEditedKidung.text.toString().isEmpty()){
            layoutEditedNamaKidung.isErrorEnabled = true
            layoutEditedNamaKidung.error = "Nama Kidung tidak boleh kosong!"
            return false
        }

        if(linkEditedKidung.text.toString().isEmpty()){
            layoutEditedLinkKidung.isErrorEnabled = true
            layoutEditedLinkKidung.error = "Link Youtube tidak boleh kosong!"
            return false
        }

        if(deskripsiEditedKidung.text.toString().isEmpty()){
            layoutEditedDeskripsiKidung.isErrorEnabled = true
            layoutEditedDeskripsiKidung.error = "Deskripsi Kidung tidak boleh kosong!"
            return false
        }

        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            val imgUri: Uri? = data?.data
            submitEditedImgKidung.setImageURI(imgUri) // handle chosen image
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
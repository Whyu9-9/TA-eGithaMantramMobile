package com.example.ekidungmantram.admin.mantram

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
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailMantramAdminModel
import kotlinx.android.synthetic.main.activity_edit_mantram_admin.*
import retrofit2.Call
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

@Suppress("DEPRECATION")
class EditMantramAdminActivity : AppCompatActivity(), AdapterView.OnItemClickListener {
    private val REQUEST_CODE     = 100
    private var bitmap: Bitmap?  = null
    private var yadnya : String? = null
    private var tipe : String?   = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_mantram_admin)
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_mantram")

            setFormData(postID)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = "Edit Mantram"

            selectEditedImageMantram.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE)
            }

            submitEditedMantram.setOnClickListener {
                val nama_post     = namaEditedMantram.text.toString()
                val jenis_mantram = tipe
                val kategori      = yadnya
                val video         = linkEditedMantram.text.toString()
                val deskripsi     = deskripsiEditedMantram.text.toString()
                val gambar        = bitmapToString(bitmap).toString()
                if(validateInput()){
                    postEditedMantram(postID, nama_post, jenis_mantram!!, kategori!!, video, deskripsi, gambar)
                }
            }

            cancelSubmitEditedMantram.setOnClickListener {
                goBack()
            }
        }
    }

    private fun setFormData(postID: Int) {
        ApiService.endpoint.getShowMantramAdmin(postID).enqueue(object:
            retrofit2.Callback<DetailMantramAdminModel> {
            override fun onResponse(
                call: Call<DetailMantramAdminModel>,
                response: Response<DetailMantramAdminModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskripsiEditedMantram.setText(result.deskripsi)
                    namaEditedMantram.setText(result.nama_post)
                    linkEditedMantram.setText(result.video)

                    if(result.nama_kategori != null){
                        setupSpinnerYadnya(result.nama_kategori)
                    }else{
                        setupSpinnerYadnya("Tidak Ada")
                    }

                    if(result.jenis_mantram != null){
                        setupSpinnerTipe(result.jenis_mantram)
                    }else{
                        setupSpinnerTipe("Umum")
                    }

                    if(result.gambar != null) {
                        Glide.with(this@EditMantramAdminActivity)
                            .load(Constant.IMAGE_URL+result.gambar).into(submitEditedImgMantram)
                    }
                }
            }

            override fun onFailure(call: Call<DetailMantramAdminModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun postEditedMantram(id: Int, namaPost: String, jenisMantram: String, kategori: String, video: String, deskripsi: String, gambar: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.updateMantramAdmin(id ,namaPost, jenisMantram, video, deskripsi, kategori, gambar)
            .enqueue(object: retrofit2.Callback<CrudModel> {
                override fun onResponse(
                    call: Call<CrudModel>,
                    response: Response<CrudModel>
                ) {
                    if(response.body()?.status == 200){
                        progressDialog.dismiss()
                        Toast.makeText(this@EditMantramAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        goBack()
                    }else{
                        progressDialog.dismiss()
                        Toast.makeText(this@EditMantramAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(this@EditMantramAdminActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val check = p0?.getItemAtPosition(p2).toString()
        if(check.contains("yadnya", ignoreCase = true) || check.contains("ada", ignoreCase = true)){
            val item = p0?.getItemAtPosition(p2).toString()
            yadnya = item
        }else {
            val item2 = p0?.getItemAtPosition(p2).toString()
            tipe = item2
        }
    }

    private fun goBack() {
        val intent = Intent(this, AllMantramAdminActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setupSpinnerYadnya(data: String) {
        val options = listOf("Tidak Ada", "Dewa Yadnya", "Pitra Yadnya", "Manusa Yadnya", "Rsi Yadnya", "Bhuta Yadnya")
        val adapter = ArrayAdapter(this, R.layout.spinner_yadnya, options)
        with(jenisEditedMantram){
            setText(data, false)
            if(jenisEditedMantram.text.toString().isNotEmpty())
                yadnya = jenisEditedMantram.text.toString()
            onItemClickListener = this@EditMantramAdminActivity
            setAdapter(adapter)
        }
    }

    private fun setupSpinnerTipe(data: String) {
        val options = listOf("Umum", "Khusus")
        val adapter = ArrayAdapter(this, R.layout.spinner_tipe_mantram, options)
        with(tipeEditedMantram){
            setText(data, false)
            if(tipeEditedMantram.text.toString().isNotEmpty())
                tipe = tipeEditedMantram.text.toString()
            onItemClickListener = this@EditMantramAdminActivity
            setAdapter(adapter)
        }
    }

    private fun validateInput(): Boolean {
        if(namaEditedMantram.text.toString().isEmpty()){
            layoutEditedNamaMantram.isErrorEnabled = true
            layoutEditedNamaMantram.error = "Nama Mantram tidak boleh kosong!"
            return false
        }

        if(linkEditedMantram.text.toString().isEmpty()){
            layoutEditedLinkMantram.isErrorEnabled = true
            layoutEditedLinkMantram.error = "Link Youtube tidak boleh kosong!"
            return false
        }

        if(deskripsiEditedMantram.text.toString().isEmpty()){
            layoutEditedDeskripsiMantram.isErrorEnabled = true
            layoutEditedDeskripsiMantram.error = "Deskripsi Mantram tidak boleh kosong!"
            return false
        }

        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            val imgUri: Uri? = data?.data
            submitEditedImgMantram.setImageURI(imgUri) // handle chosen image
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
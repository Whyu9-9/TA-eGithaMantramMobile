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
import com.example.ekidungmantram.R
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import kotlinx.android.synthetic.main.activity_add_kidung_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

@Suppress("DEPRECATION")
class AddKidungAdminActivity : AppCompatActivity(), AdapterView.OnItemClickListener {
    private val REQUEST_CODE     = 100
    private var bitmap: Bitmap?  = null
    private var yadnya : String? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_kidung_admin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Tambah Kidung"
        setupSpinnerYadnya()

        selectImageKidung.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE)
        }

        submitKidung.setOnClickListener {
            val nama_post     = namaKidung.text.toString()
            val kategori      = yadnya
            val video         = linkKidung.text.toString()
            val deskripsi     = deskripsiKidung.text.toString()
            val gambar        = bitmapToString(bitmap).toString()
            if(validateInput()){
                postKidung(nama_post, kategori!!, video, deskripsi, gambar)
            }
        }

        cancelSubmitAddKidung.setOnClickListener {
            goBack()
        }
    }

    private fun postKidung(namaPost: String, kategori: String, video: String, deskripsi: String, gambar: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.createDataKidungAdmin(namaPost, video, kategori, deskripsi, gambar)
            .enqueue(object: Callback<CrudModel> {
                override fun onResponse(
                    call: Call<CrudModel>,
                    response: Response<CrudModel>
                ) {
                    if(response.body()?.status == 200){
                        progressDialog.dismiss()
                        Toast.makeText(this@AddKidungAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        goBack()
                    }else{
                        progressDialog.dismiss()
                        Toast.makeText(this@AddKidungAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(this@AddKidungAdminActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun goBack() {
        val intent = Intent(this, AllKidungAdminActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setupSpinnerYadnya() {
        val options = listOf("Dewa Yadnya", "Pitra Yadnya", "Manusa Yadnya", "Rsi Yadnya", "Bhuta Yadnya")
        val adapter = ArrayAdapter(this, R.layout.spinner_yadnya, options)
        with(jenisKidung){
            setText("Dewa Yadnya", false)
            if(jenisKidung.text.toString().isNotEmpty())
                yadnya = jenisKidung.text.toString()
            onItemClickListener = this@AddKidungAdminActivity
            setAdapter(adapter)
        }
    }

    private fun validateInput(): Boolean {
        if(namaKidung.text.toString().isEmpty()){
            layoutNamaKidung.isErrorEnabled = true
            layoutNamaKidung.error = "Nama Kidung tidak boleh kosong!"
            return false
        }

        if(linkKidung.text.toString().isEmpty()){
            layoutLinkKidung.isErrorEnabled = true
            layoutLinkKidung.error = "Link Youtube tidak boleh kosong!"
            return false
        }

        if(deskripsiKidung.text.toString().isEmpty()){
            layoutDeskripsiKidung.isErrorEnabled = true
            layoutDeskripsiKidung.error = "Deskripsi Kidung tidak boleh kosong!"
            return false
        }

        return true
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val item = p0?.getItemAtPosition(p2).toString()
        yadnya = item
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            val imgUri: Uri? = data?.data
            submitImgKidung.setImageURI(imgUri) // handle chosen image
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
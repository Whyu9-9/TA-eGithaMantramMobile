package com.example.ekidungmantram.admin.mantram

import android.app.Activity
import android.app.ProgressDialog
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.ekidungmantram.R
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import kotlinx.android.synthetic.main.activity_add_mantram_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*
import android.content.Intent

@Suppress("DEPRECATION")
class AddMantramAdminActivity : AppCompatActivity(), AdapterView.OnItemClickListener {
    private val REQUEST_CODE     = 100
    private var bitmap: Bitmap?  = null
    private var yadnya : String? = null
    private var tipe : String?   = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_mantram_admin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Tambah Mantram"
        setupSpinnerTipe()
        setupSpinnerYadnya()

        selectImageMantram.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE)
        }

        submitMantram.setOnClickListener {
            val nama_post     = namaMantram.text.toString()
            val jenis_mantram = tipe
            val kategori      = yadnya
            val video         = linkMantram.text.toString()
            val deskripsi     = deskripsiMantram.text.toString()
            val gambar        = bitmapToString(bitmap).toString()
            if(validateInput()){
                postMantram(nama_post, jenis_mantram!!, kategori!!, video, deskripsi, gambar)
            }
        }

        cancelSubmitAddMantram.setOnClickListener {
            goBack()
        }
    }

    private fun postMantram(namaPost: String, jenisMantram: String, kategori: String, video: String, deskripsi: String, gambar: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengunggah Data")
        progressDialog.show()
        ApiService.endpoint.createMantramAdmin(namaPost, jenisMantram, video, deskripsi, kategori, gambar)
            .enqueue(object: Callback<CrudModel> {
                override fun onResponse(
                    call: Call<CrudModel>,
                    response: Response<CrudModel>
                ) {
                    if(response.body()?.status == 200){
                        progressDialog.dismiss()
                        Toast.makeText(this@AddMantramAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        goBack()
                    }else{
                        progressDialog.dismiss()
                        Toast.makeText(this@AddMantramAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(this@AddMantramAdminActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun goBack() {
        val intent = Intent(this, AllMantramAdminActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setupSpinnerYadnya() {
        val options = listOf("Tidak Ada", "Dewa Yadnya", "Pitra Yadnya", "Manusa Yadnya", "Rsi Yadnya", "Bhuta Yadnya")
        val adapter = ArrayAdapter(this, R.layout.spinner_yadnya, options)
        with(jenisMantram){
            setText("Tidak Ada", false)
            if(jenisMantram.text.toString().isNotEmpty())
                yadnya = jenisMantram.text.toString()
            onItemClickListener = this@AddMantramAdminActivity
            setAdapter(adapter)
        }
    }

    private fun setupSpinnerTipe() {
        val options = listOf("Umum", "Khusus")
        val adapter = ArrayAdapter(this, R.layout.spinner_tipe_mantram, options)
        with(tipeMantram){
            setText("Umum", false)
            if(tipeMantram.text.toString().isNotEmpty())
                tipe = tipeMantram.text.toString()
            onItemClickListener = this@AddMantramAdminActivity
            setAdapter(adapter)
        }
    }

    private fun validateInput(): Boolean {
        if(namaMantram.text.toString().isEmpty()){
            layoutNamaMantram.isErrorEnabled = true
            layoutNamaMantram.error = "Nama Mantram tidak boleh kosong!"
            return false
        }

        if(linkMantram.text.toString().isEmpty()){
            layoutLinkMantram.isErrorEnabled = true
            layoutLinkMantram.error = "Link Youtube tidak boleh kosong!"
            return false
        }

        if(deskripsiMantram.text.toString().isEmpty()){
            layoutDeskripsiMantram.isErrorEnabled = true
            layoutDeskripsiMantram.error = "Deskripsi Mantram tidak boleh kosong!"
            return false
        }

        return true
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

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            val imgUri: Uri? = data?.data
            submitImgMantram.setImageURI(imgUri) // handle chosen image
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
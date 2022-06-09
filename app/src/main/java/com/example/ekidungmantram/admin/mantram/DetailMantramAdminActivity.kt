package com.example.ekidungmantram.admin.mantram

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailMantramAdminModel
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_detail_mantram_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailMantramAdminActivity : YouTubeBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_mantram_admin)
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_mantram")

            getDetailData(postID)

            tambahArtiMantram.setOnClickListener {
                val intent = Intent(this, AddArtiActivity::class.java)
                bundle.putInt("id_mantram", postID)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            tambahBaitMantram.setOnClickListener {
                val intent = Intent(this, AddBaitActivity::class.java)
                bundle.putInt("id_mantram", postID)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            editArtiMantram.setOnClickListener {
                val intent = Intent(this, EditArtiActivity::class.java)
                bundle.putInt("id_mantram", postID)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            editBaitMantram.setOnClickListener {
                val intent = Intent(this, EditBaitActivity::class.java)
                bundle.putInt("id_mantram", postID)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            editMantram.setOnClickListener {
                val intent = Intent(this, EditMantramAdminActivity::class.java)
                bundle.putInt("id_mantram", postID)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            hapusMantram.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Hapus Mantram")
                    .setMessage("Apakah anda yakin ingin menghapus mantram ini?")
                    .setCancelable(true)
                    .setPositiveButton("Iya") { _, _ ->
                        deleteMantram(postID)
                    }.setNegativeButton("Batal") { dialogInterface, _ ->
                        dialogInterface.cancel()
                    }.show()
            }
        }

        backToMantramAdmin.setOnClickListener {
            val intent = Intent(this, AllMantramAdminActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun deleteMantram(postID: Int) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteMantramAdmin(postID).enqueue(object: Callback<CrudModel>{
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@DetailMantramAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    goBack()
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@DetailMantramAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@DetailMantramAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("DetailMantramActivity", message)
    }

    private fun getDetailData(postID: Int) {
        ApiService.endpoint.getDetailMantramAdmin(postID).enqueue(object: Callback<DetailMantramAdminModel> {
            override fun onResponse(
                call: Call<DetailMantramAdminModel>,
                response: Response<DetailMantramAdminModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskripsiMantramAdmin.text   = result.deskripsi
                    detailNamaMantramAdmin.text  = result.nama_post

                    if(result.nama_kategori != null){
                        detailJenisMantramAdmin.text = "Mantram "+ result.nama_kategori
                    }else{
                        detailJenisMantramAdmin.text = "Mantram Hindu"
                    }
                    keteranganMantramAdmin.text  = result.jenis_mantram

                    if(result.bait_mantra != null) {
                        baitMantramAdmin.text = result.bait_mantra
                        editBaitMantram.visibility = View.VISIBLE
                        tambahBaitMantram.visibility = View.GONE
                    }else{
                        baitMantramAdmin.visibility = View.GONE
                        editBaitMantram.visibility = View.GONE
                        tambahBaitMantram.visibility = View.VISIBLE
                    }

                    if(result.arti_mantra != null){
                        artiMantramAdmin.text = result.arti_mantra
                        editArtiMantram.visibility = View.VISIBLE
                        tambahArtiMantram.visibility = View.GONE
                    }else{
                        artiMantramAdmin.visibility = View.GONE
                        editArtiMantram.visibility = View.GONE
                        tambahArtiMantram.visibility = View.VISIBLE
                    }

                    if (result.jenis_mantram != null) {
                        layoutKeter.visibility = View.VISIBLE
                    }

                    if(result.jenis_mantram != "Umum"){
                        keteranganMantramAdmin.setBackgroundResource(R.color.red_danger)
                    }
                    if(result.gambar != null) {
                        Glide.with(this@DetailMantramAdminActivity)
                            .load(Constant.IMAGE_URL+result.gambar).into(imageDetailMantramAdmin)
                    }else{
                        imageDetailMantramAdmin.setImageResource(R.drawable.sample_image_yadnya)
                    }
                    playYoutubeVideo(result.video)
                }
                setShimmerToStop()
            }

            override fun onFailure(call: Call<DetailMantramAdminModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun playYoutubeVideo(video: String) {
        youtubePlayerMantramAdmin.initialize(Constant.API_KEY, object : YouTubePlayer.OnInitializedListener{
            override fun onInitializationSuccess(
                p0: YouTubePlayer.Provider?,
                p1: YouTubePlayer?,
                p2: Boolean
            ) {
                p1!!.loadVideo(video)
                p1.play()
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun goBack() {
        val intent = Intent(this, AllMantramAdminActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setShimmerToStop() {
        shimmerDetailMantramAdmin.stopShimmer()
        shimmerDetailMantramAdmin.visibility = View.GONE
        scrollDetailMantramAdmin.visibility  = View.VISIBLE
    }
}
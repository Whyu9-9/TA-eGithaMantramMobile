package com.example.ekidungmantram.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.DetailMantramModel
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_detail_mantram.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailMantramActivity : YouTubeBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_mantram)
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_mantram")

            getDetailData(postID)
        }

        backToMantram.setOnClickListener {
            val intent = Intent(this, AllMantramActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun printLog(message: String) {
        Log.d("DetailMantramActivity", message)
    }

    private fun getDetailData(postID: Int) {
        ApiService.endpoint.getDetailMantram(postID).enqueue(object: Callback<DetailMantramModel> {
            override fun onResponse(
                call: Call<DetailMantramModel>,
                response: Response<DetailMantramModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskripsiMantram.text   = result.deskripsi
                    detailNamaMantram.text  = result.nama_post

                    if(result.nama_kategori != null){
                        detailJenisMantram.text = "Mantram "+ result.nama_kategori
                    }else{
                        detailJenisMantram.text = "Mantram Hindu"
                    }
                    keteranganMantram.text  = result.jenis_mantram

                    if(result.bait_mantra != null) {
                        baitMantram.text = result.bait_mantra
                    }else{
                        layoutBaitMantram.visibility = View.GONE
                    }

                    if(result.arti_mantra != null){
                        artiMantram.text = result.arti_mantra
                    }else{
                        layoutArtiMantram.visibility = View.GONE
                    }

                    if(result.jenis_mantram != "Umum"){
                        keteranganMantram.setBackgroundResource(R.color.red_danger)
                    }
                    if(result.gambar != null) {
                        Glide.with(this@DetailMantramActivity)
                            .load(Constant.IMAGE_URL+result.gambar).into(imageDetailMantram)
                    }else{
                        imageDetailMantram.setImageResource(R.drawable.sample_image_yadnya)
                    }
                    playYoutubeVideo(result.video)
                }
                setShimmerToStop()
            }

            override fun onFailure(call: Call<DetailMantramModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun playYoutubeVideo(video: String) {
        youtubePlayerMantram.initialize(Constant.API_KEY, object : YouTubePlayer.OnInitializedListener{
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

    private fun setShimmerToStop() {
        shimmerDetailMantram.stopShimmer()
        shimmerDetailMantram.visibility = View.GONE
        scrollDetailMantram.visibility  = View.VISIBLE
    }
}
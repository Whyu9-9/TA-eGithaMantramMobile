package com.example.ekidungmantram.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.DetailMantramModel
import com.example.ekidungmantram.model.DetailTabuhModel
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_detail_mantram.*
import kotlinx.android.synthetic.main.activity_detail_tabuh.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailTabuhActivity : YouTubeBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_tabuh)
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_tabuh")
            getDetailData(postID)
        }

        backToTabuh.setOnClickListener {
            val intent = Intent(this, AllTabuhActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getDetailData(postID: Int) {
        ApiService.endpoint.getDetailTabuh(postID).enqueue(object: Callback<DetailTabuhModel> {
            override fun onResponse(
                call: Call<DetailTabuhModel>,
                response: Response<DetailTabuhModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskripsiTabuh.text   = result.deskripsi
                    detailNamaTabuh.text  = result.nama_post
                    detailJenisTabuh.text = "Tabuh"

                    if(result.gambar != null) {
                        Glide.with(this@DetailTabuhActivity)
                            .load(Constant.IMAGE_URL+result.gambar).into(imageDetailTabuh)
                    }else{
                        imageDetailTabuh.setImageResource(R.drawable.music)
                    }
                    playYoutubeVideo(result.video)
                }
                setShimmerToStop()
            }

            override fun onFailure(call: Call<DetailTabuhModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun playYoutubeVideo(video: String) {
        youtubePlayerTabuh.initialize(Constant.API_KEY, object : YouTubePlayer.OnInitializedListener{
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
        shimmerDetailTabuh.stopShimmer()
        shimmerDetailTabuh.visibility = View.GONE
        scrollDetailTabuh.visibility  = View.VISIBLE
    }
}
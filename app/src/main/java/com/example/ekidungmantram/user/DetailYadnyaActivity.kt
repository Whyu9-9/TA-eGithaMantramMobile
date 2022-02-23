package com.example.ekidungmantram.user

import android.app.PendingIntent.getActivity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.CardSliderAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.DetailYadnyaModel
import com.example.ekidungmantram.model.HomeModel
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_detail_yadnya.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailYadnyaActivity : YouTubeBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_yadnya)

        val bundle :Bundle ?= intent.extras
        if (bundle!=null){
            val postID     = bundle.getInt("id_yadnya")
            val categoryID = bundle.getInt("id_kategori")
            getDetailData(postID)
        }
    }

    private fun getDetailData(id: Int) {
        ApiService.endpoint.getDetailYadnya(id).enqueue(object: Callback<DetailYadnyaModel>{
            override fun onResponse(
                call: Call<DetailYadnyaModel>,
                response: Response<DetailYadnyaModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskription.setText(result.deskripsi)
                    detailNamaYadnya.setText(result.nama_post)
                    detailJenisYadnya.setText(result.nama_kategori)
                    Glide.with(this@DetailYadnyaActivity).load(Constant.IMAGE_URL +result.gambar).into(imageDetailYadnya)
                    playYoutubeVideo(result.video)
                }
            }

            override fun onFailure(call: Call<DetailYadnyaModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun playYoutubeVideo(video: String) {
        youtubePlayer.initialize(Constant.API_KEY, object : YouTubePlayer.OnInitializedListener{
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

}
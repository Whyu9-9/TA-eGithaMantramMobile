package com.example.ekidungmantram.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.BaitKidungAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.DetailBaitKidungModel
import com.example.ekidungmantram.model.DetailKidungModel
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_detail_kidung.*
import kotlinx.android.synthetic.main.activity_detail_yadnya.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailKidungActivity : YouTubeBaseActivity() {
    private var layoutManagerBait          : LinearLayoutManager? = null
    private lateinit var baitKidungAdapter : BaitKidungAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_kidung)
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_kidung")

            getDetailData(postID)
            getBaitData(postID)
            setupRecyclerViewBait()
        }

        backToKidung.setOnClickListener {
            val intent = Intent(this, AllKidungActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun printLog(message: String) {
        Log.d("DetailKidungActivity", message)
    }

    private fun getDetailData(id: Int) {
        ApiService.endpoint.getDetailKidung(id).enqueue(object: Callback<DetailKidungModel> {
            override fun onResponse(
                call: Call<DetailKidungModel>,
                response: Response<DetailKidungModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskripsiKidung.text   = result.deskripsi
                    detailNamaKidung.text  = result.nama_post
                    detailJenisKidung.text = result.nama_kategori
                    if(result.gambar != null) {
                        Glide.with(this@DetailKidungActivity)
                            .load(Constant.IMAGE_URL + result.gambar).into(imageDetailKidung)
                    }else{
                        imageDetailKidung.setImageResource(R.drawable.sample_image_yadnya)
                    }
                    playYoutubeVideo(result.video)
                }
                setShimmerToStop()
            }

            override fun onFailure(call: Call<DetailKidungModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getBaitData(id: Int) {
        ApiService.endpoint.getDetailBaitKidung(id).enqueue(object : Callback<DetailBaitKidungModel>{
            override fun onResponse(
                call: Call<DetailBaitKidungModel>,
                response: Response<DetailBaitKidungModel>
            ) {
                showBaitKidungData(response.body()!!)
            }

            override fun onFailure(call: Call<DetailBaitKidungModel>, t: Throwable) {
                printLog("on failure: $t")
            }

        })
    }

    private fun showBaitKidungData(body: DetailBaitKidungModel) {
        val results = body.data
        baitKidungAdapter.setData(results)
    }

    private fun setupRecyclerViewBait() {
        baitKidungAdapter = BaitKidungAdapter(arrayListOf())
        baitKidungList.apply {
            layoutManagerBait = LinearLayoutManager(this@DetailKidungActivity)
            layoutManager     = layoutManagerBait
            adapter           = baitKidungAdapter
            setHasFixedSize(true)
        }
    }

    private fun playYoutubeVideo(video: String) {
        youtubePlayerKidung.initialize(Constant.API_KEY, object : YouTubePlayer.OnInitializedListener{
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
        shimmerDetailKidung.stopShimmer()
        shimmerDetailKidung.visibility = View.GONE
        scrollDetailKidung.visibility  = View.VISIBLE
    }

}
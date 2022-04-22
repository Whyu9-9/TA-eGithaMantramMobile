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
import com.example.ekidungmantram.adapter.TabuhTariAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.*
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_detail_tari.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailTariActivity : YouTubeBaseActivity() {
    private var LayoutManagerTabuh    : LinearLayoutManager? = null
    private lateinit var tabuhAdapter : TabuhTariAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_tari)
        val bundle :Bundle ?= intent.extras
        if (bundle!=null){
            val postID = bundle.getInt("id_tari")

            getDetailData(postID)
            setupRecyclerViewTabuhTari()
            getTabuhTariData(postID)

        }
        backToTari.setOnClickListener {
            val intent = Intent(this, AllTariActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun printLog(message: String) {
        Log.d("DetailTariActivity", message)
    }

    private fun getTabuhTariData(postID: Int) {
        ApiService.endpoint.getDetailTabuhTari(postID).enqueue(object : Callback<DetailTabuhTariModel>{
            override fun onResponse(
                call: Call<DetailTabuhTariModel>,
                response: Response<DetailTabuhTariModel>
            ) {
                if(response.body()!!.data.toString() == "[]") {
                    layoutTabuhTari.visibility = View.GONE
                }else{
                    layoutTabuhTari.visibility = View.VISIBLE
                    nodatataritabuh.visibility = View.GONE
                    showTabuhTariData(response.body()!!)
                }
            }

            override fun onFailure(call: Call<DetailTabuhTariModel>, t: Throwable) {
                printLog("on failure: $t")
            }

        })
    }

    private fun showTabuhTariData(body: DetailTabuhTariModel) {
        val results = body.data
        tabuhAdapter.setData(results)
    }

    private fun setupRecyclerViewTabuhTari() {
        tabuhAdapter = TabuhTariAdapter(arrayListOf(), object : TabuhTariAdapter.OnAdapterTabuhTariListener{
            override fun onClick(result: DetailTabuhTariModel.Data) {
                val bundle = Bundle()
                val intent = Intent(this@DetailTariActivity, DetailTabuhActivity::class.java)
                bundle.putInt("id_tabuh", result.id_post)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })

        tabuhTariList.apply {
            LayoutManagerTabuh  = GridLayoutManager(this@DetailTariActivity, 3, LinearLayoutManager.VERTICAL, false)
            layoutManager        = LayoutManagerTabuh
            adapter              = tabuhAdapter
            setHasFixedSize(true)
        }
    }

    private fun getDetailData(postID: Int) {
        ApiService.endpoint.getDetailTari(postID).enqueue(object: Callback<DetailTariModel> {
            override fun onResponse(
                call: Call<DetailTariModel>,
                response: Response<DetailTariModel>
            ) {
                val result = response.body()!!
                result.let {
                    detailNamaTari.text = result.nama_post
                    deskripsiTari.text = result.deskripsi
                    Glide.with(this@DetailTariActivity).load(Constant.IMAGE_URL +result.gambar).into(imageDetailTari)
                    playYoutubeVideo(result.video)
                }
                setShimmerToStop()
            }

            override fun onFailure(call: Call<DetailTariModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun playYoutubeVideo(video: String) {
        youtubePlayerTari.initialize(Constant.API_KEY, object : YouTubePlayer.OnInitializedListener{
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
        shimmerDetailTari.stopShimmer()
        shimmerDetailTari.visibility = View.GONE
        scrollDetailTari.visibility  = View.VISIBLE
    }
}
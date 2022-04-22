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
import com.example.ekidungmantram.adapter.TabuhGamelanAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.DetailGamelanModel
import com.example.ekidungmantram.model.DetailTabuhGamelanModel
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_detail_gamelan.*
import kotlinx.android.synthetic.main.activity_detail_tari.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailGamelanActivity : YouTubeBaseActivity() {
    private var LayoutManagerTabuh    : LinearLayoutManager? = null
    private lateinit var tabuhAdapter : TabuhGamelanAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_gamelan)

        val bundle :Bundle ?= intent.extras
        if (bundle!=null){
            val postID = bundle.getInt("id_gamelan")

            getDetailData(postID)
            setupRecyclerViewTabuhGamelan()
            getTabuhGamelanData(postID)

        }
        backToGamelan.setOnClickListener {
            val intent = Intent(this, AllGamelanActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showTabuhGamelanData(body: DetailTabuhGamelanModel) {
        val results = body.data
        tabuhAdapter.setData(results)
    }

    private fun getTabuhGamelanData(postID: Int) {
        ApiService.endpoint.getDetailTabuhGamelan(postID).enqueue(object : Callback<DetailTabuhGamelanModel>{
            override fun onResponse(
                call: Call<DetailTabuhGamelanModel>,
                response: Response<DetailTabuhGamelanModel>
            ) {
                if(response.body()!!.data.toString() == "[]") {
                    layoutTabuhTari.visibility = View.GONE
                }else{
                    layoutTabuhGamelan.visibility = View.VISIBLE
                    nodatagamelantabuh.visibility = View.GONE
                    showTabuhGamelanData(response.body()!!)
                }
            }

            override fun onFailure(call: Call<DetailTabuhGamelanModel>, t: Throwable) {
                printLog("on failure: $t")
            }

        })
    }

    private fun setupRecyclerViewTabuhGamelan() {
        tabuhAdapter = TabuhGamelanAdapter(arrayListOf(), object : TabuhGamelanAdapter.OnAdapterTabuhGamelanListener{
            override fun onClick(result: DetailTabuhGamelanModel.Data) {
                val bundle = Bundle()
                val intent = Intent(this@DetailGamelanActivity, DetailTabuhActivity::class.java)
                bundle.putInt("id_tabuh", result.id_post)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })

        tabuhGamelanList.apply {
            LayoutManagerTabuh  = GridLayoutManager(this@DetailGamelanActivity, 3, LinearLayoutManager.VERTICAL, false)
            layoutManager        = LayoutManagerTabuh
            adapter              = tabuhAdapter
            setHasFixedSize(true)
        }
    }

    private fun getDetailData(postID: Int) {
        ApiService.endpoint.getDetailGamelan(postID).enqueue(object: Callback<DetailGamelanModel> {
            override fun onResponse(
                call: Call<DetailGamelanModel>,
                response: Response<DetailGamelanModel>
            ) {
                val result = response.body()!!
                result.let {
                    detailNamaGamelan.text = result.nama_post
                    deskripsiGamelan.text = result.deskripsi
                    Glide.with(this@DetailGamelanActivity).load(Constant.IMAGE_URL +result.gambar).into(imageDetailGamelan)
                    playYoutubeVideo(result.video)
                }
                setShimmerToStop()
            }

            override fun onFailure(call: Call<DetailGamelanModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("DetailGamelanActivity", message)
    }

    private fun playYoutubeVideo(video: String) {
        youtubePlayerGamelan.initialize(Constant.API_KEY, object : YouTubePlayer.OnInitializedListener{
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
        shimmerDetailGamelan.stopShimmer()
        shimmerDetailGamelan.visibility = View.GONE
        scrollDetailGamelan.visibility  = View.VISIBLE
    }
}
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
import com.example.ekidungmantram.adapter.*
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.*
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_detail_prosesi.*
import kotlinx.android.synthetic.main.activity_detail_yadnya.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailProsesiActivity : YouTubeBaseActivity() {
    private var LayoutManagerGamelan    : LinearLayoutManager? = null
    private lateinit var gamelanAdapter : GamelanProsesiAdapter
    private var LayoutManagerKidung     : LinearLayoutManager? = null
    private lateinit var kidungAdapter  : KidungProsesiAdapter
    private var LayoutManagerTari       : LinearLayoutManager? = null
    private lateinit var tariAdapter    : TariProsesiAdapter
    private var LayoutManagerTabuh      : LinearLayoutManager? = null
    private lateinit var tabuhAdapter   : TabuhProsesiAdapter
    private var LayoutManagerMantram    : LinearLayoutManager? = null
    private lateinit var mantramAdapter : MantramProsesiAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_prosesi)
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_prosesi")

            getDetailData(postID)

            setupRecyclerViewGamelan()
            getProsesiGamelanData(postID)

            setupRecyclerViewKidung()
            getProsesiKidungData(postID)

            setupRecyclerViewTari()
            getProsesiTariData(postID)

            setupRecyclerViewTabuh()
            getProsesiTabuhData(postID)

            setupRecyclerViewMantram()
            getProsesiMantramData(postID)
        }

        backToHomeProsesi.setOnClickListener {
            val intent = Intent(this, AllProsesiActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun printLog(message: String) {
        Log.d("DetailProsesiActivity", message)
    }

    private fun getProsesiMantramData(postID: Int) {
        ApiService.endpoint.getDetailMantramProsesi(postID).enqueue(object : Callback<MantramProsesiModel>{
            override fun onResponse(
                call: Call<MantramProsesiModel>,
                response: Response<MantramProsesiModel>
            ) {
                if(response.body()!!.data.toString() == "[]") {
                    layoutMantramProsesi.visibility = View.GONE
                }else{
                    layoutMantramProsesi.visibility = View.VISIBLE
                    nodataprosesimantram.visibility = View.GONE
                    showMantramProsesiData(response.body()!!)
                }
            }

            override fun onFailure(call: Call<MantramProsesiModel>, t: Throwable) {
                printLog("on failure: $t")
            }

        })
    }

    private fun showMantramProsesiData(body: MantramProsesiModel) {
        val results = body.data
        mantramAdapter.setData(results)
    }

    private fun setupRecyclerViewMantram() {
        mantramAdapter = MantramProsesiAdapter(arrayListOf(), object : MantramProsesiAdapter.OnAdapterMantramProsesiListener{
            override fun onClick(result: MantramProsesiModel.Data) {
                val bundle = Bundle()
                val intent = Intent(this@DetailProsesiActivity, DetailMantramActivity::class.java)
                bundle.putInt("id_mantram", result.id_post)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })

        mantramProsesiList.apply {
            LayoutManagerMantram = GridLayoutManager(this@DetailProsesiActivity, 3, LinearLayoutManager.VERTICAL, false)
            layoutManager        = LayoutManagerMantram
            adapter              = mantramAdapter
            setHasFixedSize(true)
        }
    }

    private fun getProsesiTabuhData(postID: Int) {
        ApiService.endpoint.getDetailTabuhProsesi(postID).enqueue(object : Callback<TabuhProsesiModel>{
            override fun onResponse(
                call: Call<TabuhProsesiModel>,
                response: Response<TabuhProsesiModel>
            ) {
                if(response.body()!!.data.toString() == "[]") {
                    layoutTabuhProsesi.visibility = View.GONE
                }else{
                    layoutTabuhProsesi.visibility = View.VISIBLE
                    nodataprosesitabuh.visibility = View.GONE
                    showTabuhProsesiData(response.body()!!)
                }
            }

            override fun onFailure(call: Call<TabuhProsesiModel>, t: Throwable) {
                printLog("on failure: $t")
            }

        })
    }

    private fun showTabuhProsesiData(body: TabuhProsesiModel) {
        val results = body.data
        tabuhAdapter.setData(results)
    }

    private fun setupRecyclerViewTabuh() {
        tabuhAdapter = TabuhProsesiAdapter(arrayListOf(), object : TabuhProsesiAdapter.OnAdapterTabuhProsesiListener{
            override fun onClick(result: TabuhProsesiModel.Data) {
                val bundle = Bundle()
                val intent = Intent(this@DetailProsesiActivity, DetailTabuhActivity::class.java)
                bundle.putInt("id_tabuh", result.id_post)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })

        tabuhProsesiList.apply {
            LayoutManagerTabuh = GridLayoutManager(this@DetailProsesiActivity, 3, LinearLayoutManager.VERTICAL, false)
            layoutManager      = LayoutManagerTabuh
            adapter            = tabuhAdapter
            setHasFixedSize(true)
        }
    }

    private fun getProsesiTariData(postID: Int) {
        ApiService.endpoint.getDetailTariProsesi(postID).enqueue(object : Callback<TariProsesiModel>{
            override fun onResponse(
                call: Call<TariProsesiModel>,
                response: Response<TariProsesiModel>
            ) {
                if(response.body()!!.data.toString() == "[]") {
                    layoutTariProsesi.visibility = View.GONE
                }else{
                    layoutTariProsesi.visibility = View.VISIBLE
                    nodataprosesitari.visibility = View.GONE
                    showTariProsesiData(response.body()!!)
                }
            }

            override fun onFailure(call: Call<TariProsesiModel>, t: Throwable) {
                printLog("on failure: $t")
            }

        })
    }

    private fun showTariProsesiData(body: TariProsesiModel) {
        val results = body.data
        tariAdapter.setData(results)
    }

    private fun setupRecyclerViewTari() {
        tariAdapter = TariProsesiAdapter(arrayListOf(), object : TariProsesiAdapter.OnAdapterTariProsesiListener{
            override fun onClick(result: TariProsesiModel.Data) {
                val bundle = Bundle()
                val intent = Intent(this@DetailProsesiActivity, DetailTariActivity::class.java)
                bundle.putInt("id_tari", result.id_post)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })

        tariProsesiList.apply {
            LayoutManagerTari = GridLayoutManager(this@DetailProsesiActivity, 3, LinearLayoutManager.VERTICAL, false)
            layoutManager     = LayoutManagerTari
            adapter           = tariAdapter
            setHasFixedSize(true)
        }
    }

    private fun getProsesiKidungData(postID: Int) {
        ApiService.endpoint.getDetailKidungProsesi(postID).enqueue(object : Callback<KidungProsesiModel>{
            override fun onResponse(
                call: Call<KidungProsesiModel>,
                response: Response<KidungProsesiModel>
            ) {
                if(response.body()!!.data.toString() == "[]") {
                    layoutKidungProsesi.visibility = View.GONE
                }else{
                    layoutKidungProsesi.visibility = View.VISIBLE
                    nodataprosesikidung.visibility = View.GONE
                    showKidungProsesiData(response.body()!!)
                }
            }

            override fun onFailure(call: Call<KidungProsesiModel>, t: Throwable) {
                printLog("on failure: $t")
            }

        })
    }

    private fun showKidungProsesiData(body: KidungProsesiModel) {
        val results = body.data
        kidungAdapter.setData(results)
    }

    private fun setupRecyclerViewKidung() {
        kidungAdapter = KidungProsesiAdapter(arrayListOf(), object : KidungProsesiAdapter.OnAdapterKidungProsesiListener{
            override fun onClick(result: KidungProsesiModel.Data) {
                val bundle = Bundle()
                val intent = Intent(this@DetailProsesiActivity, DetailKidungActivity::class.java)
                bundle.putInt("id_kidung", result.id_post)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })

        kidungProsesiList.apply {
            LayoutManagerKidung = GridLayoutManager(this@DetailProsesiActivity, 3, LinearLayoutManager.VERTICAL, false)
            layoutManager       = LayoutManagerKidung
            adapter             = kidungAdapter
            setHasFixedSize(true)
        }
    }

    private fun getProsesiGamelanData(postID: Int) {
        ApiService.endpoint.getDetailGamelanProsesi(postID).enqueue(object : Callback<GamelanProsesiModel>{
            override fun onResponse(
                call: Call<GamelanProsesiModel>,
                response: Response<GamelanProsesiModel>
            ) {
                if(response.body()!!.data.toString() == "[]") {
                    layoutGamelanProsesi.visibility = View.GONE
                }else{
                    layoutGamelanProsesi.visibility = View.VISIBLE
                    nodataprosesigamelan.visibility = View.GONE
                    showGamelanProsesiData(response.body()!!)
                }
            }

            override fun onFailure(call: Call<GamelanProsesiModel>, t: Throwable) {
                printLog("on failure: $t")
            }

        })
    }

    private fun showGamelanProsesiData(body: GamelanProsesiModel) {
        val results = body.data
        gamelanAdapter.setData(results)
    }

    private fun setupRecyclerViewGamelan() {
        gamelanAdapter = GamelanProsesiAdapter(arrayListOf(), object : GamelanProsesiAdapter.OnAdapterGamelanProsesiListener{
            override fun onClick(result: GamelanProsesiModel.Data) {
                val bundle = Bundle()
                val intent = Intent(this@DetailProsesiActivity, DetailGamelanActivity::class.java)
                bundle.putInt("id_gamelan", result.id_post)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })

        gamelanProsesiList.apply {
            LayoutManagerGamelan = GridLayoutManager(this@DetailProsesiActivity, 3, LinearLayoutManager.VERTICAL, false)
            layoutManager        = LayoutManagerGamelan
            adapter              = gamelanAdapter
            setHasFixedSize(true)
        }
    }

    private fun getDetailData(postID: Int) {
        ApiService.endpoint.getDetailProsesi(postID).enqueue(object: Callback<DetailProsesiModel> {
            override fun onResponse(
                call: Call<DetailProsesiModel>,
                response: Response<DetailProsesiModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskriptionProsesi.text = result.deskripsi
                    detailNamaProsesi.text  = result.nama_post
                    detailJenisProsesi.text = "Prosesi Upacara"

                    if(result.gambar != null) {
                        Glide.with(this@DetailProsesiActivity)
                            .load(Constant.IMAGE_URL+result.gambar).into(imageDetailProsesi)
                    }else{
                        imageDetailProsesi.setImageResource(R.drawable.sample_image_yadnya)
                    }
                    playYoutubeVideo(result.video)
                }
                setShimmerToStop()
            }

            override fun onFailure(call: Call<DetailProsesiModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun playYoutubeVideo(video: String) {
        youtubePlayerProsesi.initialize(Constant.API_KEY, object : YouTubePlayer.OnInitializedListener{
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
        shimmerDetailProsesi.stopShimmer()
        shimmerDetailProsesi.visibility = View.GONE
        scrollDetailProsesi.visibility  = View.VISIBLE
    }
}
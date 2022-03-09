package com.example.ekidungmantram.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.GamelanYadnyaAdapter
import com.example.ekidungmantram.adapter.ProsesiAkhirAdapter
import com.example.ekidungmantram.adapter.ProsesiAwalAdapter
import com.example.ekidungmantram.adapter.ProsesiPuncakAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.database.data.Yadnya
import com.example.ekidungmantram.database.setup.YadnyaDb
import com.example.ekidungmantram.model.*
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_detail_yadnya.*
import kotlinx.android.synthetic.main.layout_list_prosesi_awal.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailYadnyaActivity : YouTubeBaseActivity() {
//    private val db by lazy { YadnyaDb(this) }
    lateinit var db                     : YadnyaDb
    private var LayoutManagerAwal       : LinearLayoutManager? = null
    private lateinit var awalAdapter    : ProsesiAwalAdapter
    private var LayoutManagerPuncak     : LinearLayoutManager? = null
    private lateinit var puncakAdapter  : ProsesiPuncakAdapter
    private var LayoutManagerAkhir      : LinearLayoutManager? = null
    private lateinit var akhirAdapter   : ProsesiAkhirAdapter
    private var LayoutManagerGamelan    : LinearLayoutManager? = null
    private lateinit var gamelanAdapter : GamelanYadnyaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_yadnya)
        db = Room.databaseBuilder(applicationContext, YadnyaDb::class.java, "yadnyabookmarked.db").build()
        val bundle :Bundle ?= intent.extras
        if (bundle!=null){
            val postID       = bundle.getInt("id_yadnya")
            val categoryID   = bundle.getInt("id_kategori")
            val namayadnya   = bundle.getString("nama_yadnya")
            val kategoris    = bundle.getString("kategori")
            val gambars      = bundle.getString("gambar")
            val url : String = Constant.URL + "kategori_pengguna/detil/" + postID.toString() + "/" + categoryID.toString()
//            shareURL.setOnClickListener {
//                shareYadnya(url)
//            }
            backToHome.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            bookmarked.setOnClickListener {
                if(bookmarked.isChecked){
                    Toast.makeText(this, "Bookmark Berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Bookmark Berhasil dihapus", Toast.LENGTH_SHORT).show()
                }
            }
            bookmarked.setOnCheckedChangeListener { checkBox, isChecked->
                if (isChecked){
                    addBookmark(postID, categoryID , namayadnya, kategoris, gambars)
                }else{
                    deleteBookmark(postID)
                }
            }
            CoroutineScope(Dispatchers.IO).launch {
                val test = db.yadnyaDao().fetch(postID)
                if(test != null){
                    bookmarked.setChecked(true)
                }else{
                    bookmarked.setChecked(false)
                }
            }

            getDetailData(postID)
            setupRecyclerViewAwal()
            getProsesiAwalData(postID)
            setupRecyclerViewPuncak()
            getProsesiPuncakData(postID)
            setupRecyclerViewAkhir()
            getProsesiAkhirData(postID)
            setupRecyclerViewGamelan()
            getGamelanYadnyaData(postID)
        }
    }


    private fun addBookmark(postID: Int, categoryID: Int , namayadnya: String?, kategoris: String?, gambars: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            db.yadnyaDao().addBookmarkedYadnya(
                Yadnya(0, postID, categoryID , kategoris.toString() ,namayadnya.toString(), gambars.toString())
            )
            val test = db.yadnyaDao().fetch(postID)
            printLog(test.toString())
        }
    }

    private fun deleteBookmark(postID: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            db.yadnyaDao().deleteById(postID)
        }
    }


//    private fun shareYadnya(url: String) {
//        val link    = url
//        val intent  = Intent(Intent.ACTION_SEND)
//        intent.type = "type/plain"
//        intent.putExtra(Intent.EXTRA_SUBJECT, "Silahkan dilihat yadnya yang saya bagikan!")
//        intent.putExtra(Intent.EXTRA_TEXT, link)
//        val chooser = Intent.createChooser(intent, "Bagikan lewat")
//        startActivity(chooser)
//    }

    private fun printLog(message: String) {
        Log.d("DetailYadnyaActivity", message)
    }

    private fun getProsesiAwalData(id: Int) {
        ApiService.endpoint.getDetailAwal(id).enqueue(object : Callback<ProsesiAwalModel>{
            override fun onResponse(
                call: Call<ProsesiAwalModel>,
                response: Response<ProsesiAwalModel>
            ) {
                showAwalData(response.body()!!)
            }

            override fun onFailure(call: Call<ProsesiAwalModel>, t: Throwable) {
                printLog("on failure: $t")
            }

        })
    }

    private fun showAwalData(data: ProsesiAwalModel) {
        val results = data.data
        awalAdapter.setData(results)
    }

    private fun setupRecyclerViewAwal() {
        awalAdapter = ProsesiAwalAdapter(arrayListOf(), object : ProsesiAwalAdapter.OnAdapterAwalListener{
            override fun onClick(result: ProsesiAwalModel.Data) {
                val bundle = Bundle()
                val intent = Intent(this@DetailYadnyaActivity, DetailProsesiActivity::class.java)
                bundle.putInt("id_yadnya", result.id_post)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })

        awal.apply {
            LayoutManagerAwal = LinearLayoutManager(this@DetailYadnyaActivity)
            layoutManager     = LayoutManagerAwal
            adapter           = awalAdapter
            setHasFixedSize(true)
        }
    }

    private fun getProsesiPuncakData(id: Int) {
        ApiService.endpoint.getDetailPuncak(id).enqueue(object : Callback<ProsesiPuncakModel>{
            override fun onResponse(
                call: Call<ProsesiPuncakModel>,
                response: Response<ProsesiPuncakModel>
            ) {
                showPuncakData(response.body()!!)
            }

            override fun onFailure(call: Call<ProsesiPuncakModel>, t: Throwable) {
                printLog("on failure: $t")
            }

        })
    }

    private fun showPuncakData(data: ProsesiPuncakModel) {
        val results = data.data
        puncakAdapter.setData(results)
    }

    private fun setupRecyclerViewPuncak() {
        puncakAdapter = ProsesiPuncakAdapter(arrayListOf(), object : ProsesiPuncakAdapter.OnAdapterPuncakListener{
            override fun onClick(result: ProsesiPuncakModel.Data) {
                val bundle = Bundle()
                val intent = Intent(this@DetailYadnyaActivity, DetailProsesiActivity::class.java)
                bundle.putInt("id_yadnya", result.id_post)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })

        puncak.apply {
            LayoutManagerPuncak = LinearLayoutManager(this@DetailYadnyaActivity)
            layoutManager       = LayoutManagerPuncak
            adapter             = puncakAdapter
            setHasFixedSize(true)
        }
    }

    private fun getProsesiAkhirData(id: Int) {
        ApiService.endpoint.getDetailAkhir(id).enqueue(object : Callback<ProsesiAkhirModel>{
            override fun onResponse(
                call: Call<ProsesiAkhirModel>,
                response: Response<ProsesiAkhirModel>
            ) {
                showAkhirData(response.body()!!)
            }

            override fun onFailure(call: Call<ProsesiAkhirModel>, t: Throwable) {
                printLog("on failure: $t")
            }

        })
    }

    private fun showAkhirData(data: ProsesiAkhirModel) {
        val results = data.data
        akhirAdapter.setData(results)
    }

    private fun setupRecyclerViewAkhir() {
        akhirAdapter = ProsesiAkhirAdapter(arrayListOf(), object : ProsesiAkhirAdapter.OnAdapterAkhirListener{
            override fun onClick(result: ProsesiAkhirModel.Data) {
                val bundle = Bundle()
                val intent = Intent(this@DetailYadnyaActivity, DetailProsesiActivity::class.java)
                bundle.putInt("id_yadnya", result.id_post)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })

        akhir.apply {
            LayoutManagerAkhir  = LinearLayoutManager(this@DetailYadnyaActivity)
            layoutManager       = LayoutManagerAkhir
            adapter             = akhirAdapter
            setHasFixedSize(true)
        }
    }

    private fun getGamelanYadnyaData(id: Int) {
        ApiService.endpoint.getDetailGamelanYadnya(id).enqueue(object : Callback<GamelanYadnyaModel>{
            override fun onResponse(
                call: Call<GamelanYadnyaModel>,
                response: Response<GamelanYadnyaModel>
            ) {
                if(response.body()!!.data.toString() == "[]") {
                    nodatayadnyagamelan.visibility = View.VISIBLE
                }else{
                    nodatayadnyagamelan.visibility = View.GONE
                    showGamelanYadnyaData(response.body()!!)
                }
            }

            override fun onFailure(call: Call<GamelanYadnyaModel>, t: Throwable) {
                printLog("on failure: $t")
            }

        })
    }

    private fun showGamelanYadnyaData(data: GamelanYadnyaModel) {
        val results = data.data
        gamelanAdapter.setData(results)
    }

    private fun setupRecyclerViewGamelan() {
        gamelanAdapter = GamelanYadnyaAdapter(arrayListOf(), object : GamelanYadnyaAdapter.OnAdapterGamelanYadnyaListener{
            override fun onClick(result: GamelanYadnyaModel.Data) {
                val bundle = Bundle()
                val intent = Intent(this@DetailYadnyaActivity, DetailProsesiActivity::class.java)
                bundle.putInt("id_yadnya", result.id_post)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })

        gamelanYadnyaList.apply {
            LayoutManagerGamelan = GridLayoutManager(this@DetailYadnyaActivity, 3, LinearLayoutManager.VERTICAL, false)
            layoutManager        = LayoutManagerGamelan
            adapter              = gamelanAdapter
            setHasFixedSize(true)
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
                setShimmerToStop()
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

    private fun setShimmerToStop() {
        shimmerDetail.stopShimmer()
        shimmerDetail.visibility = View.GONE
        scrollDetail.visibility  = View.VISIBLE
    }

}
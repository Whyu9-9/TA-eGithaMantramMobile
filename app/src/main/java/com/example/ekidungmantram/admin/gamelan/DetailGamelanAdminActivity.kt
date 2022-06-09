package com.example.ekidungmantram.admin.gamelan

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.admin.BaitKidungListAdminAdapter
import com.example.ekidungmantram.adapter.admin.TabuhGamelanAdminAdapter
import com.example.ekidungmantram.admin.kidung.AllKidungAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.*
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_detail_gamelan_admin.*
import kotlinx.android.synthetic.main.activity_detail_kidung_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailGamelanAdminActivity : YouTubeBaseActivity() {
    private lateinit var setAdapter : TabuhGamelanAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_gamelan_admin)
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_gamelan")
            val namaPost = bundle.getString("nama_gamelan")

            getDetailData(postID)
            tabuhGamelanListAdmin.layoutManager = GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false)
            getTabuhData(postID)

            editTabuhGamelan.setOnClickListener {
                val intent = Intent(this, AllTabuhOnGamelanAdminActivity::class.java)
                bundle.putInt("id_gamelan", postID)
                bundle.putString("nama_gamelan", namaPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            tambahTabuhGamelan.setOnClickListener {
                val intent = Intent(this, AddTabuhToGamelanAdminActivity::class.java)
                bundle.putInt("id_gamelan", postID)
                bundle.putString("nama_gamelan", namaPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            goToEditGamelan.setOnClickListener {
                val intent = Intent(this, EditGamelanAdminActivity::class.java)
                bundle.putInt("id_gamelan", postID)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            hapusGamelan.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Hapus Gamelan")
                    .setMessage("Apakah anda yakin ingin menghapus gamelan ini?")
                    .setCancelable(true)
                    .setPositiveButton("Iya") { _, _ ->
                        deleteGamelan(postID)
                    }.setNegativeButton("Batal") { dialogInterface, _ ->
                        dialogInterface.cancel()
                    }.show()
            }
        }

        backToGamelanAdmin.setOnClickListener {
            val intent = Intent(this, AllGamelanAdminActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun getTabuhData(postID: Int) {
        ApiService.endpoint.getDetailAllTabuhOnGamelanAdmin(postID)
            .enqueue(object: Callback<ArrayList<DetailAllTabuhOnGamelanAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<DetailAllTabuhOnGamelanAdminModel>>,
                    response: Response<ArrayList<DetailAllTabuhOnGamelanAdminModel>>
                ) {
                    val datalist = response.body()
                    if(datalist!!.isNotEmpty()){
                        editTabuhGamelan.visibility = View.VISIBLE
                        tambahTabuhGamelan.visibility = View.GONE
                    }else{
                        tambahTabuhGamelan.visibility = View.VISIBLE
                        editTabuhGamelan.visibility = View.GONE
                    }
                    setAdapter = TabuhGamelanAdminAdapter(datalist!!)
                    tabuhGamelanListAdmin.adapter = setAdapter
                }

                override fun onFailure(call: Call<ArrayList<DetailAllTabuhOnGamelanAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }
            })
    }

    private fun getDetailData(postID: Int) {
        ApiService.endpoint.getDetailGamelanAdmin(postID).enqueue(object: Callback<DetailGamelanAdminModel> {
            override fun onResponse(
                call: Call<DetailGamelanAdminModel>,
                response: Response<DetailGamelanAdminModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskripsiGamelanAdmin.text   = result.deskripsi
                    detailNamaGamelanAdmin.text  = result.nama_post
                    detailJenisGamelanAdmin.text = "Gamelan Bali"

                    if(result.gambar != null) {
                        Glide.with(this@DetailGamelanAdminActivity)
                            .load(Constant.IMAGE_URL+result.gambar).into(imageDetailGamelanAdmin)
                    }else{
                        imageDetailGamelanAdmin.setImageResource(R.drawable.sample_image_gamelan)
                    }
                    playYoutubeVideo(result.video)
                }
                setShimmerToStop()
            }

            override fun onFailure(call: Call<DetailGamelanAdminModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteGamelan(postID: Int) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataGamelanAdmin(postID).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@DetailGamelanAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    goBack()
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@DetailGamelanAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@DetailGamelanAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun playYoutubeVideo(video: String) {
        youtubePlayerGamelanAdmin.initialize(Constant.API_KEY, object : YouTubePlayer.OnInitializedListener{
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
        val intent = Intent(this, AllGamelanAdminActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setShimmerToStop() {
        shimmerDetailGamelanAdmin.stopShimmer()
        shimmerDetailGamelanAdmin.visibility = View.GONE
        scrollDetailGamelanAdmin.visibility  = View.VISIBLE
    }
}
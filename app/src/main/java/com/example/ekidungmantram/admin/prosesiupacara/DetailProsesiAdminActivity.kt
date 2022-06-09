package com.example.ekidungmantram.admin.prosesiupacara

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
import com.example.ekidungmantram.adapter.admin.GamelanProsesiAdminAdapter
import com.example.ekidungmantram.adapter.admin.TabuhGamelanAdminAdapter
import com.example.ekidungmantram.admin.gamelan.AllGamelanAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailAllGamelanOnProsesiAdminModel
import com.example.ekidungmantram.model.adminmodel.DetailAllTabuhOnGamelanAdminModel
import com.example.ekidungmantram.model.adminmodel.DetailProsesiAdminModel
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_detail_gamelan_admin.*
import kotlinx.android.synthetic.main.activity_detail_prosesi.*
import kotlinx.android.synthetic.main.activity_detail_prosesi_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailProsesiAdminActivity : YouTubeBaseActivity() {
    private lateinit var gamelanAdapter : GamelanProsesiAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_prosesi_admin)
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_prosesi")
            val namaPost = bundle.getString("nama_prosesi")

            getDetailData(postID)
            gamelanProsesiListAdmin.layoutManager = GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false)
            getGamelanData(postID)
            editGamelanProsesi.setOnClickListener {
                val intent = Intent(this, AllGamelanOnProsesiAdminActivity::class.java)
                bundle.putInt("id_prosesi", postID)
                bundle.putString("nama_prosesi", namaPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }
            tambahGamelanProsesi.setOnClickListener {
                val intent = Intent(this, AddGamelanToProsesiAdminActivity::class.java)
                bundle.putInt("id_prosesi", postID)
                bundle.putString("nama_prosesi", namaPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            goToEditProsesi.setOnClickListener {
                val intent = Intent(this, EditProsesiAdminActivity::class.java)
                bundle.putInt("id_prosesi", postID)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            hapusProsesi.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Hapus Prosesi")
                    .setMessage("Apakah anda yakin ingin menghapus prosesi ini?")
                    .setCancelable(true)
                    .setPositiveButton("Iya") { _, _ ->
                        deleteProsesi(postID)
                    }.setNegativeButton("Batal") { dialogInterface, _ ->
                        dialogInterface.cancel()
                    }.show()
            }
        }

        backToHomeProsesiAdmin.setOnClickListener {
            val intent = Intent(this, AllProsesiAdminActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getGamelanData(postID: Int) {
        ApiService.endpoint.getDetailAllGamelanOnProsesiAdmin(postID)
            .enqueue(object: Callback<ArrayList<DetailAllGamelanOnProsesiAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<DetailAllGamelanOnProsesiAdminModel>>,
                    response: Response<ArrayList<DetailAllGamelanOnProsesiAdminModel>>
                ) {
                    val datalist = response.body()
                    if(datalist!!.isNotEmpty()){
                        editGamelanProsesi.visibility = View.VISIBLE
                        tambahGamelanProsesi.visibility = View.GONE
                    }else{
                        tambahGamelanProsesi.visibility = View.VISIBLE
                        editGamelanProsesi.visibility = View.GONE
                    }
                    gamelanAdapter = GamelanProsesiAdminAdapter(datalist!!)
                    gamelanProsesiListAdmin.adapter = gamelanAdapter
                }

                override fun onFailure(call: Call<ArrayList<DetailAllGamelanOnProsesiAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }
            })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun getDetailData(postID: Int) {
        ApiService.endpoint.getDetailProsesiAdmin(postID).enqueue(object:
            Callback<DetailProsesiAdminModel> {
            override fun onResponse(
                call: Call<DetailProsesiAdminModel>,
                response: Response<DetailProsesiAdminModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskriptionProsesiAdmin.text   = result.deskripsi
                    detailNamaProsesiAdmin.text  = result.nama_post
                    detailJenisProsesiAdmin.text = "Prosesi Upacara"

                    if(result.gambar != null) {
                        Glide.with(this@DetailProsesiAdminActivity)
                            .load(Constant.IMAGE_URL+result.gambar).into(imageDetailProsesiAdmin)
                    }else{
                        imageDetailGamelanAdmin.setImageResource(R.drawable.sample_image_gamelan)
                    }
                    playYoutubeVideo(result.video)
                }
                setShimmerToStop()
            }

            override fun onFailure(call: Call<DetailProsesiAdminModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteProsesi(postID: Int) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataProsesiAdmin(postID).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@DetailProsesiAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    goBack()
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@DetailProsesiAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@DetailProsesiAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun playYoutubeVideo(video: String) {
        youtubePlayerProsesiAdmin.initialize(Constant.API_KEY, object : YouTubePlayer.OnInitializedListener{
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
        val intent = Intent(this, AllProsesiAdminActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setShimmerToStop() {
        shimmerDetailProsesiAdmin.stopShimmer()
        shimmerDetailProsesiAdmin.visibility = View.GONE
        scrollDetailProsesiAdmin.visibility  = View.VISIBLE
    }
}
package com.example.ekidungmantram.admin.kidung

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.admin.BaitKidungListAdminAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.*
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_all_kidung_admin.*
import kotlinx.android.synthetic.main.activity_detail_kidung_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailKidungAdminActivity : YouTubeBaseActivity() {
    private lateinit var setAdapter : BaitKidungListAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_kidung_admin)
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_kidung")
            val namaPost = bundle.getString("nama_kidung")

            getDetailData(postID)
            baitKidungListAdmin.layoutManager = LinearLayoutManager(applicationContext)
            getBaitData(postID)

            goToListLirik.setOnClickListener {
                val intent = Intent(this, AllLirikKidungAdminActivity::class.java)
                bundle.putInt("id_kidung", postID)
                bundle.putString("nama_kidung", namaPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            tambahLirikKidung.setOnClickListener {
                val intent = Intent(this, AddLirikKidungAdminActivity::class.java)
                bundle.putInt("id_kidung", postID)
                bundle.putString("nama_kidung", namaPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            toEditKidung.setOnClickListener {
                val intent = Intent(this, EditKidungAdminActivity::class.java)
                bundle.putInt("id_kidung", postID)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            deleteKidung.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Hapus Kidung")
                    .setMessage("Apakah anda yakin ingin menghapus kidung ini?")
                    .setCancelable(true)
                    .setPositiveButton("Iya") { _, _ ->
                        hapusKidung(postID)
                    }.setNegativeButton("Batal") { dialogInterface, _ ->
                        dialogInterface.cancel()
                    }.show()
            }
        }

        backToKidungAdmin.setOnClickListener {
            val intent = Intent(this, AllKidungAdminActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun getBaitData(postID: Int) {
        ApiService.endpoint.getDetailAllLirikKidungAdmin(postID)
            .enqueue(object: Callback<ArrayList<DetailAllLirikKidungAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<DetailAllLirikKidungAdminModel>>,
                    response: Response<ArrayList<DetailAllLirikKidungAdminModel>>
                ) {
                    val datalist = response.body()
                    if(datalist!!.isNotEmpty()){
                        rv_layout_bait.visibility = View.VISIBLE
                        tambahLirikKidung.visibility = View.GONE
                        goToListLirik.visibility = View.VISIBLE
                    }else{
                        rv_layout_bait.visibility   = View.GONE
                        tambahLirikKidung.visibility = View.VISIBLE
                        goToListLirik.visibility = View.GONE
                    }
                    setAdapter = BaitKidungListAdminAdapter(datalist!!)
                    baitKidungListAdmin.adapter = setAdapter
                }

                override fun onFailure(call: Call<ArrayList<DetailAllLirikKidungAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }
            })
    }

    private fun getDetailData(postID: Int) {
        ApiService.endpoint.getDetailKidungAdmin(postID).enqueue(object: Callback<DetailKidungAdminModel> {
            override fun onResponse(
                call: Call<DetailKidungAdminModel>,
                response: Response<DetailKidungAdminModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskripsiKidungAdmin.text   = result.deskripsi
                    detailNamaKidungAdmin.text  = result.nama_post

                    if(result.nama_kategori != null){
                        detailJenisKidungAdmin.text = "Kidung "+ result.nama_kategori
                    }

                    if(result.gambar != null) {
                        Glide.with(this@DetailKidungAdminActivity)
                            .load(Constant.IMAGE_URL+result.gambar).into(imageDetailKidungAdmin)
                    }else{
                        imageDetailKidungAdmin.setImageResource(R.drawable.sample_image_yadnya)
                    }
                    playYoutubeVideo(result.video)
                }
                setShimmerToStop()
            }

            override fun onFailure(call: Call<DetailKidungAdminModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun hapusKidung(postID: Int) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataKidungAdmin(postID).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@DetailKidungAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    goBack()
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@DetailKidungAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@DetailKidungAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun playYoutubeVideo(video: String) {
        youtubePlayerKidungAdmin.initialize(Constant.API_KEY, object : YouTubePlayer.OnInitializedListener{
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
        val intent = Intent(this, AllKidungAdminActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setShimmerToStop() {
        shimmerDetailKidungAdmin.stopShimmer()
        shimmerDetailKidungAdmin.visibility = View.GONE
        scrollDetailKidungAdmin.visibility  = View.VISIBLE
    }
}
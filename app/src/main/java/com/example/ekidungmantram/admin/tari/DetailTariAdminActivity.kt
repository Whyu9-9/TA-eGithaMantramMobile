package com.example.ekidungmantram.admin.tari

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
import com.example.ekidungmantram.adapter.admin.TabuhTariAdminAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailAllTabuhOnTariAdminModel
import com.example.ekidungmantram.model.adminmodel.DetailTariAdminModel
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_detail_tari_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailTariAdminActivity : YouTubeBaseActivity() {
    private lateinit var setAdapter : TabuhTariAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_tari_admin)
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_tari")
            val namaPost = bundle.getString("nama_tari")

            getDetailData(postID)
            tabuhTariAdminList.layoutManager = GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false)
            getTabuhData(postID)

            editTabuhTari.setOnClickListener {
                val intent = Intent(this, AllTabuhOnTariAdminActivity::class.java)
                bundle.putInt("id_tari", postID)
                bundle.putString("nama_tari", namaPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            tambahTabuhTari.setOnClickListener {
                val intent = Intent(this, AddTabuhToTariAdminActivity::class.java)
                bundle.putInt("id_tari", postID)
                bundle.putString("nama_tari", namaPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            goToEditTari.setOnClickListener {
                val intent = Intent(this, EditTariAdminActivity::class.java)
                bundle.putInt("id_tari", postID)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            hapusTari.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Hapus Tari")
                    .setMessage("Apakah anda yakin ingin menghapus tari ini?")
                    .setCancelable(true)
                    .setPositiveButton("Iya") { _, _ ->
                        deleteTari(postID)
                    }.setNegativeButton("Batal") { dialogInterface, _ ->
                        dialogInterface.cancel()
                    }.show()
            }
        }

        backToTariAdmin.setOnClickListener {
            val intent = Intent(this, AllTariAdminActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun getTabuhData(postID: Int) {
        ApiService.endpoint.getDetailAllTabuhOnTariAdmin(postID)
            .enqueue(object: Callback<ArrayList<DetailAllTabuhOnTariAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<DetailAllTabuhOnTariAdminModel>>,
                    response: Response<ArrayList<DetailAllTabuhOnTariAdminModel>>
                ) {
                    val datalist = response.body()
                    if(datalist!!.isNotEmpty()){
                        editTabuhTari.visibility = View.VISIBLE
                        tambahTabuhTari.visibility = View.GONE
                    }else{
                        tambahTabuhTari.visibility = View.VISIBLE
                        editTabuhTari.visibility = View.GONE
                    }
                    setAdapter = TabuhTariAdminAdapter(datalist!!)
                    tabuhTariAdminList.adapter = setAdapter
                }

                override fun onFailure(call: Call<ArrayList<DetailAllTabuhOnTariAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }
            })
    }

    private fun getDetailData(postID: Int) {
        ApiService.endpoint.getDetailTariAdmin(postID).enqueue(object: Callback<DetailTariAdminModel> {
            override fun onResponse(
                call: Call<DetailTariAdminModel>,
                response: Response<DetailTariAdminModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskripsiDetailTariAdmin.text   = result.deskripsi
                    detailNamaTariAdmin.text  = result.nama_post
                    detailJenisTariAdmin.text = "Tari Bali"

                    if(result.gambar != null) {
                        Glide.with(this@DetailTariAdminActivity)
                            .load(Constant.IMAGE_URL+result.gambar).into(imageDetailTariAdmin)
                    }else{
                        imageDetailTariAdmin.setImageResource(R.drawable.sample_image_tari)
                    }
                    playYoutubeVideo(result.video)
                }
                setShimmerToStop()
            }

            override fun onFailure(call: Call<DetailTariAdminModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteTari(postID: Int) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataTariAdmin(postID).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@DetailTariAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    goBack()
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@DetailTariAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@DetailTariAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun playYoutubeVideo(video: String) {
        youtubePlayerTariAdmin.initialize(Constant.API_KEY, object : YouTubePlayer.OnInitializedListener{
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
        val intent = Intent(this, AllTariAdminActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setShimmerToStop() {
        shimmerDetailTariAdmin.stopShimmer()
        shimmerDetailTariAdmin.visibility = View.GONE
        scrollDetailTariAdmin.visibility  = View.VISIBLE
    }
}
package com.example.ekidungmantram.admin.upacarayadnya

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailYadnyaAdminModel
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_detail_yadnya_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailYadnyaAdminActivity : YouTubeBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_yadnya_admin)
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val katID = bundle.getInt("id_kategori")
            val postID = bundle.getInt("id_yadnya")
            val namaPost = bundle.getString("nama_yadnya")

            getDetailData(postID)

            goToEditYadnya.setOnClickListener {
                val intent = Intent(this, EditYadnyaAdminActivity::class.java)
                bundle.putInt("id_yadnya", postID)
                bundle.putInt("id_kategori", katID)
                bundle.putString("nama_yadnya", namaPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            hapusYadnya.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Hapus Yadnya")
                    .setMessage("Apakah anda yakin ingin menghapus yadnya ini?")
                    .setCancelable(true)
                    .setPositiveButton("Iya") { _, _ ->
                        deleteYadnya(postID, katID, namaPost!!)
                    }.setNegativeButton("Batal") { dialogInterface, _ ->
                        dialogInterface.cancel()
                    }.show()
            }

            backToHomeYadnyaAdmin.setOnClickListener {
                val bundles = Bundle()
                val intent = Intent(this, SelectedAllYadnyaAdminActivity::class.java)
                bundles.putInt("id_yadnya", katID)
                bundles.putString("nama_yadnya", namaPost)
                intent.putExtras(bundles)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun getDetailData(postID: Int) {
        ApiService.endpoint.getDetailYadnyaAdmin(postID).enqueue(object:
            Callback<DetailYadnyaAdminModel> {
            override fun onResponse(
                call: Call<DetailYadnyaAdminModel>,
                response: Response<DetailYadnyaAdminModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskriptionYadnyaAdmin.text = result.deskripsi
                    detailNamaYadnyaAdmin.text  = result.nama_post
                    detailJenisYadnyaAdmin.text = result.nama_kategori

                    if(result.gambar != null) {
                        Glide.with(this@DetailYadnyaAdminActivity)
                            .load(Constant.IMAGE_URL+result.gambar).into(imageDetailYadnyaAdmin)
                    }else{
                        imageDetailYadnyaAdmin.setImageResource(R.drawable.sample_image_yadnya)
                    }
                    playYoutubeVideo(result.video)
                }
                setShimmerToStop()
            }

            override fun onFailure(call: Call<DetailYadnyaAdminModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteYadnya(postID: Int, katId: Int, name: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataYadnyaAdmin(postID).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@DetailYadnyaAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    goBack(katId, name)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@DetailYadnyaAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@DetailYadnyaAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun goBack(katId: Int, name: String) {
        val bundles = Bundle()
        val intent = Intent(this, SelectedAllYadnyaAdminActivity::class.java)
        bundles.putInt("id_yadnya", katId)
        bundles.putString("nama_yadnya", name)
        intent.putExtras(bundles)
        startActivity(intent)
        finish()
    }

    private fun playYoutubeVideo(video: String) {
        youtubePlayerYadnya.initialize(Constant.API_KEY, object : YouTubePlayer.OnInitializedListener{
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
        shimmerDetailYadnya.stopShimmer()
        shimmerDetailYadnya.visibility = View.GONE
        scrollDetailYadnya.visibility  = View.VISIBLE
    }
}
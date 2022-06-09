package com.example.ekidungmantram.admin.tabuh

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.admin.mantram.AllMantramAdminActivity
import com.example.ekidungmantram.admin.mantram.EditMantramAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailMantramAdminModel
import com.example.ekidungmantram.model.adminmodel.DetailTabuhAdminModel
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_detail_mantram_admin.*
import kotlinx.android.synthetic.main.activity_detail_tabuh_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailTabuhAdminActivity : YouTubeBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_tabuh_admin)

        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            val postID = bundle.getInt("id_tabuh")

            getDetailData(postID)

            toEditTabuh.setOnClickListener {
                val intent = Intent(this, EditTabuhAdminActivity::class.java)
                bundle.putInt("id_tabuh", postID)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            deleteTabuh.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Hapus Tabuh")
                    .setMessage("Apakah anda yakin ingin menghapus tabuh ini?")
                    .setCancelable(true)
                    .setPositiveButton("Iya") { _, _ ->
                        hapusTabuh(postID)
                    }.setNegativeButton("Batal") { dialogInterface, _ ->
                        dialogInterface.cancel()
                    }.show()
            }

            backToTabuhAdmin.setOnClickListener {
                val intent = Intent(this, AllTabuhAdminActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun getDetailData(postID: Int) {
        ApiService.endpoint.getDetailTabuhAdmin(postID).enqueue(object: Callback<DetailTabuhAdminModel> {
            override fun onResponse(
                call: Call<DetailTabuhAdminModel>,
                response: Response<DetailTabuhAdminModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskripsiTabuhAdmin.text   = result.deskripsi
                    detailNamaTabuhAdmin.text  = result.nama_post
                    detailJenisTabuhAdmin.text = "Tabuh Bali"

                    if(result.gambar != null) {
                        Glide.with(this@DetailTabuhAdminActivity)
                            .load(Constant.IMAGE_URL+result.gambar).into(imageDetailTabuhAdmin)
                    }else{
                        imageDetailTabuhAdmin.setImageResource(R.drawable.sample_image_tabuh)
                    }
                    playYoutubeVideo(result.video)
                }
                setShimmerToStop()
            }

            override fun onFailure(call: Call<DetailTabuhAdminModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun hapusTabuh(postID: Int) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataTabuhAdmin(postID).enqueue(object : Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if (response.body()?.status == 200) {
                    progressDialog.dismiss()
                    Toast.makeText(
                        this@DetailTabuhAdminActivity,
                        response.body()?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    goBack()
                } else {
                    progressDialog.dismiss()
                    Toast.makeText(
                        this@DetailTabuhAdminActivity,
                        response.body()?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@DetailTabuhAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun playYoutubeVideo(video: String) {
        youtubePlayerTabuhAdmin.initialize(Constant.API_KEY, object : YouTubePlayer.OnInitializedListener{
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
        val intent = Intent(this, AllTabuhAdminActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setShimmerToStop() {
        shimmerDetailTabuhAdmin.stopShimmer()
        shimmerDetailTabuhAdmin.visibility = View.GONE
        scrollDetailTabuhAdmin.visibility  = View.VISIBLE
    }
}

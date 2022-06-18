package com.example.ekidungmantram.admin.kajimantram

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
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailMantramAdminModel
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_detail_mantram_admin.*
import kotlinx.android.synthetic.main.activity_detail_mantram_need_approval.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailMantramNeedApprovalActivity : YouTubeBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_mantram_need_approval)
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_mantram")

            getDetailData(postID)

            acceptMantram.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Terima Mantram")
                    .setMessage("Apakah anda yakin ingin terima mantram ini untuk dipublish?")
                    .setCancelable(true)
                    .setPositiveButton("Iya") { _, _ ->
                        accMantram(postID, "yes")
                    }.setNegativeButton("Batal") { dialogInterface, _ ->
                        dialogInterface.cancel()
                    }.show()
            }

            rejectMantram.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Tolak Mantram")
                    .setMessage("Apakah anda yakin ingin menolak mantram ini untuk dipublish?")
                    .setCancelable(true)
                    .setPositiveButton("Iya") { _, _ ->
                        accMantram(postID, "no")
                    }.setNegativeButton("Batal") { dialogInterface, _ ->
                        dialogInterface.cancel()
                    }.show()
            }
        }

        backToMantramNAAdmin.setOnClickListener {
            val intent = Intent(this, ListMantramNeedApprovalActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun accMantram(postID: Int, s: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengupdate Data")
        progressDialog.show()
        ApiService.endpoint.approveMantram(postID, s).enqueue(object: Callback<CrudModel>{
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@DetailMantramNeedApprovalActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    goBack()
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@DetailMantramNeedApprovalActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@DetailMantramNeedApprovalActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getDetailData(postID: Int) {
        ApiService.endpoint.getDetailNeedApprovalMantramAdmin(postID).enqueue(object:
            Callback<DetailMantramAdminModel> {
            override fun onResponse(
                call: Call<DetailMantramAdminModel>,
                response: Response<DetailMantramAdminModel>
            ) {
                val result = response.body()!!
                result.let {
                    deskripsiMantramNAAdmin.text   = result.deskripsi
                    detailNamaMantramNAAdmin.text  = result.nama_post

                    if(result.nama_kategori != null){
                        detailJenisMantramNAAdmin.text = "Mantram "+ result.nama_kategori
                    }else{
                        detailJenisMantramNAAdmin.text = "Mantram Hindu"
                    }
                    keteranganMantramNAAdmin.text  = result.jenis_mantram

                    if(result.bait_mantra != null) {
                        baitMantramNAAdmin.text = result.bait_mantra
                    }else{
                        baitMantramNAAdmin.text = "-"
                    }

                    if(result.arti_mantra != null) {
                        artiMantramNAAdmin.text = result.arti_mantra
                    }else{
                        artiMantramNAAdmin.text = "-"
                    }

                    catatanMantramNAAdmin.text = result.approval_notes

                    if (result.jenis_mantram != null) {
                        layoutKeterNA.visibility = View.VISIBLE
                    }

                    if(result.jenis_mantram != "Umum"){
                        keteranganMantramNAAdmin.setBackgroundResource(R.color.red_danger)
                    }

                    if(result.gambar != null) {
                        Glide.with(this@DetailMantramNeedApprovalActivity)
                            .load(Constant.IMAGE_URL+result.gambar).into(imageDetailMantramNAAdmin)
                    }else{
                        imageDetailMantramNAAdmin.setImageResource(R.drawable.sample_image_yadnya)
                    }
                    playYoutubeVideo(result.video)
                }
                setShimmerToStop()
            }

            override fun onFailure(call: Call<DetailMantramAdminModel>, t: Throwable) {
                Toast.makeText(applicationContext, "No Connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun playYoutubeVideo(video: String) {
        youtubePlayerMantramNAAdmin.initialize(Constant.API_KEY, object : YouTubePlayer.OnInitializedListener{
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
        val intent = Intent(this, ListMantramNeedApprovalActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setShimmerToStop() {
        shimmerDetailMantramNAAdmin.stopShimmer()
        shimmerDetailMantramNAAdmin.visibility = View.GONE
        scrollDetailMantramNAAdmin.visibility  = View.VISIBLE
    }
}
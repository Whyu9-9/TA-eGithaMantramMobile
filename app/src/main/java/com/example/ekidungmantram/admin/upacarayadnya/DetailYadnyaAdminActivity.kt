package com.example.ekidungmantram.admin.upacarayadnya

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
import com.example.ekidungmantram.adapter.admin.*
import com.example.ekidungmantram.admin.prosesiupacara.DetailProsesiAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailAllProsesiAwalOnYadnyaAdminModel
import com.example.ekidungmantram.model.adminmodel.DetailYadnyaAdminModel
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_detail_yadnya_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailYadnyaAdminActivity : YouTubeBaseActivity() {
    private lateinit var prosesiAwalAdapter   : ProsesiAwalYadnyaAdminAdapter
    private lateinit var prosesiPuncakAdapter : ProsesiAkhirYadnyaAdminAdapter
    private lateinit var prosesiAkhirAdapter  : ProsesiAkhirYadnyaAdminAdapter
//    private lateinit var gamelanAdapter : GamelanProsesiAdminAdapter
//    private lateinit var tariAdapter    : TariProsesiAdminAdapter
//    private lateinit var kidungAdapter  : KidungProsesiAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_yadnya_admin)
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val katID = bundle.getInt("id_kategori")
            val postID = bundle.getInt("id_yadnya")
            val namaPost = bundle.getString("nama_yadnya")

            getDetailData(postID)

            awalAdmin.layoutManager = LinearLayoutManager(applicationContext)
            getProsesiAwalData(postID)
            editProsesiAwal.setOnClickListener {
                val intent = Intent(this, AllProsesiAwalYadnyaAdminActivity::class.java)
                bundle.putInt("id_kategori", katID)
                bundle.putInt("id_yadnya", postID)
                bundle.putString("nama_yadnya", namaPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            tambahProsesiAwal.setOnClickListener {
                val intent = Intent(this, AddProsesiAwalToYadnyaAdminActivity::class.java)
                bundle.putInt("id_kategori", katID)
                bundle.putInt("id_yadnya", postID)
                bundle.putString("nama_yadnya", namaPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }

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

    private fun getProsesiAwalData(postID: Int) {
        ApiService.endpoint.getDetailAllProsesiAwalOnYadnyaAdmin(postID)
            .enqueue(object: Callback<ArrayList<DetailAllProsesiAwalOnYadnyaAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<DetailAllProsesiAwalOnYadnyaAdminModel>>,
                    response: Response<ArrayList<DetailAllProsesiAwalOnYadnyaAdminModel>>
                ) {
                    val datalist = response.body()
                    if(datalist!!.isNotEmpty()){
                        editProsesiAwal.visibility   = View.VISIBLE
                        tambahProsesiAwal.visibility = View.GONE
                    }else{
                        tambahProsesiAwal.visibility = View.VISIBLE
                        editProsesiAwal.visibility   = View.GONE
                    }

                    prosesiAwalAdapter = datalist?.let { ProsesiAwalYadnyaAdminAdapter(it,
                        object :ProsesiAwalYadnyaAdminAdapter.OnAdapterProsesiAwalYadnyaAdminListener {
                            override fun onClick(result: DetailAllProsesiAwalOnYadnyaAdminModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@DetailYadnyaAdminActivity, DetailProsesiAdminActivity::class.java)
                                bundle.putInt("id_prosesi", result.id_post)
                                bundle.putString("nama_prosesi", result.nama_post)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!
                    awalAdmin.adapter = prosesiAwalAdapter
                }

                override fun onFailure(call: Call<ArrayList<DetailAllProsesiAwalOnYadnyaAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }
            })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
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
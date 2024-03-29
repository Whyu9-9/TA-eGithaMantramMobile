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
import com.example.ekidungmantram.adapter.admin.*
import com.example.ekidungmantram.admin.prosesiupacara.prosesikhusus.AddProsesiKhususActivity
import com.example.ekidungmantram.admin.prosesiupacara.prosesikhusus.AllProsesiKhususActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.*
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_detail_gamelan_admin.*
import kotlinx.android.synthetic.main.activity_detail_prosesi_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailProsesiAdminActivity : YouTubeBaseActivity() {
    private lateinit var gamelanAdapter       : GamelanProsesiAdminAdapter
    private lateinit var tariAdapter          : TariProsesiAdminAdapter
    private lateinit var kidungAdapter        : KidungProsesiAdminAdapter
    private lateinit var tabuhAdapter         : TabuhProsesiAdminAdapter
    private lateinit var mantramAdapter       : MantramProsesiAdminAdapter
    private lateinit var prosesiKhususAdapter : ProsesiKhususAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_prosesi_admin)
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_prosesi")
            val namaPost = bundle.getString("nama_prosesi")
            val yadnya = bundle.getInt("id_yadnya")

            if(yadnya != 0){
                layoutProsesiKhususAdmin.visibility = View.VISIBLE
            }

            getDetailData(postID)
            prosesiKhususListAdmin.layoutManager = LinearLayoutManager(this)
            getProsesiKhususData(postID, yadnya)
            editProsesiKhusus.setOnClickListener {
                val intent = Intent(this, AllProsesiKhususActivity::class.java)
                bundle.putInt("id_yadnya", yadnya)
                bundle.putInt("id_prosesi", postID)
                bundle.putString("nama_prosesi", namaPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            tambahProsesiKhusus.setOnClickListener {
                val intent = Intent(this, AddProsesiKhususActivity::class.java)
                bundle.putInt("id_yadnya", yadnya)
                bundle.putInt("id_prosesi", postID)
                bundle.putString("nama_prosesi", namaPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }

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

            tariProsesiListAdmin.layoutManager = GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false)
            getTariData(postID)
            editTariProsesi.setOnClickListener {
                val intent = Intent(this, AllTariOnProsesiAdminActivity::class.java)
                bundle.putInt("id_prosesi", postID)
                bundle.putString("nama_prosesi", namaPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }
            tambahTariProsesi.setOnClickListener {
                val intent = Intent(this, AddTariToProsesiAdminActivity::class.java)
                bundle.putInt("id_prosesi", postID)
                bundle.putString("nama_prosesi", namaPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            kidungProsesiListAdmin.layoutManager = GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false)
            getKidungData(postID)
            editKidungProsesi.setOnClickListener {
                val intent = Intent(this, AllKidungOnProsesiAdminActivity::class.java)
                bundle.putInt("id_prosesi", postID)
                bundle.putString("nama_prosesi", namaPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }
            tambahKidungProsesi.setOnClickListener {
                val intent = Intent(this, AddKidungToProsesiAdminActivity::class.java)
                bundle.putInt("id_prosesi", postID)
                bundle.putString("nama_prosesi", namaPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            tabuhProsesiListAdmin.layoutManager = GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false)
            getTabuhData(postID)
            editTabuhProsesi.setOnClickListener {
                val intent = Intent(this, AllTabuhOnProsesiAdminActivity::class.java)
                bundle.putInt("id_prosesi", postID)
                bundle.putString("nama_prosesi", namaPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }
            tambahTabuhProsesi.setOnClickListener {
                val intent = Intent(this, AddTabuhToProsesiAdminActivity::class.java)
                bundle.putInt("id_prosesi", postID)
                bundle.putString("nama_prosesi", namaPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            mantramProsesiListAdmin.layoutManager = GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false)
            getMantramData(postID)
            editMantramProsesi.setOnClickListener {
                val intent = Intent(this, AllMantramOnProsesiAdminActivity::class.java)
                bundle.putInt("id_prosesi", postID)
                bundle.putString("nama_prosesi", namaPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }
            tambahMantramProsesi.setOnClickListener {
                val intent = Intent(this, AddMantramToProsesiAdminActivity::class.java)
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

    private fun getProsesiKhususData(postID: Int, yadnya: Int) {
        ApiService.endpoint.getDetailAllProsesiKhusus(postID, yadnya)
            .enqueue(object: Callback<ArrayList<DetailAllProsesiKhususAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<DetailAllProsesiKhususAdminModel>>,
                    response: Response<ArrayList<DetailAllProsesiKhususAdminModel>>
                ) {
                    val datalist = response.body()
                    if(datalist!!.isNotEmpty()){
                        editProsesiKhusus.visibility = View.VISIBLE
                        tambahProsesiKhusus.visibility = View.GONE
                    }else{
                        tambahProsesiKhusus.visibility = View.VISIBLE
                        editProsesiKhusus.visibility = View.GONE
                    }
                    prosesiKhususAdapter = ProsesiKhususAdminAdapter(datalist!!)
                    prosesiKhususListAdmin.adapter = prosesiKhususAdapter
                }

                override fun onFailure(call: Call<ArrayList<DetailAllProsesiKhususAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }
            })
    }

    private fun getMantramData(postID: Int) {
        ApiService.endpoint.getDetailAllMantramOnProsesiAdmin(postID)
            .enqueue(object: Callback<ArrayList<DetailAllMantramOnProsesiAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<DetailAllMantramOnProsesiAdminModel>>,
                    response: Response<ArrayList<DetailAllMantramOnProsesiAdminModel>>
                ) {
                    val datalist = response.body()
                    if(datalist!!.isNotEmpty()){
                        editMantramProsesi.visibility = View.VISIBLE
                        tambahMantramProsesi.visibility = View.GONE
                    }else{
                        tambahMantramProsesi.visibility = View.VISIBLE
                        editMantramProsesi.visibility = View.GONE
                    }
                    mantramAdapter = MantramProsesiAdminAdapter(datalist!!)
                    mantramProsesiListAdmin.adapter = mantramAdapter
                }

                override fun onFailure(call: Call<ArrayList<DetailAllMantramOnProsesiAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }
            })
    }

    private fun getTabuhData(postID: Int) {
        ApiService.endpoint.getDetailAllTabuhOnProsesiAdmin(postID)
            .enqueue(object: Callback<ArrayList<DetailAllTabuhOnProsesiAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<DetailAllTabuhOnProsesiAdminModel>>,
                    response: Response<ArrayList<DetailAllTabuhOnProsesiAdminModel>>
                ) {
                    val datalist = response.body()
                    if(datalist!!.isNotEmpty()){
                        editTabuhProsesi.visibility = View.VISIBLE
                        tambahTabuhProsesi.visibility = View.GONE
                    }else{
                        tambahTabuhProsesi.visibility = View.VISIBLE
                        editTabuhProsesi.visibility = View.GONE
                    }
                    tabuhAdapter = TabuhProsesiAdminAdapter(datalist!!)
                    tabuhProsesiListAdmin.adapter = tabuhAdapter
                }

                override fun onFailure(call: Call<ArrayList<DetailAllTabuhOnProsesiAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }
            })
    }

    private fun getKidungData(postID: Int) {
        ApiService.endpoint.getDetailAllKidungOnProsesiAdmin(postID)
            .enqueue(object: Callback<ArrayList<DetailAllKidungOnProsesiAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<DetailAllKidungOnProsesiAdminModel>>,
                    response: Response<ArrayList<DetailAllKidungOnProsesiAdminModel>>
                ) {
                    val datalist = response.body()
                    if(datalist!!.isNotEmpty()){
                        editKidungProsesi.visibility = View.VISIBLE
                        tambahKidungProsesi.visibility = View.GONE
                    }else{
                        tambahKidungProsesi.visibility = View.VISIBLE
                        editKidungProsesi.visibility = View.GONE
                    }
                    kidungAdapter = KidungProsesiAdminAdapter(datalist!!)
                    kidungProsesiListAdmin.adapter = kidungAdapter
                }

                override fun onFailure(call: Call<ArrayList<DetailAllKidungOnProsesiAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }
            })
    }

    private fun getTariData(postID: Int) {
        ApiService.endpoint.getDetailAllTariOnProsesiAdmin(postID)
            .enqueue(object: Callback<ArrayList<DetailAllTariOnProsesiAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<DetailAllTariOnProsesiAdminModel>>,
                    response: Response<ArrayList<DetailAllTariOnProsesiAdminModel>>
                ) {
                    val datalist = response.body()
                    if(datalist!!.isNotEmpty()){
                        editTariProsesi.visibility = View.VISIBLE
                        tambahTariProsesi.visibility = View.GONE
                    }else{
                        tambahTariProsesi.visibility = View.VISIBLE
                        editTariProsesi.visibility = View.GONE
                    }
                    tariAdapter = TariProsesiAdminAdapter(datalist!!)
                    tariProsesiListAdmin.adapter = tariAdapter
                }

                override fun onFailure(call: Call<ArrayList<DetailAllTariOnProsesiAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }
            })
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
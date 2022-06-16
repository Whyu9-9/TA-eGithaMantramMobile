package com.example.ekidungmantram.admin.upacarayadnya

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.admin.AllGamelanNotOnYadnyaAdminAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AllGamelanAdminModel
import com.example.ekidungmantram.model.adminmodel.CrudModel
import kotlinx.android.synthetic.main.activity_add_gamelan_to_yadnya_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddGamelanToYadnyaAdminActivity : AppCompatActivity() {
    private lateinit var gamelanAdapter : AllGamelanNotOnYadnyaAdminAdapter
    private lateinit var setAdapter     : AllGamelanNotOnYadnyaAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_gamelan_to_yadnya_admin)
        supportActionBar!!.title = "Daftar Semua Gamelan Bali"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val katID = bundle.getInt("id_kategori")
            val postID = bundle.getInt("id_yadnya")
            val namaPost = bundle.getString("nama_yadnya")

            allAddYadnyaGamelanAdmin1.layoutManager = LinearLayoutManager(applicationContext)
            allAddYadnyaGamelanAdmin2.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataGamelanYadnya(katID, postID, namaPost!!)

            swipeYadnyaAddGamelanAdmin.setOnRefreshListener {
                getAllDataGamelanYadnya(katID, postID, namaPost!!)
                swipeYadnyaAddGamelanAdmin.isRefreshing = false
            }
        }
    }

    private fun getAllDataGamelanYadnya(katID: Int, postID: Int, namaPost: String) {
        ApiService.endpoint.getDetailAllGamelanNotOnYadnyaAdmin(postID)
            .enqueue(object: Callback<ArrayList<AllGamelanAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllGamelanAdminModel>>,
                    response: Response<ArrayList<AllGamelanAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null && datalist.isNotEmpty()){
                        swipeYadnyaAddGamelanAdmin.visibility   = View.VISIBLE
                        shimmerYadnyaAddGamelanAdmin.visibility = View.GONE
                        noYadnyaAddGamelanAdmin.visibility      = View.GONE
                    }else{
                        swipeYadnyaAddGamelanAdmin.visibility   = View.GONE
                        shimmerYadnyaAddGamelanAdmin.visibility = View.VISIBLE
                        noYadnyaAddGamelanAdmin.visibility      = View.VISIBLE
                    }
                    setAdapter = AllGamelanNotOnYadnyaAdminAdapter(datalist!!)
                    setAdapter.setOnClickAdd {
                        val builder = AlertDialog.Builder(this@AddGamelanToYadnyaAdminActivity)
                        builder.setTitle("Tambah Gamelan")
                            .setMessage("Apakah anda yakin ingin menambahkan gamelan ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                addGamelanYadnya(katID, postID, it.id_post, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }

                    allAddYadnyaGamelanAdmin1.adapter  = setAdapter
                    setShimmerToStop()

                    cariYadnyaAddGamelanAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noYadnyaAddGamelanAdmin.visibility   = View.GONE
                                    allAddYadnyaGamelanAdmin1.visibility = View.VISIBLE
                                    allAddYadnyaGamelanAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    gamelanAdapter = AllGamelanNotOnYadnyaAdminAdapter(filter as ArrayList<AllGamelanAdminModel>)
                                    gamelanAdapter.setOnClickAdd {
                                        val builder = AlertDialog.Builder(this@AddGamelanToYadnyaAdminActivity)
                                        builder.setTitle("Tambah Gamelan")
                                            .setMessage("Apakah anda yakin ingin menambahkan gamelan ini?")
                                            .setCancelable(true)
                                            .setPositiveButton("Iya") { _, _ ->
                                                addGamelanYadnya(katID, postID, it.id_post, namaPost)
                                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                                dialogInterface.cancel()
                                            }.show()
                                    }
                                    if(filter.isEmpty()){
                                        noYadnyaAddGamelanAdmin.visibility   = View.VISIBLE
                                        allAddYadnyaGamelanAdmin1.visibility = View.GONE
                                        allAddYadnyaGamelanAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noYadnyaAddGamelanAdmin.visibility   = View.GONE
                                        allAddYadnyaGamelanAdmin2.visibility = View.VISIBLE
                                        allAddYadnyaGamelanAdmin2.adapter    = gamelanAdapter
                                        allAddYadnyaGamelanAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allAddYadnyaGamelanAdmin1.visibility = View.VISIBLE
                                        allAddYadnyaGamelanAdmin2.visibility = View.GONE
                                        noYadnyaAddGamelanAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }
                    })
                }

                override fun onFailure(call: Call<ArrayList<AllGamelanAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun addGamelanYadnya(katID: Int, postID: Int, idGamelan: Int, namaPost: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menambahkan Data")
        progressDialog.show()
        ApiService.endpoint.addDataGamelanToYadnyaAdmin(postID, idGamelan).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AddGamelanToYadnyaAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AddGamelanToYadnyaAdminActivity, DetailYadnyaAdminActivity::class.java)
                    bundle.putInt("id_kategori", katID)
                    bundle.putInt("id_yadnya", postID)
                    bundle.putString("nama_yadnya", namaPost)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AddGamelanToYadnyaAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AddGamelanToYadnyaAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerYadnyaAddGamelanAdmin.stopShimmer()
        shimmerYadnyaAddGamelanAdmin.visibility = View.GONE
        swipeYadnyaAddGamelanAdmin.visibility   = View.VISIBLE
    }
}
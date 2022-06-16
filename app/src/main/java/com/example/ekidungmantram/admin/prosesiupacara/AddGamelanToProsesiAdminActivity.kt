package com.example.ekidungmantram.admin.prosesiupacara

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
import com.example.ekidungmantram.adapter.admin.AllGamelanNotOnProsesiAdminAdapter
import com.example.ekidungmantram.adapter.admin.AllTabuhNotOnGamelanAdminAdapter
import com.example.ekidungmantram.admin.gamelan.DetailGamelanAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AllGamelanAdminModel
import com.example.ekidungmantram.model.adminmodel.AllTabuhAdminModel
import com.example.ekidungmantram.model.adminmodel.CrudModel
import kotlinx.android.synthetic.main.activity_add_gamelan_to_prosesi_admin.*
import kotlinx.android.synthetic.main.activity_add_tabuh_to_gamelan_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddGamelanToProsesiAdminActivity : AppCompatActivity() {
    private lateinit var gamelanAdapter : AllGamelanNotOnProsesiAdminAdapter
    private lateinit var setAdapter     : AllGamelanNotOnProsesiAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_gamelan_to_prosesi_admin)
        supportActionBar!!.title = "Daftar Semua Gamelan Bali"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_prosesi")
            val namaPost = bundle.getString("nama_prosesi")

            allAddProsesiGamelanAdmin1.layoutManager = LinearLayoutManager(applicationContext)
            allAddProsesiGamelanAdmin2.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataGamelanProsesi(postID, namaPost!!)

            swipeProsesiAddGamelanAdmin.setOnRefreshListener {
                getAllDataGamelanProsesi(postID, namaPost!!)
                swipeProsesiAddGamelanAdmin.isRefreshing = false
            }
        }
    }

    private fun getAllDataGamelanProsesi(postID: Int, namaPost: String) {
        ApiService.endpoint.getDetailAllGamelanNotOnProsesiAdmin(postID)
            .enqueue(object: Callback<ArrayList<AllGamelanAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllGamelanAdminModel>>,
                    response: Response<ArrayList<AllGamelanAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null && datalist.isNotEmpty()){
                        swipeProsesiAddGamelanAdmin.visibility   = View.VISIBLE
                        shimmerProsesiAddGamelanAdmin.visibility = View.GONE
                        noProsesiAddGamelanAdmin.visibility      = View.GONE
                    }else{
                        swipeProsesiAddGamelanAdmin.visibility   = View.GONE
                        shimmerProsesiAddGamelanAdmin.visibility = View.VISIBLE
                        noProsesiAddGamelanAdmin.visibility      = View.VISIBLE
                    }
                    setAdapter = AllGamelanNotOnProsesiAdminAdapter(datalist!!)
                    setAdapter.setOnClickAdd {
                        val builder = AlertDialog.Builder(this@AddGamelanToProsesiAdminActivity)
                        builder.setTitle("Tambah Gamelan")
                            .setMessage("Apakah anda yakin ingin menambahkan gamelan ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                addGamelanProsesi(postID, it.id_post, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }

                    allAddProsesiGamelanAdmin1.adapter  = setAdapter
                    setShimmerToStop()

                    cariProsesiAddGamelanAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noProsesiAddGamelanAdmin.visibility   = View.GONE
                                    allAddProsesiGamelanAdmin1.visibility = View.VISIBLE
                                    allAddProsesiGamelanAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    gamelanAdapter = AllGamelanNotOnProsesiAdminAdapter(filter as ArrayList<AllGamelanAdminModel>)
                                    gamelanAdapter.setOnClickAdd {
                                        val builder = AlertDialog.Builder(this@AddGamelanToProsesiAdminActivity)
                                        builder.setTitle("Tambah Gamelan")
                                            .setMessage("Apakah anda yakin ingin menambahkan gamelan ini?")
                                            .setCancelable(true)
                                            .setPositiveButton("Iya") { _, _ ->
                                                addGamelanProsesi(postID, it.id_post, namaPost)
                                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                                dialogInterface.cancel()
                                            }.show()
                                    }
                                    if(filter.isEmpty()){
                                        noProsesiAddGamelanAdmin.visibility   = View.VISIBLE
                                        allAddProsesiGamelanAdmin1.visibility = View.GONE
                                        allAddProsesiGamelanAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noProsesiAddGamelanAdmin.visibility   = View.GONE
                                        allAddProsesiGamelanAdmin2.visibility = View.VISIBLE
                                        allAddProsesiGamelanAdmin2.adapter    = gamelanAdapter
                                        allAddProsesiGamelanAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allAddProsesiGamelanAdmin1.visibility = View.VISIBLE
                                        allAddProsesiGamelanAdmin2.visibility = View.GONE
                                        noProsesiAddGamelanAdmin.visibility   = View.GONE
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

    private fun addGamelanProsesi(postID: Int, idGamelan: Int, namaPost: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menambahkan Data")
        progressDialog.show()
        ApiService.endpoint.addDataGamelanToProsesiAdmin(postID, idGamelan).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AddGamelanToProsesiAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AddGamelanToProsesiAdminActivity, DetailProsesiAdminActivity::class.java)
                    bundle.putInt("id_prosesi", postID)
                    bundle.putString("nama_prosesi", namaPost)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AddGamelanToProsesiAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AddGamelanToProsesiAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerProsesiAddGamelanAdmin.stopShimmer()
        shimmerProsesiAddGamelanAdmin.visibility = View.GONE
        swipeProsesiAddGamelanAdmin.visibility   = View.VISIBLE
    }
}
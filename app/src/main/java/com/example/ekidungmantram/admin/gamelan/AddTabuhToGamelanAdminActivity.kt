package com.example.ekidungmantram.admin.gamelan

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
import com.example.ekidungmantram.adapter.admin.AllTabuhNotOnGamelanAdminAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AllTabuhAdminModel
import com.example.ekidungmantram.model.adminmodel.CrudModel
import kotlinx.android.synthetic.main.activity_add_tabuh_to_gamelan_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddTabuhToGamelanAdminActivity : AppCompatActivity() {
    private lateinit var tabuhAdapter : AllTabuhNotOnGamelanAdminAdapter
    private lateinit var setAdapter   : AllTabuhNotOnGamelanAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tabuh_to_gamelan_admin)
        supportActionBar!!.title = "Daftar Semua Tabuh"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_gamelan")
            val namaPost = bundle.getString("nama_gamelan")

            allAddGamelanTabuhAdmin1.layoutManager = LinearLayoutManager(applicationContext)
            allAddGamelanTabuhAdmin2.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataTabuhGamelan(postID, namaPost!!)

            swipeGamelanAddTabuhAdmin.setOnRefreshListener {
                getAllDataTabuhGamelan(postID, namaPost!!)
                swipeGamelanAddTabuhAdmin.isRefreshing = false
            }
        }
    }

    private fun getAllDataTabuhGamelan(postID: Int, name: String) {
        ApiService.endpoint.getDetailAllTabuhNotOnGamelanAdmin(postID)
            .enqueue(object: Callback<ArrayList<AllTabuhAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllTabuhAdminModel>>,
                    response: Response<ArrayList<AllTabuhAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null){
                        swipeGamelanAddTabuhAdmin.visibility   = View.VISIBLE
                        shimmerGamelanAddTabuhAdmin.visibility = View.GONE
                    }else{
                        swipeGamelanAddTabuhAdmin.visibility   = View.GONE
                        shimmerGamelanAddTabuhAdmin.visibility = View.VISIBLE
                    }
                    setAdapter = AllTabuhNotOnGamelanAdminAdapter(datalist!!)
                    setAdapter.setOnClickAdd {
                        val builder = AlertDialog.Builder(this@AddTabuhToGamelanAdminActivity)
                        builder.setTitle("Tambah Tabuh")
                            .setMessage("Apakah anda yakin ingin menambahkan tabuh ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                addTabuhGamelan(postID, it.id_post, name)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }

                    allAddGamelanTabuhAdmin1.adapter  = setAdapter
                    noGamelanAddTabuhAdmin.visibility = View.GONE
                    setShimmerToStop()

                    cariGamelanAddTabuhAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noGamelanAddTabuhAdmin.visibility   = View.GONE
                                    allAddGamelanTabuhAdmin1.visibility = View.VISIBLE
                                    allAddGamelanTabuhAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    tabuhAdapter = AllTabuhNotOnGamelanAdminAdapter(filter as ArrayList<AllTabuhAdminModel>)
                                    tabuhAdapter.setOnClickAdd {
                                        val builder = AlertDialog.Builder(this@AddTabuhToGamelanAdminActivity)
                                        builder.setTitle("Tambah Tabuh")
                                            .setMessage("Apakah anda yakin ingin menambahkan tabuh ini?")
                                            .setCancelable(true)
                                            .setPositiveButton("Iya") { _, _ ->
                                                addTabuhGamelan(postID, it.id_post, name)
                                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                                dialogInterface.cancel()
                                            }.show()
                                    }
                                    if(filter.isEmpty()){
                                        noGamelanAddTabuhAdmin.visibility   = View.VISIBLE
                                        allAddGamelanTabuhAdmin1.visibility = View.GONE
                                        allAddGamelanTabuhAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noGamelanAddTabuhAdmin.visibility   = View.GONE
                                        allAddGamelanTabuhAdmin2.visibility = View.VISIBLE
                                        allAddGamelanTabuhAdmin2.adapter    = tabuhAdapter
                                        allAddGamelanTabuhAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allAddGamelanTabuhAdmin1.visibility = View.VISIBLE
                                        allAddGamelanTabuhAdmin2.visibility = View.GONE
                                        noGamelanAddTabuhAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }
                    })
                }

                override fun onFailure(call: Call<ArrayList<AllTabuhAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun addTabuhGamelan(postID: Int, idTabuh: Int, name: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menambahkan Data")
        progressDialog.show()
        ApiService.endpoint.addDataTabuhToGamelanAdmin(postID, idTabuh).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AddTabuhToGamelanAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AddTabuhToGamelanAdminActivity, DetailGamelanAdminActivity::class.java)
                    bundle.putInt("id_gamelan", postID)
                    bundle.putString("nama_gamelan", name)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AddTabuhToGamelanAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AddTabuhToGamelanAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerGamelanAddTabuhAdmin.stopShimmer()
        shimmerGamelanAddTabuhAdmin.visibility = View.GONE
        swipeGamelanAddTabuhAdmin.visibility   = View.VISIBLE
    }
}
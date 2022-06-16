package com.example.ekidungmantram.admin.tari

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
import com.example.ekidungmantram.adapter.admin.AllTabuhNotOnTariAdminAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AllTabuhAdminModel
import com.example.ekidungmantram.model.adminmodel.CrudModel
import kotlinx.android.synthetic.main.activity_add_tabuh_to_tari_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddTabuhToTariAdminActivity : AppCompatActivity() {
    private lateinit var tabuhAdapter : AllTabuhNotOnTariAdminAdapter
    private lateinit var setAdapter   : AllTabuhNotOnTariAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tabuh_to_tari_admin)
        supportActionBar!!.title = "Daftar Semua Tabuh"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_tari")
            val namaPost = bundle.getString("nama_tari")

            allAddTariTabuhAdmin1.layoutManager = LinearLayoutManager(applicationContext)
            allAddTariTabuhAdmin2.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataTabuhTari(postID, namaPost!!)

            swipeTariAddTabuhAdmin.setOnRefreshListener {
                getAllDataTabuhTari(postID, namaPost!!)
                swipeTariAddTabuhAdmin.isRefreshing = false
            }
        }
    }

    private fun getAllDataTabuhTari(postID: Int, namaPost: String) {
        ApiService.endpoint.getDetailAllTabuhNotOnTariAdmin(postID)
            .enqueue(object: Callback<ArrayList<AllTabuhAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllTabuhAdminModel>>,
                    response: Response<ArrayList<AllTabuhAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null){
                        swipeTariAddTabuhAdmin.visibility   = View.VISIBLE
                        shimmerTariAddTabuhAdmin.visibility = View.GONE
                    }else{
                        swipeTariAddTabuhAdmin.visibility   = View.GONE
                        shimmerTariAddTabuhAdmin.visibility = View.VISIBLE
                    }
                    setAdapter = AllTabuhNotOnTariAdminAdapter(datalist!!)
                    setAdapter.setOnClickAdd {
                        val builder = AlertDialog.Builder(this@AddTabuhToTariAdminActivity)
                        builder.setTitle("Tambah Tabuh")
                            .setMessage("Apakah anda yakin ingin menambahkan tabuh ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                addTabuhTari(postID, it.id_post, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }

                    allAddTariTabuhAdmin1.adapter  = setAdapter
                    noTariAddTabuhAdmin.visibility = View.GONE
                    setShimmerToStop()

                    cariTariAddTabuhAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noTariAddTabuhAdmin.visibility   = View.GONE
                                    allAddTariTabuhAdmin1.visibility = View.VISIBLE
                                    allAddTariTabuhAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    tabuhAdapter = AllTabuhNotOnTariAdminAdapter(filter as ArrayList<AllTabuhAdminModel>)
                                    tabuhAdapter.setOnClickAdd {
                                        val builder = AlertDialog.Builder(this@AddTabuhToTariAdminActivity)
                                        builder.setTitle("Tambah Tabuh")
                                            .setMessage("Apakah anda yakin ingin menambahkan tabuh ini?")
                                            .setCancelable(true)
                                            .setPositiveButton("Iya") { _, _ ->
                                                addTabuhTari(postID, it.id_post, namaPost)
                                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                                dialogInterface.cancel()
                                            }.show()
                                    }
                                    if(filter.isEmpty()){
                                        noTariAddTabuhAdmin.visibility   = View.VISIBLE
                                        allAddTariTabuhAdmin1.visibility = View.GONE
                                        allAddTariTabuhAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noTariAddTabuhAdmin.visibility   = View.GONE
                                        allAddTariTabuhAdmin2.visibility = View.VISIBLE
                                        allAddTariTabuhAdmin2.adapter    = tabuhAdapter
                                        allAddTariTabuhAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allAddTariTabuhAdmin1.visibility = View.VISIBLE
                                        allAddTariTabuhAdmin2.visibility = View.GONE
                                        noTariAddTabuhAdmin.visibility   = View.GONE
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

    private fun addTabuhTari(postID: Int, idTabuh: Int, namaPost: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menambahkan Data")
        progressDialog.show()
        ApiService.endpoint.addDataTabuhToTariAdmin(postID, idTabuh).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AddTabuhToTariAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AddTabuhToTariAdminActivity, DetailTariAdminActivity::class.java)
                    bundle.putInt("id_tari", postID)
                    bundle.putString("nama_tari", namaPost)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AddTabuhToTariAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AddTabuhToTariAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerTariAddTabuhAdmin.stopShimmer()
        shimmerTariAddTabuhAdmin.visibility = View.GONE
        swipeTariAddTabuhAdmin.visibility   = View.VISIBLE
    }
}
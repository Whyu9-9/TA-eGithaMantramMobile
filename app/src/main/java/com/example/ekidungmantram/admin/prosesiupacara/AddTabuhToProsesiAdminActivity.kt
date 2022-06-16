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
import com.example.ekidungmantram.adapter.admin.AllKidungNotOnProsesiAdminAdapter
import com.example.ekidungmantram.adapter.admin.AllTabuhNotOnProsesiAdminAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AllKidungAdminModel
import com.example.ekidungmantram.model.adminmodel.AllTabuhAdminModel
import com.example.ekidungmantram.model.adminmodel.CrudModel
import kotlinx.android.synthetic.main.activity_add_kidung_to_prosesi_admin.*
import kotlinx.android.synthetic.main.activity_add_tabuh_to_prosesi_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddTabuhToProsesiAdminActivity : AppCompatActivity() {
    private lateinit var tabuhAdapter : AllTabuhNotOnProsesiAdminAdapter
    private lateinit var setAdapter   : AllTabuhNotOnProsesiAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tabuh_to_prosesi_admin)
        supportActionBar!!.title = "Daftar Semua Tabuh"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_prosesi")
            val namaPost = bundle.getString("nama_prosesi")

            allAddProsesiTabuhAdmin1.layoutManager = LinearLayoutManager(applicationContext)
            allAddProsesiTabuhAdmin2.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataTabuhProsesi(postID, namaPost!!)

            swipeProsesiAddTabuhAdmin.setOnRefreshListener {
                getAllDataTabuhProsesi(postID, namaPost!!)
                swipeProsesiAddTabuhAdmin.isRefreshing = false
            }
        }
    }

    private fun getAllDataTabuhProsesi(postID: Int, namaPost: String) {
        ApiService.endpoint.getDetailAllTabuhNotOnProsesiAdmin(postID)
            .enqueue(object: Callback<ArrayList<AllTabuhAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllTabuhAdminModel>>,
                    response: Response<ArrayList<AllTabuhAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null && datalist.isNotEmpty()){
                        swipeProsesiAddTabuhAdmin.visibility   = View.VISIBLE
                        shimmerProsesiAddTabuhAdmin.visibility = View.GONE
                        noProsesiAddTabuhAdmin.visibility      = View.GONE
                    }else{
                        swipeProsesiAddTabuhAdmin.visibility   = View.GONE
                        shimmerProsesiAddTabuhAdmin.visibility = View.VISIBLE
                        noProsesiAddTabuhAdmin.visibility      = View.VISIBLE
                    }
                    setAdapter = AllTabuhNotOnProsesiAdminAdapter(datalist!!)
                    setAdapter.setOnClickAdd {
                        val builder = AlertDialog.Builder(this@AddTabuhToProsesiAdminActivity)
                        builder.setTitle("Tambah Tabuh")
                            .setMessage("Apakah anda yakin ingin menambahkan tabuh ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                addTabuhProsesi(postID, it.id_post, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }

                    allAddProsesiTabuhAdmin1.adapter  = setAdapter
                    setShimmerToStop()

                    cariProsesiAddTabuhAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noProsesiAddTabuhAdmin.visibility   = View.GONE
                                    allAddProsesiTabuhAdmin1.visibility = View.VISIBLE
                                    allAddProsesiTabuhAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    tabuhAdapter = AllTabuhNotOnProsesiAdminAdapter(filter as ArrayList<AllTabuhAdminModel>)
                                    tabuhAdapter.setOnClickAdd {
                                        val builder = AlertDialog.Builder(this@AddTabuhToProsesiAdminActivity)
                                        builder.setTitle("Tambah Tabuh")
                                            .setMessage("Apakah anda yakin ingin menambahkan tabuh ini?")
                                            .setCancelable(true)
                                            .setPositiveButton("Iya") { _, _ ->
                                                addTabuhProsesi(postID, it.id_post, namaPost)
                                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                                dialogInterface.cancel()
                                            }.show()
                                    }
                                    if(filter.isEmpty()){
                                        noProsesiAddTabuhAdmin.visibility   = View.VISIBLE
                                        allAddProsesiTabuhAdmin1.visibility = View.GONE
                                        allAddProsesiTabuhAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noProsesiAddTabuhAdmin.visibility   = View.GONE
                                        allAddProsesiTabuhAdmin2.visibility = View.VISIBLE
                                        allAddProsesiTabuhAdmin2.adapter    = tabuhAdapter
                                        allAddProsesiTabuhAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allAddProsesiTabuhAdmin1.visibility = View.VISIBLE
                                        allAddProsesiTabuhAdmin2.visibility = View.GONE
                                        noProsesiAddTabuhAdmin.visibility   = View.GONE
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

    private fun addTabuhProsesi(postID: Int, idTabuh: Int, namaPost: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menambahkan Data")
        progressDialog.show()
        ApiService.endpoint.addDataTabuhToProsesiAdmin(postID, idTabuh).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AddTabuhToProsesiAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AddTabuhToProsesiAdminActivity, DetailProsesiAdminActivity::class.java)
                    bundle.putInt("id_prosesi", postID)
                    bundle.putString("nama_prosesi", namaPost)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AddTabuhToProsesiAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AddTabuhToProsesiAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerProsesiAddTabuhAdmin.stopShimmer()
        shimmerProsesiAddTabuhAdmin.visibility = View.GONE
        swipeProsesiAddTabuhAdmin.visibility   = View.VISIBLE
    }
}
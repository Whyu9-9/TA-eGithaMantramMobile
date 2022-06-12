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
import com.example.ekidungmantram.adapter.admin.AllProsesiAwalNotOnYadnyaAdminAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AllProsesiAdminModel
import com.example.ekidungmantram.model.adminmodel.CrudModel
import kotlinx.android.synthetic.main.activity_add_prosesi_awal_to_yadnya_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddProsesiAwalToYadnyaAdminActivity : AppCompatActivity() {
    private lateinit var prosesiAdapter : AllProsesiAwalNotOnYadnyaAdminAdapter
    private lateinit var setAdapter     : AllProsesiAwalNotOnYadnyaAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_prosesi_awal_to_yadnya_admin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Daftar Semua Prosesi"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val katID = bundle.getInt("id_kategori")
            val postID = bundle.getInt("id_yadnya")
            val namaPost = bundle.getString("nama_yadnya")

            allAddYadnyaProsesiAwalAdmin1.layoutManager = LinearLayoutManager(applicationContext)
            allAddYadnyaProsesiAwalAdmin2.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataProsesi(katID, postID, namaPost!!)

            swipeYadnyaAddProsesiAwalAdmin.setOnRefreshListener {
                getAllDataProsesi(katID, postID, namaPost!!)
                swipeYadnyaAddProsesiAwalAdmin.isRefreshing = false
            }
        }
    }

    private fun getAllDataProsesi(katID: Int, postID: Int, namaPost: String) {
        ApiService.endpoint.getDetailAllProsesiAwalNotOnYadnyaAdmin(postID)
            .enqueue(object: Callback<ArrayList<AllProsesiAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllProsesiAdminModel>>,
                    response: Response<ArrayList<AllProsesiAdminModel>>
                ) {
                    val datalist = response.body()
                    if(datalist != null && datalist.isNotEmpty()){
                        swipeYadnyaAddProsesiAwalAdmin.visibility   = View.VISIBLE
                        shimmerYadnyaAddProsesiAwalAdmin.visibility = View.GONE
                        noYadnyaAddProsesiAwalAdmin.visibility      = View.GONE
                    }else{
                        swipeYadnyaAddProsesiAwalAdmin.visibility   = View.GONE
                        shimmerYadnyaAddProsesiAwalAdmin.visibility = View.VISIBLE
                        noYadnyaAddProsesiAwalAdmin.visibility      = View.VISIBLE
                    }
                    setAdapter = AllProsesiAwalNotOnYadnyaAdminAdapter(datalist!!)
                    setAdapter.setOnClickAdd {
                        val builder = AlertDialog.Builder(this@AddProsesiAwalToYadnyaAdminActivity)
                        builder.setTitle("Tambah Prosesi Awal")
                            .setMessage("Apakah anda yakin ingin menambahkan prosesi ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                addProsesiAwal(katID, postID, it.id_post, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }

                    allAddYadnyaProsesiAwalAdmin1.adapter  = setAdapter
                    setShimmerToStop()

                    cariYadnyaAddProsesiAwalAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noYadnyaAddProsesiAwalAdmin.visibility   = View.GONE
                                    allAddYadnyaProsesiAwalAdmin1.visibility = View.VISIBLE
                                    allAddYadnyaProsesiAwalAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    prosesiAdapter = AllProsesiAwalNotOnYadnyaAdminAdapter(filter as ArrayList<AllProsesiAdminModel>)
                                    prosesiAdapter.setOnClickAdd {
                                        val builder = AlertDialog.Builder(this@AddProsesiAwalToYadnyaAdminActivity)
                                        builder.setTitle("Tambah Prosesi Awal")
                                            .setMessage("Apakah anda yakin ingin menambahkan prosesi ini?")
                                            .setCancelable(true)
                                            .setPositiveButton("Iya") { _, _ ->
                                                addProsesiAwal(katID, postID, it.id_post, namaPost)
                                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                                dialogInterface.cancel()
                                            }.show()
                                    }
                                    if(filter.isEmpty()){
                                        noYadnyaAddProsesiAwalAdmin.visibility   = View.VISIBLE
                                        allAddYadnyaProsesiAwalAdmin1.visibility = View.GONE
                                        allAddYadnyaProsesiAwalAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noYadnyaAddProsesiAwalAdmin.visibility   = View.GONE
                                        allAddYadnyaProsesiAwalAdmin2.visibility = View.VISIBLE
                                        allAddYadnyaProsesiAwalAdmin2.adapter    = prosesiAdapter
                                        allAddYadnyaProsesiAwalAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allAddYadnyaProsesiAwalAdmin1.visibility = View.VISIBLE
                                        allAddYadnyaProsesiAwalAdmin2.visibility = View.GONE
                                        noYadnyaAddProsesiAwalAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }
                    })
                }

                override fun onFailure(call: Call<ArrayList<AllProsesiAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun addProsesiAwal(katID: Int, postID: Int, idProsesi: Int, namaPost: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menambahkan Data")
        progressDialog.show()
        ApiService.endpoint.addDataProsesiAwalToYadnyaAdmin(postID, idProsesi).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AddProsesiAwalToYadnyaAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AddProsesiAwalToYadnyaAdminActivity, DetailYadnyaAdminActivity::class.java)
                    bundle.putInt("id_kategori", katID)
                    bundle.putInt("id_yadnya", postID)
                    bundle.putString("nama_yadnya", namaPost)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AddProsesiAwalToYadnyaAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AddProsesiAwalToYadnyaAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }


    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerYadnyaAddProsesiAwalAdmin.stopShimmer()
        shimmerYadnyaAddProsesiAwalAdmin.visibility = View.GONE
        swipeYadnyaAddProsesiAwalAdmin.visibility   = View.VISIBLE
    }
}
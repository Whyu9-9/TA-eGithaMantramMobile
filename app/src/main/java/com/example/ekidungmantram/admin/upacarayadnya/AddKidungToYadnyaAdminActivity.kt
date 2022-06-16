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
import com.example.ekidungmantram.adapter.admin.AllKidungNotOnYadnyaAdminAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AllKidungAdminModel
import com.example.ekidungmantram.model.adminmodel.CrudModel
import kotlinx.android.synthetic.main.activity_add_kidung_to_yadnya_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddKidungToYadnyaAdminActivity : AppCompatActivity() {
    private lateinit var kidungAdapter : AllKidungNotOnYadnyaAdminAdapter
    private lateinit var setAdapter    : AllKidungNotOnYadnyaAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_kidung_to_yadnya_admin)
        supportActionBar!!.title = "Daftar Semua Kidung"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val katID = bundle.getInt("id_kategori")
            val postID = bundle.getInt("id_yadnya")
            val namaPost = bundle.getString("nama_yadnya")

            allAddYadnyaKidungAdmin1.layoutManager = LinearLayoutManager(applicationContext)
            allAddYadnyaKidungAdmin2.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataKidungYadnya(katID, postID, namaPost!!)

            swipeYadnyaAddKidungAdmin.setOnRefreshListener {
                getAllDataKidungYadnya(katID, postID, namaPost!!)
                swipeYadnyaAddKidungAdmin.isRefreshing = false
            }
        }
    }

    private fun getAllDataKidungYadnya(katID: Int, postID: Int, namaPost: String) {
        ApiService.endpoint.getDetailAllKidungNotOnYadnyaAdmin(postID)
            .enqueue(object: Callback<ArrayList<AllKidungAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllKidungAdminModel>>,
                    response: Response<ArrayList<AllKidungAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null && datalist.isNotEmpty()){
                        swipeYadnyaAddKidungAdmin.visibility   = View.VISIBLE
                        shimmerYadnyaAddKidungAdmin.visibility = View.GONE
                        noYadnyaAddKidungAdmin.visibility      = View.GONE
                    }else{
                        swipeYadnyaAddKidungAdmin.visibility   = View.GONE
                        shimmerYadnyaAddKidungAdmin.visibility = View.VISIBLE
                        noYadnyaAddKidungAdmin.visibility      = View.VISIBLE
                    }
                    setAdapter = AllKidungNotOnYadnyaAdminAdapter(datalist!!)
                    setAdapter.setOnClickAdd {
                        val builder = AlertDialog.Builder(this@AddKidungToYadnyaAdminActivity)
                        builder.setTitle("Tambah Kidung")
                            .setMessage("Apakah anda yakin ingin menambahkan kidung ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                addKidungYadnya(katID, postID, it.id_post, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }

                    allAddYadnyaKidungAdmin1.adapter  = setAdapter
                    setShimmerToStop()

                    cariYadnyaAddKidungAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noYadnyaAddKidungAdmin.visibility   = View.GONE
                                    allAddYadnyaKidungAdmin1.visibility = View.VISIBLE
                                    allAddYadnyaKidungAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    kidungAdapter = AllKidungNotOnYadnyaAdminAdapter(filter as ArrayList<AllKidungAdminModel>)
                                    kidungAdapter.setOnClickAdd {
                                        val builder = AlertDialog.Builder(this@AddKidungToYadnyaAdminActivity)
                                        builder.setTitle("Tambah Kidung")
                                            .setMessage("Apakah anda yakin ingin menambahkan kidung ini?")
                                            .setCancelable(true)
                                            .setPositiveButton("Iya") { _, _ ->
                                                addKidungYadnya(katID, postID, it.id_post, namaPost)
                                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                                dialogInterface.cancel()
                                            }.show()
                                    }
                                    if(filter.isEmpty()){
                                        noYadnyaAddKidungAdmin.visibility   = View.VISIBLE
                                        allAddYadnyaKidungAdmin1.visibility = View.GONE
                                        allAddYadnyaKidungAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noYadnyaAddKidungAdmin.visibility   = View.GONE
                                        allAddYadnyaKidungAdmin2.visibility = View.VISIBLE
                                        allAddYadnyaKidungAdmin2.adapter    = kidungAdapter
                                        allAddYadnyaKidungAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allAddYadnyaKidungAdmin1.visibility = View.VISIBLE
                                        allAddYadnyaKidungAdmin2.visibility = View.GONE
                                        noYadnyaAddKidungAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }
                    })
                }

                override fun onFailure(call: Call<ArrayList<AllKidungAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun addKidungYadnya(katID: Int, postID: Int, idKidung: Int, namaPost: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menambahkan Data")
        progressDialog.show()
        ApiService.endpoint.addDataKidungToYadnyaAdmin(postID, idKidung).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AddKidungToYadnyaAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AddKidungToYadnyaAdminActivity, DetailYadnyaAdminActivity::class.java)
                    bundle.putInt("id_kategori", katID)
                    bundle.putInt("id_yadnya", postID)
                    bundle.putString("nama_yadnya", namaPost)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AddKidungToYadnyaAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AddKidungToYadnyaAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerYadnyaAddKidungAdmin.stopShimmer()
        shimmerYadnyaAddKidungAdmin.visibility = View.GONE
        swipeYadnyaAddKidungAdmin.visibility   = View.VISIBLE
    }
}
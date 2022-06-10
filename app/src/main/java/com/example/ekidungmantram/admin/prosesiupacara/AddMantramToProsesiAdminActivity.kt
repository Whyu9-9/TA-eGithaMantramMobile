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
import com.example.ekidungmantram.adapter.admin.AllMantramNotOnProsesiAdminAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AllMantramAdminModel
import com.example.ekidungmantram.model.adminmodel.CrudModel
import kotlinx.android.synthetic.main.activity_add_kidung_to_prosesi_admin.*
import kotlinx.android.synthetic.main.activity_add_mantram_to_prosesi_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddMantramToProsesiAdminActivity : AppCompatActivity() {
    private lateinit var mantramAdapter : AllMantramNotOnProsesiAdminAdapter
    private lateinit var setAdapter     : AllMantramNotOnProsesiAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_mantram_to_prosesi_admin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "List Semua Mantram"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_prosesi")
            val namaPost = bundle.getString("nama_prosesi")

            allAddProsesiMantramAdmin1.layoutManager = LinearLayoutManager(applicationContext)
            allAddProsesiMantramAdmin2.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataMantramProsesi(postID, namaPost!!)

            swipeProsesiAddMantramAdmin.setOnRefreshListener {
                getAllDataMantramProsesi(postID, namaPost!!)
                swipeProsesiAddMantramAdmin.isRefreshing = false
            }
        }
    }

    private fun getAllDataMantramProsesi(postID: Int, namaPost: String) {
        ApiService.endpoint.getDetailAllMantramNotOnProsesiAdmin(postID)
            .enqueue(object: Callback<ArrayList<AllMantramAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllMantramAdminModel>>,
                    response: Response<ArrayList<AllMantramAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null && datalist.isNotEmpty()){
                        swipeProsesiAddMantramAdmin.visibility   = View.VISIBLE
                        shimmerProsesiAddMantramAdmin.visibility = View.GONE
                        noProsesiAddMantramAdmin.visibility      = View.GONE
                    }else{
                        swipeProsesiAddMantramAdmin.visibility   = View.GONE
                        shimmerProsesiAddMantramAdmin.visibility = View.VISIBLE
                        noProsesiAddMantramAdmin.visibility      = View.VISIBLE
                    }
                    setAdapter = AllMantramNotOnProsesiAdminAdapter(datalist!!)
                    setAdapter.setOnClickAdd {
                        val builder = AlertDialog.Builder(this@AddMantramToProsesiAdminActivity)
                        builder.setTitle("Tambah Mantram")
                            .setMessage("Apakah anda yakin ingin menambahkan mantram ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                addMantramProsesi(postID, it.id_post, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }

                    allAddProsesiMantramAdmin1.adapter  = setAdapter
                    setShimmerToStop()

                    cariProsesiAddMantramAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noProsesiAddMantramAdmin.visibility   = View.GONE
                                    allAddProsesiMantramAdmin1.visibility = View.VISIBLE
                                    allAddProsesiMantramAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    mantramAdapter = AllMantramNotOnProsesiAdminAdapter(filter as ArrayList<AllMantramAdminModel>)
                                    mantramAdapter.setOnClickAdd {
                                        val builder = AlertDialog.Builder(this@AddMantramToProsesiAdminActivity)
                                        builder.setTitle("Tambah Mantram")
                                            .setMessage("Apakah anda yakin ingin menambahkan mantram ini?")
                                            .setCancelable(true)
                                            .setPositiveButton("Iya") { _, _ ->
                                                addMantramProsesi(postID, it.id_post, namaPost)
                                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                                dialogInterface.cancel()
                                            }.show()
                                    }
                                    if(filter.isEmpty()){
                                        noProsesiAddMantramAdmin.visibility   = View.VISIBLE
                                        allAddProsesiMantramAdmin1.visibility = View.GONE
                                        allAddProsesiMantramAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noProsesiAddMantramAdmin.visibility   = View.GONE
                                        allAddProsesiMantramAdmin2.visibility = View.VISIBLE
                                        allAddProsesiMantramAdmin2.adapter    = mantramAdapter
                                        allAddProsesiMantramAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allAddProsesiMantramAdmin1.visibility = View.VISIBLE
                                        allAddProsesiMantramAdmin2.visibility = View.GONE
                                        noProsesiAddMantramAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }
                    })
                }

                override fun onFailure(call: Call<ArrayList<AllMantramAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun addMantramProsesi(postID: Int, idMantram: Int, namaPost: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menambahkan Data")
        progressDialog.show()
        ApiService.endpoint.addDataMantramToProsesiAdmin(postID, idMantram).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AddMantramToProsesiAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AddMantramToProsesiAdminActivity, DetailProsesiAdminActivity::class.java)
                    bundle.putInt("id_prosesi", postID)
                    bundle.putString("nama_prosesi", namaPost)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AddMantramToProsesiAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AddMantramToProsesiAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerProsesiAddMantramAdmin.stopShimmer()
        shimmerProsesiAddMantramAdmin.visibility = View.GONE
        swipeProsesiAddMantramAdmin.visibility   = View.VISIBLE
    }
}
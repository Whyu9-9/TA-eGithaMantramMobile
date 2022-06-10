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
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AllKidungAdminModel
import com.example.ekidungmantram.model.adminmodel.CrudModel
import kotlinx.android.synthetic.main.activity_add_kidung_to_prosesi_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddKidungToProsesiAdminActivity : AppCompatActivity() {
    private lateinit var kidungAdapter : AllKidungNotOnProsesiAdminAdapter
    private lateinit var setAdapter    : AllKidungNotOnProsesiAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_kidung_to_prosesi_admin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "List Semua Kidung"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_prosesi")
            val namaPost = bundle.getString("nama_prosesi")

            allAddProsesiKidungAdmin1.layoutManager = LinearLayoutManager(applicationContext)
            allAddProsesiKidungAdmin2.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataKidungProsesi(postID, namaPost!!)

            swipeProsesiAddKidungAdmin.setOnRefreshListener {
                getAllDataKidungProsesi(postID, namaPost!!)
                swipeProsesiAddKidungAdmin.isRefreshing = false
            }
        }
    }

    private fun getAllDataKidungProsesi(postID: Int, namaPost: String) {
        ApiService.endpoint.getDetailAllKidungNotOnProsesiAdmin(postID)
            .enqueue(object: Callback<ArrayList<AllKidungAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllKidungAdminModel>>,
                    response: Response<ArrayList<AllKidungAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null){
                        swipeProsesiAddKidungAdmin.visibility   = View.VISIBLE
                        shimmerProsesiAddKidungAdmin.visibility = View.GONE
                    }else{
                        swipeProsesiAddKidungAdmin.visibility   = View.GONE
                        shimmerProsesiAddKidungAdmin.visibility = View.VISIBLE
                    }
                    setAdapter = AllKidungNotOnProsesiAdminAdapter(datalist!!)
                    setAdapter.setOnClickAdd {
                        val builder = AlertDialog.Builder(this@AddKidungToProsesiAdminActivity)
                        builder.setTitle("Tambah Kidung")
                            .setMessage("Apakah anda yakin ingin menambahkan kidung ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                addKidungProsesi(postID, it.id_post, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }

                    allAddProsesiKidungAdmin1.adapter  = setAdapter
                    noProsesiAddKidungAdmin.visibility = View.GONE
                    setShimmerToStop()

                    cariProsesiAddKidungAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noProsesiAddKidungAdmin.visibility   = View.GONE
                                    allAddProsesiKidungAdmin1.visibility = View.VISIBLE
                                    allAddProsesiKidungAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    kidungAdapter = AllKidungNotOnProsesiAdminAdapter(filter as ArrayList<AllKidungAdminModel>)
                                    kidungAdapter.setOnClickAdd {
                                        val builder = AlertDialog.Builder(this@AddKidungToProsesiAdminActivity)
                                        builder.setTitle("Tambah Kidung")
                                            .setMessage("Apakah anda yakin ingin menambahkan kidung ini?")
                                            .setCancelable(true)
                                            .setPositiveButton("Iya") { _, _ ->
                                                addKidungProsesi(postID, it.id_post, namaPost)
                                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                                dialogInterface.cancel()
                                            }.show()
                                    }
                                    if(filter.isEmpty()){
                                        noProsesiAddKidungAdmin.visibility   = View.VISIBLE
                                        allAddProsesiKidungAdmin1.visibility = View.GONE
                                        allAddProsesiKidungAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noProsesiAddKidungAdmin.visibility   = View.GONE
                                        allAddProsesiKidungAdmin2.visibility = View.VISIBLE
                                        allAddProsesiKidungAdmin2.adapter    = kidungAdapter
                                        allAddProsesiKidungAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allAddProsesiKidungAdmin1.visibility = View.VISIBLE
                                        allAddProsesiKidungAdmin2.visibility = View.GONE
                                        noProsesiAddKidungAdmin.visibility   = View.GONE
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

    private fun addKidungProsesi(postID: Int, idKidung: Int, namaPost: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menambahkan Data")
        progressDialog.show()
        ApiService.endpoint.addDataKidungToProsesiAdmin(postID, idKidung).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AddKidungToProsesiAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AddKidungToProsesiAdminActivity, DetailProsesiAdminActivity::class.java)
                    bundle.putInt("id_prosesi", postID)
                    bundle.putString("nama_prosesi", namaPost)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AddKidungToProsesiAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AddKidungToProsesiAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerProsesiAddKidungAdmin.stopShimmer()
        shimmerProsesiAddKidungAdmin.visibility = View.GONE
        swipeProsesiAddKidungAdmin.visibility   = View.VISIBLE
    }
}
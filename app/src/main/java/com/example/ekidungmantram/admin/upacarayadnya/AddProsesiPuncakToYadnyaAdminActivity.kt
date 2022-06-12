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
import com.example.ekidungmantram.adapter.admin.AllProsesiPuncakNotOnYadnyaAdminAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AllProsesiAdminModel
import com.example.ekidungmantram.model.adminmodel.CrudModel
import kotlinx.android.synthetic.main.activity_add_prosesi_awal_to_yadnya_admin.*
import kotlinx.android.synthetic.main.activity_add_prosesi_puncak_to_yadnya_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddProsesiPuncakToYadnyaAdminActivity : AppCompatActivity() {
    private lateinit var prosesiAdapter : AllProsesiPuncakNotOnYadnyaAdminAdapter
    private lateinit var setAdapter     : AllProsesiPuncakNotOnYadnyaAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_prosesi_puncak_to_yadnya_admin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Daftar Semua Prosesi"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val katID = bundle.getInt("id_kategori")
            val postID = bundle.getInt("id_yadnya")
            val namaPost = bundle.getString("nama_yadnya")

            allAddYadnyaProsesiPuncakAdmin1.layoutManager = LinearLayoutManager(applicationContext)
            allAddYadnyaProsesiPuncakAdmin2.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataProsesi(katID, postID, namaPost!!)

            swipeYadnyaAddProsesiPuncakAdmin.setOnRefreshListener {
                getAllDataProsesi(katID, postID, namaPost!!)
                swipeYadnyaAddProsesiPuncakAdmin.isRefreshing = false
            }
        }
    }

    private fun getAllDataProsesi(katID: Int, postID: Int, namaPost: String) {
        ApiService.endpoint.getDetailAllProsesiPuncakNotOnYadnyaAdmin(postID)
            .enqueue(object: Callback<ArrayList<AllProsesiAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllProsesiAdminModel>>,
                    response: Response<ArrayList<AllProsesiAdminModel>>
                ) {
                    val datalist = response.body()
                    if(datalist != null && datalist.isNotEmpty()){
                        swipeYadnyaAddProsesiPuncakAdmin.visibility   = View.VISIBLE
                        shimmerYadnyaAddProsesiPuncakAdmin.visibility = View.GONE
                        noYadnyaAddProsesiPuncakAdmin.visibility      = View.GONE
                    }else{
                        swipeYadnyaAddProsesiPuncakAdmin.visibility   = View.GONE
                        shimmerYadnyaAddProsesiPuncakAdmin.visibility = View.VISIBLE
                        noYadnyaAddProsesiPuncakAdmin.visibility      = View.VISIBLE
                    }
                    setAdapter = AllProsesiPuncakNotOnYadnyaAdminAdapter(datalist!!)
                    setAdapter.setOnClickAdd {
                        val builder = AlertDialog.Builder(this@AddProsesiPuncakToYadnyaAdminActivity)
                        builder.setTitle("Tambah Prosesi Puncak")
                            .setMessage("Apakah anda yakin ingin menambahkan prosesi ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                addProsesiPuncak(katID, postID, it.id_post, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }

                    allAddYadnyaProsesiPuncakAdmin1.adapter  = setAdapter
                    setShimmerToStop()

                    cariYadnyaAddProsesiPuncakAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noYadnyaAddProsesiPuncakAdmin.visibility   = View.GONE
                                    allAddYadnyaProsesiPuncakAdmin1.visibility = View.VISIBLE
                                    allAddYadnyaProsesiPuncakAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    prosesiAdapter = AllProsesiPuncakNotOnYadnyaAdminAdapter(filter as ArrayList<AllProsesiAdminModel>)
                                    prosesiAdapter.setOnClickAdd {
                                        val builder = AlertDialog.Builder(this@AddProsesiPuncakToYadnyaAdminActivity)
                                        builder.setTitle("Tambah Prosesi Puncak")
                                            .setMessage("Apakah anda yakin ingin menambahkan prosesi ini?")
                                            .setCancelable(true)
                                            .setPositiveButton("Iya") { _, _ ->
                                                addProsesiPuncak(katID, postID, it.id_post, namaPost)
                                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                                dialogInterface.cancel()
                                            }.show()
                                    }
                                    if(filter.isEmpty()){
                                        noYadnyaAddProsesiPuncakAdmin.visibility   = View.VISIBLE
                                        allAddYadnyaProsesiPuncakAdmin1.visibility = View.GONE
                                        allAddYadnyaProsesiPuncakAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noYadnyaAddProsesiPuncakAdmin.visibility   = View.GONE
                                        allAddYadnyaProsesiPuncakAdmin2.visibility = View.VISIBLE
                                        allAddYadnyaProsesiPuncakAdmin2.adapter    = prosesiAdapter
                                        allAddYadnyaProsesiPuncakAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allAddYadnyaProsesiPuncakAdmin1.visibility = View.VISIBLE
                                        allAddYadnyaProsesiPuncakAdmin2.visibility = View.GONE
                                        noYadnyaAddProsesiPuncakAdmin.visibility   = View.GONE
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

    private fun addProsesiPuncak(katID: Int, postID: Int, idProsesi: Int, namaPost: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menambahkan Data")
        progressDialog.show()
        ApiService.endpoint.addDataProsesiPuncakToYadnyaAdmin(postID, idProsesi).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AddProsesiPuncakToYadnyaAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AddProsesiPuncakToYadnyaAdminActivity, DetailYadnyaAdminActivity::class.java)
                    bundle.putInt("id_kategori", katID)
                    bundle.putInt("id_yadnya", postID)
                    bundle.putString("nama_yadnya", namaPost)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AddProsesiPuncakToYadnyaAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AddProsesiPuncakToYadnyaAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerYadnyaAddProsesiPuncakAdmin.stopShimmer()
        shimmerYadnyaAddProsesiPuncakAdmin.visibility = View.GONE
        swipeYadnyaAddProsesiPuncakAdmin.visibility   = View.VISIBLE
    }
}
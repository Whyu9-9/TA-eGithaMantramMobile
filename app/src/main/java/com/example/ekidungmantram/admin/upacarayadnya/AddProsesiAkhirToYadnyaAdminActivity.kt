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
import com.example.ekidungmantram.adapter.admin.AllProsesiAkhirNotOnYadnyaAdminAdapter
import com.example.ekidungmantram.adapter.admin.AllProsesiAwalNotOnYadnyaAdminAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AllProsesiAdminModel
import com.example.ekidungmantram.model.adminmodel.CrudModel
import kotlinx.android.synthetic.main.activity_add_prosesi_akhir_to_yadnya_admin.*
import kotlinx.android.synthetic.main.activity_add_prosesi_awal_to_yadnya_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddProsesiAkhirToYadnyaAdminActivity : AppCompatActivity() {
    private lateinit var prosesiAdapter : AllProsesiAkhirNotOnYadnyaAdminAdapter
    private lateinit var setAdapter     : AllProsesiAkhirNotOnYadnyaAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_prosesi_akhir_to_yadnya_admin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Daftar Semua Prosesi"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val katID = bundle.getInt("id_kategori")
            val postID = bundle.getInt("id_yadnya")
            val namaPost = bundle.getString("nama_yadnya")

            allAddYadnyaProsesiAkhirAdmin1.layoutManager = LinearLayoutManager(applicationContext)
            allAddYadnyaProsesiAkhirAdmin2.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataProsesi(katID, postID, namaPost!!)

            swipeYadnyaAddProsesiAkhirAdmin.setOnRefreshListener {
                getAllDataProsesi(katID, postID, namaPost!!)
                swipeYadnyaAddProsesiAkhirAdmin.isRefreshing = false
            }
        }
    }

    private fun getAllDataProsesi(katID: Int, postID: Int, namaPost: String) {
        ApiService.endpoint.getDetailAllProsesiAkhirNotOnYadnyaAdmin(postID)
            .enqueue(object: Callback<ArrayList<AllProsesiAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllProsesiAdminModel>>,
                    response: Response<ArrayList<AllProsesiAdminModel>>
                ) {
                    val datalist = response.body()
                    if(datalist != null && datalist.isNotEmpty()){
                        swipeYadnyaAddProsesiAkhirAdmin.visibility   = View.VISIBLE
                        shimmerYadnyaAddProsesiAkhirAdmin.visibility = View.GONE
                        noYadnyaAddProsesiAkhirAdmin.visibility      = View.GONE
                    }else{
                        swipeYadnyaAddProsesiAkhirAdmin.visibility   = View.GONE
                        shimmerYadnyaAddProsesiAkhirAdmin.visibility = View.VISIBLE
                        noYadnyaAddProsesiAkhirAdmin.visibility      = View.VISIBLE
                    }
                    setAdapter = AllProsesiAkhirNotOnYadnyaAdminAdapter(datalist!!)
                    setAdapter.setOnClickAdd {
                        val builder = AlertDialog.Builder(this@AddProsesiAkhirToYadnyaAdminActivity)
                        builder.setTitle("Tambah Prosesi Awal")
                            .setMessage("Apakah anda yakin ingin menambahkan prosesi ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                addProsesiAkhir(katID, postID, it.id_post, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }

                    allAddYadnyaProsesiAkhirAdmin1.adapter  = setAdapter
                    setShimmerToStop()

                    cariYadnyaAddProsesiAkhirAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noYadnyaAddProsesiAkhirAdmin.visibility   = View.GONE
                                    allAddYadnyaProsesiAkhirAdmin1.visibility = View.VISIBLE
                                    allAddYadnyaProsesiAkhirAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    prosesiAdapter = AllProsesiAkhirNotOnYadnyaAdminAdapter(filter as ArrayList<AllProsesiAdminModel>)
                                    prosesiAdapter.setOnClickAdd {
                                        val builder = AlertDialog.Builder(this@AddProsesiAkhirToYadnyaAdminActivity)
                                        builder.setTitle("Tambah Prosesi Awal")
                                            .setMessage("Apakah anda yakin ingin menambahkan prosesi ini?")
                                            .setCancelable(true)
                                            .setPositiveButton("Iya") { _, _ ->
                                                addProsesiAkhir(katID, postID, it.id_post, namaPost)
                                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                                dialogInterface.cancel()
                                            }.show()
                                    }
                                    if(filter.isEmpty()){
                                        noYadnyaAddProsesiAkhirAdmin.visibility   = View.VISIBLE
                                        allAddYadnyaProsesiAkhirAdmin1.visibility = View.GONE
                                        allAddYadnyaProsesiAkhirAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noYadnyaAddProsesiAkhirAdmin.visibility   = View.GONE
                                        allAddYadnyaProsesiAkhirAdmin2.visibility = View.VISIBLE
                                        allAddYadnyaProsesiAkhirAdmin2.adapter    = prosesiAdapter
                                        allAddYadnyaProsesiAkhirAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allAddYadnyaProsesiAkhirAdmin1.visibility = View.VISIBLE
                                        allAddYadnyaProsesiAkhirAdmin2.visibility = View.GONE
                                        noYadnyaAddProsesiAkhirAdmin.visibility   = View.GONE
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

    private fun addProsesiAkhir(katID: Int, postID: Int, idProsesi: Int, namaPost: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menambahkan Data")
        progressDialog.show()
        ApiService.endpoint.addDataProsesiAkhirToYadnyaAdmin(postID, idProsesi).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AddProsesiAkhirToYadnyaAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AddProsesiAkhirToYadnyaAdminActivity, DetailYadnyaAdminActivity::class.java)
                    bundle.putInt("id_kategori", katID)
                    bundle.putInt("id_yadnya", postID)
                    bundle.putString("nama_yadnya", namaPost)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AddProsesiAkhirToYadnyaAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AddProsesiAkhirToYadnyaAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerYadnyaAddProsesiAkhirAdmin.stopShimmer()
        shimmerYadnyaAddProsesiAkhirAdmin.visibility = View.GONE
        swipeYadnyaAddProsesiAkhirAdmin.visibility   = View.VISIBLE
    }
}
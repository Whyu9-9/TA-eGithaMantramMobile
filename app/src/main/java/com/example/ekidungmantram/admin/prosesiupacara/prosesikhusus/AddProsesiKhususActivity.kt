package com.example.ekidungmantram.admin.prosesiupacara.prosesikhusus

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
import com.example.ekidungmantram.adapter.admin.AllProsesiKhususNotYetAdminAdapter
import com.example.ekidungmantram.admin.prosesiupacara.DetailProsesiAdminActivity
import com.example.ekidungmantram.admin.upacarayadnya.DetailYadnyaAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AllProsesiAdminModel
import com.example.ekidungmantram.model.adminmodel.CrudModel
import kotlinx.android.synthetic.main.activity_add_prosesi_akhir_to_yadnya_admin.*
import kotlinx.android.synthetic.main.activity_add_prosesi_khusus.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddProsesiKhususActivity : AppCompatActivity() {
    private lateinit var prosesiAdapter : AllProsesiKhususNotYetAdminAdapter
    private lateinit var setAdapter     : AllProsesiKhususNotYetAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_prosesi_khusus)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Daftar Semua Prosesi"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val katID = bundle.getInt("id_yadnya")
            val postID = bundle.getInt("id_prosesi")
            val namaPost = bundle.getString("nama_prosesi")

            allAddProsesiKhusus1.layoutManager = LinearLayoutManager(applicationContext)
            allAddProsesiKhusus1.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataProsesi(katID, postID, namaPost!!)

            swipeAddProsesiKhususAdmin.setOnRefreshListener {
                getAllDataProsesi(katID, postID, namaPost!!)
                swipeAddProsesiKhususAdmin.isRefreshing = false
            }
        }
    }

    private fun getAllDataProsesi(katID: Int, postID: Int, namaPost: String) {
        ApiService.endpoint.getDetailAllProsesiKhususNotYet(postID, katID)
            .enqueue(object: Callback<ArrayList<AllProsesiAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllProsesiAdminModel>>,
                    response: Response<ArrayList<AllProsesiAdminModel>>
                ) {
                    val datalist = response.body()
                    if(datalist != null && datalist.isNotEmpty()){
                        swipeAddProsesiKhususAdmin.visibility   = View.VISIBLE
                        shimmerAddProsesiKhususAdmin.visibility = View.GONE
                        noAddProsesiKhususAdmin.visibility      = View.GONE
                    }else{
                        swipeAddProsesiKhususAdmin.visibility   = View.GONE
                        shimmerAddProsesiKhususAdmin.visibility = View.VISIBLE
                        noAddProsesiKhususAdmin.visibility      = View.VISIBLE
                    }
                    setAdapter = AllProsesiKhususNotYetAdminAdapter(datalist!!)
                    setAdapter.setOnClickAdd {
                        val builder = AlertDialog.Builder(this@AddProsesiKhususActivity)
                        builder.setTitle("Tambah Prosesi")
                            .setMessage("Apakah anda yakin ingin menambahkan prosesi ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                addProsesiKhususs(katID, postID, it.id_post, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }

                    allAddProsesiKhusus1.adapter  = setAdapter
                    setShimmerToStop()

                    cariProsesiKhususAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noAddProsesiKhususAdmin.visibility   = View.GONE
                                    allAddProsesiKhusus1.visibility = View.VISIBLE
                                    allAddProsesiKhusus2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    prosesiAdapter = AllProsesiKhususNotYetAdminAdapter(filter as ArrayList<AllProsesiAdminModel>)
                                    prosesiAdapter.setOnClickAdd {
                                        val builder = AlertDialog.Builder(this@AddProsesiKhususActivity)
                                        builder.setTitle("Tambah Prosesi")
                                            .setMessage("Apakah anda yakin ingin menambahkan prosesi ini?")
                                            .setCancelable(true)
                                            .setPositiveButton("Iya") { _, _ ->
                                                addProsesiKhususs(katID, postID, it.id_post, namaPost)
                                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                                dialogInterface.cancel()
                                            }.show()
                                    }
                                    if(filter.isEmpty()){
                                        noAddProsesiKhususAdmin.visibility   = View.VISIBLE
                                        allAddProsesiKhusus1.visibility = View.GONE
                                        allAddProsesiKhusus2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noAddProsesiKhususAdmin.visibility   = View.GONE
                                        allAddProsesiKhusus2.visibility = View.VISIBLE
                                        allAddProsesiKhusus2.adapter    = prosesiAdapter
                                        allAddProsesiKhusus1.visibility = View.INVISIBLE
                                    }else{
                                        allAddProsesiKhusus1.visibility = View.VISIBLE
                                        allAddProsesiKhusus2.visibility = View.GONE
                                        noAddProsesiKhususAdmin.visibility   = View.GONE
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

    private fun addProsesiKhususs(katID: Int, postID: Int, idProsesi: Int, namaPost: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menambahkan Data")
        progressDialog.show()
        ApiService.endpoint.addDataProsesiKhusus(postID, katID ,idProsesi).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AddProsesiKhususActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AddProsesiKhususActivity, DetailProsesiAdminActivity::class.java)
                    bundle.putInt("id_yadnya", katID)
                    bundle.putInt("id_prosesi", postID)
                    bundle.putString("nama_prosesi", namaPost)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AddProsesiKhususActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AddProsesiKhususActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerAddProsesiKhususAdmin.stopShimmer()
        shimmerAddProsesiKhususAdmin.visibility = View.GONE
        swipeAddProsesiKhususAdmin.visibility   = View.VISIBLE
    }
}
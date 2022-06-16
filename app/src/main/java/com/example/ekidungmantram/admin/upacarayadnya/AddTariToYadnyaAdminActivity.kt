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
import com.example.ekidungmantram.adapter.admin.AllTariNotOnYadnyaAdminAdapter
import com.example.ekidungmantram.admin.prosesiupacara.DetailProsesiAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AllTariAdminModel
import com.example.ekidungmantram.model.adminmodel.CrudModel
import kotlinx.android.synthetic.main.activity_add_tari_to_yadnya_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddTariToYadnyaAdminActivity : AppCompatActivity() {
    private lateinit var tariAdapter : AllTariNotOnYadnyaAdminAdapter
    private lateinit var setAdapter  : AllTariNotOnYadnyaAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tari_to_yadnya_admin)
        supportActionBar!!.title = "Daftar Semua Tari Bali"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val katID = bundle.getInt("id_kategori")
            val postID = bundle.getInt("id_yadnya")
            val namaPost = bundle.getString("nama_yadnya")

            allAddYadnyaTariAdmin1.layoutManager = LinearLayoutManager(applicationContext)
            allAddYadnyaTariAdmin2.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataTariYadnya(katID, postID, namaPost!!)

            swipeYadnyaAddTariAdmin.setOnRefreshListener {
                getAllDataTariYadnya(katID, postID, namaPost!!)
                swipeYadnyaAddTariAdmin.isRefreshing = false
            }
        }
    }

    private fun getAllDataTariYadnya(katID: Int, postID: Int, namaPost: String) {
        ApiService.endpoint.getDetailAllTariNotOnYadnyaAdmin(postID)
            .enqueue(object: Callback<ArrayList<AllTariAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllTariAdminModel>>,
                    response: Response<ArrayList<AllTariAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null && datalist.isNotEmpty()){
                        swipeYadnyaAddTariAdmin.visibility   = View.VISIBLE
                        shimmerYadnyaAddTariAdmin.visibility = View.GONE
                        noYadnyaAddTariAdmin.visibility      = View.GONE
                    }else{
                        swipeYadnyaAddTariAdmin.visibility   = View.GONE
                        shimmerYadnyaAddTariAdmin.visibility = View.VISIBLE
                        noYadnyaAddTariAdmin.visibility      = View.VISIBLE
                    }
                    setAdapter = AllTariNotOnYadnyaAdminAdapter(datalist!!)
                    setAdapter.setOnClickAdd {
                        val builder = AlertDialog.Builder(this@AddTariToYadnyaAdminActivity)
                        builder.setTitle("Tambah Tari")
                            .setMessage("Apakah anda yakin ingin menambahkan tari ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                addTariYadnya(katID, postID, it.id_post, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }

                    allAddYadnyaTariAdmin1.adapter  = setAdapter
                    setShimmerToStop()

                    cariYadnyaAddTariAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noYadnyaAddTariAdmin.visibility   = View.GONE
                                    allAddYadnyaTariAdmin1.visibility = View.VISIBLE
                                    allAddYadnyaTariAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    tariAdapter = AllTariNotOnYadnyaAdminAdapter(filter as ArrayList<AllTariAdminModel>)
                                    tariAdapter.setOnClickAdd {
                                        val builder = AlertDialog.Builder(this@AddTariToYadnyaAdminActivity)
                                        builder.setTitle("Tambah Tari")
                                            .setMessage("Apakah anda yakin ingin menambahkan tari ini?")
                                            .setCancelable(true)
                                            .setPositiveButton("Iya") { _, _ ->
                                                addTariYadnya(katID, postID, it.id_post, namaPost)
                                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                                dialogInterface.cancel()
                                            }.show()
                                    }
                                    if(filter.isEmpty()){
                                        noYadnyaAddTariAdmin.visibility   = View.VISIBLE
                                        allAddYadnyaTariAdmin1.visibility = View.GONE
                                        allAddYadnyaTariAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noYadnyaAddTariAdmin.visibility   = View.GONE
                                        allAddYadnyaTariAdmin2.visibility = View.VISIBLE
                                        allAddYadnyaTariAdmin2.adapter    = tariAdapter
                                        allAddYadnyaTariAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allAddYadnyaTariAdmin1.visibility = View.VISIBLE
                                        allAddYadnyaTariAdmin2.visibility = View.GONE
                                        noYadnyaAddTariAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }
                    })
                }

                override fun onFailure(call: Call<ArrayList<AllTariAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun addTariYadnya(katID: Int, postID: Int, idTari: Int, namaPost: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menambahkan Data")
        progressDialog.show()
        ApiService.endpoint.addDataTariToYadnyaAdmin(postID, idTari).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AddTariToYadnyaAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AddTariToYadnyaAdminActivity, DetailYadnyaAdminActivity::class.java)
                    bundle.putInt("id_kategori", katID)
                    bundle.putInt("id_yadnya", postID)
                    bundle.putString("nama_yadnya", namaPost)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AddTariToYadnyaAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AddTariToYadnyaAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerYadnyaAddTariAdmin.stopShimmer()
        shimmerYadnyaAddTariAdmin.visibility = View.GONE
        swipeYadnyaAddTariAdmin.visibility   = View.VISIBLE
    }
}
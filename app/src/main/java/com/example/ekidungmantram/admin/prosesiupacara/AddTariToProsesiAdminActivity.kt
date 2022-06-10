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
import com.example.ekidungmantram.adapter.admin.AllGamelanNotOnProsesiAdminAdapter
import com.example.ekidungmantram.adapter.admin.AllTariNotOnProsesiAdminAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AllGamelanAdminModel
import com.example.ekidungmantram.model.adminmodel.AllTariAdminModel
import com.example.ekidungmantram.model.adminmodel.CrudModel
import kotlinx.android.synthetic.main.activity_add_gamelan_to_prosesi_admin.*
import kotlinx.android.synthetic.main.activity_add_tari_to_prosesi_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddTariToProsesiAdminActivity : AppCompatActivity() {
    private lateinit var tariAdapter : AllTariNotOnProsesiAdminAdapter
    private lateinit var setAdapter  : AllTariNotOnProsesiAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tari_to_prosesi_admin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "List Semua Tari Bali"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_prosesi")
            val namaPost = bundle.getString("nama_prosesi")

            allAddProsesiTariAdmin1.layoutManager = LinearLayoutManager(applicationContext)
            allAddProsesiTariAdmin2.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataTariProsesi(postID, namaPost!!)

            swipeProsesiAddTariAdmin.setOnRefreshListener {
                getAllDataTariProsesi(postID, namaPost!!)
                swipeProsesiAddTariAdmin.isRefreshing = false
            }
        }
    }

    private fun getAllDataTariProsesi(postID: Int, namaPost: String) {
        ApiService.endpoint.getDetailAllTariNotOnProsesiAdmin(postID)
            .enqueue(object: Callback<ArrayList<AllTariAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllTariAdminModel>>,
                    response: Response<ArrayList<AllTariAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null){
                        swipeProsesiAddTariAdmin.visibility   = View.VISIBLE
                        shimmerProsesiAddTariAdmin.visibility = View.GONE
                    }else{
                        swipeProsesiAddTariAdmin.visibility   = View.GONE
                        shimmerProsesiAddTariAdmin.visibility = View.VISIBLE
                    }
                    setAdapter = AllTariNotOnProsesiAdminAdapter(datalist!!)
                    setAdapter.setOnClickAdd {
                        val builder = AlertDialog.Builder(this@AddTariToProsesiAdminActivity)
                        builder.setTitle("Tambah Tari")
                            .setMessage("Apakah anda yakin ingin menambahkan tari ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                addTariProsesi(postID, it.id_post, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }

                    allAddProsesiTariAdmin1.adapter  = setAdapter
                    noProsesiAddTariAdmin.visibility = View.GONE
                    setShimmerToStop()

                    cariProsesiAddTariAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noProsesiAddTariAdmin.visibility   = View.GONE
                                    allAddProsesiTariAdmin1.visibility = View.VISIBLE
                                    allAddProsesiTariAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    tariAdapter = AllTariNotOnProsesiAdminAdapter(filter as ArrayList<AllTariAdminModel>)
                                    tariAdapter.setOnClickAdd {
                                        val builder = AlertDialog.Builder(this@AddTariToProsesiAdminActivity)
                                        builder.setTitle("Tambah Tari")
                                            .setMessage("Apakah anda yakin ingin menambahkan tari ini?")
                                            .setCancelable(true)
                                            .setPositiveButton("Iya") { _, _ ->
                                                addTariProsesi(postID, it.id_post, namaPost)
                                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                                dialogInterface.cancel()
                                            }.show()
                                    }
                                    if(filter.isEmpty()){
                                        noProsesiAddTariAdmin.visibility   = View.VISIBLE
                                        allAddProsesiTariAdmin1.visibility = View.GONE
                                        allAddProsesiTariAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noProsesiAddTariAdmin.visibility   = View.GONE
                                        allAddProsesiTariAdmin2.visibility = View.VISIBLE
                                        allAddProsesiTariAdmin2.adapter    = tariAdapter
                                        allAddProsesiTariAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allAddProsesiTariAdmin1.visibility = View.VISIBLE
                                        allAddProsesiTariAdmin2.visibility = View.GONE
                                        noProsesiAddTariAdmin.visibility   = View.GONE
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

    private fun addTariProsesi(postID: Int, idTari: Int, namaPost: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menambahkan Data")
        progressDialog.show()
        ApiService.endpoint.addDataTariToProsesiAdmin(postID, idTari).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AddTariToProsesiAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AddTariToProsesiAdminActivity, DetailProsesiAdminActivity::class.java)
                    bundle.putInt("id_prosesi", postID)
                    bundle.putString("nama_prosesi", namaPost)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AddTariToProsesiAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AddTariToProsesiAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerProsesiAddTariAdmin.stopShimmer()
        shimmerProsesiAddTariAdmin.visibility = View.GONE
        swipeProsesiAddTariAdmin.visibility   = View.VISIBLE
    }
}
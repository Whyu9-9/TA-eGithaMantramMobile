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
import com.example.ekidungmantram.adapter.admin.AllGamelanOnYadnyaAdminAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailAllGamelanOnYadnyaAdminModel
import kotlinx.android.synthetic.main.activity_all_gamelan_on_prosesi_admin.*
import kotlinx.android.synthetic.main.activity_all_gamelan_on_yadnya_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllGamelanOnYadnyaAdminActivity : AppCompatActivity() {
    private lateinit var gamelanAdapter : AllGamelanOnYadnyaAdminAdapter
    private lateinit var setAdapter     : AllGamelanOnYadnyaAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_gamelan_on_yadnya_admin)
        supportActionBar!!.title = "Daftar Gamelan Yadnya"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val katID = bundle.getInt("id_kategori")
            val postID = bundle.getInt("id_yadnya")
            val namaPost = bundle.getString("nama_yadnya")

            namaYadnyaGamelan.text = namaPost
            allYadnyaGamelanAdmin1.layoutManager = LinearLayoutManager(applicationContext)
            allYadnyaGamelanAdmin2.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataGamelanYadnya(katID, postID, namaPost!!)

            swipeYadnyaGamelanAdmin.setOnRefreshListener {
                getAllDataGamelanYadnya(katID, postID, namaPost!!)
                swipeYadnyaGamelanAdmin.isRefreshing = false
            }

            fabYadnyaAddGamelan.setOnClickListener {
                val intent = Intent(this, AddGamelanToYadnyaAdminActivity::class.java)
                bundle.putInt("id_kategori", katID)
                bundle.putInt("id_yadnya", postID)
                bundle.putString("nama_yadnya", namaPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    private fun getAllDataGamelanYadnya(katID: Int, postID: Int, namaPost: String) {
        ApiService.endpoint.getDetailAllGamelansOnYadnyaAdmin(postID)
            .enqueue(object: Callback<ArrayList<DetailAllGamelanOnYadnyaAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<DetailAllGamelanOnYadnyaAdminModel>>,
                    response: Response<ArrayList<DetailAllGamelanOnYadnyaAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null){
                        swipeYadnyaGamelanAdmin.visibility   = View.VISIBLE
                        shimmerYadnyaGamelanAdmin.visibility = View.GONE
                    }else{
                        swipeYadnyaGamelanAdmin.visibility   = View.GONE
                        shimmerYadnyaGamelanAdmin.visibility = View.VISIBLE
                    }
                    setAdapter = AllGamelanOnYadnyaAdminAdapter(datalist!!)
                    setAdapter.setOnClickDelete {
                        val builder = AlertDialog.Builder(this@AllGamelanOnYadnyaAdminActivity)
                        builder.setTitle("Hapus Gamelan dari Yadnya")
                            .setMessage("Apakah anda yakin ingin menghapus gamelan dari yadnya ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                hapusGamelanYadnya(katID, it.id, postID, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }

                    allYadnyaGamelanAdmin1.adapter  = setAdapter
                    noYadnyaGamelanAdmin.visibility = View.GONE
                    setShimmerToStop()

                    cariYadnyaGamelanAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noYadnyaGamelanAdmin.visibility   = View.GONE
                                    allYadnyaGamelanAdmin1.visibility = View.VISIBLE
                                    allYadnyaGamelanAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    gamelanAdapter = AllGamelanOnYadnyaAdminAdapter(filter as ArrayList<DetailAllGamelanOnYadnyaAdminModel>)
                                    gamelanAdapter.setOnClickDelete {
                                        val builder = AlertDialog.Builder(this@AllGamelanOnYadnyaAdminActivity)
                                        builder.setTitle("Hapus Gamelan dari Yadnya")
                                            .setMessage("Apakah anda yakin ingin menghapus gamelan dari yadnya ini?")
                                            .setCancelable(true)
                                            .setPositiveButton("Iya") { _, _ ->
                                                hapusGamelanYadnya(katID, it.id, postID, namaPost)
                                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                                dialogInterface.cancel()
                                            }.show()
                                    }
                                    if(filter.isEmpty()){
                                        noYadnyaGamelanAdmin.visibility   = View.VISIBLE
                                        allYadnyaGamelanAdmin1.visibility = View.GONE
                                        allYadnyaGamelanAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noYadnyaGamelanAdmin.visibility   = View.GONE
                                        allYadnyaGamelanAdmin2.visibility = View.VISIBLE
                                        allYadnyaGamelanAdmin2.adapter    = gamelanAdapter
                                        allYadnyaGamelanAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allYadnyaGamelanAdmin1.visibility = View.VISIBLE
                                        allYadnyaGamelanAdmin2.visibility = View.GONE
                                        noYadnyaGamelanAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }
                    })
                }

                override fun onFailure(call: Call<ArrayList<DetailAllGamelanOnYadnyaAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun hapusGamelanYadnya(katID: Int, id: Int, postID: Int, namaPost: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataGamelanOnYadnyaAdmin(id).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AllGamelanOnYadnyaAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AllGamelanOnYadnyaAdminActivity, DetailYadnyaAdminActivity::class.java)
                    bundle.putInt("id_kategori", katID)
                    bundle.putInt("id_yadnya", postID)
                    bundle.putString("nama_yadnya", namaPost)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AllGamelanOnYadnyaAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AllGamelanOnYadnyaAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerYadnyaGamelanAdmin.stopShimmer()
        shimmerYadnyaGamelanAdmin.visibility = View.GONE
        swipeYadnyaGamelanAdmin.visibility   = View.VISIBLE
    }
}
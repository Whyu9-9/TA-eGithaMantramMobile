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
import com.example.ekidungmantram.adapter.admin.AllGamelanOnProsesiAdminAdapter
import com.example.ekidungmantram.adapter.admin.AllTabuhOnGamelanAdminAdapter
import com.example.ekidungmantram.admin.gamelan.AddTabuhToGamelanAdminActivity
import com.example.ekidungmantram.admin.gamelan.DetailGamelanAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailAllGamelanOnProsesiAdminModel
import com.example.ekidungmantram.model.adminmodel.DetailAllTabuhOnGamelanAdminModel
import kotlinx.android.synthetic.main.activity_all_gamelan_on_prosesi_admin.*
import kotlinx.android.synthetic.main.activity_all_tabuh_on_gamelan_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllGamelanOnProsesiAdminActivity : AppCompatActivity() {
    private lateinit var gamelanAdapter : AllGamelanOnProsesiAdminAdapter
    private lateinit var setAdapter     : AllGamelanOnProsesiAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_gamelan_on_prosesi_admin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Daftar Gamelan Prosesi"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_prosesi")
            val namaPost = bundle.getString("nama_prosesi")

            namaProsesiGamelan.text = namaPost
            allProsesiGamelanAdmin1.layoutManager = LinearLayoutManager(applicationContext)
            allProsesiGamelanAdmin2.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataGamelanProsesi(postID, namaPost!!)

            swipeProsesiGamelanAdmin.setOnRefreshListener {
                getAllDataGamelanProsesi(postID, namaPost!!)
                swipeProsesiGamelanAdmin.isRefreshing = false
            }

            fabProsesiAddGamelan.setOnClickListener {
                val intent = Intent(this, AddGamelanToProsesiAdminActivity::class.java)
                bundle.putInt("id_prosesi", postID)
                bundle.putString("nama_prosesi", namaPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    private fun getAllDataGamelanProsesi(postID: Int, namaPost: String) {
        ApiService.endpoint.getDetailAllGamelanOnProsesiAdmin(postID)
            .enqueue(object: Callback<ArrayList<DetailAllGamelanOnProsesiAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<DetailAllGamelanOnProsesiAdminModel>>,
                    response: Response<ArrayList<DetailAllGamelanOnProsesiAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null){
                        swipeProsesiGamelanAdmin.visibility   = View.VISIBLE
                        shimmerProsesiGamelanAdmin.visibility = View.GONE
                    }else{
                        swipeProsesiGamelanAdmin.visibility   = View.GONE
                        shimmerProsesiGamelanAdmin.visibility = View.VISIBLE
                    }
                    setAdapter = AllGamelanOnProsesiAdminAdapter(datalist!!)
                    setAdapter.setOnClickDelete {
                        val builder = AlertDialog.Builder(this@AllGamelanOnProsesiAdminActivity)
                        builder.setTitle("Hapus Gamelan dari Prosesi")
                            .setMessage("Apakah anda yakin ingin menghapus gamelan dari prosesi ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                hapusGamelanProsesi(it.id, postID, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }

                    allProsesiGamelanAdmin1.adapter  = setAdapter
                    noProsesiGamelanAdmin.visibility = View.GONE
                    setShimmerToStop()

                    cariProsesiGamelanAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noProsesiGamelanAdmin.visibility   = View.GONE
                                    allProsesiGamelanAdmin1.visibility = View.VISIBLE
                                    allProsesiGamelanAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    gamelanAdapter = AllGamelanOnProsesiAdminAdapter(filter as ArrayList<DetailAllGamelanOnProsesiAdminModel>)
                                    gamelanAdapter.setOnClickDelete {
                                        val builder = AlertDialog.Builder(this@AllGamelanOnProsesiAdminActivity)
                                        builder.setTitle("Hapus Gamelan dari Prosesi")
                                            .setMessage("Apakah anda yakin ingin menghapus gamelan dari prosesi ini?")
                                            .setCancelable(true)
                                            .setPositiveButton("Iya") { _, _ ->
                                                hapusGamelanProsesi(it.id, postID, namaPost)
                                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                                dialogInterface.cancel()
                                            }.show()
                                    }
                                    if(filter.isEmpty()){
                                        noProsesiGamelanAdmin.visibility   = View.VISIBLE
                                        allProsesiGamelanAdmin1.visibility = View.GONE
                                        allProsesiGamelanAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noProsesiGamelanAdmin.visibility   = View.GONE
                                        allProsesiGamelanAdmin2.visibility = View.VISIBLE
                                        allProsesiGamelanAdmin2.adapter    = gamelanAdapter
                                        allProsesiGamelanAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allProsesiGamelanAdmin1.visibility = View.VISIBLE
                                        allProsesiGamelanAdmin2.visibility = View.GONE
                                        noProsesiGamelanAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }
                    })
                }

                override fun onFailure(call: Call<ArrayList<DetailAllGamelanOnProsesiAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun hapusGamelanProsesi(id: Int, postID: Int, namaPost: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataGamelanOnProsesiAdmin(id).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AllGamelanOnProsesiAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AllGamelanOnProsesiAdminActivity, DetailProsesiAdminActivity::class.java)
                    bundle.putInt("id_prosesi", postID)
                    bundle.putString("nama_prosesi", namaPost)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AllGamelanOnProsesiAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AllGamelanOnProsesiAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerProsesiGamelanAdmin.stopShimmer()
        shimmerProsesiGamelanAdmin.visibility = View.GONE
        swipeProsesiGamelanAdmin.visibility   = View.VISIBLE
    }
}
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
import com.example.ekidungmantram.adapter.admin.AllMantramOnProsesiAdminAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailAllMantramOnProsesiAdminModel
import kotlinx.android.synthetic.main.activity_all_mantram_on_prosesi_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllMantramOnProsesiAdminActivity : AppCompatActivity() {
    private lateinit var mantramAdapter : AllMantramOnProsesiAdminAdapter
    private lateinit var setAdapter     : AllMantramOnProsesiAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_mantram_on_prosesi_admin)
        supportActionBar!!.title = "Daftar Mantram Prosesi"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_prosesi")
            val namaPost = bundle.getString("nama_prosesi")

            namaProsesiMantram.text = namaPost
            allProsesiMantramAdmin1.layoutManager = LinearLayoutManager(applicationContext)
            allProsesiMantramAdmin2.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataMantramProsesi(postID, namaPost!!)

            swipeProsesiMantramAdmin.setOnRefreshListener {
                getAllDataMantramProsesi(postID, namaPost!!)
                swipeProsesiMantramAdmin.isRefreshing = false
            }

            fabProsesiAddMantram.setOnClickListener {
                val intent = Intent(this, AddMantramToProsesiAdminActivity::class.java)
                bundle.putInt("id_prosesi", postID)
                bundle.putString("nama_prosesi", namaPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    private fun getAllDataMantramProsesi(postID: Int, namaPost: String) {
        ApiService.endpoint.getDetailAllMantramOnProsesiAdmin(postID)
            .enqueue(object: Callback<ArrayList<DetailAllMantramOnProsesiAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<DetailAllMantramOnProsesiAdminModel>>,
                    response: Response<ArrayList<DetailAllMantramOnProsesiAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null){
                        swipeProsesiMantramAdmin.visibility   = View.VISIBLE
                        shimmerProsesiMantramAdmin.visibility = View.GONE
                    }else{
                        swipeProsesiMantramAdmin.visibility   = View.GONE
                        shimmerProsesiMantramAdmin.visibility = View.VISIBLE
                    }
                    setAdapter = AllMantramOnProsesiAdminAdapter(datalist!!)
                    setAdapter.setOnClickDelete {
                        val builder = AlertDialog.Builder(this@AllMantramOnProsesiAdminActivity)
                        builder.setTitle("Hapus Mantram dari Prosesi")
                            .setMessage("Apakah anda yakin ingin menghapus mantram dari prosesi ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                hapusMantramProsesi(it.id, postID, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }

                    allProsesiMantramAdmin1.adapter  = setAdapter
                    noProsesiMantramAdmin.visibility = View.GONE
                    setShimmerToStop()

                    cariProsesiMantramAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noProsesiMantramAdmin.visibility   = View.GONE
                                    allProsesiMantramAdmin1.visibility = View.VISIBLE
                                    allProsesiMantramAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    mantramAdapter = AllMantramOnProsesiAdminAdapter(filter as ArrayList<DetailAllMantramOnProsesiAdminModel>)
                                    mantramAdapter.setOnClickDelete {
                                        val builder = AlertDialog.Builder(this@AllMantramOnProsesiAdminActivity)
                                        builder.setTitle("Hapus Mantram dari Prosesi")
                                            .setMessage("Apakah anda yakin ingin menghapus mantram dari prosesi ini?")
                                            .setCancelable(true)
                                            .setPositiveButton("Iya") { _, _ ->
                                                hapusMantramProsesi(it.id, postID, namaPost)
                                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                                dialogInterface.cancel()
                                            }.show()
                                    }
                                    if(filter.isEmpty()){
                                        noProsesiMantramAdmin.visibility   = View.VISIBLE
                                        allProsesiMantramAdmin1.visibility = View.GONE
                                        allProsesiMantramAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noProsesiMantramAdmin.visibility   = View.GONE
                                        allProsesiMantramAdmin2.visibility = View.VISIBLE
                                        allProsesiMantramAdmin2.adapter    = mantramAdapter
                                        allProsesiMantramAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allProsesiMantramAdmin1.visibility = View.VISIBLE
                                        allProsesiMantramAdmin2.visibility = View.GONE
                                        noProsesiMantramAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }
                    })
                }

                override fun onFailure(call: Call<ArrayList<DetailAllMantramOnProsesiAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun hapusMantramProsesi(id: Int, postID: Int, namaPost: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataMantramOnProsesiAdmin(id).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AllMantramOnProsesiAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AllMantramOnProsesiAdminActivity, DetailProsesiAdminActivity::class.java)
                    bundle.putInt("id_prosesi", postID)
                    bundle.putString("nama_prosesi", namaPost)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AllMantramOnProsesiAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AllMantramOnProsesiAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerProsesiMantramAdmin.stopShimmer()
        shimmerProsesiMantramAdmin.visibility = View.GONE
        swipeProsesiMantramAdmin.visibility   = View.VISIBLE
    }
}
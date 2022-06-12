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
import com.example.ekidungmantram.adapter.admin.AllKidungOnProsesiAdminAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailAllGamelanOnProsesiAdminModel
import com.example.ekidungmantram.model.adminmodel.DetailAllKidungOnProsesiAdminModel
import kotlinx.android.synthetic.main.activity_all_gamelan_on_prosesi_admin.*
import kotlinx.android.synthetic.main.activity_all_kidung_on_prosesi_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllKidungOnProsesiAdminActivity : AppCompatActivity() {
    private lateinit var kidungAdapter : AllKidungOnProsesiAdminAdapter
    private lateinit var setAdapter    : AllKidungOnProsesiAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_kidung_on_prosesi_admin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Daftar Kidung Prosesi"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_prosesi")
            val namaPost = bundle.getString("nama_prosesi")

            namaProsesiKidung.text = namaPost
            allProsesiKidungAdmin1.layoutManager = LinearLayoutManager(applicationContext)
            allProsesiKidungAdmin2.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataKidungProsesi(postID, namaPost!!)

            swipeProsesiKidungAdmin.setOnRefreshListener {
                getAllDataKidungProsesi(postID, namaPost!!)
                swipeProsesiKidungAdmin.isRefreshing = false
            }

            fabProsesiAddKidung.setOnClickListener {
                val intent = Intent(this, AddKidungToProsesiAdminActivity::class.java)
                bundle.putInt("id_prosesi", postID)
                bundle.putString("nama_prosesi", namaPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    private fun getAllDataKidungProsesi(postID: Int, namaPost: String) {
        ApiService.endpoint.getDetailAllKidungOnProsesiAdmin(postID)
            .enqueue(object: Callback<ArrayList<DetailAllKidungOnProsesiAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<DetailAllKidungOnProsesiAdminModel>>,
                    response: Response<ArrayList<DetailAllKidungOnProsesiAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null){
                        swipeProsesiKidungAdmin.visibility   = View.VISIBLE
                        shimmerProsesiKidungAdmin.visibility = View.GONE
                    }else{
                        swipeProsesiKidungAdmin.visibility   = View.GONE
                        shimmerProsesiKidungAdmin.visibility = View.VISIBLE
                    }
                    setAdapter = AllKidungOnProsesiAdminAdapter(datalist!!)
                    setAdapter.setOnClickDelete {
                        val builder = AlertDialog.Builder(this@AllKidungOnProsesiAdminActivity)
                        builder.setTitle("Hapus Kidung dari Prosesi")
                            .setMessage("Apakah anda yakin ingin menghapus kidung dari prosesi ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                hapusKidungProsesi(it.id, postID, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }

                    allProsesiKidungAdmin1.adapter  = setAdapter
                    noProsesiKidungAdmin.visibility = View.GONE
                    setShimmerToStop()

                    cariProsesiKidungAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noProsesiKidungAdmin.visibility   = View.GONE
                                    allProsesiKidungAdmin1.visibility = View.VISIBLE
                                    allProsesiKidungAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    kidungAdapter = AllKidungOnProsesiAdminAdapter(filter as ArrayList<DetailAllKidungOnProsesiAdminModel>)
                                    kidungAdapter.setOnClickDelete {
                                        val builder = AlertDialog.Builder(this@AllKidungOnProsesiAdminActivity)
                                        builder.setTitle("Hapus Kidung dari Prosesi")
                                            .setMessage("Apakah anda yakin ingin menghapus kidung dari prosesi ini?")
                                            .setCancelable(true)
                                            .setPositiveButton("Iya") { _, _ ->
                                                hapusKidungProsesi(it.id, postID, namaPost)
                                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                                dialogInterface.cancel()
                                            }.show()
                                    }
                                    if(filter.isEmpty()){
                                        noProsesiKidungAdmin.visibility   = View.VISIBLE
                                        allProsesiKidungAdmin1.visibility = View.GONE
                                        allProsesiKidungAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noProsesiKidungAdmin.visibility   = View.GONE
                                        allProsesiKidungAdmin2.visibility = View.VISIBLE
                                        allProsesiKidungAdmin2.adapter    = kidungAdapter
                                        allProsesiKidungAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allProsesiKidungAdmin1.visibility = View.VISIBLE
                                        allProsesiKidungAdmin2.visibility = View.GONE
                                        noProsesiKidungAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }
                    })
                }

                override fun onFailure(call: Call<ArrayList<DetailAllKidungOnProsesiAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun hapusKidungProsesi(id: Int, postID: Int, namaPost: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataKidungOnProsesiAdmin(id).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AllKidungOnProsesiAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AllKidungOnProsesiAdminActivity, DetailProsesiAdminActivity::class.java)
                    bundle.putInt("id_prosesi", postID)
                    bundle.putString("nama_prosesi", namaPost)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AllKidungOnProsesiAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AllKidungOnProsesiAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerProsesiKidungAdmin.stopShimmer()
        shimmerProsesiKidungAdmin.visibility = View.GONE
        swipeProsesiKidungAdmin.visibility   = View.VISIBLE
    }
}
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
import com.example.ekidungmantram.adapter.admin.AllKidungOnYadnyaAdminAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailAllKidungOnYadnyaAdminModel
import kotlinx.android.synthetic.main.activity_all_kidung_on_yadnya_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllKidungOnYadnyaAdminActivity : AppCompatActivity() {
    private lateinit var kidungAdapter : AllKidungOnYadnyaAdminAdapter
    private lateinit var setAdapter    : AllKidungOnYadnyaAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_kidung_on_yadnya_admin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Daftar Kidung Yadnya"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val katID = bundle.getInt("id_kategori")
            val postID = bundle.getInt("id_yadnya")
            val namaPost = bundle.getString("nama_yadnya")

            namaYadnyaKidung.text = namaPost
            allYadnyaKidungAdmin1.layoutManager = LinearLayoutManager(applicationContext)
            allYadnyaKidungAdmin2.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataKidungYadnya(katID, postID, namaPost!!)

            swipeYadnyaKidungAdmin.setOnRefreshListener {
                getAllDataKidungYadnya(katID, postID, namaPost!!)
                swipeYadnyaKidungAdmin.isRefreshing = false
            }

            fabYadnyaAddKidung.setOnClickListener {
                val intent = Intent(this, AddKidungToYadnyaAdminActivity::class.java)
                bundle.putInt("id_kategori", katID)
                bundle.putInt("id_yadnya", postID)
                bundle.putString("nama_yadnya", namaPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    private fun getAllDataKidungYadnya(katID: Int, postID: Int, namaPost: String) {
        ApiService.endpoint.getDetailAllKidungOnYadnyaAdmin(postID)
            .enqueue(object: Callback<ArrayList<DetailAllKidungOnYadnyaAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<DetailAllKidungOnYadnyaAdminModel>>,
                    response: Response<ArrayList<DetailAllKidungOnYadnyaAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null){
                        swipeYadnyaKidungAdmin.visibility   = View.VISIBLE
                        shimmerYadnyaKidungAdmin.visibility = View.GONE
                    }else{
                        swipeYadnyaKidungAdmin.visibility   = View.GONE
                        shimmerYadnyaKidungAdmin.visibility = View.VISIBLE
                    }
                    setAdapter = AllKidungOnYadnyaAdminAdapter(datalist!!)
                    setAdapter.setOnClickDelete {
                        val builder = AlertDialog.Builder(this@AllKidungOnYadnyaAdminActivity)
                        builder.setTitle("Hapus Kidung dari Yadnya")
                            .setMessage("Apakah anda yakin ingin menghapus kidung dari yadnya ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                hapusKidungYadnya(katID, it.id, postID, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }

                    allYadnyaKidungAdmin1.adapter  = setAdapter
                    noYadnyaKidungAdmin.visibility = View.GONE
                    setShimmerToStop()

                    cariYadnyaKidungAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noYadnyaKidungAdmin.visibility   = View.GONE
                                    allYadnyaKidungAdmin1.visibility = View.VISIBLE
                                    allYadnyaKidungAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    kidungAdapter = AllKidungOnYadnyaAdminAdapter(filter as ArrayList<DetailAllKidungOnYadnyaAdminModel>)
                                    kidungAdapter.setOnClickDelete {
                                        val builder = AlertDialog.Builder(this@AllKidungOnYadnyaAdminActivity)
                                        builder.setTitle("Hapus Kidung dari Yadnya")
                                            .setMessage("Apakah anda yakin ingin menghapus kidung dari yadnya ini?")
                                            .setCancelable(true)
                                            .setPositiveButton("Iya") { _, _ ->
                                                hapusKidungYadnya(katID, it.id, postID, namaPost)
                                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                                dialogInterface.cancel()
                                            }.show()
                                    }
                                    if(filter.isEmpty()){
                                        noYadnyaKidungAdmin.visibility   = View.VISIBLE
                                        allYadnyaKidungAdmin1.visibility = View.GONE
                                        allYadnyaKidungAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noYadnyaKidungAdmin.visibility   = View.GONE
                                        allYadnyaKidungAdmin2.visibility = View.VISIBLE
                                        allYadnyaKidungAdmin2.adapter    = kidungAdapter
                                        allYadnyaKidungAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allYadnyaKidungAdmin1.visibility = View.VISIBLE
                                        allYadnyaKidungAdmin2.visibility = View.GONE
                                        noYadnyaKidungAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }
                    })
                }

                override fun onFailure(call: Call<ArrayList<DetailAllKidungOnYadnyaAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun hapusKidungYadnya(katID: Int, id: Int, postID: Int, namaPost: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataKidungOnYadnyaAdmin(id).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AllKidungOnYadnyaAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AllKidungOnYadnyaAdminActivity, DetailYadnyaAdminActivity::class.java)
                    bundle.putInt("id_kategori", katID)
                    bundle.putInt("id_yadnya", postID)
                    bundle.putString("nama_yadnya", namaPost)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AllKidungOnYadnyaAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AllKidungOnYadnyaAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerYadnyaKidungAdmin.stopShimmer()
        shimmerYadnyaKidungAdmin.visibility = View.GONE
        swipeYadnyaKidungAdmin.visibility   = View.VISIBLE
    }
}
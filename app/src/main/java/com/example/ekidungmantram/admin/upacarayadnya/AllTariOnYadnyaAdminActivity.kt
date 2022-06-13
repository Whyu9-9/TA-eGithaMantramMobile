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
import com.example.ekidungmantram.adapter.admin.AllTariOnYadnyaAdminAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailAllTariOnYadnyaAdminModel
import kotlinx.android.synthetic.main.activity_all_tari_on_yadnya_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllTariOnYadnyaAdminActivity : AppCompatActivity() {
    private lateinit var tariAdapter : AllTariOnYadnyaAdminAdapter
    private lateinit var setAdapter  : AllTariOnYadnyaAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_tari_on_yadnya_admin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Daftar Tari Prosesi"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val katID = bundle.getInt("id_kategori")
            val postID = bundle.getInt("id_yadnya")
            val namaPost = bundle.getString("nama_yadnya")

            namaYadnyaTari.text = namaPost
            allYadnyaTariAdmin1.layoutManager = LinearLayoutManager(applicationContext)
            allYadnyaTariAdmin2.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataTariYadnya(katID, postID, namaPost!!)

            swipeYadnyaTariAdmin.setOnRefreshListener {
                getAllDataTariYadnya(katID, postID, namaPost!!)
                swipeYadnyaTariAdmin.isRefreshing = false
            }

            fabYadnyaAddTari.setOnClickListener {
                val intent = Intent(this, AddTariToYadnyaAdminActivity::class.java)
                bundle.putInt("id_kategori", katID)
                bundle.putInt("id_yadnya", postID)
                bundle.putString("nama_yadnya", namaPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    private fun getAllDataTariYadnya(katID: Int, postID: Int, namaPost: String) {
        ApiService.endpoint.getDetailAllTariOnYadnyaAdmin(postID)
            .enqueue(object: Callback<ArrayList<DetailAllTariOnYadnyaAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<DetailAllTariOnYadnyaAdminModel>>,
                    response: Response<ArrayList<DetailAllTariOnYadnyaAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null){
                        swipeYadnyaTariAdmin.visibility   = View.VISIBLE
                        shimmerYadnyaTariAdmin.visibility = View.GONE
                    }else{
                        swipeYadnyaTariAdmin.visibility   = View.GONE
                        shimmerYadnyaTariAdmin.visibility = View.VISIBLE
                    }
                    setAdapter = AllTariOnYadnyaAdminAdapter(datalist!!)
                    setAdapter.setOnClickDelete {
                        val builder = AlertDialog.Builder(this@AllTariOnYadnyaAdminActivity)
                        builder.setTitle("Hapus Tari dari Prosesi")
                            .setMessage("Apakah anda yakin ingin menghapus tari dari prosesi ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                hapusTariYadnya(katID, it.id, postID, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }

                    allYadnyaTariAdmin1.adapter  = setAdapter
                    noYadnyaTariAdmin.visibility = View.GONE
                    setShimmerToStop()

                    cariYadnyaTariAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noYadnyaTariAdmin.visibility   = View.GONE
                                    allYadnyaTariAdmin1.visibility = View.VISIBLE
                                    allYadnyaTariAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    tariAdapter = AllTariOnYadnyaAdminAdapter(filter as ArrayList<DetailAllTariOnYadnyaAdminModel>)
                                    tariAdapter.setOnClickDelete {
                                        val builder = AlertDialog.Builder(this@AllTariOnYadnyaAdminActivity)
                                        builder.setTitle("Hapus Tari dari Yadnya")
                                            .setMessage("Apakah anda yakin ingin menghapus tari dari yadnya ini?")
                                            .setCancelable(true)
                                            .setPositiveButton("Iya") { _, _ ->
                                                hapusTariYadnya(katID, it.id, postID, namaPost)
                                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                                dialogInterface.cancel()
                                            }.show()
                                    }
                                    if(filter.isEmpty()){
                                        noYadnyaTariAdmin.visibility   = View.VISIBLE
                                        allYadnyaTariAdmin1.visibility = View.GONE
                                        allYadnyaTariAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noYadnyaTariAdmin.visibility   = View.GONE
                                        allYadnyaTariAdmin2.visibility = View.VISIBLE
                                        allYadnyaTariAdmin2.adapter    = tariAdapter
                                        allYadnyaTariAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allYadnyaTariAdmin1.visibility = View.VISIBLE
                                        allYadnyaTariAdmin2.visibility = View.GONE
                                        noYadnyaTariAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }
                    })
                }

                override fun onFailure(call: Call<ArrayList<DetailAllTariOnYadnyaAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun hapusTariYadnya(katID: Int, id: Int, postID: Int, namaPost: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataTariOnYadnyaAdmin(id).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AllTariOnYadnyaAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AllTariOnYadnyaAdminActivity, DetailYadnyaAdminActivity::class.java)
                    bundle.putInt("id_kategori", katID)
                    bundle.putInt("id_yadnya", postID)
                    bundle.putString("nama_yadnya", namaPost)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AllTariOnYadnyaAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AllTariOnYadnyaAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerYadnyaTariAdmin.stopShimmer()
        shimmerYadnyaTariAdmin.visibility = View.GONE
        swipeYadnyaTariAdmin.visibility   = View.VISIBLE
    }
}
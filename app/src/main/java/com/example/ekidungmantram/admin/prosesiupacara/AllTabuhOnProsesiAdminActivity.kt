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
import com.example.ekidungmantram.adapter.admin.AllTabuhOnProsesiAdminAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailAllTabuhOnProsesiAdminModel
import kotlinx.android.synthetic.main.activity_all_tabuh_on_prosesi_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllTabuhOnProsesiAdminActivity : AppCompatActivity() {
    private lateinit var tabuhAdapter : AllTabuhOnProsesiAdminAdapter
    private lateinit var setAdapter   : AllTabuhOnProsesiAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_tabuh_on_prosesi_admin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Daftar Tabuh Prosesi"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_prosesi")
            val namaPost = bundle.getString("nama_prosesi")

            namaProsesiTabuh.text = namaPost
            allProsesiTabuhAdmin1.layoutManager = LinearLayoutManager(applicationContext)
            allProsesiTabuhAdmin2.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataTabuhProsesi(postID, namaPost!!)

            swipeProsesiTabuhAdmin.setOnRefreshListener {
                getAllDataTabuhProsesi(postID, namaPost!!)
                swipeProsesiTabuhAdmin.isRefreshing = false
            }

            fabProsesiAddTabuh.setOnClickListener {
                val intent = Intent(this, AddTabuhToProsesiAdminActivity::class.java)
                bundle.putInt("id_prosesi", postID)
                bundle.putString("nama_prosesi", namaPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    private fun getAllDataTabuhProsesi(postID: Int, namaPost: String) {
        ApiService.endpoint.getDetailAllTabuhOnProsesiAdmin(postID)
            .enqueue(object: Callback<ArrayList<DetailAllTabuhOnProsesiAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<DetailAllTabuhOnProsesiAdminModel>>,
                    response: Response<ArrayList<DetailAllTabuhOnProsesiAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null){
                        swipeProsesiTabuhAdmin.visibility   = View.VISIBLE
                        shimmerProsesiTabuhAdmin.visibility = View.GONE
                    }else{
                        swipeProsesiTabuhAdmin.visibility   = View.GONE
                        shimmerProsesiTabuhAdmin.visibility = View.VISIBLE
                    }
                    setAdapter = AllTabuhOnProsesiAdminAdapter(datalist!!)
                    setAdapter.setOnClickDelete {
                        val builder = AlertDialog.Builder(this@AllTabuhOnProsesiAdminActivity)
                        builder.setTitle("Hapus Tabuh dari Prosesi")
                            .setMessage("Apakah anda yakin ingin menghapus tabuh dari prosesi ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                hapusTabuhProsesi(it.id, postID, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }

                    allProsesiTabuhAdmin1.adapter  = setAdapter
                    noProsesiTabuhAdmin.visibility = View.GONE
                    setShimmerToStop()

                    cariProsesiTabuhAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noProsesiTabuhAdmin.visibility   = View.GONE
                                    allProsesiTabuhAdmin1.visibility = View.VISIBLE
                                    allProsesiTabuhAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    tabuhAdapter = AllTabuhOnProsesiAdminAdapter(filter as ArrayList<DetailAllTabuhOnProsesiAdminModel>)
                                    tabuhAdapter.setOnClickDelete {
                                        val builder = AlertDialog.Builder(this@AllTabuhOnProsesiAdminActivity)
                                        builder.setTitle("Hapus Tabuh dari Prosesi")
                                            .setMessage("Apakah anda yakin ingin menghapus tabuh dari prosesi ini?")
                                            .setCancelable(true)
                                            .setPositiveButton("Iya") { _, _ ->
                                                hapusTabuhProsesi(it.id, postID, namaPost)
                                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                                dialogInterface.cancel()
                                            }.show()
                                    }
                                    if(filter.isEmpty()){
                                        noProsesiTabuhAdmin.visibility   = View.VISIBLE
                                        allProsesiTabuhAdmin1.visibility = View.GONE
                                        allProsesiTabuhAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noProsesiTabuhAdmin.visibility   = View.GONE
                                        allProsesiTabuhAdmin2.visibility = View.VISIBLE
                                        allProsesiTabuhAdmin2.adapter    = tabuhAdapter
                                        allProsesiTabuhAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allProsesiTabuhAdmin1.visibility = View.VISIBLE
                                        allProsesiTabuhAdmin2.visibility = View.GONE
                                        noProsesiTabuhAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }
                    })
                }

                override fun onFailure(call: Call<ArrayList<DetailAllTabuhOnProsesiAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun hapusTabuhProsesi(id: Int, postID: Int, namaPost: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataTabuhOnProsesiAdmin(id).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AllTabuhOnProsesiAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AllTabuhOnProsesiAdminActivity, DetailProsesiAdminActivity::class.java)
                    bundle.putInt("id_prosesi", postID)
                    bundle.putString("nama_prosesi", namaPost)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AllTabuhOnProsesiAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AllTabuhOnProsesiAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerProsesiTabuhAdmin.stopShimmer()
        shimmerProsesiTabuhAdmin.visibility = View.GONE
        swipeProsesiTabuhAdmin.visibility   = View.VISIBLE
    }
}
package com.example.ekidungmantram.admin.gamelan

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
import com.example.ekidungmantram.adapter.admin.AllTabuhOnGamelanAdminAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailAllTabuhOnGamelanAdminModel
import kotlinx.android.synthetic.main.activity_all_tabuh_on_gamelan_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllTabuhOnGamelanAdminActivity : AppCompatActivity() {
    private lateinit var tabuhAdapter : AllTabuhOnGamelanAdminAdapter
    private lateinit var setAdapter   : AllTabuhOnGamelanAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_tabuh_on_gamelan_admin)
        supportActionBar!!.title = "Daftar Tabuh Gamelan"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_gamelan")
            val namaPost = bundle.getString("nama_gamelan")

            namaGamelanTabuh.text = namaPost
            allGamelanTabuhAdmin1.layoutManager = LinearLayoutManager(applicationContext)
            allGamelanTabuhAdmin2.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataTabuhGamelan(postID, namaPost!!)

            swipeGamelanTabuhAdmin.setOnRefreshListener {
                getAllDataTabuhGamelan(postID, namaPost!!)
                swipeGamelanTabuhAdmin.isRefreshing = false
            }

            fabGamelanAddTabuh.setOnClickListener {
                val intent = Intent(this, AddTabuhToGamelanAdminActivity::class.java)
                bundle.putInt("id_gamelan", postID)
                bundle.putString("nama_gamelan", namaPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    private fun getAllDataTabuhGamelan(postID: Int, name: String) {
        ApiService.endpoint.getDetailAllTabuhOnGamelanAdmin(postID)
            .enqueue(object: Callback<ArrayList<DetailAllTabuhOnGamelanAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<DetailAllTabuhOnGamelanAdminModel>>,
                    response: Response<ArrayList<DetailAllTabuhOnGamelanAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null){
                        swipeGamelanTabuhAdmin.visibility   = View.VISIBLE
                        shimmerGamelanTabuhAdmin.visibility = View.GONE
                    }else{
                        swipeGamelanTabuhAdmin.visibility   = View.GONE
                        shimmerGamelanTabuhAdmin.visibility = View.VISIBLE
                    }
                    setAdapter = AllTabuhOnGamelanAdminAdapter(datalist!!)
                    setAdapter.setOnClickDelete {
                        val builder = AlertDialog.Builder(this@AllTabuhOnGamelanAdminActivity)
                        builder.setTitle("Hapus Tabuh dari Gamelan")
                            .setMessage("Apakah anda yakin ingin menghapus tabuh dari gamelan ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                hapusTabuhGamelan(it.id, postID, name)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }

                    allGamelanTabuhAdmin1.adapter  = setAdapter
                    noGamelanTabuhAdmin.visibility = View.GONE
                    setShimmerToStop()

                    cariGamelanTabuhAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noGamelanTabuhAdmin.visibility   = View.GONE
                                    allGamelanTabuhAdmin1.visibility = View.VISIBLE
                                    allGamelanTabuhAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    tabuhAdapter = AllTabuhOnGamelanAdminAdapter(filter as ArrayList<DetailAllTabuhOnGamelanAdminModel>)
                                    tabuhAdapter.setOnClickDelete {
                                        val builder = AlertDialog.Builder(this@AllTabuhOnGamelanAdminActivity)
                                        builder.setTitle("Hapus Tabuh dari Gamelan")
                                            .setMessage("Apakah anda yakin ingin menghapus tabuh dari gamelan ini?")
                                            .setCancelable(true)
                                            .setPositiveButton("Iya") { _, _ ->
                                                hapusTabuhGamelan(it.id, postID, name)
                                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                                dialogInterface.cancel()
                                            }.show()
                                    }
                                    if(filter.isEmpty()){
                                        noGamelanTabuhAdmin.visibility   = View.VISIBLE
                                        allGamelanTabuhAdmin1.visibility = View.GONE
                                        allGamelanTabuhAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noGamelanTabuhAdmin.visibility   = View.GONE
                                        allGamelanTabuhAdmin2.visibility = View.VISIBLE
                                        allGamelanTabuhAdmin2.adapter    = tabuhAdapter
                                        allGamelanTabuhAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allGamelanTabuhAdmin1.visibility = View.VISIBLE
                                        allGamelanTabuhAdmin2.visibility = View.GONE
                                        noGamelanTabuhAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }
                    })
                }

                override fun onFailure(call: Call<ArrayList<DetailAllTabuhOnGamelanAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun hapusTabuhGamelan(id: Int, postID: Int, name: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataTabuhOnGamelanAdmin(id).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AllTabuhOnGamelanAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AllTabuhOnGamelanAdminActivity, DetailGamelanAdminActivity::class.java)
                    bundle.putInt("id_gamelan", postID)
                    bundle.putString("nama_gamelan", name)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AllTabuhOnGamelanAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AllTabuhOnGamelanAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerGamelanTabuhAdmin.stopShimmer()
        shimmerGamelanTabuhAdmin.visibility = View.GONE
        swipeGamelanTabuhAdmin.visibility   = View.VISIBLE
    }
}
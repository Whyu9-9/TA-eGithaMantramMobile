package com.example.ekidungmantram.admin.tari

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
import com.example.ekidungmantram.adapter.admin.AllTabuhOnTariAdminAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailAllTabuhOnTariAdminModel
import kotlinx.android.synthetic.main.activity_all_tabuh_on_tari_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllTabuhOnTariAdminActivity : AppCompatActivity() {
    private lateinit var tabuhAdapter : AllTabuhOnTariAdminAdapter
    private lateinit var setAdapter   : AllTabuhOnTariAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_tabuh_on_tari_admin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "List Tabuh Tari"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_tari")
            val namaPost = bundle.getString("nama_tari")

            namaTariTabuh.text = namaPost
            allTariTabuhAdmin1.layoutManager = LinearLayoutManager(applicationContext)
            allTariTabuhAdmin2.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataTabuhTari(postID, namaPost!!)

            swipeTariTabuhAdmin.setOnRefreshListener {
                getAllDataTabuhTari(postID, namaPost!!)
                swipeTariTabuhAdmin.isRefreshing = false
            }

            fabTariAddTabuh.setOnClickListener {
                val intent = Intent(this, AddTabuhToTariAdminActivity::class.java)
                bundle.putInt("id_tari", postID)
                bundle.putString("nama_tari", namaPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    private fun getAllDataTabuhTari(postID: Int, namaPost: String) {
        ApiService.endpoint.getDetailAllTabuhOnTariAdmin(postID)
            .enqueue(object: Callback<ArrayList<DetailAllTabuhOnTariAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<DetailAllTabuhOnTariAdminModel>>,
                    response: Response<ArrayList<DetailAllTabuhOnTariAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null){
                        swipeTariTabuhAdmin.visibility   = View.VISIBLE
                        shimmerTariTabuhAdmin.visibility = View.GONE
                    }else{
                        swipeTariTabuhAdmin.visibility   = View.GONE
                        shimmerTariTabuhAdmin.visibility = View.VISIBLE
                    }
                    setAdapter = AllTabuhOnTariAdminAdapter(datalist!!)
                    setAdapter.setOnClickDelete {
                        val builder = AlertDialog.Builder(this@AllTabuhOnTariAdminActivity)
                        builder.setTitle("Hapus Tabuh dari Tari")
                            .setMessage("Apakah anda yakin ingin menghapus tabuh dari tari ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                hapusTabuhTari(it.id, postID, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }

                    allTariTabuhAdmin1.adapter  = setAdapter
                    noTariTabuhAdmin.visibility = View.GONE
                    setShimmerToStop()

                    cariTariTabuhAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noTariTabuhAdmin.visibility   = View.GONE
                                    allTariTabuhAdmin1.visibility = View.VISIBLE
                                    allTariTabuhAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    tabuhAdapter = AllTabuhOnTariAdminAdapter(filter as ArrayList<DetailAllTabuhOnTariAdminModel>)
                                    tabuhAdapter.setOnClickDelete {
                                        val builder = AlertDialog.Builder(this@AllTabuhOnTariAdminActivity)
                                        builder.setTitle("Hapus Tabuh dari Tari")
                                            .setMessage("Apakah anda yakin ingin menghapus tabuh dari tari ini?")
                                            .setCancelable(true)
                                            .setPositiveButton("Iya") { _, _ ->
                                                hapusTabuhTari(it.id, postID, namaPost)
                                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                                dialogInterface.cancel()
                                            }.show()
                                    }
                                    if(filter.isEmpty()){
                                        noTariTabuhAdmin.visibility   = View.VISIBLE
                                        allTariTabuhAdmin1.visibility = View.GONE
                                        allTariTabuhAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noTariTabuhAdmin.visibility   = View.GONE
                                        allTariTabuhAdmin2.visibility = View.VISIBLE
                                        allTariTabuhAdmin2.adapter    = tabuhAdapter
                                        allTariTabuhAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allTariTabuhAdmin1.visibility = View.VISIBLE
                                        allTariTabuhAdmin2.visibility = View.GONE
                                        noTariTabuhAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }
                    })
                }

                override fun onFailure(call: Call<ArrayList<DetailAllTabuhOnTariAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun hapusTabuhTari(id: Int, postID: Int, namaPost: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataTabuhOnTariAdmin(id).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AllTabuhOnTariAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AllTabuhOnTariAdminActivity, DetailTariAdminActivity::class.java)
                    bundle.putInt("id_tari", postID)
                    bundle.putString("nama_tari", namaPost)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AllTabuhOnTariAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AllTabuhOnTariAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerTariTabuhAdmin.stopShimmer()
        shimmerTariTabuhAdmin.visibility = View.GONE
        swipeTariTabuhAdmin.visibility   = View.VISIBLE
    }
}
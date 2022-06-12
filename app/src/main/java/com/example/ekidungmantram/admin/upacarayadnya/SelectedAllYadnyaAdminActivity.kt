package com.example.ekidungmantram.admin.upacarayadnya

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.admin.AllKidungAdminAdapter
import com.example.ekidungmantram.adapter.admin.SelectedAllYadnyaAdminAdapter
import com.example.ekidungmantram.admin.kidung.AddKidungAdminActivity
import com.example.ekidungmantram.admin.kidung.DetailKidungAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AllKidungAdminModel
import com.example.ekidungmantram.model.adminmodel.AllYadnyaAdminModel
import kotlinx.android.synthetic.main.activity_all_kidung_admin.*
import kotlinx.android.synthetic.main.activity_selected_all_yadnya_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectedAllYadnyaAdminActivity : AppCompatActivity() {
    private lateinit var yadnyaAdapter : SelectedAllYadnyaAdminAdapter
    private lateinit var setAdapter    : SelectedAllYadnyaAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected_all_yadnya_admin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-Yadnya"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_yadnya")
            val katID = bundle.getInt("id_yadnya")
            val namaPost = bundle.getString("nama_yadnya")

            namaDaftarYadnya.text = "Daftar " + namaPost

            allYadnyaAdmin1.layoutManager = LinearLayoutManager(applicationContext)
            allYadnyaAdmin2.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataYadnya(postID)

            swipeYadnyaAdmin.setOnRefreshListener {
                getAllDataYadnya(postID)
                swipeYadnyaAdmin.isRefreshing = false
            }

            fabYadnya.setOnClickListener {
                val bundles = Bundle()
                val intent = Intent(this, AddYadnyaAdminActivity::class.java)
                bundles.putInt("id_yadnya", postID)
                bundles.putString("nama_yadnya", namaPost)
                intent.putExtras(bundles)
                startActivity(intent)
            }
        }
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun getAllDataYadnya(postID: Int) {
        ApiService.endpoint.getAllListYadnyaAdmin(postID)
            .enqueue(object: Callback<ArrayList<AllYadnyaAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllYadnyaAdminModel>>,
                    response: Response<ArrayList<AllYadnyaAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null){
                        swipeYadnyaAdmin.visibility   = View.VISIBLE
                        shimmerYadnyaAdmin.visibility = View.GONE
                    }else{
                        swipeYadnyaAdmin.visibility   = View.GONE
                        shimmerYadnyaAdmin.visibility = View.VISIBLE
                    }
                    setAdapter = datalist?.let { SelectedAllYadnyaAdminAdapter(it,
                        object : SelectedAllYadnyaAdminAdapter.OnAdapterAllYadnyaAdminListener{
                            override fun onClick(result: AllYadnyaAdminModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@SelectedAllYadnyaAdminActivity, DetailYadnyaAdminActivity::class.java)
                                bundle.putInt("id_yadnya", result.id_post)
                                bundle.putInt("id_kategori", postID)
                                bundle.putString("nama_yadnya", result.nama_post)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    allYadnyaAdmin1.adapter  = setAdapter
                    noYadnyaAdmin.visibility = View.GONE
                    setShimmerToStop()

                    cariYadnyaAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noYadnyaAdmin.visibility   = View.GONE
                                    allYadnyaAdmin1.visibility = View.VISIBLE
                                    allYadnyaAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    yadnyaAdapter = SelectedAllYadnyaAdminAdapter(filter as ArrayList<AllYadnyaAdminModel>,
                                        object : SelectedAllYadnyaAdminAdapter.OnAdapterAllYadnyaAdminListener{
                                            override fun onClick(result: AllYadnyaAdminModel) {
                                                val bundle = Bundle()
                                                val intent = Intent(this@SelectedAllYadnyaAdminActivity, DetailYadnyaAdminActivity::class.java)
                                                bundle.putInt("id_yadnya", result.id_post)
                                                bundle.putInt("id_kategori", postID)
                                                bundle.putString("nama_yadnya", result.nama_post)
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
                                        })
                                    if(filter.isEmpty()){
                                        noYadnyaAdmin.visibility   = View.VISIBLE
                                        allYadnyaAdmin1.visibility = View.GONE
                                        allYadnyaAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noYadnyaAdmin.visibility   = View.GONE
                                        allYadnyaAdmin2.visibility = View.VISIBLE
                                        allYadnyaAdmin2.adapter    = yadnyaAdapter
                                        allYadnyaAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allYadnyaAdmin1.visibility = View.VISIBLE
                                        allYadnyaAdmin2.visibility = View.GONE
                                        noYadnyaAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }

                    })
                }

                override fun onFailure(call: Call<ArrayList<AllYadnyaAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun setShimmerToStop() {
        shimmerYadnyaAdmin.stopShimmer()
        shimmerYadnyaAdmin.visibility = View.GONE
        swipeYadnyaAdmin.visibility   = View.VISIBLE
    }
}
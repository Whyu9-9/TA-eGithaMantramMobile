package com.example.ekidungmantram.admin.kidung

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.admin.AllDataAdminAdapter
import com.example.ekidungmantram.adapter.admin.AllKidungAdminAdapter
import com.example.ekidungmantram.admin.adminmanager.AddAdminActivity
import com.example.ekidungmantram.admin.adminmanager.DetailAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AllDataAdminModel
import com.example.ekidungmantram.model.adminmodel.AllKidungAdminModel
import kotlinx.android.synthetic.main.activity_all_admin.*
import kotlinx.android.synthetic.main.activity_all_kidung_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllKidungAdminActivity : AppCompatActivity() {
    private lateinit var kidungAdapter : AllKidungAdminAdapter
    private lateinit var setAdapter    : AllKidungAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_kidung_admin)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-Kidung"

        allKidungAdmin1.layoutManager = LinearLayoutManager(applicationContext)
        allKidungAdmin2.layoutManager = LinearLayoutManager(applicationContext)
        getAllDataKidung()

        swipeKidungAdmin.setOnRefreshListener {
            getAllDataKidung()
            swipeKidungAdmin.isRefreshing = false
        }

        fabKidung.setOnClickListener {
            val intent = Intent(this, AddKidungAdminActivity::class.java)
            startActivity(intent)
        }
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun getAllDataKidung() {
        ApiService.endpoint.getAllListKidungAdmin()
            .enqueue(object: Callback<ArrayList<AllKidungAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllKidungAdminModel>>,
                    response: Response<ArrayList<AllKidungAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null && datalist.isNotEmpty()){
                        swipeKidungAdmin.visibility   = View.VISIBLE
                        shimmerKidungAdmin.visibility = View.GONE
                        noKidungAdmin.visibility      = View.GONE
                    }else{
                        swipeKidungAdmin.visibility   = View.GONE
                        shimmerKidungAdmin.visibility = View.VISIBLE
                        noKidungAdmin.visibility      = View.VISIBLE
                    }
                    setAdapter = datalist?.let { AllKidungAdminAdapter(it,
                        object : AllKidungAdminAdapter.OnAdapterAllKidungAdminListener{
                            override fun onClick(result: AllKidungAdminModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@AllKidungAdminActivity, DetailKidungAdminActivity::class.java)
                                bundle.putInt("id_kidung", result.id_post)
                                bundle.putString("nama_kidung", result.nama_post)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    allKidungAdmin1.adapter  = setAdapter
                    setShimmerToStop()

                    cariKidungAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noKidungAdmin.visibility   = View.GONE
                                    allKidungAdmin1.visibility = View.VISIBLE
                                    allKidungAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    kidungAdapter = AllKidungAdminAdapter(filter as ArrayList<AllKidungAdminModel>,
                                        object : AllKidungAdminAdapter.OnAdapterAllKidungAdminListener{
                                            override fun onClick(result: AllKidungAdminModel) {
                                                val bundle = Bundle()
                                                val intent = Intent(this@AllKidungAdminActivity, DetailKidungAdminActivity::class.java)
                                                bundle.putInt("id_kidung", result.id_post)
                                                bundle.putString("nama_kidung", result.nama_post)
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
                                        })
                                    if(filter.isEmpty()){
                                        noKidungAdmin.visibility   = View.VISIBLE
                                        allKidungAdmin1.visibility = View.GONE
                                        allKidungAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noKidungAdmin.visibility   = View.GONE
                                        allKidungAdmin2.visibility = View.VISIBLE
                                        allKidungAdmin2.adapter    = kidungAdapter
                                        allKidungAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allKidungAdmin1.visibility = View.VISIBLE
                                        allKidungAdmin2.visibility = View.GONE
                                        noKidungAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }

                    })
                }

                override fun onFailure(call: Call<ArrayList<AllKidungAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun setShimmerToStop() {
        shimmerKidungAdmin.stopShimmer()
        shimmerKidungAdmin.visibility = View.GONE
        swipeKidungAdmin.visibility   = View.VISIBLE
    }
}
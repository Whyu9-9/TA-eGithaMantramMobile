package com.example.ekidungmantram.admin.tabuh

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.admin.AllTabuhAdminAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AllTabuhAdminModel
import kotlinx.android.synthetic.main.activity_all_tabuh_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllTabuhAdminActivity : AppCompatActivity() {
    private lateinit var tabuhAdapter  : AllTabuhAdminAdapter
    private lateinit var setAdapter    : AllTabuhAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_tabuh_admin)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-Tabuh"

        allTabuhAdmin1.layoutManager = LinearLayoutManager(applicationContext)
        allTabuhAdmin2.layoutManager = LinearLayoutManager(applicationContext)
        getAllTabuhAdmin()

        swipeTabuhAdmin.setOnRefreshListener {
            getAllTabuhAdmin()
            swipeTabuhAdmin.isRefreshing = false
        }

        fabTabuh.setOnClickListener {
            val intent = Intent(this, AddTabuhAdminActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getAllTabuhAdmin() {
        ApiService.endpoint.getAllListTabuhAdmin()
            .enqueue(object: Callback<ArrayList<AllTabuhAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllTabuhAdminModel>>,
                    response: Response<ArrayList<AllTabuhAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null && datalist.isNotEmpty()){
                        swipeTabuhAdmin.visibility   = View.VISIBLE
                        shimmerTabuhAdmin.visibility = View.GONE
                        noTabuhAdmin.visibility      = View.GONE
                    }else{
                        swipeTabuhAdmin.visibility   = View.GONE
                        shimmerTabuhAdmin.visibility = View.VISIBLE
                        noTabuhAdmin.visibility      = View.VISIBLE
                    }
                    setAdapter = datalist?.let { AllTabuhAdminAdapter(it,
                        object : AllTabuhAdminAdapter.OnAdapterAllTabuhAdminListener{
                            override fun onClick(result: AllTabuhAdminModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@AllTabuhAdminActivity, DetailTabuhAdminActivity::class.java)
                                bundle.putInt("id_tabuh", result.id_post)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    allTabuhAdmin1.adapter  = setAdapter
                    setShimmerToStop()

                    cariTabuhAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noTabuhAdmin.visibility   = View.GONE
                                    allTabuhAdmin1.visibility = View.VISIBLE
                                    allTabuhAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    tabuhAdapter = AllTabuhAdminAdapter(filter as ArrayList<AllTabuhAdminModel>,
                                        object : AllTabuhAdminAdapter.OnAdapterAllTabuhAdminListener{
                                            override fun onClick(result: AllTabuhAdminModel) {
                                                val bundle = Bundle()
                                                val intent = Intent(this@AllTabuhAdminActivity, DetailTabuhAdminActivity::class.java)
                                                bundle.putInt("id_tabuh", result.id_post)
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
                                        })
                                    if(filter.isEmpty()){
                                        noTabuhAdmin.visibility   = View.VISIBLE
                                        allTabuhAdmin1.visibility = View.GONE
                                        allTabuhAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noTabuhAdmin.visibility   = View.GONE
                                        allTabuhAdmin2.visibility = View.VISIBLE
                                        allTabuhAdmin2.adapter    = tabuhAdapter
                                        allTabuhAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allTabuhAdmin1.visibility = View.VISIBLE
                                        allTabuhAdmin2.visibility = View.GONE
                                        noTabuhAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }

                    })
                }

                override fun onFailure(call: Call<ArrayList<AllTabuhAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerTabuhAdmin.stopShimmer()
        shimmerTabuhAdmin.visibility = View.GONE
        swipeTabuhAdmin.visibility   = View.VISIBLE
    }
}
package com.example.ekidungmantram.admin.mantram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.admin.AllMantramAdminAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AllMantramAdminModel
import kotlinx.android.synthetic.main.activity_all_mantram_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllMantramAdminActivity : AppCompatActivity() {
    private lateinit var mantramAdapter  : AllMantramAdminAdapter
    private lateinit var setAdapter      : AllMantramAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_mantram_admin)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-Mantram"

        allMantramAdmin1.layoutManager = LinearLayoutManager(applicationContext)
        allMantramAdmin2.layoutManager = LinearLayoutManager(applicationContext)
        getAllMantramAdmin()

        swipeMantramAdmin.setOnRefreshListener {
            getAllMantramAdmin()
            swipeMantramAdmin.isRefreshing = false
        }

        fabMantram.setOnClickListener {
            val intent = Intent(this, AddMantramAdminActivity::class.java)
            startActivity(intent)
        }
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun getAllMantramAdmin() {
        ApiService.endpoint.getAllMantramListAdmin()
            .enqueue(object: Callback<ArrayList<AllMantramAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllMantramAdminModel>>,
                    response: Response<ArrayList<AllMantramAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null && datalist.isNotEmpty()){
                        swipeMantramAdmin.visibility   = View.VISIBLE
                        shimmerMantramAdmin.visibility = View.GONE
                        noMantramAdmin.visibility      = View.GONE
                    }else{
                        swipeMantramAdmin.visibility   = View.GONE
                        shimmerMantramAdmin.visibility = View.VISIBLE
                        noMantramAdmin.visibility      = View.VISIBLE
                    }
                    setAdapter = datalist?.let { AllMantramAdminAdapter(it,
                        object : AllMantramAdminAdapter.OnAdapterAllMantramAdminListener{
                            override fun onClick(result: AllMantramAdminModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@AllMantramAdminActivity, DetailMantramAdminActivity::class.java)
                                bundle.putInt("id_mantram", result.id_post)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    allMantramAdmin1.adapter  = setAdapter
                    setShimmerToStop()

                    cariMantramAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noMantramAdmin.visibility   = View.GONE
                                    allMantramAdmin1.visibility = View.VISIBLE
                                    allMantramAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    mantramAdapter = AllMantramAdminAdapter(filter as ArrayList<AllMantramAdminModel>,
                                        object : AllMantramAdminAdapter.OnAdapterAllMantramAdminListener{
                                            override fun onClick(result: AllMantramAdminModel) {
                                                val bundle = Bundle()
                                                val intent = Intent(this@AllMantramAdminActivity, DetailMantramAdminActivity::class.java)
                                                bundle.putInt("id_mantram", result.id_post)
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
                                        })
                                    if(filter.isEmpty()){
                                        noMantramAdmin.visibility   = View.VISIBLE
                                        allMantramAdmin1.visibility = View.GONE
                                        allMantramAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noMantramAdmin.visibility   = View.GONE
                                        allMantramAdmin2.visibility = View.VISIBLE
                                        allMantramAdmin2.adapter    = mantramAdapter
                                        allMantramAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allMantramAdmin1.visibility = View.VISIBLE
                                        allMantramAdmin2.visibility = View.GONE
                                        noMantramAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }

                    })
                }

                override fun onFailure(call: Call<ArrayList<AllMantramAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun setShimmerToStop() {
        shimmerMantramAdmin.stopShimmer()
        shimmerMantramAdmin.visibility = View.GONE
        swipeMantramAdmin.visibility   = View.VISIBLE
    }
}
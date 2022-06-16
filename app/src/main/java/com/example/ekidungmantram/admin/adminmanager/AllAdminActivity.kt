package com.example.ekidungmantram.admin.adminmanager

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
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AllDataAdminModel
import kotlinx.android.synthetic.main.activity_all_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllAdminActivity : AppCompatActivity() {
    private lateinit var adminAdapter  : AllDataAdminAdapter
    private lateinit var setAdapter    : AllDataAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_admin)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Kelola Admin"

        allDataAdmin1.layoutManager = LinearLayoutManager(applicationContext)
        allDataAdmin2.layoutManager = LinearLayoutManager(applicationContext)
        getAllDataAdmin()

        swipeAdminManager.setOnRefreshListener {
            getAllDataAdmin()
            swipeAdminManager.isRefreshing = false
        }

        fabAdmin.setOnClickListener {
            val intent = Intent(this, AddAdminActivity::class.java)
            startActivity(intent)
        }
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun getAllDataAdmin() {
        ApiService.endpoint.getAllListAdmin()
            .enqueue(object: Callback<ArrayList<AllDataAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllDataAdminModel>>,
                    response: Response<ArrayList<AllDataAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null){
                        swipeAdminManager.visibility   = View.VISIBLE
                        shimmerAdminManager.visibility = View.GONE
                    }else{
                        swipeAdminManager.visibility   = View.GONE
                        shimmerAdminManager.visibility = View.VISIBLE
                    }
                    setAdapter = datalist?.let { AllDataAdminAdapter(it,
                        object : AllDataAdminAdapter.OnAdapterAllDataAdminListener{
                            override fun onClick(result: AllDataAdminModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@AllAdminActivity, DetailAdminActivity::class.java)
                                bundle.putInt("id_user", result.id_user)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!
                    allDataAdmin1.adapter  = setAdapter
                    noAdminData.visibility = View.GONE
                    setShimmerToStop()

                    cariDataAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noAdminData.visibility   = View.GONE
                                    allDataAdmin1.visibility = View.VISIBLE
                                    allDataAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.name.contains("$p0", true) }
                                    adminAdapter = AllDataAdminAdapter(filter as ArrayList<AllDataAdminModel>,
                                        object : AllDataAdminAdapter.OnAdapterAllDataAdminListener{
                                            override fun onClick(result: AllDataAdminModel) {
                                                val bundle = Bundle()
                                                val intent = Intent(this@AllAdminActivity, DetailAdminActivity::class.java)
                                                bundle.putInt("id_user", result.id_user)
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
                                        })
                                    if(filter.isEmpty()){
                                        noAdminData.visibility   = View.VISIBLE
                                        allDataAdmin1.visibility = View.GONE
                                        allDataAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noAdminData.visibility   = View.GONE
                                        allDataAdmin2.visibility = View.VISIBLE
                                        allDataAdmin2.adapter    = adminAdapter
                                        allDataAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allDataAdmin1.visibility = View.VISIBLE
                                        allDataAdmin2.visibility = View.GONE
                                        noAdminData.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }

                    })
                }

                override fun onFailure(call: Call<ArrayList<AllDataAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun setShimmerToStop() {
        shimmerAdminManager.stopShimmer()
        shimmerAdminManager.visibility = View.GONE
        swipeAdminManager.visibility   = View.VISIBLE
    }
}
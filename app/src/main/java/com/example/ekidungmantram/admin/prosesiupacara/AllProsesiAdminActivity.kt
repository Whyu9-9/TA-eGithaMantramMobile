package com.example.ekidungmantram.admin.prosesiupacara

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.admin.AllProsesiAdminAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AllProsesiAdminModel
import kotlinx.android.synthetic.main.activity_all_prosesi_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllProsesiAdminActivity : AppCompatActivity() {
    private lateinit var prosesiAdapter : AllProsesiAdminAdapter
    private lateinit var setAdapter     : AllProsesiAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_prosesi_admin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-Prosesi"

        allProsesiAdmin1.layoutManager = LinearLayoutManager(applicationContext)
        allProsesiAdmin2.layoutManager = LinearLayoutManager(applicationContext)
        getAllDataProsesi()

        swipeProsesiAdmin.setOnRefreshListener {
            getAllDataProsesi()
            swipeProsesiAdmin.isRefreshing = false
        }

        fabProsesi.setOnClickListener {
            val intent = Intent(this, AddProsesiAdminActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getAllDataProsesi() {
        ApiService.endpoint.getAllListProsesiAdmin()
            .enqueue(object: Callback<ArrayList<AllProsesiAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllProsesiAdminModel>>,
                    response: Response<ArrayList<AllProsesiAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null){
                        swipeProsesiAdmin.visibility   = View.VISIBLE
                        shimmerProsesiAdmin.visibility = View.GONE
                        noProsesiAdmin.visibility      = View.GONE
                    }else{
                        swipeProsesiAdmin.visibility   = View.GONE
                        shimmerProsesiAdmin.visibility = View.VISIBLE
                        noProsesiAdmin.visibility      = View.VISIBLE
                    }
                    setAdapter = datalist?.let { AllProsesiAdminAdapter(it,
                        object : AllProsesiAdminAdapter.OnAdapterAllProsesiAdminListener{
                            override fun onClick(result: AllProsesiAdminModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@AllProsesiAdminActivity, DetailProsesiAdminActivity::class.java)
                                bundle.putInt("id_prosesi", result.id_post)
                                bundle.putString("nama_prosesi", result.nama_post)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    allProsesiAdmin1.adapter  = setAdapter
                    setShimmerToStop()

                    cariProsesiAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noProsesiAdmin.visibility   = View.GONE
                                    allProsesiAdmin1.visibility = View.VISIBLE
                                    allProsesiAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    prosesiAdapter = AllProsesiAdminAdapter(filter as ArrayList<AllProsesiAdminModel>,
                                        object : AllProsesiAdminAdapter.OnAdapterAllProsesiAdminListener{
                                            override fun onClick(result: AllProsesiAdminModel) {
                                                val bundle = Bundle()
                                                val intent = Intent(this@AllProsesiAdminActivity, DetailProsesiAdminActivity::class.java)
                                                bundle.putInt("id_prosesi", result.id_post)
                                                bundle.putString("nama_prosesi", result.nama_post)
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
                                        })
                                    if(filter.isEmpty()){
                                        noProsesiAdmin.visibility   = View.VISIBLE
                                        allProsesiAdmin1.visibility = View.GONE
                                        allProsesiAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noProsesiAdmin.visibility   = View.GONE
                                        allProsesiAdmin2.visibility = View.VISIBLE
                                        allProsesiAdmin2.adapter    = prosesiAdapter
                                        allProsesiAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allProsesiAdmin1.visibility = View.VISIBLE
                                        allProsesiAdmin2.visibility = View.GONE
                                        noProsesiAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }
                    })
                }

                override fun onFailure(call: Call<ArrayList<AllProsesiAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerProsesiAdmin.stopShimmer()
        shimmerProsesiAdmin.visibility = View.GONE
        swipeProsesiAdmin.visibility   = View.VISIBLE
    }
}
package com.example.ekidungmantram.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.AllMantramAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.AllMantramModel
import kotlinx.android.synthetic.main.activity_all_mantram.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllMantramActivity : AppCompatActivity() {
    private lateinit var mantramAdapter  : AllMantramAdapter
    private lateinit var setAdapter      : AllMantramAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_mantram)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-Mantram"

        allMantram1.layoutManager = LinearLayoutManager(applicationContext)
        allMantram2.layoutManager = LinearLayoutManager(applicationContext)
        getAllMantram()

        swipeMantram.setOnRefreshListener {
            getAllMantram()
            swipeMantram.isRefreshing = false
        }
    }

    private fun printLog(message: String) {
        Log.d("AllMantramActivity", message)
    }

    private fun getAllMantram() {
        ApiService.endpoint.getMantramMasterList()
            .enqueue(object: Callback<ArrayList<AllMantramModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllMantramModel>>,
                    response: Response<ArrayList<AllMantramModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null && datalist.isNotEmpty()){
                        swipeMantram.visibility   = View.VISIBLE
                        shimmerMantram.visibility = View.GONE
                        noMantram.visibility      = View.GONE
                    }else{
                        swipeMantram.visibility   = View.GONE
                        shimmerMantram.visibility = View.VISIBLE
                        noMantram.visibility      = View.VISIBLE
                    }
                    setAdapter = datalist?.let { AllMantramAdapter(it,
                        object : AllMantramAdapter.OnAdapterAllMantramListener{
                            override fun onClick(result: AllMantramModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@AllMantramActivity, DetailMantramActivity::class.java)
                                bundle.putInt("id_mantram", result.id_post)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    allMantram1.adapter  = setAdapter
                    setShimmerToStop()

                    cariMantram.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noMantram.visibility   = View.GONE
                                    allMantram1.visibility = View.VISIBLE
                                    allMantram2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    mantramAdapter = AllMantramAdapter(filter as ArrayList<AllMantramModel>,
                                        object : AllMantramAdapter.OnAdapterAllMantramListener{
                                            override fun onClick(result: AllMantramModel) {
                                                val bundle = Bundle()
                                                val intent = Intent(this@AllMantramActivity, DetailMantramActivity::class.java)
                                                bundle.putInt("id_mantram", result.id_post)
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
                                        })
                                    if(filter.isEmpty()){
                                        noMantram.visibility   = View.VISIBLE
                                        allMantram1.visibility = View.GONE
                                        allMantram2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noMantram.visibility   = View.GONE
                                        allMantram2.visibility = View.VISIBLE
                                        allMantram2.adapter    = mantramAdapter
                                        allMantram1.visibility = View.INVISIBLE
                                    }else{
                                        allMantram1.visibility = View.VISIBLE
                                        allMantram2.visibility = View.GONE
                                        noMantram.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }

                    })
                }

                override fun onFailure(call: Call<ArrayList<AllMantramModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun setShimmerToStop() {
        shimmerMantram.stopShimmer()
        shimmerMantram.visibility = View.GONE
        swipeMantram.visibility   = View.VISIBLE
    }
}
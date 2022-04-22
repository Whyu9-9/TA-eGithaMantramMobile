package com.example.ekidungmantram.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.AllTabuhAdapter
import com.example.ekidungmantram.adapter.AllTariAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.AllTabuhModel
import com.example.ekidungmantram.model.AllTariModel
import kotlinx.android.synthetic.main.activity_all_mantram.*
import kotlinx.android.synthetic.main.activity_all_tabuh.*
import kotlinx.android.synthetic.main.activity_all_tari.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllTabuhActivity : AppCompatActivity() {
    private lateinit var tabuhAdapter : AllTabuhAdapter
    private lateinit var setAdapter   : AllTabuhAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_tabuh)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-Tabuh"

        allTabuh1.layoutManager = LinearLayoutManager(applicationContext)
        allTabuh2.layoutManager = LinearLayoutManager(applicationContext)
        getAllTabuh()

        swipeTabuh.setOnRefreshListener {
            getAllTabuh()
            swipeTabuh.isRefreshing = false
        }
    }

    private fun printLog(message: String) {
        Log.d("AllTabuhActivity", message)
    }

    private fun getAllTabuh() {
        ApiService.endpoint.getTabuhMasterList()
            .enqueue(object: Callback<ArrayList<AllTabuhModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllTabuhModel>>,
                    response: Response<ArrayList<AllTabuhModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null){
                        swipeTabuh.visibility   = View.VISIBLE
                        shimmerTabuh.visibility = View.GONE
                    }else{
                        swipeTabuh.visibility   = View.GONE
                        shimmerTabuh.visibility = View.VISIBLE
                    }
                    setAdapter = datalist?.let { AllTabuhAdapter(it,
                        object : AllTabuhAdapter.OnAdapterAllTabuhListener{
                            override fun onClick(result: AllTabuhModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@AllTabuhActivity, DetailTabuhActivity::class.java)
                                bundle.putInt("id_tabuh", result.id_post)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    allTabuh1.adapter  = setAdapter
                    noTabuh.visibility = View.GONE
                    setShimmerToStop()

                    cariTabuh.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noTabuh.visibility   = View.GONE
                                    allTabuh1.visibility = View.VISIBLE
                                    allTabuh2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    tabuhAdapter = AllTabuhAdapter(filter as ArrayList<AllTabuhModel>,
                                        object : AllTabuhAdapter.OnAdapterAllTabuhListener{
                                            override fun onClick(result: AllTabuhModel) {
                                                val bundle = Bundle()
                                                val intent = Intent(this@AllTabuhActivity, DetailTabuhActivity::class.java)
                                                bundle.putInt("id_tabuh", result.id_post)
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
                                        })
                                    if(filter.isEmpty()){
                                        noTabuh.visibility   = View.VISIBLE
                                        allTabuh1.visibility = View.GONE
                                        allTabuh2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noTabuh.visibility   = View.GONE
                                        allTabuh2.visibility = View.VISIBLE
                                        allTabuh2.adapter    = tabuhAdapter
                                        allTabuh1.visibility = View.INVISIBLE
                                    }else{
                                        allTabuh1.visibility = View.VISIBLE
                                        allTabuh2.visibility = View.GONE
                                        noTabuh.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }

                    })
                }

                override fun onFailure(call: Call<ArrayList<AllTabuhModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun setShimmerToStop() {
        shimmerTabuh.stopShimmer()
        shimmerTabuh.visibility = View.GONE
        swipeTabuh.visibility   = View.VISIBLE
    }
}
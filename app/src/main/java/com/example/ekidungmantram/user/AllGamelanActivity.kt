package com.example.ekidungmantram.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.AllGamelanAdapter
import com.example.ekidungmantram.adapter.AllKidungAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.AllGamelanModel
import com.example.ekidungmantram.model.AllKidungModel
import kotlinx.android.synthetic.main.activity_all_gamelan.*
import kotlinx.android.synthetic.main.activity_all_kidung.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllGamelanActivity : AppCompatActivity() {
    private lateinit var gamelanAdapter : AllGamelanAdapter
    private lateinit var setAdapter     : AllGamelanAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_gamelan)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-Gamelan"

        allGamelan1.layoutManager = LinearLayoutManager(applicationContext)
        allGamelan2.layoutManager = LinearLayoutManager(applicationContext)
        getAllGamelan()

        swipeGamelan.setOnRefreshListener {
            getAllGamelan()
            swipeGamelan.isRefreshing = false
        }
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun getAllGamelan() {
        ApiService.endpoint.getGamelanMasterList()
            .enqueue(object: Callback<ArrayList<AllGamelanModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllGamelanModel>>,
                    response: Response<ArrayList<AllGamelanModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null){
                        swipeGamelan.visibility   = View.VISIBLE
                        shimmerGamelan.visibility = View.GONE
                    }else{
                        swipeGamelan.visibility   = View.GONE
                        shimmerGamelan.visibility = View.VISIBLE
                    }
                    setAdapter = datalist?.let { AllGamelanAdapter(it,
                        object : AllGamelanAdapter.OnAdapterAllGamelanListener{
                            override fun onClick(result: AllGamelanModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@AllGamelanActivity, DetailGamelanActivity::class.java)
                                bundle.putInt("id_gamelan", result.id_post)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    allGamelan1.adapter  = setAdapter
                    noGamelan.visibility = View.GONE
                    setShimmerToStop()

                    cariGamelan.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noGamelan.visibility   = View.GONE
                                    allGamelan1.visibility = View.VISIBLE
                                    allGamelan2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    gamelanAdapter = AllGamelanAdapter(filter as ArrayList<AllGamelanModel>,
                                        object : AllGamelanAdapter.OnAdapterAllGamelanListener{
                                            override fun onClick(result: AllGamelanModel) {
                                                val bundle = Bundle()
                                                val intent = Intent(this@AllGamelanActivity, DetailGamelanActivity::class.java)
                                                bundle.putInt("id_gamelan", result.id_post)
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
                                        })
                                    if(filter.isEmpty()){
                                        noGamelan.visibility   = View.VISIBLE
                                        allGamelan1.visibility = View.GONE
                                        allGamelan2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noGamelan.visibility   = View.GONE
                                        allGamelan2.visibility = View.VISIBLE
                                        allGamelan2.adapter    = gamelanAdapter
                                        allGamelan1.visibility = View.INVISIBLE
                                    }else{
                                        allGamelan1.visibility = View.VISIBLE
                                        allGamelan2.visibility = View.GONE
                                        noGamelan.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }

                    })
                }

                override fun onFailure(call: Call<ArrayList<AllGamelanModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun setShimmerToStop() {
        shimmerGamelan.stopShimmer()
        shimmerGamelan.visibility = View.GONE
        swipeGamelan.visibility   = View.VISIBLE
    }
}
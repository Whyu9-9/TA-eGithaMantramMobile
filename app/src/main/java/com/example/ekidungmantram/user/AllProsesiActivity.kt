package com.example.ekidungmantram.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.AllProsesiAdapter
import com.example.ekidungmantram.adapter.AllTariAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.AllProsesiModel
import com.example.ekidungmantram.model.AllTariModel
import kotlinx.android.synthetic.main.activity_all_mantram.*
import kotlinx.android.synthetic.main.activity_all_prosesi.*
import kotlinx.android.synthetic.main.activity_all_tari.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllProsesiActivity : AppCompatActivity() {
    private lateinit var prosesiAdapter : AllProsesiAdapter
    private lateinit var setAdapter     : AllProsesiAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_prosesi)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-Prosesi"

        allProsesi1.layoutManager = LinearLayoutManager(applicationContext)
        allProsesi2.layoutManager = LinearLayoutManager(applicationContext)
        getAllProsesi()

        swipeProsesi.setOnRefreshListener {
            getAllProsesi()
            swipeProsesi.isRefreshing = false
        }
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun getAllProsesi() {
        ApiService.endpoint.getProsesiMasterList()
            .enqueue(object: Callback<ArrayList<AllProsesiModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllProsesiModel>>,
                    response: Response<ArrayList<AllProsesiModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null){
                        swipeProsesi.visibility   = View.VISIBLE
                        shimmerProsesi.visibility = View.GONE
                    }else{
                        swipeProsesi.visibility   = View.GONE
                        shimmerProsesi.visibility = View.VISIBLE
                    }
                    setAdapter = datalist?.let { AllProsesiAdapter(it,
                        object : AllProsesiAdapter.OnAdapterAllProsesiListener{
                            override fun onClick(result: AllProsesiModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@AllProsesiActivity, DetailProsesiActivity::class.java)
                                bundle.putInt("id_prosesi", result.id_post)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    allProsesi1.adapter  = setAdapter
                    noProsesi.visibility = View.GONE
                    setShimmerToStop()

                    cariProsesi.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noProsesi.visibility   = View.GONE
                                    allProsesi1.visibility = View.VISIBLE
                                    allProsesi2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    prosesiAdapter = AllProsesiAdapter(filter as ArrayList<AllProsesiModel>,
                                        object : AllProsesiAdapter.OnAdapterAllProsesiListener{
                                            override fun onClick(result: AllProsesiModel) {
                                                val bundle = Bundle()
                                                val intent = Intent(this@AllProsesiActivity, DetailProsesiActivity::class.java)
                                                bundle.putInt("id_prosesi", result.id_post)
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
                                        })
                                    if(filter.isEmpty()){
                                        noProsesi.visibility   = View.VISIBLE
                                        allProsesi1.visibility = View.GONE
                                        allProsesi2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noProsesi.visibility   = View.GONE
                                        allProsesi2.visibility = View.VISIBLE
                                        allProsesi2.adapter    = prosesiAdapter
                                        allProsesi1.visibility = View.INVISIBLE
                                    }else{
                                        allProsesi1.visibility = View.VISIBLE
                                        allProsesi2.visibility = View.GONE
                                        noProsesi.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }

                    })
                }

                override fun onFailure(call: Call<ArrayList<AllProsesiModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun setShimmerToStop() {
        shimmerProsesi.stopShimmer()
        shimmerProsesi.visibility = View.GONE
        swipeProsesi.visibility   = View.VISIBLE
    }
}
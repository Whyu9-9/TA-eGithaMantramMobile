package com.example.ekidungmantram.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.AllKidungAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.AllKidungModel
import kotlinx.android.synthetic.main.activity_all_kidung.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllKidungActivity : AppCompatActivity() {
    private lateinit var kidungAdapter : AllKidungAdapter
    private lateinit var setAdapter    : AllKidungAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_kidung)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-Kidung"

        allKidung1.layoutManager = LinearLayoutManager(applicationContext)
        allKidung2.layoutManager = LinearLayoutManager(applicationContext)
        getAllKidung()

        swipeKidung.setOnRefreshListener {
            getAllKidung()
            swipeKidung.isRefreshing = false
        }
    }

    private fun printLog(message: String) {
        Log.d("AllKidungActivity", message)
    }

    private fun getAllKidung() {
        ApiService.endpoint.getKidungMasterList()
            .enqueue(object: Callback<ArrayList<AllKidungModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllKidungModel>>,
                    response: Response<ArrayList<AllKidungModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null && datalist.isNotEmpty()){
                        swipeKidung.visibility   = View.VISIBLE
                        shimmerKidung.visibility = View.GONE
                        noKidung.visibility      = View.GONE
                    }else{
                        swipeKidung.visibility   = View.GONE
                        shimmerKidung.visibility = View.VISIBLE
                        noKidung.visibility      = View.VISIBLE
                    }
                    setAdapter = datalist?.let { AllKidungAdapter(it,
                        object : AllKidungAdapter.OnAdapterAllKidungListener{
                            override fun onClick(result: AllKidungModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@AllKidungActivity, DetailKidungActivity::class.java)
                                bundle.putInt("id_kidung", result.id_post)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    allKidung1.adapter  = setAdapter
                    setShimmerToStop()

                    cariKidung.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noKidung.visibility   = View.GONE
                                    allKidung1.visibility = View.VISIBLE
                                    allKidung2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    kidungAdapter = AllKidungAdapter(filter as ArrayList<AllKidungModel>,
                                        object : AllKidungAdapter.OnAdapterAllKidungListener{
                                            override fun onClick(result: AllKidungModel) {
                                                val bundle = Bundle()
                                                val intent = Intent(this@AllKidungActivity, DetailKidungActivity::class.java)
                                                bundle.putInt("id_kidung", result.id_post)
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
                                        })
                                    if(filter.isEmpty()){
                                        noKidung.visibility   = View.VISIBLE
                                        allKidung1.visibility = View.GONE
                                        allKidung2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noKidung.visibility   = View.GONE
                                        allKidung2.visibility = View.VISIBLE
                                        allKidung2.adapter    = kidungAdapter
                                        allKidung1.visibility = View.INVISIBLE
                                    }else{
                                        allKidung1.visibility = View.VISIBLE
                                        allKidung2.visibility = View.GONE
                                        noKidung.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }

                    })
                }

                override fun onFailure(call: Call<ArrayList<AllKidungModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun setShimmerToStop() {
        shimmerKidung.stopShimmer()
        shimmerKidung.visibility = View.GONE
        swipeKidung.visibility   = View.VISIBLE
    }

}
package com.example.ekidungmantram.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.AllTariAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.AllTariModel
import kotlinx.android.synthetic.main.activity_all_mantram.*
import kotlinx.android.synthetic.main.activity_all_tari.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllTariActivity : AppCompatActivity() {
    private lateinit var tariAdapter : AllTariAdapter
    private lateinit var setAdapter  : AllTariAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_tari)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-Tari"

        allTari1.layoutManager = LinearLayoutManager(applicationContext)
        allTari2.layoutManager = LinearLayoutManager(applicationContext)
        getAllTari()

        swipeTari.setOnRefreshListener {
            getAllTari()
            swipeTari.isRefreshing = false
        }
    }

    private fun printLog(message: String) {
        Log.d("AllTariActivity", message)
    }

    private fun getAllTari() {
        ApiService.endpoint.getTariMasterList()
            .enqueue(object: Callback<ArrayList<AllTariModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllTariModel>>,
                    response: Response<ArrayList<AllTariModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null && datalist.isNotEmpty()){
                        swipeTari.visibility   = View.VISIBLE
                        shimmerTari.visibility = View.GONE
                        noTari.visibility      = View.GONE
                    }else{
                        swipeTari.visibility   = View.GONE
                        shimmerTari.visibility = View.VISIBLE
                        noTari.visibility      = View.VISIBLE
                    }
                    setAdapter = datalist?.let { AllTariAdapter(it,
                        object : AllTariAdapter.OnAdapterAllTariListener{
                            override fun onClick(result: AllTariModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@AllTariActivity, DetailTariActivity::class.java)
                                bundle.putInt("id_tari", result.id_post)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    allTari1.adapter  = setAdapter
                    setShimmerToStop()

                    cariTari.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noTari.visibility   = View.GONE
                                    allTari1.visibility = View.VISIBLE
                                    allTari2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    tariAdapter = AllTariAdapter(filter as ArrayList<AllTariModel>,
                                        object : AllTariAdapter.OnAdapterAllTariListener{
                                            override fun onClick(result: AllTariModel) {
                                                val bundle = Bundle()
                                                val intent = Intent(this@AllTariActivity, DetailTariActivity::class.java)
                                                bundle.putInt("id_tari", result.id_post)
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
                                        })
                                    if(filter.isEmpty()){
                                        noTari.visibility   = View.VISIBLE
                                        allTari1.visibility = View.GONE
                                        allTari2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noTari.visibility   = View.GONE
                                        allTari2.visibility = View.VISIBLE
                                        allTari2.adapter    = tariAdapter
                                        allTari1.visibility = View.INVISIBLE
                                    }else{
                                        allTari1.visibility = View.VISIBLE
                                        allTari2.visibility = View.GONE
                                        noTari.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }

                    })
                }

                override fun onFailure(call: Call<ArrayList<AllTariModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun setShimmerToStop() {
        shimmerTari.stopShimmer()
        shimmerTari.visibility = View.GONE
        swipeTari.visibility   = View.VISIBLE
    }
}
package com.example.ekidungmantram.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.YadnyaCardClickedAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.YadnyaCardClickedModel
import kotlinx.android.synthetic.main.activity_yadnya.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class YadnyaActivity : AppCompatActivity() {
    private lateinit var yadnyaAdapter : YadnyaCardClickedAdapter
    private lateinit var setAdapter    : YadnyaCardClickedAdapter
    var id : Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yadnya)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        allYadnyaCard1.layoutManager = LinearLayoutManager(applicationContext)
        allYadnyaCard2.layoutManager = LinearLayoutManager(applicationContext)

        val bundle :Bundle ?=intent.extras
        if (bundle!=null){
            val message = bundle.getString("nama_yadnya")

            supportActionBar!!.title = message
            clickCardYadnya.text = "Daftar "+message

            getAllYadnyaClickedCard(message!!.toString())

            swipeYadnya.setOnRefreshListener {
                getAllYadnyaClickedCard(message!!.toString())
                swipeYadnya.isRefreshing = false
            }
        }
    }

    private fun printLog(message: String) {
        Log.d("YadnyaActivity", message)
    }

    private fun getAllYadnyaClickedCard(nama_yadnya: String) {
        ApiService.endpoint.getYadnyaCardClickedList(nama_yadnya)
            .enqueue(object: Callback<ArrayList<YadnyaCardClickedModel>> {
                override fun onResponse(
                    call: Call<ArrayList<YadnyaCardClickedModel>>,
                    response: Response<ArrayList<YadnyaCardClickedModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null){
                        swipeYadnya.visibility   = View.VISIBLE
                        shimmerYadnya.visibility = View.GONE
                    }else{
                        swipeYadnya.visibility   = View.GONE
                        shimmerYadnya.visibility = View.VISIBLE
                    }
                    setAdapter = datalist?.let { YadnyaCardClickedAdapter(it,
                        object : YadnyaCardClickedAdapter.OnAdapterYadnyaCardClickedListener{
                            override fun onClick(result: YadnyaCardClickedModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@YadnyaActivity, DetailYadnyaActivity::class.java)
                                bundle.putInt("id_yadnya", result.id_post)
                                bundle.putInt("id_kategori", result.id_kategori)
                                bundle.putString("nama_yadnya", result.nama_post)
                                bundle.putString("kategori", result.kategori)
                                bundle.putString("gambar", result.gambar)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    allYadnyaCard1.adapter  = setAdapter
                    noYadnya.visibility = View.GONE
                    setShimmerToStop()

                    cariYadnyaCard.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noYadnya.visibility   = View.GONE
                                    allYadnyaCard1.visibility = View.VISIBLE
                                    allYadnyaCard2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    yadnyaAdapter = YadnyaCardClickedAdapter(filter as ArrayList<YadnyaCardClickedModel>,
                                        object : YadnyaCardClickedAdapter.OnAdapterYadnyaCardClickedListener{
                                            override fun onClick(result: YadnyaCardClickedModel) {
                                                val bundle = Bundle()
                                                val intent = Intent(this@YadnyaActivity, DetailYadnyaActivity::class.java)
                                                bundle.putInt("id_yadnya", result.id_post)
                                                bundle.putInt("id_kategori", result.id_kategori)
                                                bundle.putString("nama_yadnya", result.nama_post)
                                                bundle.putString("kategori", result.kategori)
                                                bundle.putString("gambar", result.gambar)
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
                                        })
                                    if(filter.isEmpty()){
                                        noYadnya.visibility   = View.VISIBLE
                                        allYadnyaCard1.visibility = View.GONE
                                        allYadnyaCard2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noYadnya.visibility   = View.GONE
                                        allYadnyaCard2.visibility = View.VISIBLE
                                        allYadnyaCard2.adapter    = yadnyaAdapter
                                        allYadnyaCard1.visibility = View.INVISIBLE
                                    }else{
                                        allYadnyaCard1.visibility = View.VISIBLE
                                        allYadnyaCard2.visibility = View.GONE
                                        noYadnya.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }

                    })
                }

                override fun onFailure(call: Call<ArrayList<YadnyaCardClickedModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun setShimmerToStop() {
        shimmerYadnya.stopShimmer()
        shimmerYadnya.visibility = View.GONE
        swipeYadnya.visibility   = View.VISIBLE
    }
}
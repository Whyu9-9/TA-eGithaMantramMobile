package com.example.ekidungmantram.admin.tari

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.admin.AllGamelanAdminAdapter
import com.example.ekidungmantram.adapter.admin.AllTariAdminAdapter
import com.example.ekidungmantram.admin.gamelan.AddGamelanAdminActivity
import com.example.ekidungmantram.admin.gamelan.DetailGamelanAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AllGamelanAdminModel
import com.example.ekidungmantram.model.adminmodel.AllTariAdminModel
import kotlinx.android.synthetic.main.activity_all_gamelan_admin.*
import kotlinx.android.synthetic.main.activity_all_tari_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllTariAdminActivity : AppCompatActivity() {
    private lateinit var tariAdapter : AllTariAdminAdapter
    private lateinit var setAdapter     : AllTariAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_tari_admin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-Tari"

        allTariAdmin1.layoutManager = LinearLayoutManager(applicationContext)
        allTariAdmin2.layoutManager = LinearLayoutManager(applicationContext)
        getAllDataTari()

        swipeTariAdmin.setOnRefreshListener {
            getAllDataTari()
            swipeTariAdmin.isRefreshing = false
        }

        fabTari.setOnClickListener {
            val intent = Intent(this, AddTariAdminActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getAllDataTari() {
        ApiService.endpoint.getAllListTariAdmin()
            .enqueue(object: Callback<ArrayList<AllTariAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllTariAdminModel>>,
                    response: Response<ArrayList<AllTariAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null){
                        swipeTariAdmin.visibility   = View.VISIBLE
                        shimmerTariAdmin.visibility = View.GONE
                    }else{
                        swipeTariAdmin.visibility   = View.GONE
                        shimmerTariAdmin.visibility = View.VISIBLE
                    }
                    setAdapter = datalist?.let { AllTariAdminAdapter(it,
                        object : AllTariAdminAdapter.OnAdapterAllTariAdminListener{
                            override fun onClick(result: AllTariAdminModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@AllTariAdminActivity, DetailTariAdminActivity::class.java)
                                bundle.putInt("id_tari", result.id_post)
                                bundle.putString("nama_tari", result.nama_post)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    allTariAdmin1.adapter  = setAdapter
                    noTariAdmin.visibility = View.GONE
                    setShimmerToStop()

                    cariTariAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noTariAdmin.visibility   = View.GONE
                                    allTariAdmin1.visibility = View.VISIBLE
                                    allTariAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    tariAdapter = AllTariAdminAdapter(filter as ArrayList<AllTariAdminModel>,
                                        object : AllTariAdminAdapter.OnAdapterAllTariAdminListener{
                                            override fun onClick(result: AllTariAdminModel) {
                                                val bundle = Bundle()
                                                val intent = Intent(this@AllTariAdminActivity, DetailTariAdminActivity::class.java)
                                                bundle.putInt("id_tari", result.id_post)
                                                bundle.putString("nama_tari", result.nama_post)
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
                                        })
                                    if(filter.isEmpty()){
                                        noTariAdmin.visibility   = View.VISIBLE
                                        allTariAdmin1.visibility = View.GONE
                                        allTariAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noTariAdmin.visibility   = View.GONE
                                        allTariAdmin2.visibility = View.VISIBLE
                                        allTariAdmin2.adapter    = tariAdapter
                                        allTariAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allTariAdmin1.visibility = View.VISIBLE
                                        allTariAdmin2.visibility = View.GONE
                                        noTariAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }
                    })
                }

                override fun onFailure(call: Call<ArrayList<AllTariAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerTariAdmin.stopShimmer()
        shimmerTariAdmin.visibility = View.GONE
        swipeTariAdmin.visibility   = View.VISIBLE
    }
}
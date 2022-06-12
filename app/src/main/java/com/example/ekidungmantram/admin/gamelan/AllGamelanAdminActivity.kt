package com.example.ekidungmantram.admin.gamelan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.admin.AllGamelanAdminAdapter
import com.example.ekidungmantram.adapter.admin.AllKidungAdminAdapter
import com.example.ekidungmantram.admin.kidung.AddKidungAdminActivity
import com.example.ekidungmantram.admin.kidung.DetailKidungAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AllGamelanAdminModel
import com.example.ekidungmantram.model.adminmodel.AllKidungAdminModel
import kotlinx.android.synthetic.main.activity_all_gamelan.*
import kotlinx.android.synthetic.main.activity_all_gamelan_admin.*
import kotlinx.android.synthetic.main.activity_all_kidung_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllGamelanAdminActivity : AppCompatActivity() {
    private lateinit var gamelanAdapter : AllGamelanAdminAdapter
    private lateinit var setAdapter     : AllGamelanAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_gamelan_admin)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-Gamelan"

        allGamelanAdmin1.layoutManager = LinearLayoutManager(applicationContext)
        allGamelanAdmin2.layoutManager = LinearLayoutManager(applicationContext)
        getAllDataGamelan()

        swipeGamelanAdmin.setOnRefreshListener {
            getAllDataGamelan()
            swipeGamelanAdmin.isRefreshing = false
        }

        fabGamelan.setOnClickListener {
            val intent = Intent(this, AddGamelanAdminActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getAllDataGamelan() {
        ApiService.endpoint.getAllListGamelanAdmin()
            .enqueue(object: Callback<ArrayList<AllGamelanAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllGamelanAdminModel>>,
                    response: Response<ArrayList<AllGamelanAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null){
                        swipeGamelanAdmin.visibility   = View.VISIBLE
                        shimmerGamelanAdmin.visibility = View.GONE
                        noGamelanAdmin.visibility      = View.GONE
                    }else{
                        swipeGamelanAdmin.visibility   = View.GONE
                        shimmerGamelanAdmin.visibility = View.VISIBLE
                        noGamelanAdmin.visibility      = View.VISIBLE
                    }
                    setAdapter = datalist?.let { AllGamelanAdminAdapter(it,
                        object : AllGamelanAdminAdapter.OnAdapterAllGamelanAdminListener{
                            override fun onClick(result: AllGamelanAdminModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@AllGamelanAdminActivity, DetailGamelanAdminActivity::class.java)
                                bundle.putInt("id_gamelan", result.id_post)
                                bundle.putString("nama_gamelan", result.nama_post)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    allGamelanAdmin1.adapter  = setAdapter
                    setShimmerToStop()

                    cariGamelanAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noGamelanAdmin.visibility   = View.GONE
                                    allGamelanAdmin1.visibility = View.VISIBLE
                                    allGamelanAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    gamelanAdapter = AllGamelanAdminAdapter(filter as ArrayList<AllGamelanAdminModel>,
                                        object : AllGamelanAdminAdapter.OnAdapterAllGamelanAdminListener{
                                            override fun onClick(result: AllGamelanAdminModel) {
                                                val bundle = Bundle()
                                                val intent = Intent(this@AllGamelanAdminActivity, DetailGamelanAdminActivity::class.java)
                                                bundle.putInt("id_gamelan", result.id_post)
                                                bundle.putString("nama_gamelan", result.nama_post)
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
                                        })
                                    if(filter.isEmpty()){
                                        noGamelanAdmin.visibility   = View.VISIBLE
                                        allGamelanAdmin1.visibility = View.GONE
                                        allGamelanAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noGamelanAdmin.visibility   = View.GONE
                                        allGamelanAdmin2.visibility = View.VISIBLE
                                        allGamelanAdmin2.adapter    = gamelanAdapter
                                        allGamelanAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allGamelanAdmin1.visibility = View.VISIBLE
                                        allGamelanAdmin2.visibility = View.GONE
                                        noGamelanAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }
                    })
                }

                override fun onFailure(call: Call<ArrayList<AllGamelanAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerGamelanAdmin.stopShimmer()
        shimmerGamelanAdmin.visibility = View.GONE
        swipeGamelanAdmin.visibility   = View.VISIBLE
    }
}
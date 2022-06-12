package com.example.ekidungmantram.admin.upacarayadnya

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.admin.AllYadnyaAdminAdapter
import com.example.ekidungmantram.admin.ListYadnyaAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AllYadnyaHomeAdminModel
import kotlinx.android.synthetic.main.activity_all_yadnya_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllYadnyaAdminActivity : AppCompatActivity() {
    private lateinit var yadnyaAdapter : AllYadnyaAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_yadnya_admin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-Yadnya"

        allListYadnyaAdmin.layoutManager = LinearLayoutManager(applicationContext)
        getAllDataYadnya()

        swipeListYadnyaAdmin.setOnRefreshListener {
            getAllDataYadnya()
            swipeListYadnyaAdmin.isRefreshing = false
        }
    }

    private fun getAllDataYadnya() {
        ApiService.endpoint.getYadnyaAdminHomeList()
            .enqueue(object: Callback<ArrayList<AllYadnyaHomeAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllYadnyaHomeAdminModel>>,
                    response: Response<ArrayList<AllYadnyaHomeAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null){
                        swipeListYadnyaAdmin.visibility = View.VISIBLE
                        shimmerListYadnyaAdmin.visibility = View.GONE
                    }else{
                        swipeListYadnyaAdmin.visibility = View.GONE
                        shimmerListYadnyaAdmin.visibility = View.VISIBLE
                    }
                    yadnyaAdapter = datalist?.let { AllYadnyaAdminAdapter(it,
                        object : AllYadnyaAdminAdapter.OnAdapterAllYadnyaHomeAdminListener{
                            override fun onClick(result: AllYadnyaHomeAdminModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@AllYadnyaAdminActivity, SelectedAllYadnyaAdminActivity::class.java)
                                bundle.putInt("id_yadnya", result.id_kategori)
                                bundle.putString("nama_yadnya", result.nama_kategori)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    allListYadnyaAdmin.adapter = yadnyaAdapter
                    setShimmerToStop()
                }

                override fun onFailure(call: Call<ArrayList<AllYadnyaHomeAdminModel>>, t: Throwable) {
                    Toast.makeText(this@AllYadnyaAdminActivity, "No Connection", Toast.LENGTH_SHORT).show()
                    setShimmerToStop()
                }

            })
    }

    private fun setShimmerToStop() {
        shimmerListYadnyaAdmin.stopShimmer()
        shimmerListYadnyaAdmin.visibility = View.GONE
        swipeListYadnyaAdmin.visibility       = View.VISIBLE
    }
}
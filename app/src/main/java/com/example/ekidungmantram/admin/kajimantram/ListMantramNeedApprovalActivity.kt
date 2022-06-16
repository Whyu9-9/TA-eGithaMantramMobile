package com.example.ekidungmantram.admin.kajimantram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.admin.AllMantramAdminAdapter
import com.example.ekidungmantram.adapter.admin.AllMantramNeedApprovalAdminAdapter
import com.example.ekidungmantram.admin.mantram.DetailMantramAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AllMantramAdminModel
import kotlinx.android.synthetic.main.activity_all_mantram_admin.*
import kotlinx.android.synthetic.main.activity_list_mantram_need_approval.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListMantramNeedApprovalActivity : AppCompatActivity() {
    private lateinit var mantramAdapter  : AllMantramNeedApprovalAdminAdapter
    private lateinit var setAdapter      : AllMantramNeedApprovalAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_mantram_need_approval)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "E-Mantram"

        allMantramNAAdmin1.layoutManager = LinearLayoutManager(applicationContext)
        allMantramNAAdmin2.layoutManager = LinearLayoutManager(applicationContext)
        getAllMantramNAAdmin()

        swipeMantramNAAdmin.setOnRefreshListener {
            getAllMantramNAAdmin()
            swipeMantramNAAdmin.isRefreshing = false
        }
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun getAllMantramNAAdmin() {
        ApiService.endpoint.getAllNotApprovedMantramListAdmin()
            .enqueue(object: Callback<ArrayList<AllMantramAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllMantramAdminModel>>,
                    response: Response<ArrayList<AllMantramAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null && datalist.isNotEmpty()){
                        swipeMantramNAAdmin.visibility   = View.VISIBLE
                        shimmerMantramNAAdmin.visibility = View.GONE
                        noMantramNAAdmin.visibility      = View.GONE
                    }else{
                        swipeMantramNAAdmin.visibility   = View.GONE
                        shimmerMantramNAAdmin.visibility = View.VISIBLE
                        noMantramNAAdmin.visibility      = View.VISIBLE
                    }
                    setAdapter = datalist?.let { AllMantramNeedApprovalAdminAdapter(it,
                        object : AllMantramNeedApprovalAdminAdapter.OnAdapterAllMantramNAAdminListener{
                            override fun onClick(result: AllMantramAdminModel) {
                                val bundle = Bundle()
                                val intent = Intent(this@ListMantramNeedApprovalActivity, DetailMantramNeedApprovalActivity::class.java)
                                bundle.putInt("id_mantram", result.id_post)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    allMantramNAAdmin1.adapter  = setAdapter
                    setShimmerToStop()

                    cariMantramNAAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noMantramNAAdmin.visibility   = View.GONE
                                    allMantramNAAdmin1.visibility = View.VISIBLE
                                    allMantramNAAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    mantramAdapter = AllMantramNeedApprovalAdminAdapter(filter as ArrayList<AllMantramAdminModel>,
                                        object : AllMantramNeedApprovalAdminAdapter.OnAdapterAllMantramNAAdminListener{
                                            override fun onClick(result: AllMantramAdminModel) {
                                                val bundle = Bundle()
                                                val intent = Intent(this@ListMantramNeedApprovalActivity, DetailMantramNeedApprovalActivity::class.java)
                                                bundle.putInt("id_mantram", result.id_post)
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
                                        })
                                    if(filter.isEmpty()){
                                        noMantramNAAdmin.visibility   = View.VISIBLE
                                        allMantramNAAdmin1.visibility = View.GONE
                                        allMantramNAAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noMantramNAAdmin.visibility   = View.GONE
                                        allMantramNAAdmin2.visibility = View.VISIBLE
                                        allMantramNAAdmin2.adapter    = mantramAdapter
                                        allMantramNAAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allMantramNAAdmin1.visibility = View.VISIBLE
                                        allMantramNAAdmin2.visibility = View.GONE
                                        noMantramNAAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }

                    })
                }

                override fun onFailure(call: Call<ArrayList<AllMantramAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun setShimmerToStop() {
        shimmerMantramNAAdmin.stopShimmer()
        shimmerMantramNAAdmin.visibility = View.GONE
        swipeMantramNAAdmin.visibility   = View.VISIBLE
    }
}
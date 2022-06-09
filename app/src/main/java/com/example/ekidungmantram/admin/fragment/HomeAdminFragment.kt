package com.example.ekidungmantram.admin.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.admin.AllYadnyaHomeAdminAdapter
import com.example.ekidungmantram.admin.ListYadnyaAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AllYadnyaHomeAdminModel
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.fragment_home_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeAdminFragment : Fragment() {
    private lateinit var yadnyaAdapter  : AllYadnyaHomeAdminAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_admin, container, false)
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        yadnyaAdminHome.layoutManager = GridLayoutManager(activity, 2, LinearLayoutManager.VERTICAL, false)
        getAdminHomeYadnyaData()
        sharedPreferences = getActivity()!!.getSharedPreferences("is_logged", Context.MODE_PRIVATE)
        val nama          = sharedPreferences.getString("NAMA", null)
        namaAdmin.text    = "Selamat Datang, $nama!"

        swipeAdmin.setOnRefreshListener {
            getAdminHomeYadnyaData()
            swipeAdmin.isRefreshing = false
        }
    }

    private fun getAdminHomeYadnyaData() {
        ApiService.endpoint.getYadnyaAdminHomeList()
            .enqueue(object: Callback<ArrayList<AllYadnyaHomeAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllYadnyaHomeAdminModel>>,
                    response: Response<ArrayList<AllYadnyaHomeAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null){
                        swipeAdmin.visibility = View.VISIBLE
                        shimmerHomeAdmin.visibility = View.GONE
                    }else{
                        swipeAdmin.visibility = View.GONE
                        shimmerHomeAdmin.visibility = View.VISIBLE
                    }
                    yadnyaAdapter = datalist?.let { AllYadnyaHomeAdminAdapter(it,
                        object : AllYadnyaHomeAdminAdapter.OnAdapterAllYadnyaHomeAdminListener{
                            override fun onClick(result: AllYadnyaHomeAdminModel) {
                                val bundle = Bundle()
                                val intent = Intent(activity, ListYadnyaAdminActivity::class.java)
                                bundle.putInt("id_yadnya", result.id_kategori)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        }) }!!

                    yadnyaAdminHome.adapter = yadnyaAdapter
                    setShimmerToStop()
                }

                override fun onFailure(call: Call<ArrayList<AllYadnyaHomeAdminModel>>, t: Throwable) {
                    Toast.makeText(activity, "No Connection", Toast.LENGTH_SHORT).show()
                    setShimmerToStop()
                }

            })
    }

    private fun setShimmerToStop() {
        shimmerHomeAdmin.stopShimmer()
        shimmerHomeAdmin.visibility = View.GONE
        swipeAdmin.visibility       = View.VISIBLE
    }
}
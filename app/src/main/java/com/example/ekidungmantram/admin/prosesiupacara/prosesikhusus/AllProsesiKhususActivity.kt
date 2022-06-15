package com.example.ekidungmantram.admin.prosesiupacara.prosesikhusus

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.admin.AllProsesiAwalOnYadnyaAdminAdapter
import com.example.ekidungmantram.adapter.admin.AllProsesiKhususAdminAdapter
import com.example.ekidungmantram.admin.prosesiupacara.DetailProsesiAdminActivity
import com.example.ekidungmantram.admin.upacarayadnya.AddProsesiAwalToYadnyaAdminActivity
import com.example.ekidungmantram.admin.upacarayadnya.DetailYadnyaAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailAllProsesiAwalOnYadnyaAdminModel
import com.example.ekidungmantram.model.adminmodel.DetailAllProsesiKhususAdminModel
import kotlinx.android.synthetic.main.activity_all_prosesi_awal_yadnya_admin.*
import kotlinx.android.synthetic.main.activity_all_prosesi_khusus.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllProsesiKhususActivity : AppCompatActivity() {
    private lateinit var prosesiAdapter : AllProsesiKhususAdminAdapter
    private lateinit var setAdapter     : AllProsesiKhususAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_prosesi_khusus)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Daftar Prosesi"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val katID = bundle.getInt("id_yadnya")
            val postID = bundle.getInt("id_prosesi")
            val namaPost = bundle.getString("nama_prosesi")

            namaProsesiProsesiKhusus.text = namaPost
            allProsesiProsesiKhususAdmin1.layoutManager = LinearLayoutManager(applicationContext)
            allProsesiProsesiKhususAdmin2.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataProsesi(katID, postID, namaPost!!)

            swipeProsesiProsesiKhususAdmin.setOnRefreshListener {
                getAllDataProsesi(katID, postID, namaPost!!)
                swipeProsesiProsesiKhususAdmin.isRefreshing = false
            }

            fabAddProsesiProsesiKhusus.setOnClickListener {
                val intent = Intent(this, AddProsesiKhususActivity::class.java)
                bundle.putInt("id_yadnya", katID)
                bundle.putInt("id_prosesi", postID)
                bundle.putString("nama_prosesi", namaPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    private fun getAllDataProsesi(katID: Int, postID: Int, namaPost: String) {
        ApiService.endpoint.getDetailAllProsesiKhusus(postID, katID)
            .enqueue(object: Callback<ArrayList<DetailAllProsesiKhususAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<DetailAllProsesiKhususAdminModel>>,
                    response: Response<ArrayList<DetailAllProsesiKhususAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null){
                        swipeProsesiProsesiKhususAdmin.visibility   = View.VISIBLE
                        shimmerProsesiProsesiKhususAdmin.visibility = View.GONE
                    }else{
                        swipeProsesiProsesiKhususAdmin.visibility   = View.GONE
                        shimmerProsesiProsesiKhususAdmin.visibility = View.VISIBLE
                    }
                    setAdapter = AllProsesiKhususAdminAdapter(datalist!!)
                    setAdapter.setOnClickDelete {
                        val builder = AlertDialog.Builder(this@AllProsesiKhususActivity)
                        builder.setTitle("Hapus Prosesi")
                            .setMessage("Apakah anda yakin ingin menghapus prosesi ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                hapusProsesiKhususs(katID, it.id, postID, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }

                    allProsesiProsesiKhususAdmin1.adapter  = setAdapter
                    noProsesiProsesiKhususAdmin.visibility = View.GONE
                    setShimmerToStop()

                    cariProsesiProsesiKhususAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noProsesiProsesiKhususAdmin.visibility   = View.GONE
                                    allProsesiProsesiKhususAdmin1.visibility = View.VISIBLE
                                    allProsesiProsesiKhususAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    prosesiAdapter = AllProsesiKhususAdminAdapter(filter as ArrayList<DetailAllProsesiKhususAdminModel>)
                                    prosesiAdapter.setOnClickDelete {
                                        val builder = AlertDialog.Builder(this@AllProsesiKhususActivity)
                                        builder.setTitle("Hapus Prosesi")
                                            .setMessage("Apakah anda yakin ingin menghapus prosesi?")
                                            .setCancelable(true)
                                            .setPositiveButton("Iya") { _, _ ->
                                                hapusProsesiKhususs(katID, it.id, postID, namaPost)
                                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                                dialogInterface.cancel()
                                            }.show()
                                    }

                                    if(filter.isEmpty()){
                                        noProsesiProsesiKhususAdmin.visibility   = View.VISIBLE
                                        allProsesiProsesiKhususAdmin1.visibility = View.GONE
                                        allProsesiProsesiKhususAdmin2.visibility = View.GONE
                                    }

                                    if(p0.isNotEmpty()){
                                        noProsesiProsesiKhususAdmin.visibility   = View.GONE
                                        allProsesiProsesiKhususAdmin2.visibility = View.VISIBLE
                                        allProsesiProsesiKhususAdmin2.adapter    = prosesiAdapter
                                        allProsesiProsesiKhususAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allProsesiProsesiKhususAdmin1.visibility = View.VISIBLE
                                        allProsesiProsesiKhususAdmin1.visibility = View.GONE
                                        noProsesiProsesiKhususAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }
                    })
                }

                override fun onFailure(call: Call<ArrayList<DetailAllProsesiKhususAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun hapusProsesiKhususs(katID: Int, id: Int, postID: Int, namaPost: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteprosesikhusus(id).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AllProsesiKhususActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AllProsesiKhususActivity, DetailProsesiAdminActivity::class.java)
                    bundle.putInt("id_yadnya", katID)
                    bundle.putInt("id_prosesi", postID)
                    bundle.putString("nama_prosesi", namaPost)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AllProsesiKhususActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AllProsesiKhususActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerProsesiProsesiKhususAdmin.stopShimmer()
        shimmerProsesiProsesiKhususAdmin.visibility = View.GONE
        swipeProsesiProsesiKhususAdmin.visibility   = View.VISIBLE
    }
}
package com.example.ekidungmantram.admin.prosesiupacara

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
import com.example.ekidungmantram.adapter.admin.AllGamelanOnProsesiAdminAdapter
import com.example.ekidungmantram.adapter.admin.AllTabuhOnGamelanAdminAdapter
import com.example.ekidungmantram.adapter.admin.AllTariOnProsesiAdminAdapter
import com.example.ekidungmantram.admin.gamelan.AddTabuhToGamelanAdminActivity
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailAllGamelanOnProsesiAdminModel
import com.example.ekidungmantram.model.adminmodel.DetailAllTariOnProsesiAdminModel
import kotlinx.android.synthetic.main.activity_all_gamelan_on_prosesi_admin.*
import kotlinx.android.synthetic.main.activity_all_tabuh_on_gamelan_admin.*
import kotlinx.android.synthetic.main.activity_all_tari_on_prosesi_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllTariOnProsesiAdminActivity : AppCompatActivity() {
    private lateinit var tariAdapter : AllTariOnProsesiAdminAdapter
    private lateinit var setAdapter  : AllTariOnProsesiAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_tari_on_prosesi_admin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "List Tari Prosesi"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_prosesi")
            val namaPost = bundle.getString("nama_prosesi")

            namaProsesiTari.text = namaPost
            allProsesiTariAdmin1.layoutManager = LinearLayoutManager(applicationContext)
            allProsesiTariAdmin2.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataTariProsesi(postID, namaPost!!)

            swipeProsesiTariAdmin.setOnRefreshListener {
                getAllDataTariProsesi(postID, namaPost!!)
                swipeProsesiTariAdmin.isRefreshing = false
            }

            fabProsesiAddTari.setOnClickListener {
                val intent = Intent(this, AddTariToProsesiAdminActivity::class.java)
                bundle.putInt("id_prosesi", postID)
                bundle.putString("nama_prosesi", namaPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    private fun getAllDataTariProsesi(postID: Int, namaPost: String) {
        ApiService.endpoint.getDetailAllTariOnProsesiAdmin(postID)
            .enqueue(object: Callback<ArrayList<DetailAllTariOnProsesiAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<DetailAllTariOnProsesiAdminModel>>,
                    response: Response<ArrayList<DetailAllTariOnProsesiAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null){
                        swipeProsesiTariAdmin.visibility   = View.VISIBLE
                        shimmerProsesiTariAdmin.visibility = View.GONE
                    }else{
                        swipeProsesiTariAdmin.visibility   = View.GONE
                        shimmerProsesiTariAdmin.visibility = View.VISIBLE
                    }
                    setAdapter = AllTariOnProsesiAdminAdapter(datalist!!)
                    setAdapter.setOnClickDelete {
                        val builder = AlertDialog.Builder(this@AllTariOnProsesiAdminActivity)
                        builder.setTitle("Hapus Tari dari Prosesi")
                            .setMessage("Apakah anda yakin ingin menghapus tari dari prosesi ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                hapusTariProsesi(it.id, postID, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }

                    allProsesiTariAdmin1.adapter  = setAdapter
                    noProsesiTariAdmin.visibility = View.GONE
                    setShimmerToStop()

                    cariProsesiTariAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noProsesiTariAdmin.visibility   = View.GONE
                                    allProsesiTariAdmin1.visibility = View.VISIBLE
                                    allProsesiTariAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    tariAdapter = AllTariOnProsesiAdminAdapter(filter as ArrayList<DetailAllTariOnProsesiAdminModel>)
                                    tariAdapter.setOnClickDelete {
                                        val builder = AlertDialog.Builder(this@AllTariOnProsesiAdminActivity)
                                        builder.setTitle("Hapus Tari dari Prosesi")
                                            .setMessage("Apakah anda yakin ingin menghapus tari dari prosesi ini?")
                                            .setCancelable(true)
                                            .setPositiveButton("Iya") { _, _ ->
                                                hapusTariProsesi(it.id, postID, namaPost)
                                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                                dialogInterface.cancel()
                                            }.show()
                                    }
                                    if(filter.isEmpty()){
                                        noProsesiTariAdmin.visibility   = View.VISIBLE
                                        allProsesiTariAdmin1.visibility = View.GONE
                                        allProsesiTariAdmin2.visibility = View.GONE
                                    }
                                    if(p0.isNotEmpty()){
                                        noProsesiTariAdmin.visibility   = View.GONE
                                        allProsesiTariAdmin2.visibility = View.VISIBLE
                                        allProsesiTariAdmin2.adapter    = tariAdapter
                                        allProsesiTariAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allProsesiTariAdmin1.visibility = View.VISIBLE
                                        allProsesiTariAdmin2.visibility = View.GONE
                                        noProsesiTariAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }
                    })
                }

                override fun onFailure(call: Call<ArrayList<DetailAllTariOnProsesiAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun hapusTariProsesi(id: Int, postID: Int, namaPost: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataTariOnProsesiAdmin(id).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AllTariOnProsesiAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AllTariOnProsesiAdminActivity, DetailProsesiAdminActivity::class.java)
                    bundle.putInt("id_prosesi", postID)
                    bundle.putString("nama_prosesi", namaPost)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AllTariOnProsesiAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AllTariOnProsesiAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerProsesiTariAdmin.stopShimmer()
        shimmerProsesiTariAdmin.visibility = View.GONE
        swipeProsesiTariAdmin.visibility   = View.VISIBLE
    }
}
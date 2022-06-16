package com.example.ekidungmantram.admin.upacarayadnya

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
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailAllProsesiAwalOnYadnyaAdminModel
import kotlinx.android.synthetic.main.activity_all_prosesi_awal_yadnya_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllProsesiAwalYadnyaAdminActivity : AppCompatActivity() {
    private lateinit var prosesiAdapter : AllProsesiAwalOnYadnyaAdminAdapter
    private lateinit var setAdapter     : AllProsesiAwalOnYadnyaAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_prosesi_awal_yadnya_admin)
        supportActionBar!!.title = "Daftar Prosesi Awal"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val katID = bundle.getInt("id_kategori")
            val postID = bundle.getInt("id_yadnya")
            val namaPost = bundle.getString("nama_yadnya")

            namaYadnyaProsesiAwal.text = namaPost
            allYadnyaProsesiAwalAdmin1.layoutManager = LinearLayoutManager(applicationContext)
            allYadnyaProsesiAwalAdmin2.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataProsesi(katID, postID, namaPost!!)

            swipeYadnyaProsesiAwalAdmin.setOnRefreshListener {
                getAllDataProsesi(katID, postID, namaPost!!)
                swipeYadnyaProsesiAwalAdmin.isRefreshing = false
            }

            fabAddYadnyaProsesiAwal.setOnClickListener {
                val intent = Intent(this, AddProsesiAwalToYadnyaAdminActivity::class.java)
                bundle.putInt("id_kategori", katID)
                bundle.putInt("id_yadnya", postID)
                bundle.putString("nama_yadnya", namaPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    private fun getAllDataProsesi(katID: Int, postID: Int, namaPost: String) {
        ApiService.endpoint.getDetailAllProsesiAwalOnYadnyaAdmin(postID)
            .enqueue(object: Callback<ArrayList<DetailAllProsesiAwalOnYadnyaAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<DetailAllProsesiAwalOnYadnyaAdminModel>>,
                    response: Response<ArrayList<DetailAllProsesiAwalOnYadnyaAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null){
                        swipeYadnyaProsesiAwalAdmin.visibility   = View.VISIBLE
                        shimmerYadnyaProsesiAwalAdmin.visibility = View.GONE
                    }else{
                        swipeYadnyaProsesiAwalAdmin.visibility   = View.GONE
                        shimmerYadnyaProsesiAwalAdmin.visibility = View.VISIBLE
                    }
                    setAdapter = AllProsesiAwalOnYadnyaAdminAdapter(datalist!!)
                    setAdapter.setOnClickDelete {
                        val builder = AlertDialog.Builder(this@AllProsesiAwalYadnyaAdminActivity)
                        builder.setTitle("Hapus Prosesi dari Yadnya")
                            .setMessage("Apakah anda yakin ingin menghapus prosesi dari yadnya ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                hapusProsesiAwal(katID, it.id, postID, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }
                    setAdapter.setOnClickUp {
                        val builder = AlertDialog.Builder(this@AllProsesiAwalYadnyaAdminActivity)
                        builder.setTitle("Ubah Urutan Prosesi")
                            .setMessage("Apakah anda yakin ingin menaikan urutan prosesi sebanyak satu tingkat?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                upProsesiAwal(katID, it.id ,postID, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }
                    setAdapter.setOnClickDown {
                        val builder = AlertDialog.Builder(this@AllProsesiAwalYadnyaAdminActivity)
                        builder.setTitle("Ubah Urutan Prosesi")
                            .setMessage("Apakah anda yakin ingin menurunkan urutan prosesi sebanyak satu tingkat?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                downProsesiAwal(katID, it.id ,postID, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }

                    allYadnyaProsesiAwalAdmin1.adapter  = setAdapter
                    noYadnyaProsesiAwalAdmin.visibility = View.GONE
                    setShimmerToStop()

                    cariYadnyaProsesiAwalAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noYadnyaProsesiAwalAdmin.visibility   = View.GONE
                                    allYadnyaProsesiAwalAdmin1.visibility = View.VISIBLE
                                    allYadnyaProsesiAwalAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    prosesiAdapter = AllProsesiAwalOnYadnyaAdminAdapter(filter as ArrayList<DetailAllProsesiAwalOnYadnyaAdminModel>)
                                    prosesiAdapter.setOnClickDelete {
                                        val builder = AlertDialog.Builder(this@AllProsesiAwalYadnyaAdminActivity)
                                        builder.setTitle("Hapus Prosesi dari Yadnya")
                                            .setMessage("Apakah anda yakin ingin menghapus prosesi dari yadnya ini?")
                                            .setCancelable(true)
                                            .setPositiveButton("Iya") { _, _ ->
                                                hapusProsesiAwal(katID, it.id, postID, namaPost)
                                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                                dialogInterface.cancel()
                                            }.show()
                                    }

                                    prosesiAdapter.setOnClickUp {
                                        val builder = AlertDialog.Builder(this@AllProsesiAwalYadnyaAdminActivity)
                                        builder.setTitle("Ubah Urutan Prosesi")
                                            .setMessage("Apakah anda yakin ingin menaikan urutan prosesi sebanyak satu tingkat?")
                                            .setCancelable(true)
                                            .setPositiveButton("Iya") { _, _ ->
                                                upProsesiAwal(katID, it.id ,postID, namaPost)
                                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                                dialogInterface.cancel()
                                            }.show()
                                    }

                                    prosesiAdapter.setOnClickDown {
                                        val builder = AlertDialog.Builder(this@AllProsesiAwalYadnyaAdminActivity)
                                        builder.setTitle("Ubah Urutan Prosesi")
                                            .setMessage("Apakah anda yakin ingin menurunkan urutan prosesi sebanyak satu tingkat?")
                                            .setCancelable(true)
                                            .setPositiveButton("Iya") { _, _ ->
                                                downProsesiAwal(katID, it.id ,postID, namaPost)
                                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                                dialogInterface.cancel()
                                            }.show()
                                    }

                                    if(filter.isEmpty()){
                                        noYadnyaProsesiAwalAdmin.visibility   = View.VISIBLE
                                        allYadnyaProsesiAwalAdmin1.visibility = View.GONE
                                        allYadnyaProsesiAwalAdmin2.visibility = View.GONE
                                    }

                                    if(p0.isNotEmpty()){
                                        noYadnyaProsesiAwalAdmin.visibility   = View.GONE
                                        allYadnyaProsesiAwalAdmin2.visibility = View.VISIBLE
                                        allYadnyaProsesiAwalAdmin2.adapter    = prosesiAdapter
                                        allYadnyaProsesiAwalAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allYadnyaProsesiAwalAdmin1.visibility = View.VISIBLE
                                        allYadnyaProsesiAwalAdmin1.visibility = View.GONE
                                        noYadnyaProsesiAwalAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }
                    })
                }

                override fun onFailure(call: Call<ArrayList<DetailAllProsesiAwalOnYadnyaAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun upProsesiAwal(katID: Int, id: Int, postID: Int, namaPost: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengubah Urutan Data")
        progressDialog.show()
        ApiService.endpoint.upDataProsesiAwalAdmin(postID, id).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    getAllDataProsesi(katID, postID, namaPost)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AllProsesiAwalYadnyaAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AllProsesiAwalYadnyaAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun downProsesiAwal(katID: Int, id: Int, postID: Int, namaPost: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengubah Urutan Data")
        progressDialog.show()
        ApiService.endpoint.downDataProsesiAwalAdmin(postID, id).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    getAllDataProsesi(katID, postID, namaPost)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AllProsesiAwalYadnyaAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AllProsesiAwalYadnyaAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun hapusProsesiAwal(katID: Int, id: Int, postID: Int, namaPost: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataProsesiAwalOnYadnyaAdmin(id, postID).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AllProsesiAwalYadnyaAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AllProsesiAwalYadnyaAdminActivity, DetailYadnyaAdminActivity::class.java)
                    bundle.putInt("id_kategori", katID)
                    bundle.putInt("id_yadnya", postID)
                    bundle.putString("nama_yadnya", namaPost)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AllProsesiAwalYadnyaAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AllProsesiAwalYadnyaAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerYadnyaProsesiAwalAdmin.stopShimmer()
        shimmerYadnyaProsesiAwalAdmin.visibility = View.GONE
        swipeYadnyaProsesiAwalAdmin.visibility   = View.VISIBLE
    }
}
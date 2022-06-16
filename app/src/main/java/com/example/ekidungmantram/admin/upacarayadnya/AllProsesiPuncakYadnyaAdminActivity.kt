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
import com.example.ekidungmantram.adapter.admin.AllProsesiPuncakOnYadnyaAdminAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailAllProsesiAwalOnYadnyaAdminModel
import com.example.ekidungmantram.model.adminmodel.DetailAllProsesiPuncakOnYadnyaAdminModel
import kotlinx.android.synthetic.main.activity_all_prosesi_awal_yadnya_admin.*
import kotlinx.android.synthetic.main.activity_all_prosesi_puncak_yadnya_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllProsesiPuncakYadnyaAdminActivity : AppCompatActivity() {
    private lateinit var prosesiAdapter : AllProsesiPuncakOnYadnyaAdminAdapter
    private lateinit var setAdapter     : AllProsesiPuncakOnYadnyaAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_prosesi_puncak_yadnya_admin)
        supportActionBar!!.title = "Daftar Prosesi Puncak"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val katID = bundle.getInt("id_kategori")
            val postID = bundle.getInt("id_yadnya")
            val namaPost = bundle.getString("nama_yadnya")

            namaYadnyaProsesiPuncak.text = namaPost
            allYadnyaProsesiPuncakAdmin1.layoutManager = LinearLayoutManager(applicationContext)
            allYadnyaProsesiPuncakAdmin2.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataProsesi(katID, postID, namaPost!!)

            swipeYadnyaProsesiPuncakAdmin.setOnRefreshListener {
                getAllDataProsesi(katID, postID, namaPost!!)
                swipeYadnyaProsesiPuncakAdmin.isRefreshing = false
            }

            fabAddYadnyaProsesiPuncak.setOnClickListener {
                val intent = Intent(this, AddProsesiPuncakToYadnyaAdminActivity::class.java)
                bundle.putInt("id_kategori", katID)
                bundle.putInt("id_yadnya", postID)
                bundle.putString("nama_yadnya", namaPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    private fun getAllDataProsesi(katID: Int, postID: Int, namaPost: String) {
        ApiService.endpoint.getDetailAllProsesiPuncakOnYadnyaAdmin(postID)
            .enqueue(object: Callback<ArrayList<DetailAllProsesiPuncakOnYadnyaAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<DetailAllProsesiPuncakOnYadnyaAdminModel>>,
                    response: Response<ArrayList<DetailAllProsesiPuncakOnYadnyaAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null){
                        swipeYadnyaProsesiPuncakAdmin.visibility   = View.VISIBLE
                        shimmerYadnyaProsesiPuncakAdmin.visibility = View.GONE
                    }else{
                        swipeYadnyaProsesiPuncakAdmin.visibility   = View.GONE
                        shimmerYadnyaProsesiPuncakAdmin.visibility = View.VISIBLE
                    }
                    setAdapter = AllProsesiPuncakOnYadnyaAdminAdapter(datalist!!)
                    setAdapter.setOnClickDelete {
                        val builder = AlertDialog.Builder(this@AllProsesiPuncakYadnyaAdminActivity)
                        builder.setTitle("Hapus Prosesi dari Yadnya")
                            .setMessage("Apakah anda yakin ingin menghapus prosesi dari yadnya ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                hapusProsesiPuncak(katID, it.id, postID, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }
                    setAdapter.setOnClickUp {
                        val builder = AlertDialog.Builder(this@AllProsesiPuncakYadnyaAdminActivity)
                        builder.setTitle("Ubah Urutan Prosesi")
                            .setMessage("Apakah anda yakin ingin menaikan urutan prosesi sebanyak satu tingkat?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                upProsesiPuncak(katID, it.id ,postID, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }
                    setAdapter.setOnClickDown {
                        val builder = AlertDialog.Builder(this@AllProsesiPuncakYadnyaAdminActivity)
                        builder.setTitle("Ubah Urutan Prosesi")
                            .setMessage("Apakah anda yakin ingin menurunkan urutan prosesi sebanyak satu tingkat?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                downProsesiPuncak(katID, it.id ,postID, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }

                    allYadnyaProsesiPuncakAdmin1.adapter  = setAdapter
                    noYadnyaProsesiPuncakAdmin.visibility = View.GONE
                    setShimmerToStop()

                    cariYadnyaProsesiPuncakAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noYadnyaProsesiPuncakAdmin.visibility   = View.GONE
                                    allYadnyaProsesiPuncakAdmin1.visibility = View.VISIBLE
                                    allYadnyaProsesiPuncakAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    prosesiAdapter = AllProsesiPuncakOnYadnyaAdminAdapter(filter as ArrayList<DetailAllProsesiPuncakOnYadnyaAdminModel>)
                                    prosesiAdapter.setOnClickDelete {
                                        val builder = AlertDialog.Builder(this@AllProsesiPuncakYadnyaAdminActivity)
                                        builder.setTitle("Hapus Prosesi dari Yadnya")
                                            .setMessage("Apakah anda yakin ingin menghapus prosesi dari yadnya ini?")
                                            .setCancelable(true)
                                            .setPositiveButton("Iya") { _, _ ->
                                                hapusProsesiPuncak(katID, it.id, postID, namaPost)
                                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                                dialogInterface.cancel()
                                            }.show()
                                    }

                                    prosesiAdapter.setOnClickUp {
                                        val builder = AlertDialog.Builder(this@AllProsesiPuncakYadnyaAdminActivity)
                                        builder.setTitle("Ubah Urutan Prosesi")
                                            .setMessage("Apakah anda yakin ingin menaikan urutan prosesi sebanyak satu tingkat?")
                                            .setCancelable(true)
                                            .setPositiveButton("Iya") { _, _ ->
                                                upProsesiPuncak(katID, it.id ,postID, namaPost)
                                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                                dialogInterface.cancel()
                                            }.show()
                                    }

                                    prosesiAdapter.setOnClickDown {
                                        val builder = AlertDialog.Builder(this@AllProsesiPuncakYadnyaAdminActivity)
                                        builder.setTitle("Ubah Urutan Prosesi")
                                            .setMessage("Apakah anda yakin ingin menurunkan urutan prosesi sebanyak satu tingkat?")
                                            .setCancelable(true)
                                            .setPositiveButton("Iya") { _, _ ->
                                                downProsesiPuncak(katID, it.id ,postID, namaPost)
                                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                                dialogInterface.cancel()
                                            }.show()
                                    }

                                    if(filter.isEmpty()){
                                        noYadnyaProsesiPuncakAdmin.visibility   = View.VISIBLE
                                        allYadnyaProsesiPuncakAdmin1.visibility = View.GONE
                                        allYadnyaProsesiPuncakAdmin2.visibility = View.GONE
                                    }

                                    if(p0.isNotEmpty()){
                                        noYadnyaProsesiPuncakAdmin.visibility   = View.GONE
                                        allYadnyaProsesiPuncakAdmin2.visibility = View.VISIBLE
                                        allYadnyaProsesiPuncakAdmin2.adapter    = prosesiAdapter
                                        allYadnyaProsesiPuncakAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allYadnyaProsesiPuncakAdmin1.visibility = View.VISIBLE
                                        allYadnyaProsesiPuncakAdmin1.visibility = View.GONE
                                        noYadnyaProsesiPuncakAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }
                    })
                }

                override fun onFailure(call: Call<ArrayList<DetailAllProsesiPuncakOnYadnyaAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun upProsesiPuncak(katID: Int, id: Int, postID: Int, namaPost: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengubah Urutan Data")
        progressDialog.show()
        ApiService.endpoint.upDataProsesiPuncakAdmin(postID, id).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    getAllDataProsesi(katID, postID, namaPost)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AllProsesiPuncakYadnyaAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AllProsesiPuncakYadnyaAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun downProsesiPuncak(katID: Int, id: Int, postID: Int, namaPost: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengubah Urutan Data")
        progressDialog.show()
        ApiService.endpoint.downDataProsesiPuncakAdmin(postID, id).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    getAllDataProsesi(katID, postID, namaPost)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AllProsesiPuncakYadnyaAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AllProsesiPuncakYadnyaAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }


    private fun hapusProsesiPuncak(katID: Int, id: Int, postID: Int, namaPost: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataProsesiPuncakOnYadnyaAdmin(id, postID).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AllProsesiPuncakYadnyaAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AllProsesiPuncakYadnyaAdminActivity, DetailYadnyaAdminActivity::class.java)
                    bundle.putInt("id_kategori", katID)
                    bundle.putInt("id_yadnya", postID)
                    bundle.putString("nama_yadnya", namaPost)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AllProsesiPuncakYadnyaAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AllProsesiPuncakYadnyaAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerYadnyaProsesiPuncakAdmin.stopShimmer()
        shimmerYadnyaProsesiPuncakAdmin.visibility = View.GONE
        swipeYadnyaProsesiPuncakAdmin.visibility   = View.VISIBLE
    }
}
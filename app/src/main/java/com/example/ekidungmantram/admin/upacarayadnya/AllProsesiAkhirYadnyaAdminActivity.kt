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
import com.example.ekidungmantram.adapter.admin.AllProsesiAkhirOnYadnyaAdminAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.CrudModel
import com.example.ekidungmantram.model.adminmodel.DetailAllProsesiAkhirOnYadnyaAdminModel
import kotlinx.android.synthetic.main.activity_all_prosesi_akhir_yadnya_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllProsesiAkhirYadnyaAdminActivity : AppCompatActivity() {
    private lateinit var prosesiAdapter : AllProsesiAkhirOnYadnyaAdminAdapter
    private lateinit var setAdapter     : AllProsesiAkhirOnYadnyaAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_prosesi_akhir_yadnya_admin)
        supportActionBar!!.title = "Daftar Prosesi Akhir"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val katID = bundle.getInt("id_kategori")
            val postID = bundle.getInt("id_yadnya")
            val namaPost = bundle.getString("nama_yadnya")

            namaYadnyaProsesiAkhir.text = namaPost
            allYadnyaProsesiAkhirAdmin1.layoutManager = LinearLayoutManager(applicationContext)
            allYadnyaProsesiAkhirAdmin2.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataProsesi(katID, postID, namaPost!!)

            swipeYadnyaProsesiAkhirAdmin.setOnRefreshListener {
                getAllDataProsesi(katID, postID, namaPost!!)
                swipeYadnyaProsesiAkhirAdmin.isRefreshing = false
            }

            fabAddYadnyaProsesiAkhir.setOnClickListener {
                val intent = Intent(this, AddProsesiAkhirToYadnyaAdminActivity::class.java)
                bundle.putInt("id_kategori", katID)
                bundle.putInt("id_yadnya", postID)
                bundle.putString("nama_yadnya", namaPost)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    private fun getAllDataProsesi(katID: Int, postID: Int, namaPost: String) {
        ApiService.endpoint.getDetailAllProsesiAkhirOnYadnyaAdmin(postID)
            .enqueue(object: Callback<ArrayList<DetailAllProsesiAkhirOnYadnyaAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<DetailAllProsesiAkhirOnYadnyaAdminModel>>,
                    response: Response<ArrayList<DetailAllProsesiAkhirOnYadnyaAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null){
                        swipeYadnyaProsesiAkhirAdmin.visibility   = View.VISIBLE
                        shimmerYadnyaProsesiAkhirAdmin.visibility = View.GONE
                    }else{
                        swipeYadnyaProsesiAkhirAdmin.visibility   = View.GONE
                        shimmerYadnyaProsesiAkhirAdmin.visibility = View.VISIBLE
                    }
                    setAdapter = AllProsesiAkhirOnYadnyaAdminAdapter(datalist!!)
                    setAdapter.setOnClickDelete {
                        val builder = AlertDialog.Builder(this@AllProsesiAkhirYadnyaAdminActivity)
                        builder.setTitle("Hapus Prosesi dari Yadnya")
                            .setMessage("Apakah anda yakin ingin menghapus prosesi dari yadnya ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                hapusProsesiAkhir(katID, it.id, postID, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }
                    setAdapter.setOnClickUp {
                        val builder = AlertDialog.Builder(this@AllProsesiAkhirYadnyaAdminActivity)
                        builder.setTitle("Ubah Urutan Prosesi")
                            .setMessage("Apakah anda yakin ingin menaikan urutan prosesi sebanyak satu tingkat?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                upProsesiAkhir(katID, it.id ,postID, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }
                    setAdapter.setOnClickDown {
                        val builder = AlertDialog.Builder(this@AllProsesiAkhirYadnyaAdminActivity)
                        builder.setTitle("Ubah Urutan Prosesi")
                            .setMessage("Apakah anda yakin ingin menurunkan urutan prosesi sebanyak satu tingkat?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                downProsesiAkhir(katID, it.id ,postID, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }

                    allYadnyaProsesiAkhirAdmin1.adapter  = setAdapter
                    noYadnyaProsesiAkhirAdmin.visibility = View.GONE
                    setShimmerToStop()

                    cariYadnyaProsesiAkhirAdmin.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean = false

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if(p0 != null){
                                if(p0.isEmpty()){
                                    noYadnyaProsesiAkhirAdmin.visibility   = View.GONE
                                    allYadnyaProsesiAkhirAdmin1.visibility = View.VISIBLE
                                    allYadnyaProsesiAkhirAdmin2.visibility = View.GONE
                                }else if(p0.length > 2){
                                    val filter = datalist.filter { it.nama_post.contains("$p0", true) }
                                    prosesiAdapter = AllProsesiAkhirOnYadnyaAdminAdapter(filter as ArrayList<DetailAllProsesiAkhirOnYadnyaAdminModel>)
                                    prosesiAdapter.setOnClickDelete {
                                        val builder = AlertDialog.Builder(this@AllProsesiAkhirYadnyaAdminActivity)
                                        builder.setTitle("Hapus Prosesi dari Yadnya")
                                            .setMessage("Apakah anda yakin ingin menghapus prosesi dari yadnya ini?")
                                            .setCancelable(true)
                                            .setPositiveButton("Iya") { _, _ ->
                                                hapusProsesiAkhir(katID, it.id, postID, namaPost)
                                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                                dialogInterface.cancel()
                                            }.show()
                                    }

                                    prosesiAdapter.setOnClickUp {
                                        val builder = AlertDialog.Builder(this@AllProsesiAkhirYadnyaAdminActivity)
                                        builder.setTitle("Ubah Urutan Prosesi")
                                            .setMessage("Apakah anda yakin ingin menaikan urutan prosesi sebanyak satu tingkat?")
                                            .setCancelable(true)
                                            .setPositiveButton("Iya") { _, _ ->
                                                upProsesiAkhir(katID, it.id ,postID, namaPost)
                                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                                dialogInterface.cancel()
                                            }.show()
                                    }

                                    prosesiAdapter.setOnClickDown {
                                        val builder = AlertDialog.Builder(this@AllProsesiAkhirYadnyaAdminActivity)
                                        builder.setTitle("Ubah Urutan Prosesi")
                                            .setMessage("Apakah anda yakin ingin menurunkan urutan prosesi sebanyak satu tingkat?")
                                            .setCancelable(true)
                                            .setPositiveButton("Iya") { _, _ ->
                                                downProsesiAkhir(katID, it.id ,postID, namaPost)
                                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                                dialogInterface.cancel()
                                            }.show()
                                    }

                                    if(filter.isEmpty()){
                                        noYadnyaProsesiAkhirAdmin.visibility   = View.VISIBLE
                                        allYadnyaProsesiAkhirAdmin1.visibility = View.GONE
                                        allYadnyaProsesiAkhirAdmin2.visibility = View.GONE
                                    }

                                    if(p0.isNotEmpty()){
                                        noYadnyaProsesiAkhirAdmin.visibility   = View.GONE
                                        allYadnyaProsesiAkhirAdmin2.visibility = View.VISIBLE
                                        allYadnyaProsesiAkhirAdmin2.adapter    = prosesiAdapter
                                        allYadnyaProsesiAkhirAdmin1.visibility = View.INVISIBLE
                                    }else{
                                        allYadnyaProsesiAkhirAdmin1.visibility = View.VISIBLE
                                        allYadnyaProsesiAkhirAdmin1.visibility = View.GONE
                                        noYadnyaProsesiAkhirAdmin.visibility   = View.GONE
                                    }
                                }
                            }
                            return false
                        }
                    })
                }

                override fun onFailure(call: Call<ArrayList<DetailAllProsesiAkhirOnYadnyaAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun upProsesiAkhir(katID: Int, id: Int, postID: Int, namaPost: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengubah Urutan Data")
        progressDialog.show()
        ApiService.endpoint.upDataProsesiAkhirAdmin(postID, id).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    getAllDataProsesi(katID, postID, namaPost)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AllProsesiAkhirYadnyaAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AllProsesiAkhirYadnyaAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun downProsesiAkhir(katID: Int, id: Int, postID: Int, namaPost: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengubah Urutan Data")
        progressDialog.show()
        ApiService.endpoint.downDataProsesiAkhirAdmin(postID, id).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    getAllDataProsesi(katID, postID, namaPost)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AllProsesiAkhirYadnyaAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AllProsesiAkhirYadnyaAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun hapusProsesiAkhir(katID: Int, id: Int, postID: Int, namaPost: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataProsesiAkhirOnYadnyaAdmin(id, postID).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AllProsesiAkhirYadnyaAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AllProsesiAkhirYadnyaAdminActivity, DetailYadnyaAdminActivity::class.java)
                    bundle.putInt("id_kategori", katID)
                    bundle.putInt("id_yadnya", postID)
                    bundle.putString("nama_yadnya", namaPost)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AllProsesiAkhirYadnyaAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AllProsesiAkhirYadnyaAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerYadnyaProsesiAkhirAdmin.stopShimmer()
        shimmerYadnyaProsesiAkhirAdmin.visibility = View.GONE
        swipeYadnyaProsesiAkhirAdmin.visibility   = View.VISIBLE
    }
}
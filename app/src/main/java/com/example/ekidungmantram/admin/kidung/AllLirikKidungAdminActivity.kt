package com.example.ekidungmantram.admin.kidung

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
import com.example.ekidungmantram.adapter.admin.AllDataLirikKidungAdminAdapter
import com.example.ekidungmantram.adapter.admin.AllKidungAdminAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.model.adminmodel.AllKidungAdminModel
import com.example.ekidungmantram.model.adminmodel.AllLirikKidungAdminModel
import com.example.ekidungmantram.model.adminmodel.CrudModel
import kotlinx.android.synthetic.main.activity_all_kidung_admin.*
import kotlinx.android.synthetic.main.activity_all_lirik_kidung_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllLirikKidungAdminActivity : AppCompatActivity() {
    private lateinit var setAdapter : AllDataLirikKidungAdminAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_lirik_kidung_admin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Lirik Kidung"
        val bundle :Bundle ?= intent.extras
        if (bundle!=null) {
            val postID = bundle.getInt("id_kidung")
            val namaPost = bundle.getString("nama_kidung")

            namaKidungLirik.text = namaPost
            allLirikKidungAdmin.layoutManager = LinearLayoutManager(applicationContext)
            getAllDataLirikKidung(postID, namaPost!!)

            swipeLirikKidungAdmin.setOnRefreshListener {
                getAllDataLirikKidung(postID, namaPost)
                swipeLirikKidungAdmin.isRefreshing = false
            }

            fabLirikKidung.setOnClickListener {
                val intent = Intent(this, AddLirikKidungAdminActivity::class.java)
                bundle.putInt("id_kidung", postID)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    private fun getAllDataLirikKidung(postID: Int, namaPost: String) {
        ApiService.endpoint.getAllLirikKidungAdmin(postID)
            .enqueue(object: Callback<ArrayList<AllLirikKidungAdminModel>> {
                override fun onResponse(
                    call: Call<ArrayList<AllLirikKidungAdminModel>>,
                    response: Response<ArrayList<AllLirikKidungAdminModel>>
                ) {
                    val datalist   = response.body()
                    if(datalist != null){
                        swipeLirikKidungAdmin.visibility   = View.VISIBLE
                        shimmerLirikKidungAdmin.visibility = View.GONE
                    }else{
                        swipeLirikKidungAdmin.visibility   = View.GONE
                        shimmerLirikKidungAdmin.visibility = View.VISIBLE
                    }
                    setAdapter = AllDataLirikKidungAdminAdapter(datalist!!)
                    setAdapter.setOnClickDelete {
                        val builder = AlertDialog.Builder(this@AllLirikKidungAdminActivity)
                        builder.setTitle("Hapus Lirik Kidung")
                            .setMessage("Apakah anda yakin ingin menghapus lirik kidung ini?")
                            .setCancelable(true)
                            .setPositiveButton("Iya") { _, _ ->
                                hapusLirikKidung(it.id_lirik_kidung, postID, namaPost)
                            }.setNegativeButton("Batal") { dialogInterface, _ ->
                                dialogInterface.cancel()
                            }.show()
                    }
                    setAdapter.setOnClickEdit {
                        val bundle = Bundle()
                        val intent = Intent(this@AllLirikKidungAdminActivity, EditLirikKidungAdminActivity::class.java)
                        bundle.putInt("id_lirik_kidung", it.id_lirik_kidung)
                        bundle.putInt("id_kidung", postID)
                        bundle.putString("nama_kidung", namaPost)
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }

                    allLirikKidungAdmin.adapter  = setAdapter
                    noLirikKidungAdmin.visibility = View.GONE
                    setShimmerToStop()

                }

                override fun onFailure(call: Call<ArrayList<AllLirikKidungAdminModel>>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun hapusLirikKidung(idLirikKidung: Int, postIDs: Int, postName:String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menghapus Data")
        progressDialog.show()
        ApiService.endpoint.deleteDataLirikKidungAdmin(idLirikKidung, postIDs).enqueue(object: Callback<CrudModel> {
            override fun onResponse(call: Call<CrudModel>, response: Response<CrudModel>) {
                if(response.body()?.status == 200){
                    progressDialog.dismiss()
                    Toast.makeText(this@AllLirikKidungAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    val intent = Intent(this@AllLirikKidungAdminActivity, DetailKidungAdminActivity::class.java)
                    bundle.putInt("id_kidung", postIDs)
                    bundle.putString("nama_kidung", postName)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@AllLirikKidungAdminActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CrudModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AllLirikKidungAdminActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    private fun setShimmerToStop() {
        shimmerLirikKidungAdmin.stopShimmer()
        shimmerLirikKidungAdmin.visibility = View.GONE
        swipeLirikKidungAdmin.visibility   = View.VISIBLE
    }
}
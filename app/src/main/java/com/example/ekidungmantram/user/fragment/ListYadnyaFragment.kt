package com.example.ekidungmantram.user.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.BookmarkedAdapter
import com.example.ekidungmantram.database.data.Yadnya
import com.example.ekidungmantram.database.setup.YadnyaDb
import com.example.ekidungmantram.user.DetailYadnyaActivity
import kotlinx.android.synthetic.main.fragment_list_yadnya.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListYadnyaFragment : Fragment() {
    private lateinit var db                : YadnyaDb
    private lateinit var bookmarkedAdapter : BookmarkedAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_yadnya, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        db = Room.databaseBuilder(requireActivity(), YadnyaDb::class.java, "yadnyabookmarked.db").build()
        getAllYadnyaBookmarkedData()
        setupRecyclerviewBookmark()
        swipeBook.setOnRefreshListener {
            getAllYadnyaBookmarkedData()
            setupRecyclerviewBookmark()
            swipeBook.isRefreshing = false
        }
    }

    private fun setupRecyclerviewBookmark() {
        bookmarkedAdapter = BookmarkedAdapter(arrayListOf(), object : BookmarkedAdapter.OnAdapterListener{
            override fun onClick(result: Yadnya) {
                val bundle = Bundle()
                val intent = Intent(activity, DetailYadnyaActivity::class.java)
                bundle.putInt("id_yadnya", result.id_yadnya)
                bundle.putInt("id_kategori", result.id_kategori)
                bundle.putString("nama_yadnya", result.nama_post)
                bundle.putString("kategori", result.kategori)
                bundle.putString("gambar", result.gambar)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })
        allYadnyasBookmarked.apply {
            layoutManager      = LinearLayoutManager(context)
            adapter            = bookmarkedAdapter
            setHasFixedSize(true)
        }
    }

    private fun getAllYadnyaBookmarkedData() {
        CoroutineScope(Dispatchers.IO).launch {
            val yadnya = db.yadnyaDao().getAllBookmarkedYadnya()
            withContext(Dispatchers.Main){
                if(yadnya.isNotEmpty()){
                    nobookmark.visibility           = View.GONE
                    allYadnyasBookmarked.visibility = View.VISIBLE
                }else{
                    nobookmark.visibility           = View.VISIBLE
                    allYadnyasBookmarked.visibility = View.GONE
                }
                showData(yadnya)
            }
        }
    }

    private fun showData(yadnya: List<Yadnya>) {
        bookmarkedAdapter.setData(yadnya)
    }

    private fun printLog(message: String) {
        Log.d("ListYadnyaFragment", message)
    }

}
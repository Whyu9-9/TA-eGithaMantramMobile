package com.example.ekidungmantram.user.fragment

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.CardSliderAdapter
import com.example.ekidungmantram.adapter.NewYadnyaAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.data.CardSliderData
import com.example.ekidungmantram.databinding.FragmentHomeBinding
import com.example.ekidungmantram.model.HomeModel
import com.example.ekidungmantram.model.NewYadnyaModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var cardAdapter : CardSliderAdapter
    private lateinit var yadnyaAdapter: NewYadnyaAdapter
    private val list = ArrayList<CardSliderData>()
    private lateinit var dots: ArrayList<TextView>
    private val yadnyaList = ArrayList<String>()
    private lateinit var handler : Handler
    private lateinit var runnable: Runnable
    private var gridLayoutManager:GridLayoutManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getYadnyaMasterData()
        setupRecyclerView()
        getLatestYadnyaData()
        runAutoSlideCard()

        val swiped = binding.swipe
        swiped.setOnRefreshListener {
            swiped.isRefreshing = false
        }
    }

    private fun runAutoSlideCard() {
        handler = Handler(Looper.myLooper()!!)
        runnable = object : Runnable {
            var index = 0
            override fun run() {
                if(index == list.size){
                    index = 0
                }
                binding.viewPager.setCurrentItem(index)
                index++
                handler.postDelayed(this, 5000)
            }
        }
        handler.post(runnable)
    }

    private fun setupRecyclerView() {
        yadnyaAdapter = NewYadnyaAdapter(arrayListOf())
        binding.yadnyaBaru.apply {
            gridLayoutManager = GridLayoutManager(getActivity(), 1, LinearLayoutManager.HORIZONTAL, false)
            layoutManager     = gridLayoutManager
            adapter           = yadnyaAdapter
            setHasFixedSize(true)
        }
    }

    private fun getLatestYadnyaData() {
        ApiService.endpoint.getYadnyaNewList()
            .enqueue(object: Callback<NewYadnyaModel>{
                override fun onResponse(
                    call: Call<NewYadnyaModel>,
                    response: Response<NewYadnyaModel>
                ) {
                    showYadnyaData(response.body()!!)
                }

                override fun onFailure(call: Call<NewYadnyaModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun showYadnyaData(data: NewYadnyaModel) {
        val results = data.data
        yadnyaAdapter.setData(results)
    }

    private fun getYadnyaMasterData() {
        ApiService.endpoint.getYadnyaMasterList()
            .enqueue(object:Callback<List<HomeModel>>{
                override fun onResponse(
                    call: Call<List<HomeModel>>,
                    response: Response<List<HomeModel>>
                ) {
                    if(response.isSuccessful){
                        val result = response.body()
                        printLog(result.toString())
                        fetchData(result!!)

                        cardAdapter = CardSliderAdapter(list)
                        binding.viewPager.adapter = cardAdapter
                        dots = ArrayList()
                        setIndicator()

                        binding.viewPager.registerOnPageChangeCallback(object  : ViewPager2.OnPageChangeCallback(){
                            override fun onPageSelected(position: Int) {
                                selectedDot(position)
                                super.onPageSelected(position)
                            }
                        })
                        setShimmerToStop()
                    }
                }

                override fun onFailure(call: Call<List<HomeModel>>, t: Throwable) {
                    Toast.makeText(getActivity(), "No Connection", Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun setShimmerToStop() {
        binding.shimmerHome.stopShimmer()
        binding.shimmerHome.visibility = View.GONE
        binding.swipe.visibility       = View.VISIBLE
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearList()
        handler.removeCallbacks(runnable)
        _binding = null
    }

    private fun clearList() {
        yadnyaList.clear()
        list.clear()
    }

    private fun selectedDot(position: Int) {
        for(i in 0 until list.size){
            if(i == position)
                dots[i].setTextColor(Color.parseColor("#E32027"))
            else
                dots[i].setTextColor(Color.parseColor("#545454"))
        }
    }

    private fun setIndicator() {
        for (i in 0 until list.size){
            dots.add(TextView(getActivity()))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                dots[i].text = Html.fromHtml("&#9679", Html.FROM_HTML_MODE_LEGACY).toString()
            } else {
                dots[i].text = Html.fromHtml("&#9679")
            }
            dots[i].textSize = 8f
            binding.dotsIndicator.addView(dots[i])
        }
    }

    private fun fetchData(titles: List<HomeModel>) {
        for (title in titles) {
            yadnyaList.add(
                title.nama_kategori
            )
        }

        for (i in yadnyaList.indices) {
            list.add(
                CardSliderData(
                    yadnyaList[i]
                )
            )
        }
    }
}

package com.example.ekidungmantram.user.fragment

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.CardSliderAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.data.CardSliderData
import com.example.ekidungmantram.databinding.FragmentHomeBinding
import com.example.ekidungmantram.model.HomeModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter : CardSliderAdapter
    private val list = ArrayList<CardSliderData>()
    private lateinit var dots: ArrayList<TextView>
    private val yadnyaList = ArrayList<String>()

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

                        adapter = CardSliderAdapter(list)
                        binding.viewPager.adapter = adapter
                        dots = ArrayList()
                        setIndicator()

                        binding.viewPager.registerOnPageChangeCallback(object  : ViewPager2.OnPageChangeCallback(){
                            override fun onPageSelected(position: Int) {
                                selectedDot(position)
                                super.onPageSelected(position)
                            }
                        })
                    }
                }

                override fun onFailure(call: Call<List<HomeModel>>, t: Throwable) {
                    Toast.makeText(getActivity(), "No Connection", Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        clearList()
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

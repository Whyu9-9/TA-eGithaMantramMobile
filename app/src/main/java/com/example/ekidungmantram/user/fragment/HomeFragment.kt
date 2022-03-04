package com.example.ekidungmantram.user.fragment

import android.content.Intent
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
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.CardSliderAdapter
import com.example.ekidungmantram.adapter.NewKidungAdapter
import com.example.ekidungmantram.adapter.NewMantramAdapter
import com.example.ekidungmantram.adapter.NewYadnyaAdapter
import com.example.ekidungmantram.api.ApiService
import com.example.ekidungmantram.data.CardSliderData
import com.example.ekidungmantram.databinding.FragmentHomeBinding
import com.example.ekidungmantram.model.HomeModel
import com.example.ekidungmantram.model.NewKidungModel
import com.example.ekidungmantram.model.NewMantramModel
import com.example.ekidungmantram.model.NewYadnyaModel
import com.example.ekidungmantram.user.DetailYadnyaActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private lateinit var binding        : FragmentHomeBinding
    private lateinit var dots           : ArrayList<TextView>
    private val yadnyaList              = ArrayList<String>()
    private val list                    = ArrayList<CardSliderData>()
    private lateinit var handler        : Handler
    private lateinit var runnable       : Runnable
    private lateinit var cardAdapter    : CardSliderAdapter
    private lateinit var yadnyaAdapter  : NewYadnyaAdapter
    private lateinit var kidungAdapter  : NewKidungAdapter
    private lateinit var mantramAdapter : NewMantramAdapter
    private var gridLayoutManagerY      : GridLayoutManager? = null
    private var gridLayoutManagerK      : GridLayoutManager? = null
    private var gridLayoutManagerM      : GridLayoutManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getYadnyaMasterData()
        setupRecyclerViewY()
        setupRecyclerViewK()
        setupRecyclerViewM()
        getLatestYadnyaData()
        getLatestKidungData()
        getLatestMantramData()
        runAutoSlideCard()

        val swiped = binding.swipe
        swiped.setOnRefreshListener {
            setupRecyclerViewY()
            setupRecyclerViewK()
            setupRecyclerViewM()
            getLatestYadnyaData()
            getLatestKidungData()
            getLatestMantramData()
            swiped.isRefreshing = false
        }
    }

    private fun runAutoSlideCard() {
        handler  = Handler(Looper.myLooper()!!)
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

    private fun setupRecyclerViewY() {
        yadnyaAdapter = NewYadnyaAdapter(arrayListOf(), object : NewYadnyaAdapter.OnAdapterListener{
            override fun onClick(result: NewYadnyaModel.Data) {
                val bundle = Bundle()
                val intent = Intent(getActivity(), DetailYadnyaActivity::class.java)
                bundle.putInt("id_yadnya", result.id_post)
                bundle.putInt("id_kategori", result.id_kategori)
                bundle.putString("nama_yadnya", result.nama_post)
                bundle.putString("kategori", result.kategori)
                bundle.putString("gambar", Constant.URL+result.gambar)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })
        binding.yadnyaBaru.apply {
            gridLayoutManagerY = GridLayoutManager(getActivity(), 1, LinearLayoutManager.HORIZONTAL, false)
            layoutManager      = gridLayoutManagerY
            adapter            = yadnyaAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupRecyclerViewK() {
        kidungAdapter = NewKidungAdapter(arrayListOf(), object : NewKidungAdapter.OnAdapterKidungListener{
            override fun onClick(result: NewKidungModel.DataK) {
                val bundle = Bundle()
                val intent = Intent(getActivity(), DetailYadnyaActivity::class.java)
                bundle.putInt("id_yadnya", result.id_post)
                bundle.putInt("id_kategori", result.id_kategori)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })
        binding.kidungBaru.apply {
            gridLayoutManagerK = GridLayoutManager(getActivity(), 1, LinearLayoutManager.HORIZONTAL, false)
            layoutManager      = gridLayoutManagerK
            adapter            = kidungAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupRecyclerViewM() {
        mantramAdapter = NewMantramAdapter(arrayListOf(), object : NewMantramAdapter.OnAdapterMantramListener{
            override fun onClick(result: NewMantramModel.DataM) {
                val bundle = Bundle()
                val intent = Intent(getActivity(), DetailYadnyaActivity::class.java)
                bundle.putInt("id_yadnya", result.id_post)
                bundle.putInt("id_kategori", result.id_kategori)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })
        binding.mantramBaru.apply {
            gridLayoutManagerM = GridLayoutManager(getActivity(), 1, LinearLayoutManager.HORIZONTAL, false)
            layoutManager      = gridLayoutManagerM
            adapter            = mantramAdapter
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
                    binding.nodatayadnya.visibility  = View.GONE
                }

                override fun onFailure(call: Call<NewYadnyaModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun getLatestKidungData() {
        ApiService.endpoint.getKidungNewList()
            .enqueue(object: Callback<NewKidungModel>{
                override fun onResponse(
                    call: Call<NewKidungModel>,
                    response: Response<NewKidungModel>
                ) {
                    if(response.body()!!.data.toString() == "[]"){
                        binding.nodatakidung.visibility  = View.VISIBLE
                    }else{
                        binding.nodatakidung.visibility  = View.GONE
                        showKidungData(response.body()!!)
                    }

                }

                override fun onFailure(call: Call<NewKidungModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun getLatestMantramData() {
        ApiService.endpoint.getMantramNewList()
            .enqueue(object: Callback<NewMantramModel>{
                override fun onResponse(
                    call: Call<NewMantramModel>,
                    response: Response<NewMantramModel>
                ) {
                    if(response.body()!!.data.toString() == "[]"){
                        binding.nodatamantram.visibility  = View.VISIBLE
                    }else{
                        showMantramData(response.body()!!)
                        binding.nodatamantram.visibility  = View.GONE
                    }
                }

                override fun onFailure(call: Call<NewMantramModel>, t: Throwable) {
                    printLog("on failure: $t")
                }

            })
    }

    private fun showYadnyaData(data: NewYadnyaModel) {
        val results = data.data
        yadnyaAdapter.setData(results)
    }

    private fun showKidungData(data: NewKidungModel) {
        val results = data.data
        kidungAdapter.setData(results)
    }

    private fun showMantramData(data: NewMantramModel) {
        val results = data.data
        mantramAdapter.setData(results)
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
                    setShimmerToStop()
                }

            })
    }

    private fun setShimmerToStop() {
        binding.shimmerHome.stopShimmer()
        binding.shimmerHome.visibility   = View.GONE
        binding.swipe.visibility         = View.VISIBLE
    }

    private fun printLog(message: String) {
        Log.d("HomeFragment", message)
    }

    override fun onDestroyView() {
        clearList()
        handler.removeCallbacks(runnable)
        super.onDestroyView()
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
            dots.add(TextView(context))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                dots[i].text = Html.fromHtml("&#9679 ", Html.FROM_HTML_MODE_LEGACY).toString()
            } else {
                dots[i].text = Html.fromHtml("&#9679 ")
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

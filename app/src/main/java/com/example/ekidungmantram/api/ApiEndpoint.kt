package com.example.ekidungmantram.api

import com.example.ekidungmantram.model.*
import com.example.ekidungmantram.model.adminmodel.AllMantramAdminModel
import com.example.ekidungmantram.model.adminmodel.AllYadnyaHomeAdminModel
import com.example.ekidungmantram.model.adminmodel.CrudModel
import retrofit2.Call
import retrofit2.http.*

interface ApiEndpoint {
    //auth
    @FormUrlEncoded
    @POST("login")
    fun loginAdmin (
        @Field("email") email:String,
        @Field("password") password:String
    ):Call<AdminModel>

    //Admin
    //Yadnya
    @GET("admin/listyadnya")
    fun getYadnyaAdminHomeList(): Call<ArrayList<AllYadnyaHomeAdminModel>>

    //mantram
    @GET("admin/listallmantram")
    fun getAllMantramListAdmin() : Call<ArrayList<AllMantramAdminModel>>
    @GET("admin/detailmantram/{id_post}")
    fun getDetailMantramAdmin(@Path("id_post") id:Int) : Call<DetailMantramModel>

    @FormUrlEncoded
    @POST("admin/createmantram")
    fun createMantramAdmin (
        @Field("nama_post") namaPost:String,
        @Field("jenis_mantram") jenisMantram:String,
        @Field("video") video:String,
        @Field("deskripsi") deskripsi:String,
        @Field("kategori") kategori:String,
        @Field("gambar") gambar:String,
    ):Call<CrudModel>









    //User
    //Yadnya
    @GET("listyadnya")
    fun getYadnyaMasterList(): Call<List<HomeModel>>
    @GET("yadnya/{nama_yadnya}")
    fun getYadnyaCardClickedList(@Path("nama_yadnya") nama_yadnya: String) : Call<ArrayList<YadnyaCardClickedModel>>
    @GET("listyadnyaterbaru")
    fun getYadnyaNewList(): Call<NewYadnyaModel>
    @GET("listkidungterbaru")
    fun getKidungNewList(): Call<NewKidungModel>
    @GET("listmantramterbaru")
    fun getMantramNewList(): Call<NewMantramModel>
    @GET("listallyadnya")
    fun getYadnyaAllList(): Call<ArrayList<AllYadnyaModel>>
    @GET("detailyadnya/{id_post}")
    fun getDetailYadnya(@Path("id_post") id: Int) : Call<DetailYadnyaModel>
    @GET("detailawal/{id_post}")
    fun getDetailAwal(@Path("id_post") id: Int) : Call<ProsesiAwalModel>
    @GET("detailpuncak/{id_post}")
    fun getDetailPuncak(@Path("id_post") id: Int) : Call<ProsesiPuncakModel>
    @GET("detailakhir/{id_post}")
    fun getDetailAkhir(@Path("id_post") id: Int) : Call<ProsesiAkhirModel>
    @GET("detailgamelanyadnya/{id_post}")
    fun getDetailGamelanYadnya(@Path("id_post") id: Int) : Call<GamelanYadnyaModel>
    @GET("detailtariyadnya/{id_post}")
    fun getDetailTariYadnya(@Path("id_post") id: Int) : Call<TariYadnyaModel>
    @GET("detailkidungyadnya/{id_post}")
    fun getDetailKidungYadnya(@Path("id_post") id: Int) : Call<KidungYadnyaModel>

    //kidung
    @GET("listallkidung")
    fun getKidungMasterList() : Call<ArrayList<AllKidungModel>>
    @GET("detailkidung/{id_post}")
    fun getDetailKidung(@Path("id_post") id: Int) : Call<DetailKidungModel>
    @GET("detailbaitkidung/{id_post}")
    fun getDetailBaitKidung(@Path("id_post") id:Int) : Call<DetailBaitKidungModel>

    //mantram
    @GET("listallmantram")
    fun getMantramMasterList() : Call<ArrayList<AllMantramModel>>
    @GET("detailmantram/{id_post}")
    fun getDetailMantram(@Path("id_post") id:Int) : Call<DetailMantramModel>

    //tari
    @GET("listalltari")
    fun getTariMasterList() : Call<ArrayList<AllTariModel>>
    @GET("detailtari/{id_post}")
    fun getDetailTari(@Path("id_post") id:Int) : Call<DetailTariModel>
    @GET("detailtabuhtari/{id_post}")
    fun getDetailTabuhTari(@Path("id_post") id:Int) : Call<DetailTabuhTariModel>

    //tabuh
    @GET("listalltabuh")
    fun getTabuhMasterList() : Call<ArrayList<AllTabuhModel>>
    @GET("detailtabuh/{id_post}")
    fun getDetailTabuh(@Path("id_post") id:Int) : Call<DetailTabuhModel>

    //gamelan
    @GET("listallgamelan")
    fun getGamelanMasterList() : Call<ArrayList<AllGamelanModel>>
    @GET("detailgamelan/{id_post}")
    fun getDetailGamelan(@Path("id_post") id:Int) : Call<DetailGamelanModel>
    @GET("detailtabuhgamelan/{id_post}")
    fun getDetailTabuhGamelan(@Path("id_post") id:Int) : Call<DetailTabuhGamelanModel>

    //prosesi
    @GET("listallprosesi")
    fun getProsesiMasterList() : Call<ArrayList<AllProsesiModel>>
    @GET("detailprosesi/{id_post}")
    fun getDetailProsesi(@Path("id_post") id:Int) : Call<DetailProsesiModel>
    @GET("detailgamelanprosesi/{id_post}")
    fun getDetailGamelanProsesi(@Path("id_post") id: Int) : Call<GamelanProsesiModel>
    @GET("detailtariprosesi/{id_post}")
    fun getDetailTariProsesi(@Path("id_post") id: Int) : Call<TariProsesiModel>
    @GET("detailkidungprosesi/{id_post}")
    fun getDetailKidungProsesi(@Path("id_post") id: Int) : Call<KidungProsesiModel>
    @GET("detailtabuhprosesi/{id_post}")
    fun getDetailTabuhProsesi(@Path("id_post") id: Int) : Call<TabuhProsesiModel>
    @GET("detailmantramprosesi/{id_post}")
    fun getDetailMantramProsesi(@Path("id_post") id: Int) : Call<MantramProsesiModel>
}
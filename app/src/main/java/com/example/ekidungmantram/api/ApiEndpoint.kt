package com.example.ekidungmantram.api

import com.example.ekidungmantram.model.*
import com.example.ekidungmantram.model.adminmodel.*
import retrofit2.Call
import retrofit2.http.*

interface ApiEndpoint {
    //Auth
    @FormUrlEncoded
    @POST("login")
    fun loginAdmin (
        @Field("email") email:String,
        @Field("password") password:String
    ):Call<AdminModel>

    //Admin
    //Home
    @GET("admin/listyadnya")
    fun getYadnyaAdminHomeList(): Call<ArrayList<AllYadnyaHomeAdminModel>>

    //Mantram
    @GET("admin/listallmantram")
    fun getAllMantramListAdmin() : Call<ArrayList<AllMantramAdminModel>>

    @GET("admin/detailmantram/{id_post}")
    fun getDetailMantramAdmin(@Path("id_post") id:Int) : Call<DetailMantramAdminModel>

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

    @GET("admin/showmantram/{id_post}")
    fun getShowMantramAdmin(@Path("id_post") id:Int) : Call<DetailMantramAdminModel>

    @FormUrlEncoded
    @POST("admin/updatemantram/{id_post}")
    fun updateMantramAdmin (
        @Path("id_post") id:Int,
        @Field("nama_post") namaPost:String,
        @Field("jenis_mantram") jenisMantram:String,
        @Field("video") video:String,
        @Field("deskripsi") deskripsi:String,
        @Field("kategori") kategori:String,
        @Field("gambar") gambar:String,
    ):Call<CrudModel>

    @POST("admin/deletemantram/{id_post}")
    fun deleteMantramAdmin (
        @Path("id_post") id:Int
    ):Call<CrudModel>

    @FormUrlEncoded
    @POST("admin/editbaitmantram/{id_post}")
    fun editBaitMantram (
        @Path("id_post") id:Int,
        @Field("bait_mantra") baitMantra:String,
    ):Call<CrudModel>

    @FormUrlEncoded
    @POST("admin/editartimantram/{id_post}")
    fun editArtiMantram (
        @Path("id_post") id:Int,
        @Field("arti_mantra") artiMantra:String,
    ):Call<CrudModel>

    //Admin Management
    @GET("admin/listadmin")
    fun getAllListAdmin() : Call<ArrayList<AllDataAdminModel>>

    @GET("admin/detailadmin/{id_user}")
    fun getDetailAdmin(@Path("id_user") id:Int) : Call<DetailDataAdminModel>

    @FormUrlEncoded
    @POST("admin/createadmin")
    fun createDataAdmin (
        @Field("name") namaAdmin:String,
        @Field("email") emailAdmin:String,
        @Field("password") passwordAdmin:String,
    ):Call<CrudModel>

    @FormUrlEncoded
    @POST("admin/editadmin/{id_user}")
    fun updateDataAdmin (
        @Path("id_user") id:Int,
        @Field("name") namaAdmin:String,
        @Field("email") emailAdmin:String,
        @Field("password") passwordAdmin:String,
    ):Call<CrudModel>

    @POST("admin/deleteadmin/{id_user}")
    fun deleteDataAdmin (
        @Path("id_user") id:Int
    ):Call<CrudModel>

    //Tabuh
    @GET("admin/listalltabuhadmin")
    fun getAllListTabuhAdmin() : Call<ArrayList<AllTabuhAdminModel>>

    @GET("admin/detailtabuhadmin/{id_post}")
    fun getDetailTabuhAdmin(@Path("id_post") id:Int) : Call<DetailTabuhAdminModel>

    @FormUrlEncoded
    @POST("admin/createtabuh")
    fun createDataTabuhAdmin (
        @Field("nama_post") namaPost:String,
        @Field("video") video:String,
        @Field("deskripsi") deskripsi:String,
        @Field("gambar") gambar:String,
    ):Call<CrudModel>

    @GET("admin/showtabuh/{id_post}")
    fun getShowTabuhAdmin(@Path("id_post") id:Int) : Call<DetailTabuhAdminModel>

    @FormUrlEncoded
    @POST("admin/edittabuh/{id_post}")
    fun updateDataTabuhAdmin (
        @Path("id_post") id:Int,
        @Field("nama_post") namaPost:String,
        @Field("video") video:String,
        @Field("deskripsi") deskripsi:String,
        @Field("gambar") gambar:String,
    ):Call<CrudModel>

    @POST("admin/deletetabuh/{id_post}")
    fun deleteDataTabuhAdmin (
        @Path("id_post") id:Int
    ):Call<CrudModel>

    //Kidung
    @GET("admin/listallkidungadmin")
    fun getAllListKidungAdmin() : Call<ArrayList<AllKidungAdminModel>>

    @GET("admin/detailkidungadmin/{id_post}")
    fun getDetailKidungAdmin(@Path("id_post") id:Int) : Call<DetailKidungAdminModel>

    @GET("admin/listlirikkidungadmin/{id_post}")
    fun getDetailAllLirikKidungAdmin(@Path("id_post") id:Int) : Call<ArrayList<DetailAllLirikKidungAdminModel>>

    @FormUrlEncoded
    @POST("admin/createkidung")
    fun createDataKidungAdmin (
        @Field("nama_post") namaPost:String,
        @Field("video") video:String,
        @Field("kategori") kategori:String,
        @Field("deskripsi") deskripsi:String,
        @Field("gambar") gambar:String,
    ):Call<CrudModel>

    @GET("admin/showkidung/{id_post}")
    fun getShowKidungAdmin(@Path("id_post") id:Int) : Call<DetailKidungAdminModel>

    @FormUrlEncoded
    @POST("admin/editkidung/{id_post}")
    fun updateDataKidungAdmin (
        @Path("id_post") id:Int,
        @Field("nama_post") namaPost:String,
        @Field("video") video:String,
        @Field("kategori") kategori:String,
        @Field("deskripsi") deskripsi:String,
        @Field("gambar") gambar:String,
    ):Call<CrudModel>

    @POST("admin/deletekidung/{id_post}")
    fun deleteDataKidungAdmin (
        @Path("id_post") id:Int
    ):Call<CrudModel>

    @GET("admin/lirikkidungadmin/{id_post}")
    fun getAllLirikKidungAdmin(@Path("id_post") id:Int) : Call<ArrayList<AllLirikKidungAdminModel>>

    @FormUrlEncoded
    @POST("admin/addlirikkidung/{id_post}")
    fun createDataLirikKidungAdmin (
        @Path("id_post") id:Int,
        @Field("bait_kidung") baitKidung:String,
    ):Call<CrudModel>

    @GET("admin/showlirikkidung/{id_det_post}")
    fun getShowLirikKidungAdmin(@Path("id_det_post") id:Int) : Call<DetailLirikKidungAdminModel>

    @FormUrlEncoded
    @POST("admin/editlirikkidung/{id_det_post}")
    fun updateDataLirikKidungAdmin (
        @Path("id_det_post") id:Int,
        @Field("bait_kidung") lirikKidung:String,
    ):Call<CrudModel>

    @FormUrlEncoded
    @POST("admin/deletelirikkidung/{id_det_post}")
    fun deleteDataLirikKidungAdmin (
        @Path("id_det_post") id:Int,
        @Field("kidung_id") idKidung:Int,
    ):Call<CrudModel>

    //Gamelan
    @GET("admin/listallgamelanadmin")
    fun getAllListGamelanAdmin() : Call<ArrayList<AllGamelanAdminModel>>

    @GET("admin/detailgamelanadmin/{id_post}")
    fun getDetailGamelanAdmin(@Path("id_post") id:Int) : Call<DetailGamelanAdminModel>

    @GET("admin/listtabuhongamelan/{id_post}")
    fun getDetailAllTabuhOnGamelanAdmin(@Path("id_post") id:Int) : Call<ArrayList<DetailAllTabuhOnGamelanAdminModel>>

    @FormUrlEncoded
    @POST("admin/creategamelan")
    fun createDataGamelanAdmin (
        @Field("nama_post") namaPost:String,
        @Field("video") video:String,
        @Field("deskripsi") deskripsi:String,
        @Field("gambar") gambar:String,
    ):Call<CrudModel>

    @GET("admin/showgamelan/{id_post}")
    fun getShowGamelanAdmin(@Path("id_post") id:Int) : Call<DetailGamelanAdminModel>

    @FormUrlEncoded
    @POST("admin/editgamelan/{id_post}")
    fun updateDataGamelanAdmin (
        @Path("id_post") id:Int,
        @Field("nama_post") namaPost:String,
        @Field("video") video:String,
        @Field("deskripsi") deskripsi:String,
        @Field("gambar") gambar:String,
    ):Call<CrudModel>

    @POST("admin/deletegamelan/{id_post}")
    fun deleteDataGamelanAdmin (
        @Path("id_post") id:Int
    ):Call<CrudModel>

    @GET("admin/listtabuhnotongamelan/{id_post}")
    fun getDetailAllTabuhNotOnGamelanAdmin(@Path("id_post") id:Int) : Call<ArrayList<AllTabuhAdminModel>>

    @FormUrlEncoded
    @POST("admin/addtabuhongamelan/{id_post}")
    fun addDataTabuhToGamelanAdmin (
        @Path("id_post") id:Int,
        @Field("id_tabuh") idTabuh:Int,
    ):Call<CrudModel>

    @POST("admin/deletetabuhongamelan/{id_post}")
    fun deleteDataTabuhOnGamelanAdmin (
        @Path("id_post") id:Int
    ):Call<CrudModel>

    //Tari
    @GET("admin/listalltariadmin")
    fun getAllListTariAdmin() : Call<ArrayList<AllTariAdminModel>>

    @GET("admin/detailtariadmin/{id_post}")
    fun getDetailTariAdmin(@Path("id_post") id:Int) : Call<DetailTariAdminModel>

    @GET("admin/listtabuhontari/{id_post}")
    fun getDetailAllTabuhOnTariAdmin(@Path("id_post") id:Int) : Call<ArrayList<DetailAllTabuhOnTariAdminModel>>

    @FormUrlEncoded
    @POST("admin/createtari")
    fun createDataTariAdmin (
        @Field("nama_post") namaPost:String,
        @Field("video") video:String,
        @Field("deskripsi") deskripsi:String,
        @Field("gambar") gambar:String,
    ):Call<CrudModel>

    @GET("admin/showtari/{id_post}")
    fun getShowTariAdmin(@Path("id_post") id:Int) : Call<DetailTariAdminModel>

    @FormUrlEncoded
    @POST("admin/edittari/{id_post}")
    fun updateDataTariAdmin (
        @Path("id_post") id:Int,
        @Field("nama_post") namaPost:String,
        @Field("video") video:String,
        @Field("deskripsi") deskripsi:String,
        @Field("gambar") gambar:String,
    ):Call<CrudModel>

    @POST("admin/deletetari/{id_post}")
    fun deleteDataTariAdmin (
        @Path("id_post") id:Int
    ):Call<CrudModel>

    @GET("admin/listtabuhnotontari/{id_post}")
    fun getDetailAllTabuhNotOnTariAdmin(@Path("id_post") id:Int) : Call<ArrayList<AllTabuhAdminModel>>

    @FormUrlEncoded
    @POST("admin/addtabuhontari/{id_post}")
    fun addDataTabuhToTariAdmin (
        @Path("id_post") id:Int,
        @Field("id_tabuh") idTabuh:Int,
    ):Call<CrudModel>

    @POST("admin/deletetabuhontari/{id_post}")
    fun deleteDataTabuhOnTariAdmin (
        @Path("id_post") id:Int
    ):Call<CrudModel>

    //Prosesi
    @GET("admin/listallprosesiadmin")
    fun getAllListProsesiAdmin() : Call<ArrayList<AllProsesiAdminModel>>

    @GET("admin/detailprosesiadmin/{id_post}")
    fun getDetailProsesiAdmin(@Path("id_post") id:Int) : Call<DetailProsesiAdminModel>

    @GET("admin/listgamelanonprosesi/{id_post}")
    fun getDetailAllGamelanOnProsesiAdmin(@Path("id_post") id:Int) : Call<ArrayList<DetailAllGamelanOnProsesiAdminModel>>

    @GET("admin/listtarionprosesi/{id_post}")
    fun getDetailAllTariOnProsesiAdmin(@Path("id_post") id:Int) : Call<ArrayList<DetailAllTariOnProsesiAdminModel>>

    @GET("admin/listkidungonprosesi/{id_post}")
    fun getDetailAllKidungOnProsesiAdmin(@Path("id_post") id:Int) : Call<ArrayList<DetailAllKidungOnProsesiAdminModel>>

    @GET("admin/listtabuhonprosesi/{id_post}")
    fun getDetailAllTabuhOnProsesiAdmin(@Path("id_post") id:Int) : Call<ArrayList<DetailAllTabuhOnProsesiAdminModel>>

    @GET("admin/listmantramonprosesi/{id_post}")
    fun getDetailAllMantramOnProsesiAdmin(@Path("id_post") id:Int) : Call<ArrayList<DetailAllMantramOnProsesiAdminModel>>

    @FormUrlEncoded
    @POST("admin/createprosesi")
    fun createDataProsesiAdmin (
        @Field("nama_post") namaPost:String,
        @Field("video") video:String,
        @Field("deskripsi") deskripsi:String,
        @Field("gambar") gambar:String,
    ):Call<CrudModel>

    @GET("admin/showprosesi/{id_post}")
    fun getShowProsesiAdmin(@Path("id_post") id:Int) : Call<DetailProsesiAdminModel>

    @FormUrlEncoded
    @POST("admin/editprosesi/{id_post}")
    fun updateDataProsesiAdmin (
        @Path("id_post") id:Int,
        @Field("nama_post") namaPost:String,
        @Field("video") video:String,
        @Field("deskripsi") deskripsi:String,
        @Field("gambar") gambar:String,
    ):Call<CrudModel>

    @POST("admin/deleteprosesi/{id_post}")
    fun deleteDataProsesiAdmin (
        @Path("id_post") id:Int
    ):Call<CrudModel>

    @GET("admin/listgamelannotonprosesi/{id_post}")
    fun getDetailAllGamelanNotOnProsesiAdmin(@Path("id_post") id:Int) : Call<ArrayList<AllGamelanAdminModel>>

    @FormUrlEncoded
    @POST("admin/addgamelanonprosesi/{id_post}")
    fun addDataGamelanToProsesiAdmin (
        @Path("id_post") id:Int,
        @Field("id_gamelan") idGamelan:Int,
    ):Call<CrudModel>

    @POST("admin/deletegamelanonprosesi/{id_post}")
    fun deleteDataGamelanOnProsesiAdmin (
        @Path("id_post") id:Int
    ):Call<CrudModel>

    @GET("admin/listtarinotonprosesi/{id_post}")
    fun getDetailAllTariNotOnProsesiAdmin(@Path("id_post") id:Int) : Call<ArrayList<AllTariAdminModel>>

    @FormUrlEncoded
    @POST("admin/addtarionprosesi/{id_post}")
    fun addDataTariToProsesiAdmin (
        @Path("id_post") id:Int,
        @Field("id_tari") idTari:Int,
    ):Call<CrudModel>

    @POST("admin/deletetarionprosesi/{id_post}")
    fun deleteDataTariOnProsesiAdmin (
        @Path("id_post") id:Int
    ):Call<CrudModel>

    @GET("admin/listkidungnotonprosesi/{id_post}")
    fun getDetailAllKidungNotOnProsesiAdmin(@Path("id_post") id:Int) : Call<ArrayList<AllKidungAdminModel>>

    @FormUrlEncoded
    @POST("admin/addkidungonprosesi/{id_post}")
    fun addDataKidungToProsesiAdmin (
        @Path("id_post") id:Int,
        @Field("id_kidung") idKidung:Int,
    ):Call<CrudModel>

    @POST("admin/deletekidungonprosesi/{id_post}")
    fun deleteDataKidungOnProsesiAdmin (
        @Path("id_post") id:Int
    ):Call<CrudModel>

    @GET("admin/listtabuhnotonprosesi/{id_post}")
    fun getDetailAllTabuhNotOnProsesiAdmin(@Path("id_post") id:Int) : Call<ArrayList<AllTabuhAdminModel>>

    @FormUrlEncoded
    @POST("admin/addtabuhonprosesi/{id_post}")
    fun addDataTabuhToProsesiAdmin (
        @Path("id_post") id:Int,
        @Field("id_tabuh") idTabuh:Int,
    ):Call<CrudModel>

    @POST("admin/deletetabuhonprosesi/{id_post}")
    fun deleteDataTabuhOnProsesiAdmin (
        @Path("id_post") id:Int
    ):Call<CrudModel>

    @GET("admin/listmantramnotonprosesi/{id_post}")
    fun getDetailAllMantramNotOnProsesiAdmin(@Path("id_post") id:Int) : Call<ArrayList<AllMantramAdminModel>>

    @FormUrlEncoded
    @POST("admin/addmantramonprosesi/{id_post}")
    fun addDataMantramToProsesiAdmin (
        @Path("id_post") id:Int,
        @Field("id_mantram") idMantram:Int,
    ):Call<CrudModel>

    @POST("admin/deletemantramonprosesi/{id_post}")
    fun deleteDataMantramOnProsesiAdmin (
        @Path("id_post") id:Int
    ):Call<CrudModel>

    //Yadnya
    @GET("admin/listallyadnyaadmin/{id_yadnya}")
    fun getAllListYadnyaAdmin(
        @Path("id_yadnya") id:Int
    ) : Call<ArrayList<AllYadnyaAdminModel>>

    @GET("admin/detailyadnyaadmin/{id_post}")
    fun getDetailYadnyaAdmin(@Path("id_post") id:Int) : Call<DetailYadnyaAdminModel>

    @GET("admin/listprosesiawalonyadnya/{id_post}")
    fun getDetailAllProsesiAwalOnYadnyaAdmin(@Path("id_post") id:Int) : Call<ArrayList<DetailAllProsesiAwalOnYadnyaAdminModel>>
//
//    @GET("admin/listprosesipuncakonyadnya/{id_post}")
//    fun getDetailAllProsesiPuncakOnYadnyaAdmin(@Path("id_post") id:Int) : Call<ArrayList<DetailAllProsesiPuncakOnYadnyaAdminModel>>
//
//    @GET("admin/listprosesiakhironyadnya/{id_post}")
//    fun getDetailAllProsesiAkhirOnYadnyaAdmin(@Path("id_post") id:Int) : Call<ArrayList<DetailAllProsesiAkhirOnYadnyaAdminModel>>
//
//    @GET("admin/listgamelanonyadnya{id_post}")
//    fun getDetailAllGamelanOnYadnyaAdmin(@Path("id_post") id:Int) : Call<ArrayList<DetailAllGamelanOnYadnyaAdminModel>>
//
//    @GET("admin/listtarionyadnya/{id_post}")
//    fun getDetailAllTariOnYadnyaAdmin(@Path("id_post") id:Int) : Call<ArrayList<DetailAllTariOnYadnyaAdminModel>>
//
//    @GET("admin/listkidungonyadnya/{id_post}")
//    fun getDetailAllKidungOnYadnyaAdmin(@Path("id_post") id:Int) : Call<ArrayList<DetailAllKidungOnYadnyaAdminModel>>

    @FormUrlEncoded
    @POST("admin/createyadnya")
    fun createDataYadnyaAdmin (
        @Field("nama_post") namaPost:String,
        @Field("video") video:String,
        @Field("deskripsi") deskripsi:String,
        @Field("gambar") gambar:String,
        @Field("kategori") kategori:String,
    ):Call<CrudModel>

    @GET("admin/showyadnya/{id_post}")
    fun getShowYadnyaAdmin(@Path("id_post") id:Int) : Call<DetailYadnyaAdminModel>

    @FormUrlEncoded
    @POST("admin/edityadnya/{id_post}")
    fun updateDataYadnyaAdmin (
        @Path("id_post") id:Int,
        @Field("nama_post") namaPost:String,
        @Field("video") video:String,
        @Field("deskripsi") deskripsi:String,
        @Field("gambar") gambar:String,
    ):Call<CrudModel>

    @POST("admin/deleteyadnya/{id_post}")
    fun deleteDataYadnyaAdmin (
        @Path("id_post") id:Int
    ):Call<CrudModel>

    @GET("admin/listprosesiawalnotonyadnya/{id_post}")
    fun getDetailAllProsesiAwalNotOnYadnyaAdmin(@Path("id_post") id:Int) : Call<ArrayList<AllProsesiAdminModel>>

    @FormUrlEncoded
    @POST("admin/addprosesiawalonyadnya/{id_post}")
    fun addDataProsesiAwalToYadnyaAdmin (
        @Path("id_post") id:Int,
        @Field("id_prosesi") idProsesi:Int,
    ):Call<CrudModel>

    @FormUrlEncoded
    @POST("admin/upprosesiawalonyadnya/{id_post}")
    fun upDataProsesiAwalAdmin (
        @Path("id_post") id:Int,
        @Field("id_prosesi") idProsesi:Int,
    ):Call<CrudModel>

    @FormUrlEncoded
    @POST("admin/downprosesiawalonyadnya/{id_post}")
    fun downDataProsesiAwalAdmin (
        @Path("id_post") id:Int,
        @Field("id_prosesi") idProsesi:Int,
    ):Call<CrudModel>

    @FormUrlEncoded
    @POST("admin/deleteprosesiawalonyadnya/{id_post}")
    fun deleteDataProsesiAwalOnYadnyaAdmin (
        @Path("id_post") id:Int,
        @Field("yadnya_id") idYadnya:Int,
    ):Call<CrudModel>
//
//    @GET("admin/listprosesipuncaknotonyadnya/{id_post}")
//    fun getDetailAllProsesiPuncakNotOnYadnyaAdmin(@Path("id_post") id:Int) : Call<ArrayList<AllProsesiAdminModel>>
//
//    @FormUrlEncoded
//    @POST("admin/addprosesipuncakonyadnya/{id_post}")
//    fun addDataProsesiPuncakToYadnyaAdmin (
//        @Path("id_post") id:Int,
//        @Field("id_prosesi") idProsesi:Int,
//    ):Call<CrudModel>
//
//    @FormUrlEncoded
//    @POST("admin/deleteprosesipuncakonyadnya/{id_post}")
//    fun deleteDataProsesiPuncakOnYadnyaAdmin (
//        @Path("id_post") id:Int,
//        @Field("yadnya_id") idYadnya:Int,
//    ):Call<CrudModel>
//
//    @GET("admin/listprosesiakhirnotonyadnya/{id_post}")
//    fun getDetailAllProsesiAkhirNotOnYadnyaAdmin(@Path("id_post") id:Int) : Call<ArrayList<AllProsesiAdminModel>>
//
//    @FormUrlEncoded
//    @POST("admin/addprosesiakhironyadnya/{id_post}")
//    fun addDataProsesiAkhirToYadnyaAdmin (
//        @Path("id_post") id:Int,
//        @Field("id_prosesi") idProsesi:Int,
//    ):Call<CrudModel>
//
//    @FormUrlEncoded
//    @POST("admin/deleteprosesiakhironyadnya/{id_post}")
//    fun deleteDataProsesiAkhirOnYadnyaAdmin (
//        @Path("id_post") id:Int,
//        @Field("yadnya_id") idYadnya:Int,
//    ):Call<CrudModel>

//    @GET("admin/listgamelannotonyadnya/{id_post}")
//    fun getDetailAllGamelanNotOnYadnyaAdmin(@Path("id_post") id:Int) : Call<ArrayList<AllGamelanAdminModel>>
//
//    @FormUrlEncoded
//    @POST("admin/addgamelanonyadnya/{id_post}")
//    fun addDataGamelanToYadnyaAdmin (
//        @Path("id_post") id:Int,
//        @Field("id_gamelan") idGamelan:Int,
//    ):Call<CrudModel>
//
//    @POST("admin/deletegamelanonyadnya/{id_post}")
//    fun deleteDataGamelanOnYadnyaAdmin (
//        @Path("id_post") id:Int
//    ):Call<CrudModel>
//
//    @GET("admin/listtarinotonyadnya/{id_post}")
//    fun getDetailAllTariNotOnYadnyaAdmin(@Path("id_post") id:Int) : Call<ArrayList<AllTariAdminModel>>
//
//    @FormUrlEncoded
//    @POST("admin/addtarionyadnya/{id_post}")
//    fun addDataTariToYadnyaAdmin (
//        @Path("id_post") id:Int,
//        @Field("id_tari") idTari:Int,
//    ):Call<CrudModel>
//
//    @POST("admin/deletetarionyadnya/{id_post}")
//    fun deleteDataTariOnYadnyaAdmin (
//        @Path("id_post") id:Int
//    ):Call<CrudModel>
//
//    @GET("admin/listkidungnotonyadnyan/{id_post}")
//    fun getDetailAllKidungNotOnYadnyaAdmin(@Path("id_post") id:Int) : Call<ArrayList<AllKidungAdminModel>>
//
//    @FormUrlEncoded
//    @POST("admin/addkidungonyadnya/{id_post}")
//    fun addDataKidungToYadnyaAdmin (
//        @Path("id_post") id:Int,
//        @Field("id_kidung") idKidung:Int,
//    ):Call<CrudModel>
//
//    @POST("admin/deletekidungonyadnya/{id_post}")
//    fun deleteDataKidungOnYadnyaAdmin (
//        @Path("id_post") id:Int
//    ):Call<CrudModel>

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

    //Kidung
    @GET("listallkidung")
    fun getKidungMasterList() : Call<ArrayList<AllKidungModel>>
    @GET("detailkidung/{id_post}")
    fun getDetailKidung(@Path("id_post") id: Int) : Call<DetailKidungModel>
    @GET("detailbaitkidung/{id_post}")
    fun getDetailBaitKidung(@Path("id_post") id:Int) : Call<DetailBaitKidungModel>

    //Mantram
    @GET("listallmantram")
    fun getMantramMasterList() : Call<ArrayList<AllMantramModel>>
    @GET("detailmantram/{id_post}")
    fun getDetailMantram(@Path("id_post") id:Int) : Call<DetailMantramModel>

    //Tari
    @GET("listalltari")
    fun getTariMasterList() : Call<ArrayList<AllTariModel>>
    @GET("detailtari/{id_post}")
    fun getDetailTari(@Path("id_post") id:Int) : Call<DetailTariModel>
    @GET("detailtabuhtari/{id_post}")
    fun getDetailTabuhTari(@Path("id_post") id:Int) : Call<DetailTabuhTariModel>

    //Tabuh
    @GET("listalltabuh")
    fun getTabuhMasterList() : Call<ArrayList<AllTabuhModel>>
    @GET("detailtabuh/{id_post}")
    fun getDetailTabuh(@Path("id_post") id:Int) : Call<DetailTabuhModel>

    //Gamelan
    @GET("listallgamelan")
    fun getGamelanMasterList() : Call<ArrayList<AllGamelanModel>>
    @GET("detailgamelan/{id_post}")
    fun getDetailGamelan(@Path("id_post") id:Int) : Call<DetailGamelanModel>
    @GET("detailtabuhgamelan/{id_post}")
    fun getDetailTabuhGamelan(@Path("id_post") id:Int) : Call<DetailTabuhGamelanModel>

    //Prosesi
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
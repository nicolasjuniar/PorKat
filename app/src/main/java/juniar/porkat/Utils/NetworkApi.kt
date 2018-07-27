package juniar.porkat.Utils

import io.reactivex.Observable
import juniar.porkat.auth.login.LoginRequest
import juniar.porkat.auth.login.LoginResponse
import juniar.porkat.auth.register.RegisterKateringRequest
import juniar.porkat.auth.register.RegisterPelangganRequest
import juniar.porkat.auth.register.RegisterResponse
import juniar.porkat.common.CommonResponse
import juniar.porkat.detailkatering.menu.GetListMenuResponse
import juniar.porkat.detailkatering.review.*
import juniar.porkat.homekatering.UpdatePhotoKateringRequest
import juniar.porkat.homekatering.food.GetListMakananToday
import juniar.porkat.homekatering.menu.DeleteMenuRequest
import juniar.porkat.homekatering.menu.InsertMenuRequest
import juniar.porkat.homekatering.menu.InsertMenuResponse
import juniar.porkat.homekatering.menu.UpdateMenuRequest
import juniar.porkat.homekatering.sendfood.DirectionResponse
import juniar.porkat.homekatering.sendfood.GetListSendFoodResponse
import juniar.porkat.homekatering.setting.ChangePasswordKateringRequest
import juniar.porkat.homekatering.setting.EditProfileKateringRequest
import juniar.porkat.homekatering.transaction.GetKateringTransactionResponse
import juniar.porkat.homepelanggan.setting.ChangePasswordPelangganRequest
import juniar.porkat.homepelanggan.setting.ChangePasswordResponse
import juniar.porkat.homepelanggan.setting.EditProfilePelangganRequest
import juniar.porkat.homepelanggan.setting.EditProfileResponse
import juniar.porkat.homepelanggan.transaction.GetTransactionResponse
import juniar.porkat.homepelanggan.transaction.MenuTransactionResponse
import juniar.porkat.homescreen.ListKateringResponse
import juniar.porkat.transaction.TransactionRequest
import juniar.porkat.transaction.TransactionResponse
import retrofit2.http.*

/**
 * Created by Nicolas Juniar on 12/02/2018.
 */
interface NetworkApi {
    @GET("katering/list/rating")
    fun GetListKateringByRating(): Observable<ListKateringResponse>

    @GET("katering/list/distance")
    fun GetListKateringByDistance(@Query("latitude") latitude: Double,
                                  @Query("longitude") longitude: Double): Observable<ListKateringResponse>

    @POST("pengguna/login")
    fun login(@Body request: LoginRequest): Observable<LoginResponse>

    @POST("pelanggan/register")
    fun registerPelanggan(@Body request: RegisterPelangganRequest): Observable<RegisterResponse>

    @PUT("pelanggan/update/profile")
    fun editProfilePelanggan(@Body request: EditProfilePelangganRequest): Observable<EditProfileResponse>

    @PUT("pelanggan/update/password")
    fun changePasswordPelanggan(@Body requestBody: ChangePasswordPelangganRequest): Observable<ChangePasswordResponse>

    @GET("menu/list")
    fun getListMenu(@Query("id_katering") idKatering: Int): Observable<GetListMenuResponse>

    @GET("pelanggan/ulasan/list")
    fun getListUlasan(@Query("id_katering") idKatering: Int,
                      @Query("id_pelanggan") idPelanggan: Int): Observable<GetReviewResponse>

    @DELETE("pelanggan/ulasan/delete/{id_ulasan}")
    fun deleteUlasan(@Path("id_ulasan") idUlasan: Int,
                     @Query("id_katering") idKatering:Int): Observable<DeleteReviewResponse>

    @POST("pelanggan/ulasan/insert")
    fun insertUlasan(@Body request: InsertReviewRequest): Observable<InsertReviewResponse>

    @PUT("pelanggan/ulasan/update")
    fun updateUlasan(@Body request: UpdateReviewRequest): Observable<UpdateReviewResponse>

    @POST("pelanggan/pesan/insert")
    fun orderKatering(@Body request: TransactionRequest): Observable<TransactionResponse>

    @GET("pelanggan/pesan/list")
    fun getListTransactionPelanggan(@Query("id_pelanggan") idPelanggan: Int): Observable<GetTransactionResponse>

    @GET("pelanggan/pesan/menu")
    fun getMenuTransaction(@Query("id_pesan") idPesan: Int): Observable<MenuTransactionResponse>

    @POST("katering/register")
    fun registerKatering(@Body request: RegisterKateringRequest): Observable<RegisterResponse>

    @PUT("katering/update/password")
    fun changePasswordKatering(@Body request: ChangePasswordKateringRequest): Observable<ChangePasswordResponse>

    @PUT("katering/update/profile")
    fun editProfileKatering(@Body request: EditProfileKateringRequest): Observable<EditProfileResponse>

    @POST("menu/insert")
    fun insertMenu(@Body request: InsertMenuRequest): Observable<InsertMenuResponse>

    @PUT("menu/delete")
    fun deleteMenu(@Body request: DeleteMenuRequest): Observable<CommonResponse>

    @PUT("menu/update")
    fun updateMenu(@Body request: UpdateMenuRequest): Observable<CommonResponse>

    @FormUrlEncoded
    @POST("pelanggan/pesan/update")
    fun updateTransaction(@Field("id_pesan") idPesan: Int,
                          @Field("nota") nota: String,
                          @Field("encoded_image") encodedImage: String): Observable<CommonResponse>


    @PUT("katering/update/photo")
    fun updatePhotoKatering(@Body request: UpdatePhotoKateringRequest): Observable<CommonResponse>

    @GET("transaksi/makanan/list")
    fun getListMakananToday(@Query("id_katering") idKatering: Int): Observable<GetListMakananToday>

    @GET("katering/pesan/list")
    fun getListTransactionKatering(@Query("id_katering") idKatering: Int): Observable<GetKateringTransactionResponse>

    @GET("transaksi/pengantaran/list")
    fun getListSendFood(@Query("id_katering") idKatering: Int):Observable<GetListSendFoodResponse>

    @GET("api/directions/json?")
    fun getDirection(@Query("units") units:String="metric",
                     @Query("origin") origin:String,
                     @Query("destination") destination:String,
                     @Query("mode") mode:String="driving"):Observable<DirectionResponse>

    @FormUrlEncoded
    @POST("katering/sendfood/done")
    fun doneSendFood(@Field("id_detailpesan") idDetailPesan:Int):Observable<CommonResponse>
}
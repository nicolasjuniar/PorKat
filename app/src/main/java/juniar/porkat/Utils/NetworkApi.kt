package juniar.porkat.Utils

import io.reactivex.Observable
import juniar.porkat.auth.login.LoginRequest
import juniar.porkat.auth.login.LoginResponse
import juniar.porkat.auth.register.RegisterPelangganRequest
import juniar.porkat.auth.register.RegisterPelangganResponse
import juniar.porkat.detailkatering.GetListMenuResponse
import juniar.porkat.homepelanggan.setting.ChangePasswordPelangganRequest
import juniar.porkat.homepelanggan.setting.ChangePasswordResponse
import juniar.porkat.homepelanggan.setting.EditProfilePelangganRequest
import juniar.porkat.homepelanggan.setting.EditProfileResponse
import juniar.porkat.homescreen.ListKateringResponse
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
    fun registerPelanggan(@Body request: RegisterPelangganRequest): Observable<RegisterPelangganResponse>

    @PUT("pelanggan/update/profile")
    fun editProfilePelanggan(@Body request: EditProfilePelangganRequest): Observable<EditProfileResponse>

    @PUT("pelanggan/update/password")
    fun changePasswordPelanggan(@Body requestBody: ChangePasswordPelangganRequest): Observable<ChangePasswordResponse>

    @GET("menu/list")
    fun getListMenu(@Query("id_katering") idKatering: Int): Observable<GetListMenuResponse>
}
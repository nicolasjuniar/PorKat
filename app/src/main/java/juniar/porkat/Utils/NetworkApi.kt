package juniar.porkat.Utils

import io.reactivex.Observable
import juniar.porkat.auth.login.LoginRequest
import juniar.porkat.auth.login.LoginResponse
import juniar.porkat.auth.register.RegisterPelangganRequest
import juniar.porkat.auth.register.RegisterPelangganResponse
import juniar.porkat.homepelanggan.setting.ChangePasswordPelangganRequest
import juniar.porkat.homepelanggan.setting.ChangePasswordResponse
import juniar.porkat.homepelanggan.setting.EditProfilePelangganRequest
import juniar.porkat.homepelanggan.setting.EditProfileResponse
import juniar.porkat.homescreen.ListKateringResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

/**
 * Created by Nicolas Juniar on 12/02/2018.
 */
interface NetworkApi {
    @GET("katering/list/rating")
    fun GetListKateringByRating(): Observable<ListKateringResponse>

    @POST("pengguna/login")
    fun login(@Body request: LoginRequest): Observable<LoginResponse>

    @POST("pelanggan/register")
    fun registerPelanggan(@Body request: RegisterPelangganRequest): Observable<RegisterPelangganResponse>

    @PUT("pelanggan/update/profile")
    fun editProfilePelanggan(@Body request: EditProfilePelangganRequest): Observable<EditProfileResponse>

    @PUT("pelanggan/update/password")
    fun changePasswordPelanggan(@Body requestBody: ChangePasswordPelangganRequest): Observable<ChangePasswordResponse>
}
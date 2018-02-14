package juniar.porkat.Utils

import io.reactivex.Observable
import juniar.porkat.auth.login.LoginRequest
import juniar.porkat.auth.login.LoginResponse
import juniar.porkat.auth.register.RegisterPelangganRequest
import juniar.porkat.auth.register.RegisterPelangganResponse
import juniar.porkat.homescreen.ListKateringResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

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
}
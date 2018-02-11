package juniar.porkat.Utils

import io.reactivex.Observable
import juniar.porkat.homescreen.ListKateringResponse
import juniar.porkat.login.LoginRequest
import juniar.porkat.login.LoginResponse
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

}
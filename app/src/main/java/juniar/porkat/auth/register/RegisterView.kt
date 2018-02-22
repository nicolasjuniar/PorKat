package juniar.porkat.auth.register

/**
 * Created by Nicolas Juniar on 14/02/2018.
 */
interface RegisterView {
    fun setLoading(loading: Boolean)
    fun onRegisterResponse(error: Boolean, response: RegisterPelangganResponse?, t: Throwable?)
    fun onFieldFilled(enable: Boolean, code: Int)
    fun onAuthFilled(username: String, password: String)
    fun onPrivateFilled(fullname: String, phone: String, address: String)
}
package juniar.porkat.register

/**
 * Created by Nicolas Juniar on 14/02/2018.
 */
interface RegisterView {
    fun setLoading(loading: Boolean)
    fun onRegisterResponse(error: Boolean, response: RegisterResponse?, t: Throwable?)
}
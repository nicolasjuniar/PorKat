package juniar.porkat.auth.login

/**
 * Created by Nicolas Juniar on 12/02/2018.
 */
interface LoginView {
    fun setLoading(loading:Boolean)
    fun onLoginResponse(error: Boolean, response: LoginResponse?, t: Throwable?)
}
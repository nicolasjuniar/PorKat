package juniar.porkat.auth.register

/**
 * Created by Jarvis on 26/03/2018.
 */
interface RegisterKateringView {
    fun setLoading(loading: Boolean)
    fun onFieldFilled(enable: Boolean)
    fun onAuthFilled(username: String, password: String)
    fun onPrivateFilled(fullname: String, phone: String, address: String, verificationNumber: String, latitude: Double, longitude: Double)
    fun onRegisterResponse(error: Boolean, response: RegisterResponse?, t: Throwable?)
    fun onGetPhotoKatering(photoName: String, encodedImage: String)
}
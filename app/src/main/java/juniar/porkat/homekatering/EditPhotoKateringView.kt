package juniar.porkat.homekatering

interface EditPhotoKateringView {
    fun setLoading(loading: Boolean)
    fun onSuccessUploadPhoto(error: Boolean, message: String?, t: Throwable?)
}
package juniar.porkat.auth.register

import com.google.gson.annotations.SerializedName

data class RegisterKateringRequest(@SerializedName("id_pengguna") var idPengguna: String = "",
                                   @SerializedName("katasandi") var katasandi: String = "",
                                   @SerializedName("nama_katering") var namaKatering: String = "",
                                   @SerializedName("no_telp") var noTelp: String = "",
                                   @SerializedName("alamat") var alamat: String = "",
                                   @SerializedName("foto") var foto: String = "",
                                   @SerializedName("longitude") var longitude: Double = 0.0,
                                   @SerializedName("latitude") var latitude: Double = 0.0,
                                   @SerializedName("no_verifikasi") var noVerifikasi: String = "",
                                   @SerializedName("encoded_image") var encodedImage: String = "")
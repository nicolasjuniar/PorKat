package juniar.porkat.auth

import com.google.gson.annotations.SerializedName

/**
 * Created by Nicolas Juniar on 12/02/2018.
 */
data class KateringModel(@SerializedName("id_katering") val idKatering: Int,
                         @SerializedName("id_pengguna") val idPengguna: String,
                         @SerializedName("katasandi") val katasandi: String,
                         @SerializedName("nama_katering") val namaKatering: String,
                         @SerializedName("no_telp") val noTelp: String,
                         @SerializedName("alamat") val alamat: String,
                         @SerializedName("foto") val foto: String,
                         @SerializedName("rating") val rating: String,
                         @SerializedName("longitude") val longitude: String,
                         @SerializedName("latitude") val latitude: String,
                         @SerializedName("status") val status: String)
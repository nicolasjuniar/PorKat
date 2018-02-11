package juniar.porkat.login

import com.google.gson.annotations.SerializedName

/**
 * Created by Nicolas Juniar on 12/02/2018.
 */
data class KateringModel(@SerializedName("id_katering") val id_katering: Int,
                         @SerializedName("id_pengguna") val id_pengguna: String,
                         @SerializedName("katasandi") val katasandi: String,
                         @SerializedName("nama_katering") val nama_katering: String,
                         @SerializedName("no_telp") val no_telp: String,
                         @SerializedName("alamat") val alamat: String,
                         @SerializedName("foto") val foto: String,
                         @SerializedName("rating") val rating: String,
                         @SerializedName("longitude") val longitude: String,
                         @SerializedName("latitude") val latitude: String,
                         @SerializedName("status") val status: String)
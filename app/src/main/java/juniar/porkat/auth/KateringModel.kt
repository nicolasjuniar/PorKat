package juniar.porkat.auth

import com.google.gson.annotations.SerializedName

/**
 * Created by Nicolas Juniar on 12/02/2018.
 */
data class KateringModel(@SerializedName("id_katering") var idKatering: Int,
                         @SerializedName("id_pengguna") var idPengguna: String,
                         @SerializedName("katasandi") var katasandi: String,
                         @SerializedName("nama_katering") var namaKatering: String,
                         @SerializedName("no_telp") var noTelp: String,
                         @SerializedName("alamat") var alamat: String,
                         @SerializedName("foto") var foto: String,
                         @SerializedName("rating") var rating: String,
                         @SerializedName("longitude") var longitude: String,
                         @SerializedName("latitude") var latitude: String,
                         @SerializedName("status") var status: String)
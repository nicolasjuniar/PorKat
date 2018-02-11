package juniar.porkat.homescreen

import com.google.gson.annotations.SerializedName

/**
 * Created by Nicolas Juniar on 12/02/2018.
 */
data class GetKateringModel(@SerializedName("id_katering") var id_katering: Int,
                            @SerializedName("id_pengguna") var id_pengguna: String,
                            @SerializedName("katasandi") var katasandi: String,
                            @SerializedName("nama_katering") var nama_katering: String,
                            @SerializedName("no_telp") var no_telp: String,
                            @SerializedName("alamat") var alamat: String,
                            @SerializedName("foto") var foto: String,
                            @SerializedName("rating") var rating: Float,
                            @SerializedName("longitude") var longitude: Double,
                            @SerializedName("latitude") var latitude: Double,
                            @SerializedName("no_verifikasi") var no_verifikasi: String,
                            var distance: Double)
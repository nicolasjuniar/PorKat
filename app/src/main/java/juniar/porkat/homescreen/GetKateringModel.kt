package juniar.porkat.homescreen

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by Nicolas Juniar on 12/02/2018.
 */
data class GetKateringModel(@SerializedName("id_katering") var id_katering: Int,
                            @SerializedName("idPengguna") var id_pengguna: String,
                            @SerializedName("katasandi") var katasandi: String,
                            @SerializedName("nama_katering") var nama_katering: String,
                            @SerializedName("no_telp") var no_telp: String,
                            @SerializedName("alamat") var alamat: String,
                            @SerializedName("foto") var foto: String,
                            @SerializedName("rating") var rating: Float,
                            @SerializedName("longitude") var longitude: Double,
                            @SerializedName("latitude") var latitude: Double,
                            @SerializedName("no_verifikasi") var no_verifikasi: String,
                            var distance: Float) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readFloat(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readString() ?: "",
            parcel.readFloat())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id_katering)
        parcel.writeString(id_pengguna)
        parcel.writeString(katasandi)
        parcel.writeString(nama_katering)
        parcel.writeString(no_telp)
        parcel.writeString(alamat)
        parcel.writeString(foto)
        parcel.writeFloat(rating)
        parcel.writeDouble(longitude)
        parcel.writeDouble(latitude)
        parcel.writeString(no_verifikasi)
        parcel.writeFloat(distance)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GetKateringModel> {
        override fun createFromParcel(parcel: Parcel): GetKateringModel {
            return GetKateringModel(parcel)
        }

        override fun newArray(size: Int): Array<GetKateringModel?> {
            return arrayOfNulls(size)
        }
    }
}
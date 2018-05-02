package juniar.porkat.homekatering.sendfood

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class SendFoodModel(@SerializedName("id_pesan") val idPesan:Int,
                         @SerializedName("id_detailpesan") val idDetailPesan:Int,
                         @SerializedName("nama_lengkap") val namaLengkap:String,
                         @SerializedName("catatan") val catatan:String,
                         @SerializedName("alamat") val alamat:String,
                         @SerializedName("longitude") val longitude:Double,
                         @SerializedName("latitude") val latitude:Double,
                         @SerializedName("waktu_pengantaran") val waktuPengantaran:String,
                         @SerializedName("nama_menu") val namaMenu:String,
                         @SerializedName("foto") val foto:String,
                         @SerializedName("status") var status:String):Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(idPesan)
        parcel.writeInt(idDetailPesan)
        parcel.writeString(namaLengkap)
        parcel.writeString(catatan)
        parcel.writeString(alamat)
        parcel.writeDouble(longitude)
        parcel.writeDouble(latitude)
        parcel.writeString(waktuPengantaran)
        parcel.writeString(namaMenu)
        parcel.writeString(foto)
        parcel.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SendFoodModel> {
        override fun createFromParcel(parcel: Parcel): SendFoodModel {
            return SendFoodModel(parcel)
        }

        override fun newArray(size: Int): Array<SendFoodModel?> {
            return arrayOfNulls(size)
        }
    }
}
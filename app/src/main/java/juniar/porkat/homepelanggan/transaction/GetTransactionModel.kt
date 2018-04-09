package juniar.porkat.homepelanggan.transaction

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by Jarvis on 22/03/2018.
 */
data class GetTransactionModel(@SerializedName("id_pesan") val idPesan:Int,
                               @SerializedName("nama_katering") val namaKatering:String,
                               @SerializedName("tgl_mulai") val tglMulai:String,
                               @SerializedName("tgl_selesai") val tglSelesai:String,
                               @SerializedName("alamat") val alamat:String,
                               @SerializedName("catatan") val catatan:String,
                               @SerializedName("nota") val nota:String,
                               @SerializedName("total") val total:Int,
                               @SerializedName("status") val status:String):Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(idPesan)
        parcel.writeString(namaKatering)
        parcel.writeString(tglMulai)
        parcel.writeString(tglSelesai)
        parcel.writeString(alamat)
        parcel.writeString(catatan)
        parcel.writeString(nota)
        parcel.writeInt(total)
        parcel.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GetTransactionModel> {
        override fun createFromParcel(parcel: Parcel): GetTransactionModel {
            return GetTransactionModel(parcel)
        }

        override fun newArray(size: Int): Array<GetTransactionModel?> {
            return arrayOfNulls(size)
        }
    }
}
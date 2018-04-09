package juniar.porkat.homekatering.transaction

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class KateringTransactionModel(@SerializedName("id_pesan") val idPesan: Int,
                                    @SerializedName("nama_lengkap") val namaLengkap: String,
                                    @SerializedName("tgl_mulai") val tglMulai: String,
                                    @SerializedName("tgl_selesai") val tglSelesai: String,
                                    @SerializedName("alamat") val alamat: String,
                                    @SerializedName("catatan") val catatan: String,
                                    @SerializedName("nota") val nota: String,
                                    @SerializedName("total") val total: Int,
                                    @SerializedName("status") val status: String):Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(idPesan)
        parcel.writeString(namaLengkap)
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

    companion object CREATOR : Parcelable.Creator<KateringTransactionModel> {
        override fun createFromParcel(parcel: Parcel): KateringTransactionModel {
            return KateringTransactionModel(parcel)
        }

        override fun newArray(size: Int): Array<KateringTransactionModel?> {
            return arrayOfNulls(size)
        }
    }
}
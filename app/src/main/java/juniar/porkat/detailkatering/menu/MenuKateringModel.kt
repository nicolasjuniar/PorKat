package juniar.porkat.detailkatering.menu

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by Nicolas Juniar on 26/02/2018.
 */
data class MenuKateringModel(@SerializedName("id_menu") val idMenu: Int = -1,
                             @SerializedName("nama_menu") val namaMenu: String = "",
                             @SerializedName("foto") val foto: String = "",
                             @SerializedName("harga") val harga: Int = -1,
                             @SerializedName("id_katering") val idKatering: Int = -1):Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(idMenu)
        parcel.writeString(namaMenu)
        parcel.writeString(foto)
        parcel.writeInt(harga)
        parcel.writeInt(idKatering)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MenuKateringModel> {
        override fun createFromParcel(parcel: Parcel): MenuKateringModel {
            return MenuKateringModel(parcel)
        }

        override fun newArray(size: Int): Array<MenuKateringModel?> {
            return arrayOfNulls(size)
        }
    }
}
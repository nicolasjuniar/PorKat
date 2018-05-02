package juniar.porkat.homekatering.sendfood

import com.google.gson.annotations.SerializedName

data class DirectionResponse(@SerializedName("routes") val routes: MutableList<Route>) {
    data class Route(@SerializedName("legs") val legs: MutableList<Leg>,
                     @SerializedName("overview_polyline") val overviewPolyline: OverviewPolyline)

    data class Leg(@SerializedName("distance") val distance: Distance,
                   @SerializedName("duration") val duration: Duration)

    data class Duration(@SerializedName("text") val text: String,
                        @SerializedName("value") val value: Int)

    data class Distance(@SerializedName("text") val text: String,
                        @SerializedName("value") val value: Int)

    data class OverviewPolyline(@SerializedName("points") val points: String)
}

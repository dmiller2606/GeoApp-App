package com.example.mobileawvorstellung

import android.app.Application
import android.graphics.Color
import android.graphics.Paint
import android.util.JsonReader
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.osmdroid.bonuspack.kml.*
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Overlay
import org.osmdroid.views.overlay.Polygon
import org.osmdroid.views.overlay.Polyline
import java.io.InputStream
import java.io.InputStreamReader

/**
 *     init{
_plantList = try {
val data = InputStreamReader(getApplication<Application>().assets.open("bienenpflanzen.json"),StandardCharsets.ISO_8859_1).readText()
val plantList: List<Plant> = Json.decodeFromString(data)
plantList
} catch (e: Exception){
Timber.i(e)
emptyList<Plant>()
}
}

@Serializable
data class Plant(
val name: String,
val bluete: String,
val bluetezeit: String,
val wuchs: String,
val standort: String
)
 */
class InzidenzStyler: KmlFeature.Styler {
    override fun onFeature(overlay: Overlay?, kmlFeature: KmlFeature?) {
    }

    override fun onPoint(marker: Marker?, kmlPlacemark: KmlPlacemark?, kmlPoint: KmlPoint?) {
    }

    override fun onLineString(
        polyline: Polyline?,
        kmlPlacemark: KmlPlacemark?,
        kmlLineString: KmlLineString?
    ) {
    }

    override fun onPolygon(
        polygon: Polygon?,
        kmlPlacemark: KmlPlacemark?,
        kmlPolygon: KmlPolygon?
    ) {
        val inzidenz = kmlPlacemark?.getExtendedData("cases7_bl_per_100k")?.toFloat()
        if (inzidenz != null) {
            polygon?.fillColor = when{
                inzidenz > 1000.0 -> 0xB099000d.toInt()
                inzidenz > 500.0 -> 0xB0cb181d.toInt()
                inzidenz > 250.0 -> 0xB0ef3b2c.toInt()
                inzidenz > 100.0 -> 0xB0fb6a4a.toInt()
                inzidenz > 50.0 -> 0xB0fc9272.toInt()
                inzidenz > 25.0 -> 0xB0fcbba1.toInt()
                inzidenz > 5.0 -> 0xB0fee0d2.toInt()
                else -> {0xB0fff5f0.toInt()}
            }
        }
    }

    override fun onTrack(polyline: Polyline?, kmlPlacemark: KmlPlacemark?, kmlTrack: KmlTrack?) {
    }

}
package com.example.mobileawvorstellung

import android.graphics.Color
import android.graphics.DashPathEffect
import org.osmdroid.bonuspack.kml.*
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Overlay
import org.osmdroid.views.overlay.Polygon
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow
import java.text.DateFormat.getDateInstance
import java.util.*
import kotlin.math.roundToLong

class InzidenzStyler(private val map: MapView) : KmlFeature.Styler {

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
            polygon?.fillPaint?.color = when {
                inzidenz > 1000.0 -> 0xB099000d.toInt()
                inzidenz > 500.0 -> 0xB0cb181d.toInt()
                inzidenz > 250.0 -> 0xB0ef3b2c.toInt()
                inzidenz > 100.0 -> 0xB0fb6a4a.toInt()
                inzidenz > 50.0 -> 0xB0fc9272.toInt()
                inzidenz > 25.0 -> 0xB0fcbba1.toInt()
                inzidenz > 5.0 -> 0xB0fee0d2.toInt()
                else -> {
                    0xA0fff5f0.toInt()
                }
            }
        }

        if (polygon != null && kmlPlacemark != null) {
            polygon.outlinePaint.strokeWidth = 4F
            polygon.outlinePaint.color = Color.WHITE
            polygon.outlinePaint.pathEffect = DashPathEffect(floatArrayOf(30F,20F),0F)

            polygon.infoWindow = BasicInfoWindow(org.osmdroid.bonuspack.R.layout.bonuspack_bubble, map)
            polygon.title = kmlPlacemark.getExtendedData("LAN_ew_GEN")
            val roundedInzidenz = inzidenz?.roundToLong()
            polygon.snippet = "7 Tage Inzidenz: $roundedInzidenz / 100000 Einwohner"
            val date = Date(kmlPlacemark.getExtendedData("Aktualisierung").toLong())
            val formatter = getDateInstance()
            val formattedDate = formatter.format(date)
            polygon.subDescription = "Aktualisiert am $formattedDate | Robert Koch-Institut (RKI), dl-de/by-2-0"
        }
    }

    override fun onTrack(polyline: Polyline?, kmlPlacemark: KmlPlacemark?, kmlTrack: KmlTrack?) {
    }

}
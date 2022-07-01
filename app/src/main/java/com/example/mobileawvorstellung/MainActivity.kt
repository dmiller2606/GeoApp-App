package com.example.mobileawvorstellung

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.mobileawvorstellung.databinding.ActivityMainBinding
import org.osmdroid.bonuspack.kml.KmlDocument
import org.osmdroid.config.Configuration.getInstance
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.CopyrightOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


class MainActivity : AppCompatActivity() {
    private lateinit var map: MapView
    private val viewModel : MainViewModel by viewModels {
        MainViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        val ctx = applicationContext
        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        requestPermissions(permissions,0)

        val mainBinding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        map = mainBinding.map
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)

        val mapController = map.controller
        mapController.setZoom(7.0)
        mapController.setCenter(GeoPoint(51.1657,10.4515))
        map.setScrollableAreaLimitLatitude(57.0,45.0,10)
        map.setScrollableAreaLimitLongitude(4.0,17.0,10)
        // Countries GeoJson Overlay
        viewModel.getInzidenzJson()
        viewModel.geoJson.observe(this) {
            buildGeoJsonOverlay(it, map)
            locationOverlay(ctx,map)
            attributionOverlay(ctx,map)
        }
    }

    private fun attributionOverlay(ctx: Context?, map: MapView) {
        val attributionOverlay = CopyrightOverlay(ctx)
        attributionOverlay.setCopyrightNotice("© OpenStreetMap |  Robert Koch-Institut (RKI), dl-de/by-2-0")
        map.overlays.add(attributionOverlay)
    }

    private fun locationOverlay(ctx: Context?,map: MapView) {
        val locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(ctx), map)
        locationOverlay.enableMyLocation()
        map.overlays.add(locationOverlay)
        map.invalidate()
    }

    private fun buildGeoJsonOverlay(geoJson: String, map: MapView) {
        println(geoJson)
        val styler = InzidenzStyler(map)
        val kmlDocument = KmlDocument()
        kmlDocument.parseGeoJSON(geoJson)
        val kmlOverlay = kmlDocument.mKmlRoot.buildOverlay(map,null,styler,kmlDocument)
        map.overlays.add(kmlOverlay)
        val bb = kmlDocument.mKmlRoot.boundingBox
        map.controller.setCenter(bb.centerWithDateLine)
        map.invalidate()
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }
}
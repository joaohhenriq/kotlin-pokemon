package com.joaohhenriq.kotlin_pokemon

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    var ACCESS_LOCATION = 123
    var location: Location? = null
    var pokemonList = ArrayList<Pokemon>()
    var oldLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        // pega o fragmento que contem o mapa na UI
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        checkPermission()
        loadPokemon()
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    ACCESS_LOCATION
                )
                return
            }
        }

        getUserLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getUserLocation() {
        Toast.makeText(this, "User location access on", Toast.LENGTH_LONG).show()

        var myLocation = MyLocationListener()

        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3, 3f, myLocation)

        var myThread = MyThread()
        myThread.start()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            ACCESS_LOCATION -> {

                // nós pegamos o índice 0 pq passamos um fizemos o request de apenas uma permissão no checkPermission
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getUserLocation()
                } else {
                    Toast.makeText(this, "We can't access your location", Toast.LENGTH_LONG).show()
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    inner class MyLocationListener : LocationListener {

        constructor() {
            location = Location("Start")
            location!!.longitude = -16.73896689
            location!!.latitude = -49.29528683
        }

        override fun onLocationChanged(p0: Location) {
            location = p0
        }

        override fun onProviderEnabled(provider: String) {}

        override fun onProviderDisabled(provider: String) {}

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    }

    inner class MyThread : Thread {
        constructor() : super() {
            oldLocation = Location("Start")
            oldLocation!!.longitude = 0.0
            oldLocation!!.latitude = 0.0
        }

        override fun run() {
            while (true) {
                try {
                    if(oldLocation!!.distanceTo(location) == 0f){
                        continue
                    }

                    oldLocation = location

                    runOnUiThread {
                        mMap.clear()

                        // ME
                        val position = LatLng(location!!.longitude, location!!.latitude)
                        mMap.addMarker(
                            MarkerOptions().position(position).title("Me")
                                .snippet("here is my location")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mario))
                        )
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 14f))

                        // POKEMONS
                        pokemonList.forEach{
                            var newPokemon = it

                            if(!newPokemon.isCatched) {
                                val position = LatLng(newPokemon.lgn, newPokemon.lat)
                                mMap.addMarker(
                                    MarkerOptions().position(position).title(newPokemon.name)
                                        .snippet(newPokemon.des)
                                        .icon(BitmapDescriptorFactory.fromResource(newPokemon.image))
                                )
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 14f))
                            }
                        }
                    }

                    Thread.sleep(20000)
                } catch (e: Exception) {

                }
            }

        }
    }

    fun loadPokemon() {
        pokemonList.add(
            Pokemon(
                "Charmander",
                "This is a pokemon",
                R.drawable.charmander,
                2.5,
                -49.29528683,
                -16.73896689
            )
        )

        pokemonList.add(
            Pokemon(
                "Bulbasaur",
                "This is a pokemon",
                R.drawable.bulbasaur,
                4.5,
                -49.29528683,
                -16.73896689
            )
        )
    }
}
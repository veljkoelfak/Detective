package com.veljko.detective.ui


import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.GeoPoint
import com.veljko.detective.*
import com.veljko.detective.R


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MapFragment : Fragment(), OnMapReadyCallback {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var isTrackingLocation = false
    private var isMapReady = false
    private lateinit var viewModel: UserDataViewModel
    private lateinit var loc: Location
    private var currentLocationMarker: Marker? = null
    private var markersListData = listOf<FirebaseManager.Objects>()
    private var markerslistIDs = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        viewModel = ViewModelProvider(this).get(UserDataViewModel::class.java)


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_map, container, false)

        val MapSupportFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        if (MapSupportFragment != null) {
            MapSupportFragment.getMapAsync(this)
        }




        val addButton : Button
        addButton = view.findViewById(R.id.addButton)

        addButton.setOnClickListener{
            if (isLocationPermissionGranted()) {
                    var gp = GeoPoint(loc.latitude, loc.longitude)

                    viewModel.addObject("Object", "Test",gp )
                }
        }



        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                p0?.lastLocation?.let { location ->
                    loc = location
                    updateLocationOnMap(location)
                }
            }
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    enableMyLocation()
                }
            }

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            enableMyLocation()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }



        return view
    }



    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        isMapReady = true

        viewModel.getObjects()

        viewModel.objectData.observe(viewLifecycleOwner, Observer { data ->
            if (data != null) {
                markersListData = data
                Log.d(TAG, "read data")
                getMarkers()
            } else {

            }
        })

    }

    private fun getMarkers() {

        Log.d(TAG, "making data")
        for (d in markersListData) {
            if (d.id !in markerslistIDs) {
                Log.d(TAG, d.loc!!.latitude.toString())
                val latlng : LatLng = LatLng(d.loc!!.latitude, d.loc!!.longitude)
                val markerOptions = MarkerOptions().position(latlng).title(d.name)
                mMap.addMarker(markerOptions)
                markerslistIDs.add(d.id!!)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        if (isLocationPermissionGranted()) {
            if (isMapReady) {
                mMap.isMyLocationEnabled = true
            }
            startLocationUpdates()
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        if (isLocationPermissionGranted()) {
            val locationRequest = LocationRequest.create().apply {
                interval = 10000 // Update location every 10 seconds (adjust as needed)
                fastestInterval = 5000 // Fastest update interval
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                smallestDisplacement = 20.toFloat()
            }


            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
            isTrackingLocation = true
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        isTrackingLocation = false
    }

    private fun updateLocationOnMap(location: Location) {

        val latLng = LatLng(location.latitude, location.longitude)
        currentLocationMarker?.remove()
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        currentLocationMarker  = mMap.addMarker(MarkerOptions().position(latLng).title("My Location"))
    }

    override fun onResume() {
        super.onResume()
        if (isTrackingLocation && isLocationPermissionGranted()) {
            startLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun isLocationPermissionGranted() =
        ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

}
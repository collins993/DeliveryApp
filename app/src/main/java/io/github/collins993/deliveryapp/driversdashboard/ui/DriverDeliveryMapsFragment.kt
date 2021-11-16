package io.github.collins993.deliveryapp.driversdashboard.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import io.github.collins993.deliveryapp.R
import io.github.collins993.deliveryapp.dashboard.ui.home.HomeFragmentArgs
import io.github.collins993.deliveryapp.dashboard.ui.home.MapsFragmentDirections
import io.github.collins993.deliveryapp.databinding.FragmentDriverDeliveryMapsBinding
import io.github.collins993.deliveryapp.databinding.FragmentMapsBinding
import io.github.collins993.deliveryapp.models.MarkerModel
import java.io.IOException
import java.lang.IndexOutOfBoundsException
import java.util.*
import kotlin.collections.ArrayList

class DriverDeliveryMapsFragment : Fragment(), OnMapReadyCallback, LocationListener,
    GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraIdleListener{
    private var _binding: FragmentDriverDeliveryMapsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var fusedLocationProviderClient: FusedLocationProviderClient? = null

    var currentLocation: Location? = null

    var currentMarker: Marker? = null

    var currentAddress: String? = ""

    private lateinit var mMap: GoogleMap

    val args: DriverDeliveryMapsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDriverDeliveryMapsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        fusedLocationProviderClient =  LocationServices.getFusedLocationProviderClient(requireActivity())


        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchLocation()
//        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
//        mapFragment?.getMapAsync(callback)

        binding.idSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                val location = binding.idSearchView.query.toString()
                var addressList: List<Address>? = null
                Toast.makeText(requireActivity(), location, Toast.LENGTH_SHORT).show()
                if (location != null){


                    try {
                        val geoCoder = Geocoder(requireActivity(), Locale.getDefault())
                        addressList = geoCoder.getFromLocationName(location,1)
                        val address = addressList.get(0)
                        val latLng = LatLng(address.latitude, address.longitude)
                        drawMarker(latLng)
                    }
                    catch (e:Exception){
                        Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    }

                }

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        binding.doneBtn.setOnClickListener {

            val action = MapsFragmentDirections.actionMapsFragmentToNavHome(currentAddress.toString())
            findNavController().navigate(action)

        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {

        when(requestCode) {
            1000 -> {

                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    fetchLocation()
                }
            }
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val latLng = LatLng(currentLocation?.latitude!!, currentLocation?.longitude!!)


        drawMarker(latLng)

        addMarkers()

        mMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener{

            override fun onMarkerDragStart(p0: Marker?) {

            }

            override fun onMarkerDrag(p0: Marker?) {

            }

            override fun onMarkerDragEnd(p0: Marker?) {

                if (currentMarker != null) {

                    currentMarker?.remove()
                }
                val newLatLng = LatLng(p0?.position!!.latitude, p0?.position.latitude)

                drawMarker(newLatLng)
            }

        })

        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //  Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1000)
            return
        }
        mMap.isMyLocationEnabled = true
        mMap.setOnCameraMoveListener(this)
        mMap.setOnCameraMoveStartedListener(this)
        mMap.setOnCameraIdleListener(this)
    }

    override fun onLocationChanged(location: Location) {

        val geocoder = Geocoder(requireActivity(), Locale.getDefault())
        var addresses: List<Address>? = null
        try {
            addresses = geocoder.getFromLocation(location!!.latitude, location.longitude, 1)
        }catch (e: IOException){
            e.printStackTrace()
        }

        //setAddress(addresses!![0])

    }

    override fun onCameraMove() {}

    override fun onCameraMoveStarted(p0: Int) {}

    override fun onCameraIdle() {
        var addresses: List<Address>? = null
        val geocoder = Geocoder(requireActivity(), Locale.getDefault())
        try {
            addresses = geocoder.getFromLocation(
                mMap!!.cameraPosition.target.latitude,
                mMap!!.cameraPosition.target.longitude,
                1
            )

            //setAddress(addresses!![0])
            currentAddress = addresses[0].getAddressLine(0)


            //currentAddress = addresses!![0].toString()
        }catch (e: IndexOutOfBoundsException){
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    private fun drawMarker(latLng: LatLng) {

        val markerOptions = MarkerOptions().position(latLng).draggable(true)

        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        //mMap.moveCamera(CameraUpdateFactory.zoomTo(10f))
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        currentMarker = mMap.addMarker(markerOptions)
        currentMarker?.showInfoWindow()
    }

    private fun addMarkers(){
        var addressList: List<Address>? = null
        val orderDetails = args.orderDetails
        val pick = MarkerModel("Pick-Up Location", orderDetails.pickUpAddress)
        val drop = MarkerModel("Drop-Off Location", orderDetails.dropOffAddress)
        val addressLists = ArrayList<MarkerModel>()
        addressLists.add(pick)
        addressLists.add(drop)
        for (location in addressLists){
            try {
                val geoCoder = Geocoder(requireActivity(), Locale.getDefault())
                addressList = geoCoder.getFromLocationName(location.address,1)
                val address = addressList.get(0)
                val latLng = LatLng(address.latitude, address.longitude)
                mMap.addMarker(MarkerOptions()
                    .position
                (latLng).title(location.address))
                //drawMarker(latLng)
            }
            catch (e:Exception){
                Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun getAddress(latitude: Double, longitude: Double): String {

        val geoCoder = Geocoder(requireActivity(), Locale.getDefault())

        val address = geoCoder.getFromLocation(latitude, longitude, 1)

        Log.i("Address", address[0].getAddressLine(0).toString())

        return address[0].getAddressLine(0).toString()
    }

//    private fun setAddress(address: Address){
//        if (address != null){
//
//            if (address.getAddressLine(0) != null){
//                binding.currentAddress.text = address.getAddressLine(0).toString()
//            }
//            if (address.getAddressLine(1) != null) {
//                binding.currentAddress.text.toString() + address.getAddressLine(1)
//            }
//        }
//    }

    private fun fetchLocation() {

        //check if permissions are allowed
        if (ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {

            //request user to grant permission
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1000)
            return
        }

        //getting last location
        val task = fusedLocationProviderClient?.lastLocation

        task?.addOnSuccessListener { location ->
            if (location != null) {

                this.currentLocation = location

                val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

                mapFragment.getMapAsync(this)

            }
        }
    }
}
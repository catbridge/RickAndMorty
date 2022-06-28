package com.example.rickandmorty.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.rickandmorty.data.service.LocationService
import com.example.rickandmorty.databinding.FragmentMapBinding
import com.example.rickandmorty.viewModel.MapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.LocationSource
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapFragment : Fragment() {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = requireNotNull(_binding)

    private var myGoogleMap: GoogleMap? = null
    private val locationService: LocationService by inject()
    private var locationListener: LocationSource.OnLocationChangedListener? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private val viewModel by viewModel<MapViewModel>()

    private val permissionManager = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isEnabled ->
        if (isEnabled) {
            setLocationEnabled(isEnabled)

            viewLifecycleOwner.lifecycleScope.launch {
                locationService.getLocation()?.let(::moveCameraToLocation)
            }
            locationService
                .locationFlow
                .onEach { location ->
                    locationListener?.onLocationChanged(location)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentMapBinding.inflate(inflater, container, false)
            .also { binding ->
                _binding = binding
            }
            .root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetInclude.bottomSheet)
        setBottomSheetVisibility(false)

        permissionManager.launch(Manifest.permission.ACCESS_FINE_LOCATION)

        with(binding) {

            mapView.getMapAsync { googleMap ->
                googleMap.apply {
                    uiSettings.isCompassEnabled = true
                    uiSettings.isZoomControlsEnabled = true
                    uiSettings.isMyLocationButtonEnabled = true
                }
                googleMap.isMyLocationEnabled = hasLocationPermission()

                googleMap.setLocationSource(object : LocationSource {
                    override fun activate(listener: LocationSource.OnLocationChangedListener) {
                        locationListener = listener
                    }

                    override fun deactivate() {
                        locationListener = null
                    }
                })


                viewModel.countryFlow.onEach { countries ->
                    countries.map {
                        googleMap.addMarker(
                            MarkerOptions()
                                .position(
                                    LatLng(
                                        it.latlng?.get(0) ?: 19.282319,
                                        it.latlng?.get(1) ?: 166.647047
                                    )
                                )
                                .title(it.name)
                                .snippet(it.capital)

                        )
                    }
                }.launchIn(viewLifecycleOwner.lifecycleScope)


                googleMap.setOnMarkerClickListener { marker ->
                    onMarkerClicked(marker)
                    false
                }


                googleMap.setOnMapClickListener {
                    setBottomSheetVisibility(false)
                }

                myGoogleMap = googleMap
            }
            mapView.onCreate(savedInstanceState)
        }
    }

    override fun onStart() {
        binding.mapView.onStart()
        super.onStart()
    }

    override fun onStop() {
        binding.mapView.onStop()
        super.onStop()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        binding.mapView.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        binding.mapView.onDestroy()
        _binding = null
        myGoogleMap = null
        super.onDestroyView()
    }

    @SuppressLint("MissingPermission")
    private fun setLocationEnabled(enabled: Boolean) {
        myGoogleMap?.isMyLocationEnabled = enabled
        myGoogleMap?.uiSettings?.isMyLocationButtonEnabled = enabled
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun moveCameraToLocation(location: Location) {
        val current = LatLng(location.latitude, location.longitude)
        myGoogleMap?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(current, DEFAULT_CAMERA_ZOOM)
        )
    }

    private fun setBottomSheetVisibility(isVisible: Boolean) {
        val updateState =
            if (isVisible) BottomSheetBehavior.STATE_EXPANDED else BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.state = updateState
        binding.bottomSheetInclude.bottomSheet.isVisible = isVisible
    }

    private fun onMarkerClicked(marker: Marker) {
        binding.bottomSheetInclude.countryName.text = marker.title
        binding.bottomSheetInclude.longitude.text = marker.position.longitude.toString()
        binding.bottomSheetInclude.latitude.text = marker.position.latitude.toString()
        binding.bottomSheetInclude.capital.text = marker.snippet

        setBottomSheetVisibility(true)
    }

    companion object {
        private const val DEFAULT_CAMERA_ZOOM = 17f
    }
}
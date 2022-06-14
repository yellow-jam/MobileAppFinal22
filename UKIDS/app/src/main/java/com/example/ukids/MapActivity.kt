package com.example.ukids

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import com.example.ukids.databinding.ActivityMapBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnSuccessListener

class MapActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener{
    lateinit var toggle : ActionBarDrawerToggle // 11-6 액션바드로어 토글
    var googleMap: GoogleMap? = null
    lateinit var apiClient: GoogleApiClient
    lateinit var providerClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 11-6 액션바드로어 토글
        toggle = ActionBarDrawerToggle(this, binding.drawer, R.string.drawer_open, R.string.drawer_close)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()
        (supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment)!!.getMapAsync(this)

        providerClient = LocationServices.getFusedLocationProviderClient(this)
        apiClient = GoogleApiClient.Builder(this)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)  // MainActivity 상속
            .addOnConnectionFailedListener(this)  // MainActivity 상속
            .build()
        /* 사용자 위치정보 가져오기 */
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()){
            if(it.all { permission -> permission.value == true}){
                apiClient.connect()
            } else {
                Toast.makeText(this, "권한 거부..", Toast.LENGTH_SHORT).show()
            }
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !== PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) !== PackageManager.PERMISSION_GRANTED)
        {   // 하나라도 허용되어 있지 않으면 요청하도록
            requestPermissionLauncher.launch(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_NETWORK_STATE)
            )
        }
        else{
            apiClient.connect()
        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0

        /*
        // googleMap?.mapType = GoogleMap.MAP_TYPE_SATELLITE  // 위성 지도 모드
        // 지도가 표시되면 보여지는 지역 설정 (위도, 경도)
        val latLng = LatLng(37.568256, 126.897240)
        val position: CameraPosition = CameraPosition.Builder()
            .target(latLng)
            .zoom(16f)
            .build()
        // 카메라 이동
        googleMap!!.moveCamera(CameraUpdateFactory.newCameraPosition(position))
        // 마커 추가
        val markerOp = MarkerOptions()
        markerOp.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        markerOp.position(latLng)
        markerOp.title("월드컵경기장")
        googleMap?.addMarker(markerOp)
        */

    }

    override fun onConnected(p0: Bundle?) {
        //TODO("Not yet implemented")
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)===PackageManager.PERMISSION_GRANTED){
            providerClient.lastLocation.addOnSuccessListener(
                this@MapActivity,
                object: OnSuccessListener<Location> {
                    override fun onSuccess(p0: Location?) {
                        p0?.let{
                            val latitude = p0.latitude
                            val longitude = p0.longitude
                            Log.d("mobileApp", "lat: $latitude, lng: $longitude")
                        }
                    }
                }
            )
            apiClient.disconnect()
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        //TODO("Not yet implemented")
    }

    override fun onConnectionSuspended(p0: Int) {
        //TODO("Not yet implemented")
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menu?.add(0, 11111, 0, "위성지도")
        menu?.add(0, 22222, 0, "일반지도")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) return true // 11-6 토글 - 햄버거 버튼이 눌렸다면 true
        when(item.itemId){
            11111 -> {
                googleMap?.mapType = GoogleMap.MAP_TYPE_SATELLITE
                return true
            }
            22222 -> {
                googleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
            }

        }
        return false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0, 0);
    }

}
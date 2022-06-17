package com.example.ukids

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ukids.databinding.ActivityMapBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnSuccessListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener{
    lateinit var toggle : ActionBarDrawerToggle // 11-6 액션바드로어 토글
    var googleMap: GoogleMap? = null
    lateinit var apiClient: GoogleApiClient
    lateinit var providerClient: FusedLocationProviderClient

    lateinit var datas: MutableList<myRow>
    lateinit var lats: ArrayList<String?>
    lateinit var lngs: ArrayList<String?>
    lateinit var names: ArrayList<String?>

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("mobileApp", "onCreate")
        super.onCreate(savedInstanceState)
        val binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getData()
        // 11-6 액션바드로어 토글
        toggle = ActionBarDrawerToggle(this, binding.drawer, R.string.drawer_open, R.string.drawer_close)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()

        datas = mutableListOf<myRow>()
        lats = ArrayList<String?>()
        lngs = ArrayList<String?>()
        names = ArrayList<String?>()
        /* 지도 */
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

    override fun onMapReady(p0: GoogleMap) {
        Log.d("mobileApp", "onMapReady")
        googleMap = p0
        addMarkers()
        p0.getUiSettings().setZoomControlsEnabled(true);
        p0.getUiSettings().setCompassEnabled(true);
        p0.getUiSettings().setMapToolbarEnabled(true);

        // 마커가 클릭되면 하단의 스크롤뷰에 상세정보를 띄운다
        var index: Int = 0
        googleMap!!.setOnMarkerClickListener(object :GoogleMap.OnMarkerClickListener{
            override fun onMarkerClick(p0: Marker): Boolean {
                findViewById<ScrollView>(R.id.place_info).visibility = View.VISIBLE
                findViewById<TextView>(R.id.info_placename).setText(p0.title)
                for (i in 0 until datas.size) {
                    if (datas[i].placename==p0.title){
                        index = i
                        break
                    }
                }
                findViewById<TextView>(R.id.info_placetype).setText(datas[index].placetype)
                findViewById<TextView>(R.id.info_addr).setText(datas[index].REFINE_LOTNO_ADDR ?: datas[index].REFINE_ROADNM_ADDR)

                return false
            }
        })

    }


    private fun moveMap(latitude:Double, longitude:Double){
        Log.d("mobileApp", "moveMap")
        addMarkers()
        // googleMap?.mapType = GoogleMap.MAP_TYPE_SATELLITE  // 위성 지도 모드
        // 지도가 표시되면 보여지는 지역 설정 (위도, 경도)
        val latLng = LatLng(latitude, longitude)
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
        markerOp.title("현 위치")
        googleMap?.addMarker(markerOp)
    }


    fun getData() {
        // XML 데이터 가져오기
        val call1: Call<responseInfo1> = MyApplication.networkServiceXml.getXmlList(
            "pYVi5WRhkWtEwEK/I4kgN7b4rNT0ilJBAD0ZrcvBAAFFgV3kqfOSQ9cQn5eEczFo+9O1Q1g5b0UiGp10XfJcOA==",
            "xml",
            1,
            30
        )

        call1?.enqueue(object : Callback<responseInfo1> {

            override fun onResponse(call: Call<responseInfo1>, response: Response<responseInfo1>) {
                if (response.isSuccessful) {
                    Log.d("mobileApp", "$response")
                    val resdatas = response.body()!!.row
                    for (i in 0 until resdatas.size) {
                        datas.add(myRow(resdatas[i]))
                        Log.d("mobileApp", "${resdatas[i].FACLT_NM}")
                    }
                }
            }

            override fun onFailure(call: Call<responseInfo1>, t: Throwable) {
                Log.d("mobileApp", "onFailure")
            }
        })

        // XML 데이터 가져오기2
        val call2: Call<responseInfo2> = MyApplication.networkServiceXml.getXmlList2(
            "pYVi5WRhkWtEwEK/I4kgN7b4rNT0ilJBAD0ZrcvBAAFFgV3kqfOSQ9cQn5eEczFo+9O1Q1g5b0UiGp10XfJcOA==",
            "xml",
            2,
            30
        )

        call2?.enqueue(object : Callback<responseInfo2> {
            override fun onResponse(call: Call<responseInfo2>, response: Response<responseInfo2>) {
                if (response.isSuccessful) {
                    Log.d("mobileApp", "$response")
                    val resdatas = response.body()!!.row
                    for (i in 0 until resdatas.size) {
                        datas.add(myRow(resdatas[i]))
                        Log.d("mobileApp", "${resdatas[i].PLAY_FACLT_NM}")
                    }
                }
            }

            override fun onFailure(call: Call<responseInfo2>, t: Throwable) {
                Log.d("mobileApp", "onFailure")
            }
        })

        // XML 데이터 가져오기3
        val call3: Call<responseInfo3> = MyApplication.networkServiceXml.getXmlList3(
            "pYVi5WRhkWtEwEK/I4kgN7b4rNT0ilJBAD0ZrcvBAAFFgV3kqfOSQ9cQn5eEczFo+9O1Q1g5b0UiGp10XfJcOA==",
            "xml",
            2,
            30
        )

        call3?.enqueue(object : Callback<responseInfo3> {
            override fun onResponse(call: Call<responseInfo3>, response: Response<responseInfo3>) {
                if (response.isSuccessful) {
                    Log.d("mobileApp", "$response")
                    val resdatas = response.body()!!.row
                    for (i in 0 until resdatas.size) {
                        datas.add(myRow(resdatas[i]))
                        Log.d("mobileApp", "${resdatas[i].BIZPLC_NM}")
                    }
                }
            }

            override fun onFailure(call: Call<responseInfo3>, t: Throwable) {
                Log.d("mobileApp", "onFailure")
            }
        })
    }

    fun addMarkers() {
        Log.d("mobileApp", "데이터 사이즈 ${datas.size}")
        var latLng: LatLng?

        var lat: String = "0"
        var lng: String = "0"

        for (i in 0 until datas.size) {
            lat = if ((datas[i].REFINE_WGS84_LAT!!) != "") {
                datas[i].REFINE_WGS84_LAT!!
            } else "0"
            lng = if ((datas[i].REFINE_WGS84_LOGT!!) != "") {
                datas[i].REFINE_WGS84_LOGT!!
            } else "0"
            latLng = LatLng(
                lat.toDouble(),
                lng.toDouble()
            )
            googleMap!!.addMarker(MarkerOptions().position(latLng).title(datas[i].placename))

        }
    }



    override fun onConnected(p0: Bundle?) {
        Log.d("mobileApp", "onConnected")

        // 사용자 위치정보 가져오기
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)===PackageManager.PERMISSION_GRANTED){
            Log.d("mobileApp", "checkSelfPermission")
            providerClient.lastLocation.addOnSuccessListener(
                this@MapActivity,
                object: OnSuccessListener<Location> {
                    override fun onSuccess(p0: Location?) {
                        Log.d("mobileApp", "MapActivity - onSuccess")
                        p0?.let{
                            val latitude = p0.latitude
                            val longitude = p0.longitude
                            Log.d("mobileApp", "lat: $latitude, lng: $longitude")
                            moveMap(latitude, longitude)
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
//        menu?.add(0, 11111, 0, "위성지도")
//        menu?.add(0, 22222, 0, "일반지도")
        menuInflater.inflate(R.menu.map_menu, menu) // 두 번째 - 어디에 적용할 것인지: 옵션메뉴에 적용하겠다 menu

        // 액션 뷰 이용: SearchView - map_menu.xml
        val menuSearch = menu?.findItem(R.id.menu_search)
        val searchView = menuSearch?.actionView as SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
//                binding.tv1.text = p0
                val intent1 = Intent(this@MapActivity, SearchActivity::class.java)
                intent.putExtra("search", p0)
                startActivity(intent1)
                overridePendingTransition(0, 0);  // 액티비티 화면 전환 애니메이션 제거
                return true
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) return true // 11-6 토글 - 햄버거 버튼이 눌렸다면 true
        when(item.itemId){
            R.id.menu1 -> {  // 11111
                googleMap?.mapType = GoogleMap.MAP_TYPE_SATELLITE
                return true
            }
            R.id.menu2 -> {  // 22222
                googleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
                return true
            }

        }
        return false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0, 0);
    }

}
package com.example.ukids

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
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
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener{
    lateinit var binding: ActivityMapBinding
    lateinit var toggle : ActionBarDrawerToggle // 11-6 액션바드로어 토글
    var googleMap: GoogleMap? = null
    lateinit var apiClient: GoogleApiClient
    lateinit var providerClient: FusedLocationProviderClient

    lateinit var datas: MutableList<myRow>
    var playgrounds = arrayListOf<myRow>()
    var kidscafes = arrayListOf<myRow>()
    var libs = arrayListOf<myRow>()

    var tempmarkerOptions: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("mobileApp", "onCreate")
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getData()
        // 11-6 액션바드로어 토글
        toggle = ActionBarDrawerToggle(this, binding.drawer, R.string.drawer_open, R.string.drawer_close)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()

        datas = mutableListOf<myRow>()
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

        // fab
        binding.fabAdd.setOnClickListener {
            if (tempmarkerOptions != null) {
                Log.d("mobileApp", "${tempmarkerOptions!!.position}")
                addPlace(tempmarkerOptions!!.position)
            }
        }

        // chip을 이용한 맵 마커 필터링 기능
        binding.mapChipgroup.setOnCheckedStateChangeListener(
            object : ChipGroup.OnCheckedStateChangeListener {
                override fun onCheckedChanged(group: ChipGroup, checkedIds: MutableList<Int>) {
                    googleMap!!.clear()
                    var resarr = arrayListOf<myRow>()
                    if (binding.mapPlayground.isChecked) {
                        Log.d("mobileApp", "놀이터 선택")
                        addMarkers(playgrounds, 50.toFloat())
                        resarr.addAll(playgrounds)
                    }
                    if (binding.mapKidscafe.isChecked) {
                        Log.d("mobileApp", "키즈카페 선택")
                        resarr.addAll(kidscafes)
                        addMarkers(kidscafes, 30.toFloat())
                    }
                    if (binding.mapLib.isChecked) {
                        Log.d("mobileApp", "도서관 선택")
                        resarr.addAll(libs)
                        addMarkers(libs, 80.toFloat())
                    }
                    if (checkedIds == null) {
                        addMarkers(playgrounds, 50.toFloat())
                        addMarkers(kidscafes, 30.toFloat())
                        addMarkers(libs, 80.toFloat())
                    }
                }
            }

        )
    }

    override fun onMapReady(p0: GoogleMap) {
        Log.d("mobileApp", "onMapReady")
        googleMap = p0
        p0.getUiSettings().setZoomControlsEnabled(true);
        p0.getUiSettings().setCompassEnabled(true);
        p0.getUiSettings().setMapToolbarEnabled(true);

        /* 마커 클릭 이벤트 */
        var index: Int = 0
        googleMap!!.setOnMarkerClickListener(object :GoogleMap.OnMarkerClickListener{
            override fun onMarkerClick(p0: Marker): Boolean {

                if (p0.title=="현 위치") { // 현 위치를 나타내는 마커라면 장소 추가 버튼 활성화
                    tempmarkerOptions?.remove()
                    binding.fabAdd.visibility = View.VISIBLE
                    binding.placeInfo.visibility = View.GONE
                    binding.fabStar.visibility = View.GONE
                    return false
                }

                if (p0==tempmarkerOptions) {
                    binding.fabAdd.visibility = View.VISIBLE
                    binding.placeInfo.visibility = View.GONE
                    binding.fabStar.visibility = View.GONE
                    return false
                }

                // 마커가 클릭되면 하단의 스크롤뷰에 상세정보를 띄운다
                findViewById<ExtendedFloatingActionButton>(R.id.fab_add).visibility = View.GONE
                findViewById<ScrollView>(R.id.place_info).visibility = View.VISIBLE
                findViewById<ExtendedFloatingActionButton>(R.id.fab_star).visibility = View.VISIBLE
                findViewById<TextView>(R.id.info_placename).setText(p0.title)  // 마커의 타이틀과 같은 장소명을 가진 데이터를 찾아온다
                for (i in 0 until datas.size) {
                    if (datas[i].placename==p0.title){
                        index = i
                        break
                    }
                }
                findViewById<TextView>(R.id.info_placetype).setText(datas[index].placetype)
                findViewById<TextView>(R.id.info_addr).setText(datas[index].addr ?: datas[index].road_addr)

                return false
            }
        })


        var markerOptions : MarkerOptions
        /* 맵 클릭 이벤트 */
        googleMap!!.setOnMapClickListener(object: GoogleMap.OnMapClickListener{
            override fun onMapClick(latLng: LatLng) {
                binding.fabAdd.visibility = View.VISIBLE
                binding.placeInfo.visibility = View.GONE
                binding.fabStar.visibility = View.GONE

                // 클릭한 위치를 마커로 표시
                tempmarkerOptions?.remove()
                markerOptions = MarkerOptions()
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(20F))
                markerOptions.position(latLng!!)

                tempmarkerOptions = googleMap?.addMarker(markerOptions)
                Log.d("mobileApp", "$latLng")
            }
        })
    }

    val requestLauncher: ActivityResultLauncher<Intent>
            = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        Log.d("mobileApp", "사후처리 메소드")
        Log.d("mobileApp", "${it.data!!.getStringExtra("placename")}")
        Log.d("mobileApp", "${it.data!!.getStringExtra("placetype")}")
        Log.d("mobileApp", "${it.data!!.getStringExtra("addr")}")
        Log.d("mobileApp", "${it.data!!.getStringExtra("content")}")

        val placename = it.data!!.getStringExtra("placename")!!
        val placetype = it.data!!.getStringExtra("placetype")!!
        val addr = it.data!!.getStringExtra("addr")
        val lat = it.data!!.getStringExtra("lat")
        val lng = it.data!!.getStringExtra("lng")
        // 데이터 추가
        datas.add(myRow(placename, placetype, "", addr, lat, lng, "", ""))

        // 마커 추가
        val m = MarkerOptions().title(placename)
        m.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
        m.position(LatLng(lat!!.toDouble(), lng!!.toDouble()))
        tempmarkerOptions = googleMap?.addMarker(m)
    }


    private fun addPlace(latLng: LatLng) {
        var addr: String = ""

        // 좌표 -> 주소 변환 api
        /*
        val call: Call<C2R> = MyApplication.networkServiceC2R.getCoord2Address(
            "KakaoAK 7b133b534b22adc5d90927bc83653849",
            latLng.longitude.toString(),
            latLng.latitude.toString(),

        )

        call?.enqueue(object : Callback<C2R>{
            override fun onResponse(call: Call<C2R>, response: Response<C2R>) {
                if(response.isSuccessful){
                    Log.d("mobileApp", "$response")
                    addr = response.body()!!.road_address.address_name  // 도로명주소 이용
                }
            }
            override fun onFailure(call: Call<C2R>, t: Throwable) {
                Log.d("mobileApp", "onFailure")
            }
        })
        Log.d("mobileApp", "api 요청 이후...")
        */

        val intent = Intent(this@MapActivity, AddActivity::class.java)
        //intent.putExtra("addr", addr)
        intent.putExtra("lat", latLng.latitude.toString())
        intent.putExtra("lng", latLng.longitude.toString())
        requestLauncher.launch(intent)

    }


    private fun moveMap(latitude:Double, longitude:Double){
        Log.d("mobileApp", "moveMap")
        // googleMap?.mapType = GoogleMap.MAP_TYPE_SATELLITE  // 위성 지도 모드
        // 지도가 표시되면 보여지는 지역 설정 (위도, 경도)
        val latLng = LatLng(latitude, longitude)
        val position: CameraPosition = CameraPosition.Builder()
            .target(latLng)
            .zoom(16f)
            .build()
        // 카메라 이동
        googleMap!!.moveCamera(CameraUpdateFactory.newCameraPosition(position))
        // 현위치 마커 추가
        val markerOp = MarkerOptions()
        markerOp.icon(BitmapDescriptorFactory.defaultMarker(20.toFloat()))
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
                        playgrounds.add(myRow(resdatas[i]))
                    }
                    datas.addAll(playgrounds)
                    addMarkers(playgrounds, 50.toFloat())
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
            1,
            30
        )

        call2?.enqueue(object : Callback<responseInfo2> {
            override fun onResponse(call: Call<responseInfo2>, response: Response<responseInfo2>) {
                if (response.isSuccessful) {
                    Log.d("mobileApp", "$response")
                    val resdatas = response.body()!!.row
                    for (i in 0 until resdatas.size) {
                        playgrounds.add(myRow(resdatas[i]))
                    }
                    datas.addAll(playgrounds)
                    addMarkers(playgrounds, 50.toFloat())
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
            1,
            30
        )

        call3?.enqueue(object : Callback<responseInfo3> {
            override fun onResponse(call: Call<responseInfo3>, response: Response<responseInfo3>) {
                if (response.isSuccessful) {
                    Log.d("mobileApp", "$response")
                    val resdatas = response.body()!!.row
                    for (i in 0 until resdatas.size) {
                        kidscafes.add(myRow(resdatas[i]))
                    }
                    datas.addAll(kidscafes)
                    addMarkers(kidscafes, 30.toFloat())
                }
            }

            override fun onFailure(call: Call<responseInfo3>, t: Throwable) {
                Log.d("mobileApp", "onFailure")
            }
        })

        // 서울열린데이터 XML 가져오기4
        val call4: Call<responseInfo4> = MyApplication.networkServiceSeoul.getSeoul4(
            "6258476a566c65723633517570426e",
        )

        call4?.enqueue(object : Callback<responseInfo4> {
            override fun onResponse(call: Call<responseInfo4>, response: Response<responseInfo4>) {
                if(response.isSuccessful){
                    Log.d("mobileApp", "$response")
                    val resdatas = response.body()!!.row
                    for (i in 0 until resdatas.size) {
                        libs.add(myRow(resdatas[i]))
                    }
                    datas.addAll(libs)
                    addMarkers(libs, 80.toFloat())
                }
            }

            override fun onFailure(call: Call<responseInfo4>, t: Throwable) {
                Log.d("mobileApp", "onFailure")
            }
        })
    }

    fun addMarkers(markers: MutableList<myRow>, hue: Float) {
        Log.d("mobileApp", "addMarkers")
        var latLng: LatLng?

        var lat: String = "0"
        var lng: String = "0"

        for (i in 0 until markers.size) {
            lat = if ((markers[i].LAT!!) != "") {
                markers[i].LAT!!
            } else "0"
            lng = if ((markers[i].LOTG!!) != "") {
                markers[i].LOTG!!
            } else "0"
            latLng = LatLng(
                lat.toDouble(),
                lng.toDouble()
            )
            val markerOp = MarkerOptions()
            markerOp.icon(BitmapDescriptorFactory.defaultMarker(hue))
            markerOp.position(latLng)
            markerOp.title(markers[i].placename)
            googleMap?.addMarker(markerOp)
            //googleMap!!.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker().position(latLng).title(datas[i].placename))
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
        overridePendingTransition(0, 0);  // 액티비티 화면 전환 애니메이션 제거
    }

}
package com.example.ukids

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.ukids.databinding.ActivityMainBinding
import com.google.android.gms.maps.GoogleMap
import com.kakao.sdk.common.util.Utility

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var toggle : ActionBarDrawerToggle // 11-6 액션바드로어 토글
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        /*
       // 카카오 로그인 해시 키 구하기
       val keyHash = Utility.getKeyHash(this)
       Log.d("mobileApp", keyHash)
       */

        // 11-6 액션바드로어 토글
        toggle = ActionBarDrawerToggle(this, binding.drawer, R.string.drawer_open, R.string.drawer_close)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()

        // 12-3 내비게이션 드로어에 이벤트 리스너 추가
        binding.mainDrawerView.setNavigationItemSelectedListener {
            Log.d("mobileApp", "Navigation selected... ${it.title}")
            when(it.title){
                "내 정보" -> {  // 11111
                    startActivity(Intent(this@MainActivity, MypageActivity::class.java))
                    overridePendingTransition(0, 0);  // 액티비티 화면 전환 애니메이션 제거

                }
                "즐겨찾기" -> {  // 22222
                    //startActivity()
                    overridePendingTransition(0, 0);  // 액티비티 화면 전환 애니메이션 제거

                }
                "앱 설정" -> {
                    val intent1 = Intent(this@MainActivity, SettingActivity::class.java)
                    startActivity(intent1)
                    overridePendingTransition(0, 0);  // 액티비티 화면 전환 애니메이션 제거
                    Log.d("mobileApp", "세팅")

                }
                "문의하기" -> {
                    //startActivity()
                    overridePendingTransition(0, 0);  // 액티비티 화면 전환 애니메이션 제거

                }
            }
            true
        }

        binding.btnMap.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0);  // 액티비티 화면 전환 애니메이션 제거
        }

        binding.btnAuth.setOnClickListener {
            val intent = Intent(this, AuthActivity::class.java)
            if(binding.btnAuth.text.equals("로그인")) // 로그아웃 상태
                intent.putExtra("data", "logout")
            else if(binding.btnAuth.text.equals("로그아웃")) // 로그인 상태
                intent.putExtra("data", "login")
            startActivity(intent)
            overridePendingTransition(0, 0);  // 액티비티 화면 전환 애니메이션 제거
        }

    }

    // AuthActivity에서 돌아온 후
    override fun onStart() {
        super.onStart()
        if(MyApplication.checkAuth() || MyApplication.email != null){ // 검증된 이메일인가
            // 로그인 상태
            binding.btnAuth.text = "로그아웃"
            val username: TextView = binding.mainDrawerView.getHeaderView(0).findViewById(R.id.nav_username)
            username.setText("${MyApplication.email}")

        }
        else{
            // 로그아웃 상태
            binding.btnAuth.text = "로그인"
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) return true // 11-6 토글 - 햄버거 버튼이 눌렸다면 true

        return super.onOptionsItemSelected(item)
    }
}
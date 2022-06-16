package com.example.ukids

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.ukids.databinding.ActivityMainBinding
import com.kakao.sdk.common.util.Utility

class MainActivity : AppCompatActivity() {
    private lateinit var intent1: Intent
    lateinit var binding: ActivityMainBinding
    lateinit var toggle : ActionBarDrawerToggle // 11-6 액션바드로어 토글
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


       // 카카오 로그인 해시 키 구하기
       val keyHash = Utility.getKeyHash(this)
       Log.d("mobileApp", keyHash)


        // 11-6 액션바드로어 토글
        toggle = ActionBarDrawerToggle(this, binding.drawer, R.string.drawer_open, R.string.drawer_close)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()

        // 12-3 내비게이션 드로어에 이벤트 리스너 추가
        binding.mainDrawerView.setNavigationItemSelectedListener {
            Log.d("mobileApp", "Navigation selected... ${it.title}")
            true
        }

        binding.btnMap.setOnClickListener {
            intent1 = Intent(this, MapActivity::class.java)
            startActivity(intent1)
            overridePendingTransition(0, 0);  // 액티비티 화면 전환 애니메이션 제거
        }

        binding.btnAuth.setOnClickListener {
            intent1 = Intent(this, AuthActivity::class.java)
            if(binding.btnAuth.text.equals("로그인")) // 로그아웃 상태
                intent1.putExtra("data", "logout")
            else if(binding.btnAuth.text.equals("로그아웃")) // 로그인 상태
                intent1.putExtra("data", "login")
            startActivity(intent1)
            overridePendingTransition(0, 0);  // 액티비티 화면 전환 애니메이션 제거
        }

    }

    // AuthActivity에서 돌아온 후
    override fun onStart() {
        super.onStart()
        if(MyApplication.checkAuth() || MyApplication.email != null){ // 검증된 이메일인가
            // 로그인 상태
            binding.btnAuth.text = "로그아웃"
            //binding.authTv.text = "${MyApplication.email}님 반갑습니다."
            //binding.authTv.textSize = 16F
            val username: TextView = binding.mainDrawerView.getHeaderView(0).findViewById(R.id.nav_username)
            username.setText("${MyApplication.email}님")

        }
        else{
            // 로그아웃 상태
            binding.btnAuth.text = "로그인"
            //binding.authTv.text = "덕성 모바일"
            //binding.authTv.textSize = 24F
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) return true // 11-6 토글 - 햄버거 버튼이 눌렸다면 true
        return super.onOptionsItemSelected(item)
    }
}
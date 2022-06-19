package com.example.ukids

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.ukids.databinding.ActivityMypageBinding

class MypageActivity : AppCompatActivity() {
    lateinit var binding: ActivityMypageBinding
    lateinit var toggle : ActionBarDrawerToggle // 11-6 액션바드로어 토글
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 11-6 액션바드로어 토글
        toggle = ActionBarDrawerToggle(this, binding.drawer, R.string.drawer_open, R.string.drawer_close)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()

        // 12-3 내비게이션 드로어에 이벤트 리스너 추가
        binding.mainDrawerView.setNavigationItemSelectedListener {
            Log.d("mobileApp", "Navigation selected... ${it.title}")
            when(it.title){
                "즐겨찾기" -> {
                    //startActivity(Intent(this@MypageActivity, StarActivity::class.java))
                    overridePendingTransition(0, 0);  // 액티비티 화면 전환 애니메이션 제거
                    finish()
                }
                "앱 설정" -> {
                    val intent1 = Intent(this@MypageActivity, SettingActivity::class.java)
                    startActivity(intent1)
                    overridePendingTransition(0, 0);  // 액티비티 화면 전환 애니메이션 제거
                    finish()
                }
                "문의하기" -> {
                    //startActivity()
                    overridePendingTransition(0, 0);  // 액티비티 화면 전환 애니메이션 제거
                    finish()
                }
            }
            true
        }

        binding.useremail.setText("${MyApplication.email}")

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) return true // 11-6 토글 - 햄버거 버튼이 눌렸다면 true

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0, 0);  // 액티비티 화면 전환 애니메이션 제거
    }

}
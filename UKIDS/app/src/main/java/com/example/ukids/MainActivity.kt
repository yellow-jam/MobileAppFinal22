package com.example.ukids

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.ukids.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var intent1: Intent
    lateinit var toggle : ActionBarDrawerToggle // 11-6 액션바드로어 토글
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // 11-6 액션바드로어 토글
        toggle = ActionBarDrawerToggle(this, binding.drawer, R.string.drawer_open, R.string.drawer_close)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()

        binding.btnMap.setOnClickListener {
            intent1 = Intent(this, MapActivity::class.java)
            startActivity(intent1)
            overridePendingTransition(0, 0);  // 액티비티 화면 전환 애니메이션 제거
        }

        // 12-3 내비게이션 드로어에 이벤트 리스너 추가
        binding.mainDrawerView.setNavigationItemSelectedListener {
            Log.d("mobileApp", "Navigation selected... ${it.title}")
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) return true // 11-6 토글 - 햄버거 버튼이 눌렸다면 true
        return super.onOptionsItemSelected(item)
    }
}
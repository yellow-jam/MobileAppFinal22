package com.example.ukids

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MypageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0, 0);  // 액티비티 화면 전환 애니메이션 제거
    }

}
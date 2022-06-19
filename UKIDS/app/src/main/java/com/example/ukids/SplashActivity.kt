package com.example.ukids

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.example.ukids.databinding.ActivitySplashBinding
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginbutton.setOnClickListener {
            val intent = Intent(applicationContext, AuthActivity::class.java)
            intent.putExtra("data", "logout")
            startActivity(intent)
            finish()
        }

        val backGroundExecutor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
        val mainExecutor: Executor = ContextCompat.getMainExecutor(this)
        backGroundExecutor.schedule({
            mainExecutor.execute{

                if(MyApplication.checkAuth() || MyApplication.email != null) {
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    binding.loginbutton.visibility = View.VISIBLE

                }

            }
        }, 3, TimeUnit.SECONDS)  // 3초 후에 실행
    }
}
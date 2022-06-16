package com.example.ukids

import androidx.multidex.MultiDexApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MyApplication: MultiDexApplication() {
    companion object{
        lateinit var auth: FirebaseAuth
        var email: String? = null

        fun checkAuth(): Boolean{
            var currentUser = auth.currentUser
            return currentUser?.let{
                email = currentUser.email
                currentUser.isEmailVerified  // 인증되었다면 true, 아니라면 false 리턴
            }?: let{
                false
            }
        }
    }



    override fun onCreate() {
        super.onCreate()
        auth = Firebase.auth
    }
}
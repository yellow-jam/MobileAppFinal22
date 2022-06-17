package com.example.ukids

import androidx.multidex.MultiDexApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.common.KakaoSdk
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApplication: MultiDexApplication() {
    companion object{
        /* auth */
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

        /* 공공데이터 요청 */
        //var networkServiceJSON : NetworkService
        var networkServiceXml : NetworkService
        var networkServiceC2R : NetworkService

        /*
        val retrofitJSON : Retrofit
            get() = Retrofit.Builder()
                .baseUrl("")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        */

        val parser = TikXml.Builder().exceptionOnUnreadXml(false).build()
        val retrofitXml : Retrofit
            get() = Retrofit.Builder()
                .baseUrl("https://openapi.gg.go.kr/")
                .addConverterFactory(TikXmlConverterFactory.create(parser))
                .build()

        /* 좌표 -> 주소 api */
        val retrofitCoord2Addr : Retrofit
            get() = Retrofit.Builder()
                .baseUrl("https://dapi.kakao.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()


        init{
            //networkServiceJSON = retrofitJSON.create(NetworkService::class.java)
            networkServiceXml = retrofitXml.create(NetworkService::class.java)
            networkServiceC2R = retrofitCoord2Addr.create(NetworkService::class.java)
        }




    }


    override fun onCreate() {
        super.onCreate()
        auth = Firebase.auth
        // 카카오 sdk 초기화
        KakaoSdk.init(this, "c01ce673ace62c3000d15249f118e843")
    }
}
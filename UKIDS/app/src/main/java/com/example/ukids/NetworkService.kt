package com.example.ukids

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService {
    /*
    @GET("요청주소뒷부분")
    fun getList(
        @Query("pageNo") page:Long,
        @Query("numOfRows") pageSize:Int,
        @Query("ServiceKey") apiKey:String
    ) : Call<PageListModel>
    */

    @GET("ExcellenceChildPlayFaciliti")
    fun getXmlList(
        @Query("KEY") apiKey:String?,
        @Query("Type") type:String,
        @Query("pIndex") page:Int,
        @Query("pSize") pageSize:Int
    ) : Call<responseInfo1>

    @GET("ChildPlayFacility")
    fun getXmlList2(
        @Query("KEY") apiKey:String?,
        @Query("Type") type:String,
        @Query("pIndex") page:Int,
        @Query("pSize") pageSize:Int
    ) : Call<responseInfo2>


    @GET("Kidscafe")
    fun getXmlList3(
        @Query("KEY") apiKey:String?,
        @Query("Type") type:String,
        @Query("pIndex") page:Int,
        @Query("pSize") pageSize:Int
    ) : Call<responseInfo3>

}
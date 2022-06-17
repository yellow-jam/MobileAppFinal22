package com.example.ukids

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml


class myRow() {
    lateinit var placename: String
    lateinit var placetype: String
    var REFINE_LOTNO_ADDR: String? = null
    var REFINE_ROADNM_ADDR: String? = null
    var REFINE_WGS84_LOGT: String? = null  // 경도
    var REFINE_WGS84_LAT: String? = null  // 위도

    constructor(myRow: myRow1) : this() {
        this.placename=myRow.FACLT_NM
        this.placetype="놀이터"
        this.REFINE_LOTNO_ADDR=myRow.REFINE_LOTNO_ADDR
        this.REFINE_ROADNM_ADDR=myRow.REFINE_ROADNM_ADDR
        this.REFINE_WGS84_LOGT=myRow.REFINE_WGS84_LOGT
        this.REFINE_WGS84_LAT=myRow.REFINE_WGS84_LAT
    }

    constructor(myRow: myRow2) : this() {
        this.placename=myRow.PLAY_FACLT_NM
        this.placetype="놀이터"
        this.REFINE_LOTNO_ADDR=myRow.REFINE_LOTNO_ADDR
        this.REFINE_ROADNM_ADDR=myRow.REFINE_ROADNM_ADDR
        this.REFINE_WGS84_LOGT=myRow.REFINE_WGS84_LOGT
        this.REFINE_WGS84_LAT=myRow.REFINE_WGS84_LAT
    }

    constructor(myRow: myRow3) : this() {
        this.placename=myRow.BIZPLC_NM
        this.placetype="키즈카페"
        this.REFINE_LOTNO_ADDR=myRow.REFINE_LOTNO_ADDR
        this.REFINE_ROADNM_ADDR=myRow.REFINE_ROADNM_ADDR
        this.REFINE_WGS84_LOGT=myRow.REFINE_WGS84_LOGT
        this.REFINE_WGS84_LAT=myRow.REFINE_WGS84_LAT
    }
}


@Xml(name="ExcellenceChildPlayFaciliti")
data class responseInfo1(
    @Element(name="head")
    val header: myHeader1,
    @Element(name="row")
    val row : MutableList<myRow1>
)

@Xml(name="head")
data class myHeader1(
    @Element
    val RESULT: myRESULT1
)

@Xml(name="RESULT")
data class myRESULT1(
    @PropertyElement
    val CODE : String,
    @PropertyElement
    val MESSAGE : String
)

@Xml(name="row")
data class myRow1(
    @PropertyElement
    val FACLT_NM: String,
    @PropertyElement
    val REFINE_LOTNO_ADDR: String?,
    @PropertyElement
    val REFINE_ROADNM_ADDR: String?,
    @PropertyElement
    val REFINE_WGS84_LOGT: String?,  // 경도
    @PropertyElement
    val REFINE_WGS84_LAT: String?,  // 위도
    @PropertyElement
    val MNFACLT_NM: String?,
    @PropertyElement
    val APPONT_RESN: String,
    @PropertyElement
    val TELNO: String,
)





@Xml(name="ChildPlayFacility")
data class responseInfo2(
    @Element(name="head")
    val header: myHeader2,
    @Element(name="row")
    val row : MutableList<myRow2>
)

@Xml(name="head")
data class myHeader2(
    @Element
    val RESULT: myRESULT2
)

@Xml(name="RESULT")
data class myRESULT2(
    @PropertyElement
    val CODE : String,
    @PropertyElement
    val MESSAGE : String
)

@Xml(name="row")
data class myRow2(
    @PropertyElement
    val PLAY_FACLT_NM: String,
    @PropertyElement
    val REFINE_LOTNO_ADDR: String?,
    @PropertyElement
    val REFINE_ROADNM_ADDR: String?,
    @PropertyElement
    val REFINE_WGS84_LOGT: String?,  // 경도
    @PropertyElement
    val REFINE_WGS84_LAT: String?,  // 위도
)





@Xml(name="ChildPlayFacility")
data class responseInfo3(
    @Element(name="head")
    val header: myHeader3,
    @Element(name="row")
    val row : MutableList<myRow3>
)

@Xml(name="head")
data class myHeader3(
    @Element
    val RESULT: myRESULT3
)

@Xml(name="RESULT")
data class myRESULT3(
    @PropertyElement
    val CODE : String,
    @PropertyElement
    val MESSAGE : String
)

@Xml(name="row")
data class myRow3(
    @PropertyElement
    val BIZPLC_NM: String,
    @PropertyElement
    val REFINE_LOTNO_ADDR: String?,
    @PropertyElement
    val REFINE_ROADNM_ADDR: String?,
    @PropertyElement
    val REFINE_WGS84_LOGT: String?,  // 경도
    @PropertyElement
    val REFINE_WGS84_LAT: String?,  // 위도
)


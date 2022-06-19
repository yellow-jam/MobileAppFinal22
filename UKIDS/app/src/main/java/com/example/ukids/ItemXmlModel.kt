package com.example.ukids

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml


class myRow() {
    lateinit var placename: String
    lateinit var placetype: String
    var addr: String? = null  // 번지주소
    var road_addr: String? = null  // 도로명주소
    var LOTG: String? = null  // 경도
    var LAT: String? = null  // 위도
    var TEL_NO: String? = null // 전화번호
    var optime: String? = null // 운영시간


    constructor(placename: String, placetype: String,
      addr: String?, road_addr: String?,
      lng: String?, lat: String?, telno: String?, optime: String?): this() {
        this.placename = placename
        this.placetype = placetype
        this.addr = addr
        this.road_addr = road_addr
        this.LOTG = lng
        this.LAT = lat
        this.TEL_NO = telno
        this.optime = optime
    }

    constructor(myRow: myRow1) : this(myRow.FACLT_NM, "놀이터",
        myRow.REFINE_LOTNO_ADDR, myRow.REFINE_ROADNM_ADDR,
        myRow.REFINE_WGS84_LOGT, myRow.REFINE_WGS84_LAT, "", "")

    constructor(myRow: myRow2) : this(myRow.PLAY_FACLT_NM, "놀이터",
        myRow.REFINE_LOTNO_ADDR, myRow.REFINE_ROADNM_ADDR,
        myRow.REFINE_WGS84_LOGT, myRow.REFINE_WGS84_LAT, "", "")

    constructor(myRow: myRow3) : this(myRow.BIZPLC_NM, "키즈카페",
        myRow.REFINE_LOTNO_ADDR, myRow.REFINE_ROADNM_ADDR,
        myRow.REFINE_WGS84_LOGT, myRow.REFINE_WGS84_LAT, "", "")

    constructor(myRow: myRow4) : this(myRow.LBRRY_NAME, "도서관",
        myRow.ADRES, myRow.ADRES,
        myRow.YDNTS, myRow.XCNTS, "", "")
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


/* 도서관 데이터 https://data.seoul.go.kr/dataList/OA-15480/S/1/datasetView.do */

@Xml(name="SeoulPublicLibraryInfo")
data class responseInfo4(
    @Element(name="RESULT")
    val header: myHeader4,
    @Element(name="row")
    val row : MutableList<myRow4>
)

@Xml(name="RESULT")
data class myHeader4(
    @PropertyElement
    val CODE : String,
    @PropertyElement
    val MESSAGE : String
)

@Xml(name="row")
data class myRow4(
    @PropertyElement
    val LBRRY_NAME: String,  // 도서관 이름
    @PropertyElement
    val ADRES: String?,  // 주소
    @PropertyElement
    val XCNTS: String?,  // 위도
    @PropertyElement
    val YDNTS: String?,  // 경도
    @PropertyElement
    val LBRRY_SE_NAME: String?,  // 도서관 구분명
    @PropertyElement
    val TEL_NO: String?,  // 전화번호
    @PropertyElement
    val HMPG_URL: String?,  // 홈페이지 주소
    @PropertyElement
    val OP_TIME: String?,  // 운영시간
)



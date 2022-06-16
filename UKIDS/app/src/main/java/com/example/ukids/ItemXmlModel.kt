package com.example.ukids

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name="ExcellenceChildPlayFaciliti")
data class responseInfo(
    @Element(name="head")
    val header: myHeader,
    @Element(name="row")
    val row : MutableList<myRow>
)

@Xml(name="head")
data class myHeader(
    @Element
    val RESULT: myRESULT
)

@Xml(name="RESULT")
data class myRESULT(
    @PropertyElement
    val CODE : String,
    @PropertyElement
    val MESSAGE : String
)

@Xml(name="row")
data class myRow(
    @PropertyElement
    val FACLT_NM: String,
    @PropertyElement
    val REFINE_LOTNO_ADDR: String?,
    @PropertyElement
    val REFINE_ROADNM_ADDR: String?,
    @PropertyElement
    val REFINE_WGS84_LOGT: String?,
    @PropertyElement
    val REFINE_WGS84_LAT: String?,
    @PropertyElement
    val MNFACLT_NM: String?,
    @PropertyElement
    val APPONT_RESN: String,
    @PropertyElement
    val TELNO: String,
)



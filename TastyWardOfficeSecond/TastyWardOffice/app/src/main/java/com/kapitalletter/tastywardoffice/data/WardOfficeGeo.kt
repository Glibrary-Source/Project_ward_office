package com.kapitalletter.tastywardoffice.data

import com.google.android.gms.maps.model.LatLng

class WardOfficeGeo {
    fun getWardGeo(wardOfficeName : String) : LatLng {
        val hashMap = HashMap<String, LatLng>()
        hashMap["강남구"] = LatLng(37.517477, 127.047373)
        hashMap["강동구"] = LatLng(37.530066, 127.123766)
        hashMap["강북구"] = LatLng(37.639875, 127.025574)
        hashMap["강서구"] = LatLng(37.550843, 126.849598)
        hashMap["관악구"] = LatLng(37.478022, 126.951490)
        hashMap["광진구"] = LatLng(37.538429, 127.082109)
        hashMap["구로구"] = LatLng(37.495521, 126.887699)
        hashMap["금천구"] = LatLng(37.456816, 126.894829)
        hashMap["노원구"] = LatLng(37.654136, 127.056839)
        hashMap["도봉구"] = LatLng(37.668966, 127.046631)
        hashMap["동대문구"] = LatLng(37.574423, 127.039569)
        hashMap["동작구"] = LatLng(37.512439, 126.939504)
        hashMap["마포구"] = LatLng(37.566013, 126.902179)
        hashMap["서대문구"] = LatLng(37.579331, 126.936743)
        hashMap["서초구"] = LatLng(37.483680, 127.032562)
        hashMap["성동구"] = LatLng(37.563338, 127.037131)
        hashMap["성북구"] = LatLng(37.589205, 127.016740)
        hashMap["송파구"] = LatLng(37.514649, 127.105725)
        hashMap["양천구"] = LatLng(37.517294, 126.866537)
        hashMap["영등포구"] = LatLng(37.526447, 126.896042)
        hashMap["용산구"] = LatLng(37.532267, 126.990630)
        hashMap["은평구"] = LatLng(37.602715, 126.929323)
        hashMap["종로구"] = LatLng(37.573389, 126.979014)
        hashMap["중구"] = LatLng(37.563698, 126.997546)
        hashMap["중랑구"] = LatLng(37.606163, 127.092911)
        return hashMap[wardOfficeName]!!
    }
}
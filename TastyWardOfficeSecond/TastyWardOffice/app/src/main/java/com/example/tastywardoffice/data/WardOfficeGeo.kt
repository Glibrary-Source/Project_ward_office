package com.example.tastywardoffice.data

import com.google.android.gms.maps.model.LatLng

class WardOfficeGeo() {
    fun getWardGeo(wardOfficeName : String) : LatLng {
        val HashMap = HashMap<String, LatLng>()
        HashMap["강남구"] = LatLng(37.517477, 127.047373)
        HashMap["강동구"] = LatLng(37.530066, 127.123766)
        HashMap["강북구"] = LatLng(37.639875, 127.025574)
        HashMap["강서구"] = LatLng(37.550843, 126.849598)
        HashMap["관악구"] = LatLng(37.478022, 126.951490)
        HashMap["광진구"] = LatLng(37.538429, 127.082109)
        HashMap["구로구"] = LatLng(37.495521, 126.887699)
        HashMap["금천구"] = LatLng(37.456816, 126.894829)
        HashMap["노원구"] = LatLng(37.654136, 127.056839)
        HashMap["도봉구"] = LatLng(37.668966, 127.046631)
        HashMap["동대문구"] = LatLng(37.574423, 127.039569)
        HashMap["동작구"] = LatLng(37.512439, 126.939504)
        HashMap["마포구"] = LatLng(37.566013, 126.902179)
        HashMap["서대문구"] = LatLng(37.579331, 126.936743)
        HashMap["서초구"] = LatLng(37.483680, 127.032562)
        HashMap["성동구"] = LatLng(37.563338, 127.037131)
        HashMap["성북구"] = LatLng(37.589205, 127.016740)
        HashMap["송파구"] = LatLng(37.514649, 127.105725)
        HashMap["양천구"] = LatLng(37.517294, 126.866537)
        HashMap["영등포구"] = LatLng(37.526447, 126.896042)
        HashMap["용산구"] = LatLng(37.532267, 126.990630)
        HashMap["은평구"] = LatLng(37.602715, 126.929323)
        HashMap["종로구"] = LatLng(37.573389, 126.979014)
        HashMap["중구"] = LatLng(37.563698, 126.997546)
        HashMap["중랑구"] = LatLng(37.606163, 127.092911)
        return HashMap[wardOfficeName]!!
    }
}
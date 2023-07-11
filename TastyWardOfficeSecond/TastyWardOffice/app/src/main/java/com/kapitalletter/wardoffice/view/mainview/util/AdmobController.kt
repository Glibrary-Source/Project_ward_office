package com.kapitalletter.wardoffice.view.mainview.util

import android.app.Activity
import android.content.Context
import com.kapitalletter.wardoffice.MyGlobals

class AdmobController(
    private val context: Context
    ) {

    fun mapInfoClickCallAd() {
        if (MyGlobals.instance?.adMobCount!! % 5 == 0) {
            MyGlobals.instance?.fullAD!!.show(context as Activity)
        }
    }

}
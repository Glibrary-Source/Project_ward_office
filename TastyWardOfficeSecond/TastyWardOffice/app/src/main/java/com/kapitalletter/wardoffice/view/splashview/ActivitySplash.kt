package com.kapitalletter.wardoffice.view.splashview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.kapitalletter.wardoffice.R
import com.kapitalletter.wardoffice.view.mainview.ActivityMain

class ActivitySplash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({

            val intent = Intent(this, ActivityMain::class.java)
            startActivity(intent)

            finish()
        }, 1000)
    }

}
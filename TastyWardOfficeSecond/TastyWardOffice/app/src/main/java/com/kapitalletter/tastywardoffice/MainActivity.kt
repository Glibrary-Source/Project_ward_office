package com.kapitalletter.tastywardoffice

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.kapitalletter.tastywardoffice.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mAdView : AdView
    private lateinit var adRequest: AdRequest

    private var mInterstitialAd: InterstitialAd? = null

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        //전면광고 + 배너광고용
        MobileAds.initialize(this) {}
        mAdView = binding.adView
        adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
            }

            override fun onAdShowedFullScreenContent() {
                mInterstitialAd = null
            }
        }

        getAD()

        //하단 네비바 관련 코드
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        //네비바 액션 효과
        val options = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setEnterAnim(R.anim.enter_from_right)
            .setExitAnim(R.anim.exit_to_right)
            .setPopEnterAnim(R.anim.enter_from_right)
            .setPopExitAnim(R.anim.exit_to_right)
            .setPopUpTo(navController.graph.startDestinationId, false)
            .build()

        //네비바 선택시 화면전환
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.google_map -> {
                    navController.navigate(R.id.google_map, null, options)
                }
                R.id.restaurant_list -> {
                    navController.navigate(R.id.restaurant_list, null, options)
                }
            }
            true
        }

    }

    //광고 서버에서 불러오기
    private fun getAD() {
        InterstitialAd.load(
            this,
            "ca-app-pub-3940256099942544/1033173712",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    mInterstitialAd = null
                }

                override fun onAdLoaded(p0: InterstitialAd) {
                    mInterstitialAd = p0
                    MyGlobals.instance?.fullAD = mInterstitialAd
                }
            })
    }

}



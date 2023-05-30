package com.kapitalletter.wardoffice

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
import com.kapitalletter.wardoffice.databinding.ActivityMainBinding


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

        MobileAds.initialize(this) {}
        mAdView = binding.adView
        adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {}
            override fun onAdFailedToShowFullScreenContent(p0: AdError) {}
            override fun onAdShowedFullScreenContent() { mInterstitialAd = null }
        }

        getAD()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

//        val options = NavOptions.Builder()
//            .setLaunchSingleTop(true)
//            .setEnterAnim(R.anim.enter_from_right)
//            .setExitAnim(R.anim.exit_to_right)
//            .setPopEnterAnim(R.anim.enter_from_right)
//            .setPopExitAnim(R.anim.exit_to_right)
//            .setPopUpTo(navController.graph.startDestinationId, false)
//            .build()

        val optionsSecond = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setEnterAnim(R.anim.enter_from_right)
            .setExitAnim(R.anim.exit_to_right)
            .setPopEnterAnim(R.anim.enter_from_right)
            .setPopExitAnim(R.anim.exit_to_right)
            .build()

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val currentDestination = navController.currentDestination?.id

            val isAlreadyActive = when(item.itemId) {
                R.id.google_map -> currentDestination == R.id.google_map
                R.id.restaurant_list -> currentDestination == R.id.restaurant_list
                else -> false
            }

            if(!isAlreadyActive) {
                when (item.itemId) {
                    R.id.google_map -> {
                        navController.navigate(R.id.google_map, null, optionsSecond)
                    }
                    R.id.restaurant_list -> {
                        navController.navigate(R.id.restaurant_list, null, optionsSecond)
                    }
                }
            }
            !isAlreadyActive
        }

        binding.btnLogin.setOnClickListener {
            navController.navigate(R.id.loginPage, null, optionsSecond)
        }

    }

    //전면광고 생성
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



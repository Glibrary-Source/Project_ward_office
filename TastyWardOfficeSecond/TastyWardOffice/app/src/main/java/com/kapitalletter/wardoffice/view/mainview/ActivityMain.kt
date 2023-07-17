package com.kapitalletter.wardoffice.view.mainview

import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.ads.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.kapitalletter.wardoffice.R
import com.kapitalletter.wardoffice.databinding.ActivityMainBinding
import com.kapitalletter.wardoffice.view.mainview.util.AdmobController
import com.kapitalletter.wardoffice.view.mainview.util.NavigationOptionsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ActivityMain : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var adMobController: AdmobController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUserCurrentLocation()

        MobileAds.initialize(this) {}
        adMobController = AdmobController(this)
        adMobController.setAdFullScreen(binding.adView, AdRequest.Builder().build())

        setBottomNav()
    }

    private fun setUserCurrentLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun setBottomNav() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val navigationOptionsManager = NavigationOptionsManager(navController)
        val options = navigationOptionsManager.setBottomTransformOption()

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val currentDestination = navController.currentDestination
            val selectedDestination = when (item.itemId) {
                R.id.google_map -> R.id.google_map
                R.id.restaurant_list -> R.id.restaurant_list
                else -> return@setOnItemSelectedListener true
            }

            if(currentDestination?.id != selectedDestination) {
                navController.navigate(selectedDestination, null, options)
            }
            true
        }
    }

}
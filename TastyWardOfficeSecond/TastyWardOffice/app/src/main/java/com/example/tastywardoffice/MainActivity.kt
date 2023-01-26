package com.example.tastywardoffice

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.example.tastywardoffice.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val multiplePermissionCode = 100
    private val requiredPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        checkLocationPermission()

        val options = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setEnterAnim(R.anim.enter_from_right)
            .setExitAnim(R.anim.exit_to_right)
            .setPopEnterAnim(R.anim.enter_from_right)
            .setPopExitAnim(R.anim.exit_to_right)
            .setPopUpTo(navController.graph.startDestination, false)
            .build()

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

    @SuppressLint("MissingPermission")
    private fun checkLocationPermission() {
            if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

        } else {
            val rejectedPermissionList = ArrayList<String>()

            for (permission in requiredPermissions) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    rejectedPermissionList.add(permission)
                }
            }

            if (rejectedPermissionList.isNotEmpty()) {
                val array = arrayOfNulls<String>(rejectedPermissionList.size)
                ActivityCompat.requestPermissions(
                    this,
                    rejectedPermissionList.toArray(array),
                    multiplePermissionCode
                )
            }
            Toast.makeText(this, "위치권한을 확인해 주세요", Toast.LENGTH_SHORT).show()
        }
    }


}



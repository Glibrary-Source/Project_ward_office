package com.example.tastywardoffice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tastywardoffice.databinding.ActivityMainBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
//    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)


//        initNavigationBar()
    }





    //하단 네비게이션바에서 프래그먼트 이동 코드
//    fun initNavigationBar() {
//        binding.bottomNavigation.run {
//            setOnItemSelectedListener {
//                when (it.itemId) {
//                    R.id.item_list -> {
//                        changeFragment(restaurant_list())
//                    }
//                    R.id.item_map -> {
//                        changeFragment(google_map())
//                    }
//                }
//                true
//            }
//            selectedItemId = R.id.item_map
//        }
//    }
//
//    fun changeFragment(fragment: Fragment) {
//        supportFragmentManager
//            .beginTransaction()
//            .replace(binding.navHostFragment.id , fragment)
//            .commit()
//    }

}
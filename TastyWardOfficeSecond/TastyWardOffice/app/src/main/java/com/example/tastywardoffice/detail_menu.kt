package com.example.tastywardoffice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tastywardoffice.databinding.FragmentDetailMenuBinding
import com.example.tastywardoffice.overview.bindImage
import kotlin.properties.Delegates


class detail_menu : Fragment() {

    val StoreData by navArgs<detail_menuArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = FragmentDetailMenuBinding.inflate(inflater)
        bindImage(binding.foodImage, StoreData.imgUri)
        binding.storeName.text = StoreData.storename
        binding.locationText.text = getString(R.string.location)

        binding.menuButton.setOnClickListener {
            val fragment1 = menu_image()
            parentFragmentManager.beginTransaction().replace(R.id.nav_bar, fragment1).commit()
        }
        binding.mapButton.setOnClickListener {
            val fragment2 = google_map()
            parentFragmentManager.beginTransaction().replace(R.id.nav_bar, fragment2).commit()
        }


        return binding.root
    }
}
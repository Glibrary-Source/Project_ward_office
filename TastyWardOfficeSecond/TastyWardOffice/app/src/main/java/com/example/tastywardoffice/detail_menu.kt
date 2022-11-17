package com.example.tastywardoffice


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.test.core.app.ApplicationProvider
import com.example.tastywardoffice.databinding.FragmentDetailMenuBinding
import com.example.tastywardoffice.overview.bindImage


class detail_menu : Fragment() {

    val StoreData by navArgs<detail_menuArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentDetailMenuBinding.inflate(inflater)
        bindImage(binding.foodImage, StoreData.imgUri)
        binding.storeName.text = StoreData.storename
        binding.locationText.text = getString(R.string.location)

        parentFragmentManager.beginTransaction().replace(
            R.id.nav_bar,
            menu_image().apply {
                arguments = Bundle().apply {
                    putString("Url", StoreData.imgUri)
                }
            }
        ).commit()

        //뷰를 처음 불러올때 메뉴부터 불러오기

        binding.menuButton.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(
                R.id.nav_bar,
                menu_image().apply {
                    arguments = Bundle().apply {
                        putString("Url", StoreData.imgUri)
                    }
                }
            ).commit()
        }

        binding.mapButton.setOnClickListener {
            val fragment2 = google_map()
            parentFragmentManager.beginTransaction().replace(R.id.nav_bar, fragment2).commit()
        }

        return binding.root
    }
}
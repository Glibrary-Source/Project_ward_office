package com.example.tastywardoffice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.tastywardoffice.databinding.FragmentDetailMenuBinding
import com.example.tastywardoffice.overview.bindImage
import kotlin.properties.Delegates


class detail_menu : Fragment() {

    val StoreData by navArgs<detail_menuArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = FragmentDetailMenuBinding.inflate(inflater)
        binding.testtext.text = StoreData.storename
        bindImage(binding.testImage, StoreData.imgUri)

        return binding.root
    }
}
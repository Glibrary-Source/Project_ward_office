package com.example.tastywardoffice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.tastywardoffice.databinding.FragmentRestaurantListBinding
import com.example.tastywardoffice.overview.OverviewViewModel
import com.example.tastywardoffice.overview.PhotoGridAdapter

class restaurant_list : Fragment() {

    private val viewModel: OverviewViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val binding = FragmentRestaurantListBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.photosGrid.adapter = PhotoGridAdapter()
        binding.photosGrid.setHasFixedSize(true)


        return binding.root
    }

}
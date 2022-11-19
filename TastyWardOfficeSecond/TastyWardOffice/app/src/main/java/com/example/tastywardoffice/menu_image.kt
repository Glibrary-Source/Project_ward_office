package com.example.tastywardoffice


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tastywardoffice.databinding.FragmentMenuImageBinding
import com.example.tastywardoffice.overview.bindImage


class menu_image : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMenuImageBinding.inflate(inflater)


        bindImage(binding.mainMenuImage, arguments?.getString("Url"))

        return binding.root
    }
}
package com.example.tastywardoffice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tastywardoffice.databinding.FragmentMenuImageBinding

class menu_image : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentMenuImageBinding.inflate(inflater)

        return binding.root
    }
}
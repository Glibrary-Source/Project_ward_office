package com.example.tastywardoffice


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.tastywardoffice.databinding.FragmentMenuImageBinding


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
        binding.mainMenuImage.scaleType = ImageView.ScaleType.FIT_CENTER
        Log.d("testLog", arguments?.getString("Url").toString() )

        return binding.root
    }
}
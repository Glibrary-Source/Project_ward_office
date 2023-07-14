package com.kapitalletter.wardoffice.view.mainview


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.kapitalletter.wardoffice.R
import com.kapitalletter.wardoffice.adapter.bindImage
import com.kapitalletter.wardoffice.databinding.FragmentMenuImageBinding


class FragmentMenuImage : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMenuImageBinding.inflate(inflater)

        try{ bindImage(binding.mainMenuImage, arguments?.getString("Url")) }
        catch (e:Exception) {binding.mainMenuImage.setImageResource(R.drawable.blank_img)}

        binding.mainMenuImage.scaleType = ImageView.ScaleType.FIT_XY

        return binding.root
    }

}
package com.example.tastywardoffice


import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.tastywardoffice.databinding.FragmentDetailMenuBinding

class detail_menu : Fragment() {

    val StoreData by navArgs<detail_menuArgs>()
    private val TAG = "detailFG"

    lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is MainActivity) {
            mContext = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentDetailMenuBinding.inflate(inflater)

        Log.d(TAG, "\n" + StoreData.storename +"\n" + StoreData.imgUri+ "\n" + StoreData.latlng)

        //메인가게 이미지
        bindImage(binding.foodImage, StoreData.imgUri)
        binding.foodImage.clipToOutline = true

        arguments = Bundle()

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
            parentFragmentManager.beginTransaction().replace(
                R.id.nav_bar,
                detail_googleMap().apply {
                    arguments = Bundle().apply {
                        putParcelable("LatLng", StoreData.latlng)
                        putString("storeName", StoreData.storename)
                    }
                }
            ).commit()
        }

        return binding.root
    }
}
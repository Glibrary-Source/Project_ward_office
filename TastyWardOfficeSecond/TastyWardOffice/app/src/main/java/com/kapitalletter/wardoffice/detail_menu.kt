package com.kapitalletter.wardoffice

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.kapitalletter.wardoffice.adapter.DetailMenuAdapter
import com.kapitalletter.wardoffice.databinding.FragmentDetailMenuBinding
import com.kapitalletter.wardoffice.datamodel.Filterstore
import com.kapitalletter.wardoffice.overview.OverviewViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import java.util.*


class detail_menu : Fragment(){

    private lateinit var mContext: Context
    private val storeData by navArgs<detail_menuArgs>()
    private lateinit var overViewModel: OverviewViewModel

    private lateinit var auth: FirebaseAuth

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is MainActivity) {
            mContext = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MyGlobals.instance?.adMobCount = MyGlobals.instance?.adMobCount!! + 1

        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentDetailMenuBinding.inflate(inflater)

        overViewModel = ViewModelProvider(requireActivity())[OverviewViewModel::class.java]

        val storeDetailData = detailItemData()

        binding.navBar.clipToOutline = true
        binding.viewPager2.clipToOutline = true

        binding.storeName.text = storeData.storename

        binding.viewPager2.adapter = DetailMenuAdapter(storeDetailData.document)
        binding.viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.viewPager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            var currentState = 0
            var currentPos = 0

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                val pictureListSize: Int = if(storeDetailData.document.storeMenuPictureUrlsMenu.isEmpty()) {
                    3
                } else {
                    storeDetailData.document.storeMenuPictureUrlsMenu.size
                }
                if(currentState == ViewPager2.SCROLL_STATE_DRAGGING && currentPos == position) {
                    if(currentPos == 0) binding.viewPager2.currentItem = pictureListSize-1
                    else if(currentPos == pictureListSize-1)binding.viewPager2.currentItem = 0
                }
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                currentPos = position
                super.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                currentState = state
                super.onPageScrollStateChanged(state)
            }
        })

        val tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, binding.viewPager2) { _, _ -> }.attach()

        try{
            binding.locationText.text = locationAddress()?.get(0)?.getAddressLine(0)!!.substring(5)
        }
        catch (e: Exception) {
            binding.locationText.text = ""
        }

        arguments = Bundle()

        parentFragmentManager.beginTransaction().replace(
            R.id.nav_bar,
            menu_image().apply {
                arguments = Bundle().apply {
                    putString("Url", storeDetailData.document.storeMenuPictureUrlsMenu[0])
                }
            }
        ).commit()

        binding.menuButton.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(
                R.id.nav_bar,
                menu_image().apply {
                    arguments = Bundle().apply {
                        putString("Url", storeDetailData.document.storeMenuPictureUrlsMenu[0])
                    }
                }
            ).commit()
        }

        binding.mapButton.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(
                R.id.nav_bar,
                detail_googleMap().apply {
                    MyGlobals.instance?.detailLatLng = LatLng(storeDetailData.document.storeGEOPoints[0],storeDetailData.document.storeGEOPoints[1])
                    MyGlobals.instance?.detailStoreId = storeDetailData.document.storeId
                }
            ).commit()
        }

        binding.reviewButton.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(
                R.id.nav_bar,
                Review().apply {
                    arguments = Bundle().apply {
                        putString("docId", storeDetailData.document.docId)
                    }
                }
            ).commit()
        }

        return binding.root
    }

    private fun detailItemData() : Filterstore {
        val passingData = overViewModel.distanceStoreData.value!!.Filterstore
        for(storeData in passingData){
            if(storeData.document.docId == this.storeData.dogId) {
                return storeData
            }
        }
        return passingData[0]
    }

    private fun locationAddress(): List<Address>? {
        val geocoder = Geocoder(mContext, Locale.KOREA)
        return geocoder.getFromLocation(storeData.latlng.latitude, storeData.latlng.longitude, 1)!!
    }


}


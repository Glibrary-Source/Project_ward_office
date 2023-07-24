package com.kapitalletter.wardoffice.view.mainview

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.kapitalletter.wardoffice.view.mainview.adapter.DetailMenuAdapter
import com.kapitalletter.wardoffice.databinding.FragmentDetailMenuBinding
import com.kapitalletter.wardoffice.datamodel.FilterStore
import com.kapitalletter.wardoffice.view.mainview.viewmodel.OverviewViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.tabs.TabLayoutMediator
import com.kapitalletter.wardoffice.MyGlobals
import com.kapitalletter.wardoffice.R
import com.kapitalletter.wardoffice.view.mainview.util.AdmobController
import com.kapitalletter.wardoffice.view.mainview.util.DetailMenuDataController
import com.kapitalletter.wardoffice.view.mainview.util.LocalAddressController
import com.kapitalletter.wardoffice.view.mainview.util.ViewPagerController

class FragmentDetailMenu : Fragment() {

    private val storeData by navArgs<FragmentDetailMenuArgs>()
    private lateinit var overViewModel: OverviewViewModel
    private lateinit var localAddressController: LocalAddressController
    private lateinit var admobController: AdmobController
    private lateinit var detailMenuDataController: DetailMenuDataController
    private lateinit var viewPagerController: ViewPagerController
    private lateinit var binding: FragmentDetailMenuBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context = requireContext()
        admobController = AdmobController(context)
        localAddressController = LocalAddressController(context)
        detailMenuDataController = DetailMenuDataController()
        viewPagerController = ViewPagerController()

        admobController.detailFragmentClickCounter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDetailMenuBinding.inflate(inflater)

        overViewModel = ViewModelProvider(requireActivity())[OverviewViewModel::class.java]

        val storeDetailData = detailMenuDataController.detailItemData(overViewModel, storeData.docId)

        binding.navBar.clipToOutline = true
        binding.viewPager2.clipToOutline = true

        binding.storeName.text = storeData.storename

        binding.viewPager2.adapter = DetailMenuAdapter(storeDetailData.document)

        binding.viewPager2.registerOnPageChangeCallback(
            viewPagerController.getDetailPageFragmentOnPageChangeCallback(storeDetailData,binding)
        )

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { _, _ -> }.attach()

        try{ binding.locationText.text = localAddressController.getDetailStoreAddress(storeData.latlng) }
        catch (e: Exception) { binding.locationText.text = "" }

        setDetailNavMenuAction(storeDetailData)

        return binding.root
    }

    private fun setDetailNavMenuAction(
        storeDetailData: FilterStore
    ) {
        arguments = Bundle()

        parentFragmentManager.beginTransaction().replace(
            R.id.nav_bar,
            FragmentMenuImage().apply {
                arguments = Bundle().apply {
                    putString("Url", storeDetailData.document.storeMenuPictureUrls.menu[0])
                }
            }
        ).commit()

        binding.menuButton.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(
                R.id.nav_bar,
                FragmentMenuImage().apply {
                    arguments = Bundle().apply {
                        putString("Url", storeDetailData.document.storeMenuPictureUrls.menu[0])
                    }
                }
            ).commit()
        }

        binding.mapButton.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(
                R.id.nav_bar,
                FragmentDetailGoogleMap().apply {
                    MyGlobals.instance?.detailLatLng = LatLng(storeDetailData.document.storeGEOPoints[0],storeDetailData.document.storeGEOPoints[1])
                    MyGlobals.instance?.detailStoreId = storeDetailData.document.storeId
                }
            ).commit()
        }

        binding.reviewButton.setOnClickListener {
            Toast.makeText(requireContext(), getString(R.string.explain), Toast.LENGTH_SHORT).show()
        }
    }

}
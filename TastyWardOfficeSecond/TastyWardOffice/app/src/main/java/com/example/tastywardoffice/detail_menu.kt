package com.example.tastywardoffice

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.tastywardoffice.databinding.FragmentDetailMenuBinding
import com.example.tastywardoffice.datamodel.Filterstore
import com.example.tastywardoffice.overview.OverviewViewModel
import com.google.android.gms.maps.model.LatLng
import java.util.*


class detail_menu : Fragment() {

    private lateinit var mContext: Context
    private val StoreData by navArgs<detail_menuArgs>()
    private lateinit var overViewModel: OverviewViewModel
    private var shortAnimationDuration: Int = 0


    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is MainActivity) {
            mContext = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentDetailMenuBinding.inflate(inflater)

        overViewModel = ViewModelProvider(requireActivity()).get(OverviewViewModel::class.java)

        //detailItemData() 함수를 통해 뷰모델어서 뽑아온 스토어 데이터를 변수에 저장
        val storeDetailData = detailItemData()


        //각각의 뷰에 맞게 데이터를 입력시킨다
        binding.foodImage.clipToOutline = true
        binding.navBar.clipToOutline = true
        binding.storeName.text = StoreData.storename
        binding.locationText.text = locationAddress()[0].getAddressLine(0).substring(5)

        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)

        //메인 메뉴 이미지옆 화살표 누를때 뷰를 변환시킴
        try{ bindImage(binding.foodImage, storeDetailData.document.storeMenuPictureUrlsMenu[1]) }
        catch (e: Exception) {
            binding.foodImage.setImageResource(R.drawable.ic_baseline_broken_image_24)
        }
        binding.firstMenuButton.isChecked = true
        binding.menuImageLeftButton.setOnClickListener{
            if(binding.firstMenuButton.isChecked) {
                binding.thirdMenuButton.isChecked = true
                try{
                    bindImage(
                        binding.foodImage,
                        storeDetailData.document.storeMenuPictureUrlsMenu[3]
                    )
                } catch (e: Exception) {
                    binding.foodImage.setImageResource(R.drawable.ic_baseline_broken_image_24)
                }
            }
            else if(binding.secondMenuButton.isChecked) {
                binding.firstMenuButton.isChecked = true
                try {
                    bindImage(
                        binding.foodImage,
                        storeDetailData.document.storeMenuPictureUrlsMenu[1]
                    )
                } catch (e: Exception) {
                    binding.foodImage.setImageResource(R.drawable.ic_baseline_broken_image_24)
                }
            }
            else if(binding.thirdMenuButton.isChecked) {
                binding.secondMenuButton.isChecked = true
                try{
                    bindImage(
                        binding.foodImage,
                        storeDetailData.document.storeMenuPictureUrlsMenu[2]
                    )
                } catch (e: Exception) {
                    binding.foodImage.setImageResource(R.drawable.ic_baseline_broken_image_24)
                }
            }
        }
        binding.menuImageRightButton.setOnClickListener{
            if(binding.firstMenuButton.isChecked) {
                binding.secondMenuButton.isChecked = true
                try{
                    bindImage(
                        binding.foodImage,
                        storeDetailData.document.storeMenuPictureUrlsMenu[2]
                    )
                } catch (e: Exception) {
                    binding.foodImage.setImageResource(R.drawable.ic_baseline_broken_image_24)
                }
            }
            else if(binding.secondMenuButton.isChecked) {
                binding.thirdMenuButton.isChecked = true
                try{
                    bindImage(
                        binding.foodImage,
                        storeDetailData.document.storeMenuPictureUrlsMenu[3]
                    )
                }catch (e: Exception) {
                    binding.foodImage.setImageResource(R.drawable.ic_baseline_broken_image_24)
                }
            }
            else if(binding.thirdMenuButton.isChecked) {
                binding.firstMenuButton.isChecked = true
                try {
                    bindImage(
                        binding.foodImage,
                        storeDetailData.document.storeMenuPictureUrlsMenu[1]
                    )
                } catch (e: Exception) {
                    binding.foodImage.setImageResource(R.drawable.ic_baseline_broken_image_24)
                }
            }
        }

        //radio 버튼 변화
        binding.firstMenuButton.setOnClickListener {
            try{ bindImage(binding.foodImage, storeDetailData.document.storeMenuPictureUrlsMenu[1]) }
            catch (e: Exception) {
                binding.foodImage.setImageResource(R.drawable.ic_baseline_broken_image_24)
            }
        }
        binding.secondMenuButton.setOnClickListener {
            try{ bindImage(binding.foodImage, storeDetailData.document.storeMenuPictureUrlsMenu[2]) }
            catch (e: Exception) {
                binding.foodImage.setImageResource(R.drawable.ic_baseline_broken_image_24)
            }
        }
        binding.thirdMenuButton.setOnClickListener {
            try{ bindImage(binding.foodImage, storeDetailData.document.storeMenuPictureUrlsMenu[3]) }
            catch (e: Exception) {
                binding.foodImage.setImageResource(R.drawable.ic_baseline_broken_image_24)
            }
        }


        //네비게이션 프래그먼트 데이터 전달을 위해 번들 사용
        arguments = Bundle()

        //뷰를 처음 불러올때 메뉴부터 불러오기
        parentFragmentManager.beginTransaction().replace(
            R.id.nav_bar,
            menu_image().apply {
                arguments = Bundle().apply {
                    putString("Url", storeDetailData.document.storeMenuPictureUrlsMenu[0])
                }
            }
        ).commit()

        //메뉴 네비게이션바 클릭시 화면 전환
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

        //지도 네비게이션바 클릭시 화면 전환
        binding.mapButton.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(
                R.id.nav_bar,
                detail_googleMap().apply {
                    arguments = Bundle().apply {
                        putParcelable("LatLng", LatLng(storeDetailData.document.storeGEOPoints[0],storeDetailData.document.storeGEOPoints[1]))
                        putString("storeName", storeDetailData.document.storeId)
                    }
                }
            ).commit()
        }


        return binding.root
    }

    //뷰모델 데이터에서 일치하는 독아이디의 document 데이터를 가져옴
    private fun detailItemData() : Filterstore {
        val passingData = overViewModel.distanceStoreData.value!!.Filterstore
        for(storedata in passingData){
            if(storedata.document.docId == StoreData.dogId) {
                return storedata
            }
        }
        return passingData[0]
    }

    //주소 텍스트를 위한 코드
    private fun locationAddress(): List<Address> {
        val geocoder = Geocoder(mContext, Locale.KOREA)
        return geocoder.getFromLocation(StoreData.latlng.latitude, StoreData.latlng.longitude, 1)
    }

}


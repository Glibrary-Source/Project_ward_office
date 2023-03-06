package com.kapitalletter.tastywardoffice

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.kapitalletter.tastywardoffice.adapter.DetailMenuAdapter
import com.kapitalletter.tastywardoffice.databinding.FragmentDetailMenuBinding
import com.kapitalletter.tastywardoffice.datamodel.Filterstore
import com.kapitalletter.tastywardoffice.overview.OverviewViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*


class detail_menu : Fragment() {

    private lateinit var mContext: Context
    private val storeData by navArgs<detail_menuArgs>()
    private lateinit var overViewModel: OverviewViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is MainActivity) {
            mContext = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MyGlobals.instance?.adMobCount = MyGlobals.instance?.adMobCount!! + 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //디테일 메뉴 프래그먼트 바인딩
        val binding = FragmentDetailMenuBinding.inflate(inflater)

        //뷰모델 바인딩
        overViewModel = ViewModelProvider(requireActivity())[OverviewViewModel::class.java]

        //detailItemData() 함수를 통해 뷰모델어서 뽑아온 스토어 데이터를 변수에 저장
        val storeDetailData = detailItemData()

        //각각의 뷰 모양 둥글게
        binding.navBar.clipToOutline = true
        binding.viewPager2.clipToOutline = true

        //각각의 뷰에 데이터 입력
        binding.storeName.text = storeData.storename

        //뷰페이저 슬라이드 넘기기
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
                //주의 리스트사이즈 0일때
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

        //디테일 메뉴 인디케이터 설정
        val tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, binding.viewPager2) { _, _ -> }.attach()

        //상세주소 만약 geocode getlocation 있으면 상세주소 Text뷰에 없으면 api 웹에서 요청하자
        try{
            binding.locationText.text = locationAddress()?.get(0)?.getAddressLine(0)!!.substring(5)
        }
        catch (e: Exception) {
            binding.locationText.text = ""
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
                    MyGlobals.instance?.detailLatLng = LatLng(storeDetailData.document.storeGEOPoints[0],storeDetailData.document.storeGEOPoints[1])
                    MyGlobals.instance?.detailStoreId = storeDetailData.document.storeId
                }
            ).commit()
        }

        binding.reviewButton.setOnClickListener {
            Toast.makeText(mContext, getString(R.string.explain), Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    //뷰모델 데이터에서 일치하는 독아이디의 document 데이터를 가져옴
    private fun detailItemData() : Filterstore {
        val passingData = overViewModel.distanceStoreData.value!!.Filterstore
        for(storeData in passingData){
            if(storeData.document.docId == this.storeData.dogId) {
                return storeData
            }
        }
        return passingData[0]
    }

    //주소 텍스트를 위한 코드
    private fun locationAddress(): List<Address>? {
        val geocoder = Geocoder(mContext, Locale.KOREA)
        return geocoder.getFromLocation(storeData.latlng.latitude, storeData.latlng.longitude, 1)!!
    }

}


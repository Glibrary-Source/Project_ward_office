package com.example.tastywardoffice


import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.test.core.app.ApplicationProvider
import com.example.tastywardoffice.databinding.FragmentDetailMenuBinding
import com.example.tastywardoffice.network.RequestType
import com.example.tastywardoffice.network.TastyWardApi
import com.example.tastywardoffice.network.WholeData
import com.example.tastywardoffice.overview.OverviewViewModel
import com.example.tastywardoffice.overview.bindImage
import com.google.android.gms.maps.model.LatLng
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class detail_menu : Fragment() {

    val StoreData by navArgs<detail_menuArgs>()
    private val TAG = "detailFG"

    lateinit var mContext: Context
    lateinit var storepostion : LatLng
    lateinit var adress: MutableList<Address>
    lateinit var geocoder: Geocoder

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

        val tempData = RequestType("whole_stores")
        TastyWardApi.service.getWholeData(tempData).enqueue(object: Callback<WholeData> {
            override fun onResponse(call: Call<WholeData>, response: Response<WholeData>) {
                if(response.isSuccessful) {
                    for(i in response.body()!!.stores) {
                        if (StoreData.storename == i.storeId) {
                            Log.d("detailFG", i.storeId + "/" + StoreData.storename + "\n" + i.storeMenuPictureUrls[0])
                            bindImage(binding.foodImage, i.storeMenuPictureUrls[0])
                        }
                    }
                } else {
                    Log.d("YMC", "실패입니다")
                }
            }
            override fun onFailure(call: Call<WholeData>, t: Throwable) {
                Log.d("YMC", "onFailure 에러 " + t.message.toString())
            }
        })

        Log.d(TAG, StoreData.storename)

        storepostion = StoreData.latlng
        geocoder = Geocoder(mContext, Locale.KOREA)
        adress = geocoder.getFromLocation(StoreData.latlng.latitude, StoreData.latlng.longitude, 1)


        binding.storeName.text = StoreData.storename
        binding.locationText.text = adress[0].getAddressLine(0)

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
                    }
                }
            ).commit()
        }

        return binding.root
    }
}
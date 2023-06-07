package com.kapitalletter.wardoffice

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kapitalletter.wardoffice.databinding.FragmentMyProfileBinding
import com.kapitalletter.wardoffice.overview.OverviewViewModel
import com.kapitalletter.wardoffice.preferences.MyApplication
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback

class MyProfile : Fragment() {

    private val checkNumberOff = 0

    private lateinit var binding: FragmentMyProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var overViewModel: OverviewViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        overViewModel = ViewModelProvider(requireActivity())[OverviewViewModel::class.java]
        NaverIdLoginSDK.initialize(
            requireContext(),
            getString(R.string.social_login_info_naver_client_id),
            getString(R.string.social_login_info_naver_client_secret),
            getString(R.string.social_login_info_naver_client_name)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMyProfileBinding.inflate(inflater)

        binding.btnFragmentMyprofilLogout.setOnClickListener {
            auth.signOut()
            NaverIdLoginSDK.logout()
            startNaverDeleteToken()

            MyApplication.preferences.setString("checkLogin", "0")

            MyApplication.preferences.setString("GUserName", "")
            MyApplication.preferences.setString("GUserEmail", "")

            MyApplication.preferences.setString("NProfileName", "")
            MyApplication.preferences.setString("NProfileEmail", "")

            val action = MyProfileDirections.actionMyProfileToLoginPage()
            findNavController().navigate(action)
        }

        setProfile()

        return binding.root
    }

    private fun setProfile() {
        if(MyApplication.preferences.getString("GUserName", "") != ""){
            binding.textFragmentMyprofilUsername.text = MyApplication.preferences.getString("GUserName", "")
            binding.textFragmentMyprofilUseremail.text = MyApplication.preferences.getString("GUserEmail", "")
        } else {
            binding.textFragmentMyprofilUsername.text =  MyApplication.preferences.getString("NProfileName", "")
            binding.textFragmentMyprofilUseremail.text = MyApplication.preferences.getString("NProfileEmail", "")
        }
    }

    private fun startNaverDeleteToken() {
        NidOAuthLogin().callDeleteTokenApi(requireContext(), object : OAuthLoginCallback {
            override fun onSuccess() {
                Toast.makeText(requireContext(), "네이버 아이디 토큰삭제 성공!", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(httpStatus: Int, message: String) {
                Log.d("LoginFragment", "errorCode: ${NaverIdLoginSDK.getLastErrorCode().code}")
                Log.d("LoginFragment", "errorDesc: ${NaverIdLoginSDK.getLastErrorDescription()}")
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        })
    }
}
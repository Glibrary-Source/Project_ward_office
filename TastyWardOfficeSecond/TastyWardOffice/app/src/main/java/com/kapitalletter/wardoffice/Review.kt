package com.kapitalletter.wardoffice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.kapitalletter.wardoffice.adapter.ReviewAdapter
import com.kapitalletter.wardoffice.databinding.FragmentReviewBinding
import com.kapitalletter.wardoffice.overview.OverviewViewModel
import com.kapitalletter.wardoffice.preferences.MyApplication
import com.navercorp.nid.NaverIdLoginSDK

class Review : Fragment() {

    lateinit var binding: FragmentReviewBinding
    private val storeDocIdNavArgs by navArgs<ReviewArgs>()

    private lateinit var auth: FirebaseAuth
    private lateinit var overViewModel: OverviewViewModel
    private lateinit var storeDocId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        overViewModel = ViewModelProvider(requireActivity())[OverviewViewModel::class.java]

        //naver login 초기화
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
        binding = FragmentReviewBinding.inflate(inflater)

//        프래그먼트 안바꾸고 싶을때
//        val storeDocId = arguments?.getString("docId").toString()

        storeDocId = storeDocIdNavArgs.docId
        binding.btnCreate.setOnClickListener {
            if (auth.currentUser != null) {
                createReviewGoogleId()
            } else if (MyApplication.preferences.getString("checkLogin", "1") == "1") {
                createReviewNaverID()
            } else {
                Toast.makeText(requireContext(), "로그인해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        val recyclerView = binding.rcvReviewList

        overViewModel.readReview(storeDocId)
        overViewModel.reviewData.observe(viewLifecycleOwner) {
            recyclerView.adapter = ReviewAdapter(overViewModel.reviewData.value!!)
        }

        return binding.root
    }

    private fun createReviewGoogleId() {
        storeDocId = storeDocIdNavArgs.docId

        overViewModel.createReview(
            storeDocId,
            binding.editReviewContext.text.toString(),
            binding.editNickname.text.toString(),
            auth.currentUser!!.uid
        )
        binding.editReviewContext.text.clear()
        binding.editNickname.text.clear()
        Toast.makeText(requireContext(), "리뷰가 작성 되었습니다", Toast.LENGTH_SHORT).show()

        overViewModel.readReview(storeDocId)
    }

    private fun createReviewNaverID() {
        storeDocId = storeDocIdNavArgs.docId

        overViewModel.createReview(
            storeDocId,
            binding.editReviewContext.text.toString(),
            binding.editNickname.text.toString(),
            MyApplication.preferences.getString("NLoginUid", "")
        )
        binding.editReviewContext.text.clear()
        binding.editNickname.text.clear()
        Toast.makeText(requireContext(), "리뷰가 작성 되었습니다", Toast.LENGTH_SHORT).show()

        overViewModel.readReview(storeDocId)
    }


}
package com.kapitalletter.wardoffice

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.kapitalletter.wardoffice.adapter.ReviewAdapter
import com.kapitalletter.wardoffice.databinding.FragmentReviewBinding
import com.kapitalletter.wardoffice.overview.OverviewViewModel

class Review : Fragment() {

    lateinit var binding: FragmentReviewBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var overViewModel: OverviewViewModel
    private val uidList: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        overViewModel = ViewModelProvider(requireActivity())[OverviewViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentReviewBinding.inflate(inflater)

        val storeDocId = arguments?.getString("docId").toString()

        binding.btnCreate.setOnClickListener {
            if(auth.currentUser != null){
                if(!uidList.contains(auth.currentUser!!.uid)) {
                    overViewModel.createReview(
                        storeDocId,
                        binding.editReviewContext.text.toString(),
                        binding.editNickname.text.toString(),
                        auth.currentUser!!.uid
                    )
                    binding.editReviewContext.text.clear()
                    binding.editNickname.text.clear()
                    Toast.makeText(requireContext(), "리뷰를 작성 되었습니다", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "리뷰를 이미 작성하셨습니다", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(requireContext(), "로그인이 필요합니다", Toast.LENGTH_SHORT).show()
            }

            overViewModel.readReview(storeDocId)
        }

        val recyclerView = binding.rcvReviewList

        overViewModel.readReview(storeDocId)
        overViewModel.reviewData.observe(viewLifecycleOwner) {
            recyclerView.adapter = ReviewAdapter(overViewModel.reviewData.value!!)

            for(uid in overViewModel.reviewData.value!!.docId) {
                uidList.add(uid.reviewerUid)
            }

        }

        return binding.root
    }

}
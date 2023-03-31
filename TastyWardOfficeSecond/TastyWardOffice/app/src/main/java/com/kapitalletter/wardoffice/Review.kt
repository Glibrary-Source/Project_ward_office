package com.kapitalletter.wardoffice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.kapitalletter.wardoffice.databinding.FragmentReviewBinding
import com.kapitalletter.wardoffice.overview.OverviewViewModel

class Review : Fragment() {

    lateinit var binding: FragmentReviewBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var overViewModel: OverviewViewModel

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

        binding.btnCreate.setOnClickListener {
            overViewModel.createReview(
                "004T7UNt9uU7yBqmr6nb",
                binding.editReviewContext.text.toString(),
                binding.editNickname.text.toString(),
                "asdacwwqeee12332"
            )
        }

        return binding.root
    }

}
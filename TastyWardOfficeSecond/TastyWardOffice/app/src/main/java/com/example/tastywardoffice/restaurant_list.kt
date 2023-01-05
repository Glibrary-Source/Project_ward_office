package com.example.tastywardoffice

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tastywardoffice.adapter.StoreListAdapter
import com.example.tastywardoffice.databinding.FragmentRestaurantListBinding
import com.example.tastywardoffice.overview.OverviewViewModel


class restaurant_list : Fragment() {

    private lateinit var overViewModel: OverviewViewModel
    lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is MainActivity) {
            mContext = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overViewModel = ViewModelProvider(requireActivity()).get(OverviewViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val binding = FragmentRestaurantListBinding.inflate(inflater)

        binding.photosGrid.setHasFixedSize(true)

//        Log.d("viewModelTest" ,overViewModel.distanceStoreData.value.toString())

        val myDataset = overViewModel.distanceStoreData.value
        val recyclerView = binding.photosGrid

        recyclerView.adapter = StoreListAdapter(mContext, myDataset!!)

        return binding.root
    }

}
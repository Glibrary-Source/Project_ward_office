package com.kapitalletter.wardoffice.view.mainview

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kapitalletter.wardoffice.databinding.FragmentRestaurantListBinding
import com.kapitalletter.wardoffice.datamodel.FinalStoreDataModel
import com.kapitalletter.wardoffice.view.mainview.util.RestaurantListDataController
import com.kapitalletter.wardoffice.view.mainview.viewmodel.OverviewViewModel

class FragmentRestaurantList : Fragment() {

    private lateinit var overViewModel: OverviewViewModel
    private lateinit var mContext: Context
    private lateinit var restaurantListDataController: RestaurantListDataController

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ActivityMain) {
            mContext = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overViewModel = ViewModelProvider(requireActivity())[OverviewViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentRestaurantListBinding.inflate(inflater)

        val recyclerView = binding.rvRestaurantList
        recyclerView.setHasFixedSize(true)

        val myDataset : FinalStoreDataModel = overViewModel.getDistanceToData()
        restaurantListDataController = RestaurantListDataController(requireContext(), binding, myDataset)
        restaurantListDataController.setEmptyListImg()
        restaurantListDataController.setAdapter(overViewModel.filterState.value!!, recyclerView)

        return binding.root
    }
}
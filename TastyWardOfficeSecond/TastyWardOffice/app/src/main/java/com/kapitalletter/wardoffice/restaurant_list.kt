package com.kapitalletter.wardoffice

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.*
import com.kapitalletter.wardoffice.view.mainview.adapter.StoreListAdapter
import com.kapitalletter.wardoffice.databinding.FragmentRestaurantListBinding
import com.kapitalletter.wardoffice.datamodel.FilterStore
import com.kapitalletter.wardoffice.datamodel.FinalStoreDataModel
import com.kapitalletter.wardoffice.view.mainview.viewmodel.OverviewViewModel
import com.kapitalletter.wardoffice.view.ActivityMain

class restaurant_list : Fragment() {

    private lateinit var overViewModel: OverviewViewModel
    private lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ActivityMain) {
            mContext = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overViewModel = ViewModelProvider(requireActivity())[OverviewViewModel::class.java]

        MobileAds.initialize(mContext) {}

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentRestaurantListBinding.inflate(inflater)

        binding.photosGrid.setHasFixedSize(true)

        val myDataset : FinalStoreDataModel =
            try{
                overViewModel.distanceStoreData.value!!
            } catch (e: Exception) {
            FinalStoreDataModel(listOf())
            }

        val recyclerView = binding.photosGrid

        if(myDataset.filterStore.isEmpty()) {
            binding.statusImage.setImageResource(R.drawable.emptylist)
        }

        val filterDocument = mutableListOf<FilterStore>()
        val filterDataset = FinalStoreDataModel(filterDocument)

        if(overViewModel.filterState.value != "all"){
            for (i in myDataset.filterStore) {
                if (i.document.storeTitle == overViewModel.filterState.value) {
                    filterDocument.add(i)
                }
            }
            recyclerView.adapter = StoreListAdapter(filterDataset, mContext)
        } else {
            recyclerView.adapter = StoreListAdapter(myDataset, mContext)
        }

        return binding.root
    }
}
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
import com.example.tastywardoffice.datamodel.FinalStoreDataModel
import com.example.tastywardoffice.overview.OverviewViewModel

class restaurant_list : Fragment() {

    private lateinit var overViewModel: OverviewViewModel
    private lateinit var mContext: Context

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

        if(myDataset.Filterstore.isEmpty()) {
            binding.statusImage.setImageResource(R.drawable.emptylist)
        }

        recyclerView.adapter = StoreListAdapter(mContext, myDataset)

        return binding.root
    }
}
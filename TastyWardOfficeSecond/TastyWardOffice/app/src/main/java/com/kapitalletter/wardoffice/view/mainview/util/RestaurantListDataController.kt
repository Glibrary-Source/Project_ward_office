package com.kapitalletter.wardoffice.view.mainview.util

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.kapitalletter.wardoffice.R
import com.kapitalletter.wardoffice.databinding.FragmentRestaurantListBinding
import com.kapitalletter.wardoffice.datamodel.FilterStore
import com.kapitalletter.wardoffice.datamodel.FinalStoreDataModel
import com.kapitalletter.wardoffice.view.mainview.adapter.StoreListAdapter

class RestaurantListDataController(
    private val context: Context,
    private val binding: FragmentRestaurantListBinding,
    private val myDataset: FinalStoreDataModel
) {

    fun setEmptyListImg() {
        if (myDataset.filterStore.isEmpty()) {
            binding.statusImage.setImageResource(R.drawable.emptylist)
        }
    }

    fun setAdapter(
        filterState: String,
        recyclerView: RecyclerView
    ) {
        val filterDocument = mutableListOf<FilterStore>()
        val filterDataset = FinalStoreDataModel(filterDocument)

        if( filterState != "all" ) {
            filterStoreData(filterDocument, filterState)
            recyclerView.adapter = StoreListAdapter(filterDataset, context)
        } else {
            recyclerView.adapter = StoreListAdapter(myDataset, context)
        }
    }

    private fun filterStoreData(
        filterDocument: MutableList<FilterStore>,
        filterState: String
    ) {
        for ( i in myDataset.filterStore ) {
            if ( i.document.storeTitle == filterState ) {
                filterDocument.add(i)
            }
        }
    }

}
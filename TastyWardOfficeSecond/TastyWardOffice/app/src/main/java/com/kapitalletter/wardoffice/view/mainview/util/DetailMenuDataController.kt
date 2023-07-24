package com.kapitalletter.wardoffice.view.mainview.util

import com.kapitalletter.wardoffice.datamodel.FilterStore
import com.kapitalletter.wardoffice.view.mainview.viewmodel.OverviewViewModel

class DetailMenuDataController {

    fun detailItemData(
        overViewModel: OverviewViewModel,
        dogId: String
    ) : FilterStore {
        val passingData = overViewModel.distanceStoreData.value!!.Filterstore
        for( filterStore in passingData ) {
            if( filterStore.document.docId == dogId ) {
                return filterStore
            }
        }
        return passingData[0]
    }

}
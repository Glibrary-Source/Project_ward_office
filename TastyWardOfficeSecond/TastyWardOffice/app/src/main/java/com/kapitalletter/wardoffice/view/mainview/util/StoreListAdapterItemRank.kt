package com.kapitalletter.wardoffice.view.mainview.util

import androidx.appcompat.view.menu.MenuView
import com.kapitalletter.wardoffice.datamodel.FilterStore
import com.kapitalletter.wardoffice.view.mainview.adapter.StoreListAdapter

class StoreListAdapterItemRank() {

    fun setRankText(
        item: FilterStore,
        holder: StoreListAdapter.ItemViewHolder
    ) {
        if (item.document.storeCntLikes >= 300) {
            holder.rankText.text = "1급"
        } else if (item.document.storeCntLikes >= 250) {
            holder.rankText.text = "2급"
        } else if (item.document.storeCntLikes >= 200) {
            holder.rankText.text = "3급"
        } else if (item.document.storeCntLikes >= 150) {
            holder.rankText.text = "4급"
        } else if (item.document.storeCntLikes >= 100) {
            holder.rankText.text = "5급"
        } else if (item.document.storeCntLikes >= 50) {
            holder.rankText.text = "6급"
        } else if (item.document.storeCntLikes >= 40) {
            holder.rankText.text = "7급"
        } else if (item.document.storeCntLikes >= 10) {
            holder.rankText.text = "8급"
        } else {
            holder.rankText.text = "9급"
        }
    }

}
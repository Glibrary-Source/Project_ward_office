package com.kapitalletter.wardoffice.view.mainview.util

import androidx.viewpager2.widget.ViewPager2
import com.kapitalletter.wardoffice.databinding.FragmentDetailMenuBinding
import com.kapitalletter.wardoffice.datamodel.FilterStore

class ViewPagerController {

    fun getDetailPageFragmentOnPageChangeCallback (
        storeDetailData: FilterStore,
        binding: FragmentDetailMenuBinding
    ): ViewPager2.OnPageChangeCallback {
        return object : ViewPager2.OnPageChangeCallback() {

            var currentState = 0
            var currentPos = 0

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                val pictureListSize: Int =
                    if (storeDetailData.document.storeMenuPictureUrls.menu.isEmpty()) {
                        3
                    } else {
                        storeDetailData.document.storeMenuPictureUrls.menu.size
                    }
                if (currentState == ViewPager2.SCROLL_STATE_DRAGGING && currentPos == position) {
                    if (currentPos == 0) {
                        binding.viewPager2.currentItem = pictureListSize - 1
                    } else if (currentPos == pictureListSize - 1) {
                        binding.viewPager2.currentItem = 0
                    }
                }
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                currentPos = position
                super.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                currentState = state
                super.onPageScrollStateChanged(state)
            }
        }
    }

}
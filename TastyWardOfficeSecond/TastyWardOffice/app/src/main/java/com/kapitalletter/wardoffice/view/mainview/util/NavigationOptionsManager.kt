package com.kapitalletter.wardoffice.view.mainview.util

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.kapitalletter.wardoffice.R

class NavigationOptionsManager(private val navController: NavController) {

    fun setBottomTransformOption(): NavOptions {
        return NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setEnterAnim(R.anim.enter_from_right)
            .setExitAnim(R.anim.exit_to_right)
            .setPopEnterAnim(R.anim.enter_from_right)
            .setPopExitAnim(R.anim.exit_to_right)
            .setPopUpTo(navController.graph.startDestinationId, false)
            .build()
    }

}
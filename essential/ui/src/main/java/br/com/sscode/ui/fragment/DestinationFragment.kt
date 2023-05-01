package br.com.sscode.ui.fragment

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController

/**
 * @see DestinationFragment
 * Created with purpose of center the Fragment responsibility with Navigation.
 * */
abstract class DestinationFragment(
    @IdRes val destinationId: Int
) : Fragment() {

    /**
     * Check if the current Navigation stack still contains the destination.
     * Can be useful to avoid duplicate navigation [Exception].
     * */
    protected fun isCurrentDestination(navController: NavController) = with(navController) {
        currentDestination?.id == destinationId
    }
}
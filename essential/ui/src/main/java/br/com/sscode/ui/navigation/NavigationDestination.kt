package br.com.sscode.ui.navigation

import androidx.annotation.IdRes

interface NavigationDestination {

    @IdRes
    fun getDestinationId(): Int
}

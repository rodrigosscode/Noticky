package br.com.sscode.ui.extension

import androidx.navigation.NavController
import br.com.sscode.ui.navigation.NavigationDestination

fun NavigationDestination.isCurrentDestination(navController: NavController) = with(navController) {
    currentDestination?.id == getDestinationId()
}

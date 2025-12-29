package eryaz.software.carParts.util.extensions

import androidx.navigation.NavController
import androidx.navigation.NavDirections

fun NavController.navigateSafely(directions: NavDirections) {
    currentDestination?.getAction(directions.actionId)?.let { navigate(directions) }
}
package eryaz.software.carParts.ui

import android.net.Uri
import eryaz.software.carParts.util.SingleLiveEvent

object EventBus {
    val navigateToSplash = SingleLiveEvent<Boolean>()
    val navigateWithDeeplink = SingleLiveEvent<Uri?>()
}

package eryaz.software.carParts.util
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent

class KeyboardEventListener(
    val activity: AppCompatActivity,
    private val root: View,
    private val resizeableView: View,
    private val bottomView: View?
) : LifecycleEventObserver, ViewTreeObserver.OnGlobalLayoutListener {

    var onKeyboardChanged: ((isOpen: Boolean, keyboardHeight: Int) -> Unit)? = null

    init {
        activity.lifecycle.addObserver(this)

//        KeyboardVisibilityEvent.setEventListener(activity) {
//            if (!it)
//                activity.currentFocus?.clearFocus()
//        }

        onGlobalLayout()
    }

    override fun onGlobalLayout() {
        if (KeyboardVisibilityEvent.isKeyboardVisible(activity)) {
            val r = Rect()
            root.getWindowVisibleDisplayFrame(r)

            val bottomViewHeight = if (bottomView?.isVisible == true) bottomView.height else 0

            val height: Int = root.height
            val diff = height - r.bottom - bottomViewHeight

            onKeyboardChanged?.invoke(true, diff)
            resizeableView.setPadding(0, 0, 0, diff)
        } else {
            onKeyboardChanged?.invoke(false, 0)
            resizeableView.setPadding(0, 0, 0, 0)
        }
    }

    private fun register() {
        root.viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    private fun unRegister() {
        onGlobalLayout()
        root.viewTreeObserver.removeOnGlobalLayoutListener(this)
    }


    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_START -> register()
            Lifecycle.Event.ON_STOP -> unRegister()
            else -> {
            }
        }
    }
}
package eryaz.software.carParts.util.bindingAdapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.LayoutTransition
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import androidx.databinding.BindingAdapter

@BindingAdapter("animateLayoutChanges")
fun ViewGroup.animateLayoutChanges(boolean: Boolean) {
    val lt = LayoutTransition()
    lt.enableTransitionType(LayoutTransition.CHANGING)
    lt.setAnimateParentHierarchy(false)
    lt.setDuration(300)

    layoutTransition = lt
}

@BindingAdapter("enterReveal")
fun View.enterReveal(boolean: Boolean?) {
    if (boolean == true) {
        post {
            try {
                val cx: Int = measuredWidth / 2;
                val cy: Int = measuredHeight / 2

                val finalRadius: Int = width.coerceAtLeast(height) / 2

                val anim: Animator = ViewAnimationUtils
                    .createCircularReveal(this, cx, cy, 0f, finalRadius.toFloat())

                alpha = 1f

                isClickable = true
                isFocusable = true

                anim.start()
            } catch (e: Exception) {

            }
        }
    }
}

@BindingAdapter("exitReveal")
fun View.exitReveal(boolean: Boolean?) {
    if (boolean == true) {
        post {
            try {
                val cx: Int = measuredWidth / 2;
                val cy: Int = measuredHeight / 2

                val initialRadius = width / 2

                val anim = ViewAnimationUtils
                    .createCircularReveal(this, cx, cy, initialRadius.toFloat(), 0f)

                anim.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)

                        isClickable = false
                        isFocusable = false
                        alpha = 0f
                    }
                })

                anim.start()
            } catch (e: Exception) {
            }
        }
    }
}


@BindingAdapter("animateScaleXY")
fun View.animateScaleXY(boolean: Boolean?) {
    if (boolean == true) {
        scaleX = 0f
        scaleY = 0f

        animate()
            .scaleX(1f)
            .scaleY(1f)
            .duration = 300
    } else {
        animate()
            .scaleX(0f)
            .scaleY(0f)
            .duration = 300
    }
}
package eryaz.software.carParts.util.extensions

import android.animation.Animator
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withStateAtLeast
import com.google.android.material.snackbar.Snackbar
import eryaz.software.carParts.databinding.SnackbarLottieBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun Fragment.appCompatActivity(): AppCompatActivity {
    return activity as AppCompatActivity
}

fun Fragment.supportActionBar(): ActionBar? {
    return appCompatActivity().supportActionBar
}

fun Fragment.onBackPressedCallback(onBackPressed: () -> Unit) {
    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        })
}

fun Fragment.toast(message: Any?) {
    context?.toast(message)
}

fun Fragment.getDrawable(@DrawableRes resId: Int): Drawable? {
    context?.let {
        return ContextCompat.getDrawable(it, resId)
    }

    return null
}

inline fun <reified T : Fragment> newInstance(vararg params: Pair<String, Any?>) =
    T::class.java.newInstance().apply {
        arguments = bundleOf(*params)
    }


fun Fragment.hideSoftKeyboard() {
    activity?.hideSoftKeyboard()
}

@SuppressLint("RestrictedApi")
fun Fragment.showLottieAnimation(@RawRes resId: Int) {
    activity?.let {
        val binding = SnackbarLottieBinding.inflate(layoutInflater)
        binding.lottie.setAnimation(resId)

        val snackBar =
            Snackbar.make(it.findViewById(android.R.id.content), "", Snackbar.LENGTH_INDEFINITE)
                .apply {
                    val layout = view as Snackbar.SnackbarLayout

                    val params = layout.layoutParams as FrameLayout.LayoutParams
                    params.gravity = Gravity.CENTER
                    layout.layoutParams = params
                    layout.setBackgroundColor(Color.TRANSPARENT)
                    layout.addView(binding.root, 0)

                    show()
                }

        binding.lottie.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(animation: Animator) {
                binding.lottie
                    .animate()
                    .scaleX(0f)
                    .scaleY(0f).setListener(object : Animator.AnimatorListener {
                        override fun onAnimationEnd(animation: Animator) {
                            snackBar.dismiss()
                        }

                        override fun onAnimationStart(animation: Animator) {}
                        override fun onAnimationCancel(animation: Animator) {}
                        override fun onAnimationRepeat(animation: Animator) {}
                    })
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
            override fun onAnimationStart(animation: Animator) {}
        })
    }
}

fun Fragment.launchWhen(
    state: Lifecycle.State,
    context: CoroutineContext = EmptyCoroutineContext,
    block: (suspend CoroutineScope.() -> Unit)
) {
    lifecycleScope.launch {
        val job = launch(context, start = CoroutineStart.LAZY, block)
        withStateAtLeast(state) { job.start() }
    }
}
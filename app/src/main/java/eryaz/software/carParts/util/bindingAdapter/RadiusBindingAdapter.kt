package eryaz.software.carParts.util.bindingAdapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.databinding.BindingAdapter
import eryaz.software.carParts.util.extensions.dpToPx


@BindingAdapter("radius")
fun createShapeAll(view: View, radius: Int = 0) {
    createShape(view, radius, radius, radius, radius)
}

@BindingAdapter("radiusTop")
fun createShapeTop(view: View, radiusTop: Int) {
    createShape(view, radiusTop, radiusTop, 0, 0)
}

fun createShape(
    view: View,
    topLeft: Int = 0,
    topRight: Int = 0,
    bottomLeft: Int = 0,
    bottomRight: Int = 0
) {
    var color: Int = Color.WHITE
    val background = view.background
    if (background is ColorDrawable) color = background.color

    val shape = GradientDrawable()
    shape.shape = GradientDrawable.RECTANGLE
    shape.setColor(color)
    shape.cornerRadii = floatArrayOf(
        topLeft.dpToPx().toFloat(),
        topLeft.dpToPx().toFloat(),
        topRight.dpToPx().toFloat(),
        topRight.dpToPx().toFloat(),
        bottomLeft.dpToPx().toFloat(),
        bottomLeft.dpToPx().toFloat(),
        bottomRight.dpToPx().toFloat(),
        bottomRight.dpToPx().toFloat()
    )

    view.background = shape
}


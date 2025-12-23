package eryaz.software.carParts.util.widgets

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import eryaz.software.carParts.R
import eryaz.software.carParts.databinding.WidgetToolbarBinding
import eryaz.software.carParts.util.bindingAdapter.setOnSingleClickListener

class Toolbar(context: Context, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        WidgetToolbarBinding.inflate(LayoutInflater.from(context), this)
    }

    private fun initAttrs(attrs: AttributeSet?) {
        context.resources.obtainAttributes(attrs, R.styleable.Toolbar).apply {
            setTitle(getString(R.styleable.Toolbar_title))
            setSubTitle(getString(R.styleable.Toolbar_subTitle))
            setNavigationIcon(getDrawable(R.styleable.Toolbar_navigationIcon))
            setEndIconDrawable(getDrawable(R.styleable.Toolbar_endIconDrawable))

            recycle()
        }
    }

    fun setTitle(title: String?) {
        binding.titleTxt.text = title
    }

    fun setSubTitle(subTitle: String?) {
        binding.subTitle.text = subTitle
    }

    fun setNavigationIcon(drawable: Drawable?) {
        binding.startIconBtn.setImageDrawable(drawable)
        binding.startIconBtn.isVisible = drawable != null
    }

    fun setEndIconDrawable(drawable: Drawable?) {
        binding.endIconBtn.setImageDrawable(drawable)
        binding.endIconBtn.isVisible = drawable != null
    }

    fun setNavigationOnClickListener(listener: OnClickListener) {
        binding.startIconBtn.setOnSingleClickListener(listener)
    }

    fun setMenuOnClickListener(listener: OnClickListener) {
        binding.endIconBtn.setOnSingleClickListener(listener)
    }

    init {
        initAttrs(attrs)
    }
}


package eryaz.software.carParts.util.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.google.android.material.card.MaterialCardView
import eryaz.software.carParts.R
import eryaz.software.carParts.databinding.WidgetSettingsItemBinding

class SettingsItem(context: Context?, attrs: AttributeSet?) :
    MaterialCardView(context, attrs) {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        WidgetSettingsItemBinding.inflate(LayoutInflater.from(context), this)
    }

    init {
        initAttrs(attrs)
    }

    private fun initAttrs(attrs: AttributeSet?) {
        context.resources.obtainAttributes(attrs, R.styleable.SettingsItem).apply {
            setTitle(getString(R.styleable.SettingsItem_title))
            setValue(getString(R.styleable.SettingsItem_value))

            recycle()
        }
    }

    fun setTitle(title: String?) {
        binding.titleTxt.text = title
    }

    fun setValue(value: String?) {
        binding.valueTxt.text = value
    }
}
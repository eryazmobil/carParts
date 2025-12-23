package eryaz.software.carParts.util.widgets

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.util.AttributeSet
import android.view.LayoutInflater
import com.google.android.material.card.MaterialCardView
import eryaz.software.carParts.R
import eryaz.software.carParts.databinding.WidgetEditQuantityBinding

class EditQuantityView(context: Context?, attrs: AttributeSet?) :
    MaterialCardView(context, attrs) {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        WidgetEditQuantityBinding.inflate(LayoutInflater.from(context), this)
    }

    init {
        initAttrs(attrs)
    }

    private fun initAttrs(attrs: AttributeSet?) {
        context.resources.obtainAttributes(attrs, R.styleable.EditQuantityView).apply {
            setDetailTitle(getString(R.styleable.EditQuantityView_editTitle))
            setMultiplier(getString(R.styleable.EditQuantityView_multiplier))
            setColor(getColor(R.styleable.EditQuantityView_changeTitleColor, Color.BLACK))
            getQuantity(getString(R.styleable.EditQuantityView_quantity))
            recycle()
        }
    }

    fun setDetailTitle(title: String?) {
        binding.titleTxt.text = title
    }

    fun setMultiplier(value: String?) {
        binding.multiplierTxt.text = value
    }

    fun setColor(color: Int) {
        binding.titleTxt.setTextColor(color)
    }

    fun getQuantity(quantity:String?) {
        binding.quantityEdt.text = Editable.Factory.getInstance().newEditable(quantity)
    }

}
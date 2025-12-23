package eryaz.software.carParts.util.widgets

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import com.google.android.material.card.MaterialCardView
import eryaz.software.carParts.R
import eryaz.software.carParts.databinding.WidgetDetailRowItemBinding

class DetailRowTextView(context: Context?, attrs: AttributeSet?) :
    MaterialCardView(context, attrs) {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        WidgetDetailRowItemBinding.inflate(LayoutInflater.from(context), this)
    }

    init {
        initAttrs(attrs)
    }

    private fun initAttrs(attrs: AttributeSet?) {
        context.resources.obtainAttributes(attrs, R.styleable.DetailRowTextView).apply {
            setDetailTitle(getString(R.styleable.DetailRowTextView_detailTitle))
            setCreationTime(getString(R.styleable.DetailRowTextView_creationTime))
            setDocumentNo(getString(R.styleable.DetailRowTextView_documentNo))
            setClientName(getString(R.styleable.DetailRowTextView_clientName))
            setColor(getColor(R.styleable.DetailRowTextView_changeColor, Color.BLACK))

            recycle()
        }
    }

    fun setDetailTitle(title: String?) {
        binding.titleTxt.text = title
    }

    fun setCreationTime(value: String?) {
        binding.dateTxt.text = value
    }

    fun setDocumentNo(value: String?) {
        binding.documentNoTxt.text = value
    }

    fun setClientName(value: String?) {
        binding.clientNameTxt.text = value
    }

    fun setColor(color: Int) {
        binding.titleTxt.setTextColor(color)
        binding.dateTxt.setTextColor(color)
        binding.documentNoTxt.setTextColor(color)
        binding.clientNameTxt.setTextColor(color)
    }

}
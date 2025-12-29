package eryaz.software.carParts.util.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import eryaz.software.carParts.R
import eryaz.software.carParts.data.enums.IconType
import eryaz.software.carParts.databinding.DialogQuestionMessageBinding
import eryaz.software.carParts.ui.base.BaseDialogFragment
import eryaz.software.carParts.util.bindingAdapter.setOnSingleClickListener
import eryaz.software.carParts.util.extensions.getColorInt
import eryaz.software.carParts.util.extensions.isNetworkAvailable

class QuestionDialog(
    private val onPositiveClickListener: (() -> Unit) = {},
    private val onNegativeClickListener: (() -> Unit) = {},
    private val textHeader: String?,
    private val textMessage: String?,
    private val positiveBtnText: String = "",
    private val negativeBtnText: String = "",
    private val singleBtnText: String = "",
    private val negativeBtnViewVisible: Boolean,
    private val icType: Int
) : BaseDialogFragment() {

    private var _binding: DialogQuestionMessageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogQuestionMessageBinding.inflate(layoutInflater)

        setupUI()
        setupClickListeners()
        setupIconAndColors()

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        val dialog = builder.create()
        dialog.window?.setGravity(Gravity.CENTER)

        isCancelable = false

        return dialog
    }

    private fun setupUI() {
        binding.txtDialogHeader.text = textHeader
        binding.txtDialogMessage.text = textMessage
        binding.btnDialogPositive.text = positiveBtnText
        binding.btnDialogNegative.text = negativeBtnText
        binding.btnDialogSingle.text = singleBtnText

        if (negativeBtnViewVisible) {
            binding.btnDialogNegative.visibility = View.VISIBLE
            binding.btnDialogSingle.visibility = View.GONE
        } else {
            binding.btnDialogNegative.visibility = View.GONE
            binding.btnDialogSingle.visibility = View.VISIBLE
        }

        if (!requireContext().isNetworkAvailable()) {
            binding.txtDialogMessage.setText(R.string.in_error_dialog_no_internet_connection)
        }
    }

    private fun setupClickListeners() {
        binding.btnDialogPositive.setOnSingleClickListener {
            onPositiveClickListener.invoke()
            dismiss()
        }

        binding.btnDialogNegative.setOnSingleClickListener {
            onNegativeClickListener.invoke()
            dismiss()
        }

        binding.btnDialogSingle.setOnSingleClickListener {
            dismiss()
        }
    }

    private fun setupIconAndColors() {
        val context = requireContext()
        when (icType) {
            IconType.Danger.ordinal -> {
                binding.icDialogStatus.setImageResource(R.drawable.ic_danger)
                binding.txtDialogHeader.setTextColor(context.getColorInt(R.color.colorDangerRed))
            }
            IconType.Warning.ordinal -> {
                binding.icDialogStatus.setImageResource(R.drawable.ic_picking_warning)
                binding.txtDialogHeader.setTextColor(context.getColorInt(R.color.colorPrimaryYellow))
            }
            IconType.Success.ordinal -> {
                binding.icDialogStatus.setImageResource(R.drawable.ic_done)
                binding.txtDialogHeader.setTextColor(context.getColorInt(R.color.colorSuccessGreen))
            }
        }
        binding.btnDialogPositive.setBackgroundColor(context.getColorInt(R.color.colorPrimaryBoldBlue))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
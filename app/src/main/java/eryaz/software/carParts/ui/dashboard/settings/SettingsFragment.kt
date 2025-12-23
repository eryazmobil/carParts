package eryaz.software.carParts.ui.dashboard.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import eryaz.software.carParts.data.models.dto.CompanyDto
import eryaz.software.carParts.data.models.dto.WarehouseDto
import eryaz.software.carParts.databinding.FragmentSettingsBinding
import eryaz.software.carParts.ui.base.BaseFragment
import eryaz.software.carParts.ui.dashboard.settings.changeLanguage.LanguageFragment
import eryaz.software.carParts.ui.dashboard.settings.companies.CompanyListDialog
import eryaz.software.carParts.ui.dashboard.settings.warehouses.WarehouseListDialog
import eryaz.software.carParts.util.bindingAdapter.setOnSingleClickListener
import eryaz.software.carParts.util.extensions.observe
import eryaz.software.carParts.util.extensions.parcelable
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : BaseFragment() {
    override val viewModel by viewModel<SettingsViewModel>()

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentSettingsBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()
        return binding.root
    }

    override fun setClicks() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        viewModel.companyList.observe(this) { companyList ->
            binding.companyItem.setOnSingleClickListener {
                viewModel.checkCompany.observe(this) {
                    if (!it) {
                        findNavController().navigate(
                           SettingsFragmentDirections.actionSettingsFragmentToCompanyListDialog(
                                companyList.toTypedArray()
                            )
                        )
                    }
                }
            }
        }

        viewModel.warehouseList.observe(this) { warehouseList ->
            binding.warehouseItem.setOnSingleClickListener {
                viewModel.checkWarehouse.observe(this) {
                    if (!it) {
                        findNavController().navigate(
                           SettingsFragmentDirections.actionSettingsFragmentToWarehouseListDialog(
                                warehouseList.toTypedArray()
                            )
                        )
                    }
                }
            }
        }

        binding.appVersionUpdate.setOnSingleClickListener {

               viewModel.startVersionService(requireContext())
        }

        binding.changePassword.setOnSingleClickListener {
            findNavController().navigate(
               SettingsFragmentDirections.actionSettingFragmentToPasswordDialog()
            )
        }

        binding.appLanguage.setOnSingleClickListener {
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToLanguageFragment())
        }

    }

    override fun subscribeToObservables() {

        setFragmentResultListener(CompanyListDialog.REQUEST_KEY) { _, bundle ->
            val dto = bundle.parcelable<CompanyDto>(CompanyListDialog.ARG_COMPANY_DTO)
            dto?.let {
                viewModel.setCompany(it)
            }
        }

        setFragmentResultListener(WarehouseListDialog.REQUEST_KEY) { _, bundle ->
            val dto = bundle.parcelable<WarehouseDto>(WarehouseListDialog.ARG_COMPANY_DTO)
            dto?.let {
                viewModel.setWarehouse(it)
            }
        }

        setFragmentResultListener(LanguageFragment.LANGUAGE_FRAGMENT_TAG) { _, bundle ->
            bundle.getString(LanguageFragment.LANGUAGE_FRAGMENT_KEY)?.let {
                viewModel.setLanguage(it)
            }
        }

    }
}
package eryaz.software.carParts.ui.dashboard

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import eryaz.software.carParts.R
import eryaz.software.carParts.data.models.dto.DashboardItemDto
import eryaz.software.carParts.data.api.utils.onSuccess
import eryaz.software.carParts.data.enums.DashboardPermissionType
import eryaz.software.carParts.data.models.dto.CurrentUserDto
import eryaz.software.carParts.data.persistence.SessionManager
import eryaz.software.carParts.data.repositories.UserRepo
import eryaz.software.carParts.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class DashboardViewModel(private val repo: UserRepo) : BaseViewModel() {
    private val _dashboardItemList = MutableLiveData<List<DashboardItemDto>>(emptyList())
    val dashboardItemList: LiveData<List<DashboardItemDto>> = _dashboardItemList

    private val _currentUserDto = MutableStateFlow<CurrentUserDto?>(null)

    init {
        getCurrentLoginHasPermissionsForPDAMenu()
        loadList()
        getCurrentLoginInformations()
    }

    private fun loadList() {
        _dashboardItemList.value?.toMutableList()?.apply {
            add(
                DashboardItemDto(
                    iconRes = R.drawable.inbound,
                    titleRes = R.string.main_menu_item_1,
                    type = DashboardPermissionType.INBOUND,
                    hasPermission = ObservableField(false)
                )
            )
            add(
                DashboardItemDto(
                    iconRes = R.drawable.outbound,
                    titleRes = R.string.main_menu_item_2,
                    type = DashboardPermissionType.OUTBOUND,
                    hasPermission = ObservableField(false)
                )
            )
            add(
                DashboardItemDto(
                    iconRes = R.drawable.delivery,
                    titleRes = R.string.main_menu_item_3,
                    type = DashboardPermissionType.MOVEMENT,
                    hasPermission = ObservableField(false)
                )
            )
            add(
                DashboardItemDto(
                    iconRes = R.drawable.recording,
                    titleRes = R.string.main_menu_item_4,
                    type = DashboardPermissionType.RECORDING,
                    hasPermission = ObservableField(false)
                )
            )
            add(
                DashboardItemDto(
                    iconRes = R.drawable.return_icon,
                    titleRes = R.string.main_menu_item_5,
                    type = DashboardPermissionType.RETURNING,
                    hasPermission = ObservableField(false)
                )
            )
            add(
                DashboardItemDto(
                    iconRes = R.drawable.counting,
                    titleRes = R.string.main_menu_item_6,
                    type = DashboardPermissionType.COUNTING,
                    hasPermission = ObservableField(false)
                )
            )
            add(
                DashboardItemDto(
                    iconRes = R.drawable.smart_factory,
                    titleRes = R.string.main_menu_item_7,
                    type = DashboardPermissionType.PRODUCTION,
                    hasPermission = ObservableField(false)
                )
            )
            add(
                DashboardItemDto(
                    iconRes = R.drawable.query,
                    titleRes = R.string.main_menu_item_8,
                    type = DashboardPermissionType.QUERY,
                    hasPermission = ObservableField(false)
                )
            )
//            add(
//                DashboardItemDto(
//                    iconRes = R.drawable.settings,
//                    titleRes = R.string.main_menu_item_9,
//                    type = DashboardPermissionType.SETTING,
//                    hasPermission = ObservableField(false)
//                )
//            )

            _dashboardItemList.value = this
        }
    }

    private fun getCurrentLoginHasPermissionsForPDAMenu() =
        executeInBackground(_uiState) {
            repo.getCurrentLoginHasPermissionsForPDAMenu().onSuccess { permissionList ->
                _dashboardItemList.value?.forEach {
                    it.hasPermission.set(permissionList.contains(it.type.permission) || it.type
                            == DashboardPermissionType.SETTING)
                }
            }
        }

    private fun getCurrentLoginInformations() =
        executeInBackground {
            repo.getCurrentLoginInformations().onSuccess {
                _currentUserDto.emit(it)

                SessionManager.companyId = it.companyId ?: 0
                SessionManager.warehouseId = it.warehouseId ?: 0
                SessionManager.userId = it.userId
            }
        }


}
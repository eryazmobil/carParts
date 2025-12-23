package eryaz.software.carParts.data.enums


enum class DashboardPermissionType(val permission: String){
    INBOUND("Pages.PDA.Inbound"),
    OUTBOUND("Pages.PDA.Outbound"),
    MOVEMENT("Pages.PDA.Movement"),
    RECORDING("Pages.PDA.Recording"),
    RETURNING("Pages.PDA.Return"),
    COUNTING("Pages.PDA.StockTaking"),
    PRODUCTION("Pages.PDA.Production"),
    QUERY("Pages.PDA.Query"),
    SETTING("")
}
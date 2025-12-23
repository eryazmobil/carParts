package eryaz.software.carParts.data.enums

enum class DashboardDetailPermissionType(val permission: String) {

    //Kabul detaylarnın izinler
    ACCEPTANCE("Pages.PDA.Inbound.Acceptance"),
    PLACEMENT("Pages.PDA.Inbound.Placement"),
    CROSSDOCK("Pages.PDA.Inbound.Crossdock"),
    DATACCEPTANCE("Pages.PDA.Inbound.DATAcceptance"),
    DATPLACEMENT("Pages.PDA.Inbound.DATPlacement"),

    //Sevk detaylarnın izinler
    ORDERPICKING("Pages.PDA.Outbound.Order"),
    CROSSDOCKTRANSFER("Pages.PDA.Outbound.Crossdock"),
    CONTROLPOINT("Pages.PDA.Outbound.ControlPoint"),
    DATPICKING("Pages.PDA.Outbound.DATPicking"),
    DATCONTROL("Pages.PDA.Outbound.DATPicking"),

    //Hareket detaylarnın izinler
    TRANSFERWAREHOUSE("Pages.PDA.Movement.Warehouse"),
    TRANSFERSHELF("Pages.PDA.Movement.Shelf"),
    TRANSFERSHELFALL("Pages.PDA.Movement.ShelfAll"),
    REPLENISHMENT("Pages.PDA.Movement.Replenishment"),
    STOCKORRECTION("Pages.PDA.Movement.StockCorrection"),
    ROUTE(""),

    //Kayıt detaylarnın izinler
    VARIETY("Pages.PDA.Recording.Variety"),
    BARCODEREGISTERATION("Pages.PDA.Recording.Barcode"),

    //SAYIM
    FIRSTWAREHOUSECOUNTING("Pages.PDA.StockTaking.CountingModule"),
    FASTWAREHOUSECOUNTING("Pages.PDA.StockTaking.CountingModule"),
    PARTIALWAREHOUSECOUNTING("Pages.PDA.StockTaking.CountingModule"),

    //SORGU
    PRODUCTQUERY("Pages.PDA.Query.ProductQuery"),
    SHELFQUERY("Pages.PDA.Query.ShelfQuery"),
}
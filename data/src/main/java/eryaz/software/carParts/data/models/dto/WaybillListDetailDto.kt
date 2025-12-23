package eryaz.software.carParts.data.models.dto

data class WaybillListDetailDto(
    val product: ProductDto,
    val quantity: Int,
    val quantityOrder: Int,
    val quantityPlaced: Int,
    val quantityControlled: String,
    val id: Int
) {
    fun getQuantityForPlacement(quantityControlled: Int, quantity: Int): Int {
        return when {
            quantityControlled == 0 -> 0
            quantityControlled > quantity -> quantity
            else -> quantityControlled
        }
    }

    fun getQuantityForPlacementRemaining(
        quantityControlled: Int,
        quantity: Int,
        quantityPlaced: Int
    ): Int {
        return getQuantityForPlacement(quantityControlled, quantity) - quantityPlaced
    }

}

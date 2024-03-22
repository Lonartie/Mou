package com.team.app.data.model

data class InventoryItem (
    val item: Item,
    val quantity: Int
)

data class Inventory(
    val inventoryItems: List<InventoryItem>
)

package com.team.app.ui.shop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.app.data.database.model.Item
import com.team.app.data.repositories.AttributesRepository
import com.team.app.data.repositories.InventoryRepository
import com.team.app.data.repositories.ItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    private val inventoryRepo: InventoryRepository,
    private val itemsRepo: ItemsRepository,
    private val attributesRepo: AttributesRepository
) : ViewModel() {
    var itemsFlow = itemsRepo.getItemsFlow()
        private set

    var attributesFlow = attributesRepo.getAttributesFlow()
        private set

    fun buyItem(item: Item) {
        viewModelScope.launch {
            inventoryRepo.addOne(item)
        }
    }
}
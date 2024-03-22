package com.team.app.ui.shop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.app.data.database.model.Item
import com.team.app.data.model.ItemType
import com.team.app.data.repositories.InventoryRepository
import com.team.app.data.repositories.ItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    private val inventoryRepository: InventoryRepository,
    private val itemsRepository: ItemsRepository,
) : ViewModel() {
    var itemsFlow = itemsRepository.getItemsFlow()
        private set

    fun buyItem(item: Item) {
        viewModelScope.launch {
        }
    }
}
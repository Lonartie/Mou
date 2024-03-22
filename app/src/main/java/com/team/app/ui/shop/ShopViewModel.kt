package com.team.app.ui.shop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.app.data.database.model.OwnedItem
import com.team.app.data.model.ItemType
import com.team.app.data.repositories.ShopRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    private val shopRepository: ShopRepository
) : ViewModel() {
    fun buyItem(itemType: ItemType) {
        viewModelScope.launch {
            val newItem = OwnedItem(type = itemType)
            shopRepository.buyItem(newItem)
        }
    }
}
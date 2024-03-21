package com.team.app.ui.shop

import androidx.lifecycle.ViewModel
import com.team.app.data.repositories.ShopRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    private val inventoryRepository: ShopRepository
) : ViewModel() {

}
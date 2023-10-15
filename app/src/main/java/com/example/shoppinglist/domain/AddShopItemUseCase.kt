package com.example.shoppinglist.domain

class AddShopItemUseCase(private val shopListRepository: ShopListRepository) {

    suspend fun addItem(shopItem: ShopItem) {
        shopListRepository.addShopItem(shopItem)
    }

}
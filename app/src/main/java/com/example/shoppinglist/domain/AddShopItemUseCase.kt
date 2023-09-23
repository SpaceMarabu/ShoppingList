package com.example.shoppinglist.domain

class AddShopItemUseCase(private val shopListRepository: ShopListRepository) {

    fun addObject(shopItem: ShopItem) {
        shopListRepository.addShopItem(shopItem)
    }

}
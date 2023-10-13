package com.example.shoppinglist.presentation

import android.view.View
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.databinding.ItemShopDisabledBinding

//     класс, который содержит ссылки на заполняемые объекты в созданной вью из макета для адаптера
class ShopItemViewHolder(
    val binding: ViewDataBinding
    ) : RecyclerView.ViewHolder(binding.root) {
}
package com.example.shoppinglist.presentation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

//     класс, который содержит ссылки на заполняемые объекты в созданной вью из макета для адаптера
class ShopItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvName = view.findViewById<TextView>(com.example.shoppinglist.R.id.tv_name)
    val tvCount = view.findViewById<TextView>(com.example.shoppinglist.R.id.tv_count)
}
package com.example.shoppinglist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import com.example.shoppinglist.databinding.ItemShopDisabledBinding
import com.example.shoppinglist.databinding.ItemShopEnabledBinding
import com.example.shoppinglist.domain.ShopItem

//замена для использования более легкого ShopItemDiffCallback
//class ShopListAdapter : RecyclerView.Adapter<ShopListAdapter.ShopItemViewHolder>() {
class ShopListAdapter : ListAdapter<ShopItem, ShopItemViewHolder>(ShopItemDiffCallback()) {



//    var shopList = listOf<ShopItem>() тоже для ShopItemDiffCallback
//поскольку ListAdapter хранит в себе всю логику работы со списком

//        set(value) {
//            val callback = ShopListDiffCallback(shopList, value)
//            val diffResult = DiffUtil.calculateDiff(callback)   //собираю изменения в листе
//            diffResult.dispatchUpdatesTo(this)          //передаю изменения в адаптер
//            field = value
//        }

    //лямбда функция, которая хранится в переменной
    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    //    Расчет количества элементов в адаптере
    //не нужен с ListAdapter
//    override fun getItemCount(): Int {
//        return shopList.size
//    }

    //    создаем вью из макета
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val layoutResId: Int = if (viewType == VIEW_TYPE_ITEM_ENABLED) {
            com.example.shoppinglist.R.layout.item_shop_enabled
        } else {
            com.example.shoppinglist.R.layout.item_shop_disabled
        }
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            layoutResId,
            parent,
            false
        )
        return ShopItemViewHolder(binding)
    }

    //    наполняем видимые на экране вью контентом
    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopItem = getItem(position)
        val binding = holder.binding
        binding.root.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(shopItem)//вызов только если не null
            true
        }
        binding.root.setOnClickListener {
            onShopItemClickListener?.invoke(shopItem)
        }
        when (binding) {
            is ItemShopDisabledBinding -> {
                binding.tvName.text = shopItem.name
                binding.tvCount.text = shopItem.count.toString()
            }
            is ItemShopEnabledBinding -> {
                binding.tvName.text = shopItem.name
                binding.tvCount.text = shopItem.count.toString()
            }
        }
    }

//    получаем по позиции в листе тип элемента для создания вью
//    он полетит в onCreateViewHolder в качестве  viewType параметра
//    переопределять только если нужно использовать несколько типов вью
    override fun getItemViewType(position: Int): Int {
        val shopItem: ShopItem = getItem(position)
        return if (shopItem.enabled) {
            VIEW_TYPE_ITEM_ENABLED
        } else {
            VIEW_TYPE_ITEM_DISABLED
        }
    }
//    метод определяет как будут заполняться переиспользуемые элементы вьюхолдера, по умолчанию
//    override fun onViewRecycled(holder: ShopItemViewHolder) {
//        super.onViewRecycled(holder)
//        holder.tvName.text = ""
//        holder.tvCount.text = ""
//        holder.tvName.setTextColor(ContextCompat.getColor(
//            holder.itemView.context,
//            android.R.color.white
//        ))
//    }



    companion object {
        const val VIEW_TYPE_ITEM_ENABLED = 100
        const val VIEW_TYPE_ITEM_DISABLED = 200

        const val MAX_POOL_SIZE = 15
    }
}
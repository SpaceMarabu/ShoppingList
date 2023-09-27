package com.example.shoppinglist.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.domain.ShopItem


class ShopListAdapter : RecyclerView.Adapter<ShopListAdapter.ShopItemViewHolder>() {




    var shopList = listOf<ShopItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    //    Расчет количества элементов в адаптере
    override fun getItemCount(): Int {
        return shopList.size
    }

    //    создаем вью из макета
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val layoutResId: Int = if (viewType == VIEW_TYPE_ITEM_ENABLED) {
            com.example.shoppinglist.R.layout.item_shop_enabled
        } else {
            com.example.shoppinglist.R.layout.item_shop_disabled
        }
        val view = LayoutInflater.from(parent.context).inflate(
            layoutResId,
            parent,
            false
        )
        return ShopItemViewHolder(view)
    }

    //    наполняем видимые на экране вью контентом
    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopItem = shopList[position]
        holder.itemView.setOnLongClickListener({
            true
        })
        holder.tvName.text = shopItem.name
        holder.tvCount.text = shopItem.count.toString()
    }

//    получаем по позиции в листе тип элемента для создания вью
//    он полетит в onCreateViewHolder в качестве  viewType параметра
//    переопределять только если нужно использовать несколько типов вью
    override fun getItemViewType(position: Int): Int {
        val shopItem: ShopItem = shopList[position]
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

    //      внутренний класс, который содержит ссылки на заполняемые объекты в созданной вью из макета
    class ShopItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<TextView>(com.example.shoppinglist.R.id.tv_name)
        val tvCount = view.findViewById<TextView>(com.example.shoppinglist.R.id.tv_count)
    }

    companion object {
        const val VIEW_TYPE_ITEM_ENABLED = 100
        const val VIEW_TYPE_ITEM_DISABLED = 200

        const val MAX_POOL_SIZE = 15
    }
}
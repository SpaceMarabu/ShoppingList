package com.example.shoppinglist.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter: ShopListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.shopList.observe(this) {
//            shopListAdapter.shopList = it  не нужен с появлениемListAdapter
            shopListAdapter.submitList(it)
        }
        val intent: Intent = ShopItemActivity.newIntent(this)
        startActivity(intent)
    }


    private fun setupRecyclerView() {
        val rvShopList = findViewById<RecyclerView>(R.id.rv_shop_list)
        shopListAdapter = ShopListAdapter()
        rvShopList.adapter = shopListAdapter

//        устанавливаю максимальный размер пула сохраненных вьюхолдеров для каждого типа вью
//        размер задан руками константой в классе ShopListAdapter
        rvShopList.recycledViewPool.setMaxRecycledViews(
            ShopListAdapter.VIEW_TYPE_ITEM_ENABLED,
            ShopListAdapter.MAX_POOL_SIZE
        )
        rvShopList.recycledViewPool.setMaxRecycledViews(
            ShopListAdapter.VIEW_TYPE_ITEM_DISABLED,
            ShopListAdapter.MAX_POOL_SIZE
        )

        setupClickListeners()

        setupSwipeListener(rvShopList)

    }

    private fun setupSwipeListener(rvShopList: RecyclerView?) {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // this method is called
                // when the item is moved.
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // this method is called when we swipe our item to right direction.
                // on below line we are getting the item at a particular position.
                val item = shopListAdapter.currentList[viewHolder.adapterPosition]
                viewModel.removeShopItem(item)
            }
            // at last we are adding this
            // to our recycler view.
        }).attachToRecyclerView(rvShopList)
    }

    private fun setupClickListeners() {
        //        вызов лямбда функции из переменной созданной в адаптере
        shopListAdapter.onShopItemLongClickListener = {
            viewModel.changeEnableState(it)
        }

        shopListAdapter.onShopItemClickListener = {
            Log.d("MainActivity", it.toString())
        }
    }
}

package com.example.shoppinglist.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: ShopListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.shopList.observe(this) {
            adapter.shopList = it
        }
    }


    private fun setupRecyclerView() {
        val rvShopList = findViewById<RecyclerView>(R.id.rv_shop_list)
        adapter = ShopListAdapter()
        with(rvShopList) {
            this.adapter = adapter

//        устанавливаю максимальный размер пула сохраненных вьюхолдеров для каждого типа вью
//        размер задан руками константой в классе ShopListAdapter
            this.recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_ITEM_ENABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
            this.recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_ITEM_DISABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
        }
    }
}

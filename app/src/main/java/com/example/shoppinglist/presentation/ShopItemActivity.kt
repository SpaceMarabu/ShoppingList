package com.example.shoppinglist.presentation

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.example.shoppinglist.R


class ShopItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, ShopItemActivity::class.java)
        }
    }
}
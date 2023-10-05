package com.example.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem
import com.google.android.material.textfield.TextInputLayout
import java.lang.RuntimeException


class ShopItemActivity : AppCompatActivity() {

    private lateinit var viewModel: ShopItemViewModel
    private lateinit var tilName: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var etCount: EditText
    private lateinit var buttonSave: Button


    private var screenMode = MODE_UNKNOWN
    private var shopItemId = ShopItem.UNDEFINED_ID


    var name = ""
    var count = ""
    var errorInputName = false
    var errorInputCount = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        parseIntent()
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        initViews()
//        tilName.error = "Error"
//        etName.addTextChangedListener(object: TextWatcher {
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                TODO("Not yet implemented")
//            }
//
//            override fun afterTextChanged(p0: Editable?) {
//                TODO("Not yet implemented")
//            }
//        })
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun launchEditMode() {
        observeErrors()
        observeFinish()
        val itemId = intent.getStringExtra(EXTRA_SHOP_ITEM_ID)?.toInt()
        if (itemId != null) {
            viewModel.getShopItem(itemId)
        }
        viewModel.shopItem.observe(this){
            val oldName = it.name.toString()
            val oldCount = it.count.toString()
            etName.setText(oldName)
            etCount.setText(oldCount)
        }
        buttonSave.setOnClickListener {
            name = etName.text.toString()
            count = etCount.text.toString()
            viewModel.addShopItem(name, count)
            checkErrors()
        }

    }

    private fun launchAddMode() {
        observeErrors()
        observeFinish()
        buttonSave.setOnClickListener {
            name = etName.text.toString()
            count = etCount.text.toString()
            viewModel.addShopItem(name, count)
            checkErrors()
        }

    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Unknown screenmode $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("Param screen mode is absent")
            }
            shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    private fun initViews() {
        tilName = findViewById(R.id.til_name)
        tilCount = findViewById(R.id.til_count)
        etName = findViewById(R.id.et_name)
        etCount = findViewById(R.id.et_count)
        buttonSave = findViewById(R.id.save_button)
    }


    private fun observeErrors() {
        //подписываюсь на ошибки валидации полей
        viewModel.errorInputName.observe(this){
            errorInputName = it
        }
        viewModel.errorInputCount.observe(this){
            errorInputCount = it
        }
    }

    private fun observeFinish() {
        viewModel.shouldCloseScreen.observe(this) {
            finish()
        }
    }

    private fun checkErrors() {
        if (errorInputName or errorInputCount) {
            Toast.makeText(
                this@ShopItemActivity,
                "Incorrect name or count",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun getValues() {

    }

    companion object {

        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEditItem(context: Context, itemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, itemId)
            return intent
        }
    }
}
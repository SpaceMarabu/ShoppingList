package com.example.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

//большая часть кода переехала во фрагмент fragment_shop_item

class ShopItemActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishListener {

//    private lateinit var viewModel: ShopItemViewModel
//    private lateinit var tilName: TextInputLayout
//    private lateinit var tilCount: TextInputLayout
//    private lateinit var etName: EditText
//    private lateinit var etCount: EditText
//    private lateinit var buttonSave: Button


    private var screenMode = MODE_UNKNOWN
    private var shopItemId = ShopItem.UNDEFINED_ID


//    var name = ""
//    var count = ""
//    var errorInputName = false
//    var errorInputCount = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        parseIntent()
        //проверяю создается впервые, или пересоздается экран
        if (savedInstanceState == null) {
            launchViewByMode()
        }
//        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
//        initViews()
//        observeErrors()
//        observeFinish()
//        setTextChangedListeners()
    }


//    private fun launchViewByModeOld() {
//        when (screenMode) {
//            MODE_EDIT -> launchEditMode()
//            MODE_ADD -> launchAddMode()
//        }
//    }

    override fun onEditingFinish() {
        finish()
    }

    private fun launchViewByMode() {
        val fragment = when (screenMode) {
            MODE_EDIT -> ShopItemFragment.newInstanceEditItem(shopItemId)
            MODE_ADD -> ShopItemFragment.newInstanceAddItem()
            else -> throw RuntimeException("Unknown screenmode $screenMode")
        }
//        вызов транзакции с фрагментом(запуск фрагмента)
        supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_container, fragment)
            .addToBackStack(null)
            .commit()
    }
//
//    private fun launchEditMode() {
//        viewModel.getShopItem(shopItemId)
//        viewModel.shopItem.observe(this){
//            val oldName = it.name.toString()
//            val oldCount = it.count.toString()
//            etName.setText(oldName)
//            etCount.setText(oldCount)
//        }
//        buttonSave.setOnClickListener {
//            name = etName.text.toString()
//            count = etCount.text.toString()
//            viewModel.editShopItem(name, count)
//            checkErrors()
//        }
//
//    }
//
//    private fun launchAddMode() {
//        buttonSave.setOnClickListener {
//            name = etName.text.toString()
//            count = etCount.text.toString()
//            viewModel.addShopItem(name, count)
//            checkErrors()
//        }
//
//    }
//
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
//
//    private fun initViews() {
//        tilName = findViewById(R.id.til_name)
//        tilCount = findViewById(R.id.til_count)
//        etName = findViewById(R.id.et_name)
//        etCount = findViewById(R.id.et_count)
//        buttonSave = findViewById(R.id.save_button)
//    }
//
//
//    private fun observeErrors() {
//        //подписываюсь на ошибки валидации полей
//        viewModel.errorInputName.observe(this){
//            errorInputName = it
//        }
//        viewModel.errorInputCount.observe(this){
//            errorInputCount = it
//        }
//    }
//
//    private fun observeFinish() {
////        если прилетело хоть что, в нашем случае Unit, то окей
//        viewModel.shouldCloseScreen.observe(this) {
//            finish()
//        }
//    }
//
////    вывожу подсказки по ошибкам
//    private fun checkErrors() {
//        if (errorInputName) {
//            tilName.error = "Введите имя"
//        }
//        if (errorInputCount) {
//            tilCount.error = "Слишком мало"
//        }
//    }
//
////    слушатели изменения поля ввода. Если после ошибки начали вводить по новой, то она исчезнет
//    private fun setTextChangedListeners() {
//        etName.addTextChangedListener(object: TextWatcher {
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                tilName.isErrorEnabled = false
//            }
//
//            override fun afterTextChanged(p0: Editable?) {
//            }
//        })
//        etCount.addTextChangedListener(object: TextWatcher {
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                tilCount.isErrorEnabled = false
//            }
//
//            override fun afterTextChanged(p0: Editable?) {
//            }
//        })
//    }

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
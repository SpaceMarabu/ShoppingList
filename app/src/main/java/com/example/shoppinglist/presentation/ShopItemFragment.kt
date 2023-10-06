package com.example.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem
import com.google.android.material.textfield.TextInputLayout
import java.lang.RuntimeException

class ShopItemFragment(
) : Fragment() {

    private lateinit var viewModel: ShopItemViewModel
    private lateinit var tilName: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var etCount: EditText
    private lateinit var buttonSave: Button

    private var screenMode: String = MODE_UNKNOWN
    private var shopItemId: Int = ShopItem.UNDEFINED_ID


    var name = ""
    var count = ""
    var errorInputName = false
    var errorInputCount = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        initViews(view)
        observeErrors()
        observeFinish()
        setTextChangedListeners()
        launchViewByMode()
    }

    private fun launchViewByMode() {
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun launchEditMode() {
        viewModel.getShopItem(shopItemId)
        //жизненный цикл фрагмент отличается от ЖЦ view. По этому viewLifecycleOwner
        viewModel.shopItem.observe(viewLifecycleOwner) {
            val oldName = it.name.toString()
            val oldCount = it.count.toString()
            etName.setText(oldName)
            etCount.setText(oldCount)
        }
        buttonSave.setOnClickListener {
            name = etName.text.toString()
            count = etCount.text.toString()
            viewModel.editShopItem(name, count)
            checkErrors()
        }

    }

    private fun launchAddMode() {
        buttonSave.setOnClickListener {
            name = etName.text.toString()
            count = etCount.text.toString()
            viewModel.addShopItem(name, count)
            checkErrors()
        }

    }

    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Unknown screenmode $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(SHOP_ITEM_ID)) {
                throw RuntimeException("Param screen mode is absent")
            }
            shopItemId = args.getInt(SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    //    у фрагмента нет метода findViewById, по этому вызов от view
    private fun initViews(view: View) {
        tilName = view.findViewById(R.id.til_name)
        tilCount = view.findViewById(R.id.til_count)
        etName = view.findViewById(R.id.et_name)
        etCount = view.findViewById(R.id.et_count)
        buttonSave = view.findViewById(R.id.save_button)
    }


    private fun observeErrors() {
        //подписываюсь на ошибки валидации полей
        viewModel.errorInputName.observe(viewLifecycleOwner) {
            val message = if (it) {
                "Ошибка в поле ввода"
            } else {
                null
            }
        }
        viewModel.errorInputCount.observe(viewLifecycleOwner) {
            val message = if (it) {
                "Слишко мало"
            } else {
                null
            }
        }
    }

    private fun observeFinish() {
//        если прилетело хоть что, в нашем случае Unit, то окей
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            activity?.onBackPressed()
//            requireActivity() - то же что и активити, но проверка на нулл не нужна. Может упасть
        }
    }

    //    вывожу подсказки по ошибкам
    private fun checkErrors() {
        if (errorInputName) {
            tilName.error = "Введите имя"
        }
        if (errorInputCount) {
            tilCount.error = "Слишком мало"
        }
    }

    //    слушатели изменения поля ввода. Если после ошибки начали вводить по новой, то она исчезнет
    private fun setTextChangedListeners() {
        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                tilName.isErrorEnabled = false
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        etCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                tilCount.isErrorEnabled = false
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    companion object {

        private const val SCREEN_MODE = "extra_mode"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""
        private const val SHOP_ITEM_ID = "extra_shop_item_id"


        //        фабричные методы создание фрагмента
        fun newInstanceAddItem(): ShopItemFragment {
            val args = Bundle().apply{
                putString(SCREEN_MODE, MODE_ADD)
            }
            return ShopItemFragment().apply {
                arguments = args
            }
        }

        fun newInstanceEditItem(shopItemId: Int): ShopItemFragment {
            val args = Bundle().apply{
                putString(SCREEN_MODE, MODE_ADD)
                putInt(SHOP_ITEM_ID, shopItemId)
            }
            return ShopItemFragment().apply {
                arguments = args
            }
        }

    }
}
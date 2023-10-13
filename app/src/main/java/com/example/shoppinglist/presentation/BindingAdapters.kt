package com.example.shoppinglist.presentation

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

interface OnButtonClickListener {

    fun onButtonClick()
}

@BindingAdapter("convertToString")
fun bindConvertToString(textView: TextView, value: Int) {
    textView.text = value.toString()
}

@BindingAdapter("errorInputName")
fun bindErrorInputName(textInputLayout: TextInputLayout, isError: Boolean) {
    textInputLayout.error = if (isError) {
        "Ошибка в поле ввода"
    } else {
        null
    }
}

@BindingAdapter("errorInputCount")
fun bindErrorInputCount(textInputLayout: TextInputLayout, isError: Boolean) {
    textInputLayout.error = if (isError) {
        "Слишко мало"
    } else {
        null
    }
}
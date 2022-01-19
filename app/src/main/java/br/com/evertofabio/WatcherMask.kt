package br.com.evertofabio

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText

class WatcherMask(private val mask: String, private val editText: TextInputEditText): TextWatcher {
    private var isUpdating = false
    private var maskChars = "/-,.()"
    private val maskLength = mask.length

    private fun getMaskChar(pos: Int): String {
        var maskChar = ""

        if ((pos < maskLength) && (isMaskChar(mask[pos]))) {
            maskChar = mask[pos].toString()
        }

        return maskChar
    }

    private fun isMaskChar(char: Char): Boolean {
        return maskChars.contains(char)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (isUpdating) {
            isUpdating = false
            return
        }

        if (before==1)
            return

        val text = onlyNumbers(s.toString())
        var newText = ""

        var index = 0

        text.forEach {
            val maskSeparator = getMaskChar(index)

            if (maskSeparator.isNotEmpty()) {
                index++
                newText += maskSeparator
            }

            newText += it
            index++
        }

        isUpdating = true
        editText.setText(newText)
        editText.setSelection(newText.length)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
    }
}
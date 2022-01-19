package br.com.evertofabio

import android.app.DatePickerDialog
import android.content.Context
import android.text.InputFilter
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.inputmethod.EditorInfo
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.google.android.material.R.style
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

class EditDate : TextInputLayout {
    private lateinit var mEditDate: TextInputEditText
    private var mCurrentDate: Date? = null
    private lateinit var watcherMask: WatcherMask
    var onFocusChange: OnDateChangeListener? = null
    var mask : String = "dd/MM/yyyy"
        set(value) {
            mEditDate.removeTextChangedListener(watcherMask)
            watcherMask = WatcherMask(value, mEditDate)
            mEditDate.addTextChangedListener(watcherMask)
            field = value
        }

    var date: Date?
        get() = mCurrentDate
        set(value) {
            mCurrentDate = value
            printDate()
        }

    constructor(@NonNull context: Context) : super(ContextThemeWrapper(context, style.Widget_MaterialComponents_TextInputLayout_OutlinedBox))
    constructor(@NonNull context: Context, @Nullable attrs: AttributeSet?) : super(ContextThemeWrapper(context,style.Widget_MaterialComponents_TextInputLayout_OutlinedBox),attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(ContextThemeWrapper(context, style.Widget_MaterialComponents_TextInputLayout_OutlinedBox),attrs,defStyleAttr)

    private fun init(context: Context) {
        setWillNotDraw(false)
        endIconMode = END_ICON_CUSTOM
        endIconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_calendar)
        mEditDate = TextInputEditText(getContext())

        createEditBox(mEditDate)

        setEndIconOnClickListener {
            showCalendar()
        }
    }

    private fun createEditBox(editText: TextInputEditText) {
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        editText.layoutParams = layoutParams
        editText.inputType = EditorInfo.TYPE_CLASS_DATETIME
        editText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(10))

        watcherMask = WatcherMask(mask, editText)

        editText.addTextChangedListener(watcherMask)
        editText.addTextChangedListener {
            if (! validate()) {
                mCurrentDate = null
            }
        }

        addView(editText)

        editText.setOnFocusChangeListener { _, hasFocus ->
            onFocusChange?.onFocusChange(hasFocus)
        }
    }

    private fun showCalendar() {
        val dateDefault = Calendar.getInstance()

        if (validate()) {
            dateDefault.time = mCurrentDate
        }

        val sYear = dateDefault.get(Calendar.YEAR)
        val sMonth = dateDefault.get(Calendar.MONTH)
        val sDay = dateDefault.get(Calendar.DAY_OF_MONTH)

        val datePiker = DatePickerDialog(context, { _, year, month, dayOfMonth ->
            dateDefault.set(year, month, dayOfMonth)
            mCurrentDate = dateDefault.time

            printDate()

            onFocusChange?.onSetValue()
        }, sYear, sMonth, sDay)

        datePiker.show()
    }

    private fun validate(): Boolean {
        val data = onlyNumbers(mEditDate.text.toString())

        if (data.length<8)
            return false

        return convert()
    }

    private fun convert(): Boolean {
        return try {
            val data = SimpleDateFormat(mask, Locale.ROOT).parse(mEditDate.text.toString())
            mCurrentDate = data
            true
        } catch (e: Exception) {
            false
        }
    }

    init {
        init(context)
    }

    private fun printDate() {
        mEditDate.setText(format())
    }

    private fun format(): String {
        return SimpleDateFormat(mask).format(mCurrentDate)
    }

    fun isValidDate(): Boolean {
        return mCurrentDate != null
    }

    interface OnDateChangeListener {
        fun onFocusChange(hasFocus: Boolean)
        fun onSetValue()
    }

    override
    fun toString(): String {
        return mEditDate.text.toString()
    }
}
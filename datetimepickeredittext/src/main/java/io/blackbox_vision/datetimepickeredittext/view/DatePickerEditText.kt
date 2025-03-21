package io.blackbox_vision.datetimepickeredittext.view

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.appcompat.widget.AppCompatEditText
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker

import java.text.DateFormat
import java.util.Calendar
import java.util.Locale

import io.blackbox_vision.datetimepickeredittext.R
import io.blackbox_vision.datetimepickeredittext.internal.fragment.DatePickerFragment
import io.blackbox_vision.datetimepickeredittext.internal.utils.DateUtils

import android.view.View.OnFocusChangeListener
import android.view.View.OnClickListener
import android.app.DatePickerDialog.OnDateSetListener
import androidx.appcompat.app.AppCompatActivity


class DatePickerEditText : AppCompatEditText, OnFocusChangeListener, OnClickListener, OnDateSetListener {

    private var onFocusChangedListener: OnFocusChangeListener? = null

    private var manager: FragmentManager? = null

    private var focusCount: Int = 0
    private var themeId: Int? = null

    private var dateFormat: String? = null
    private var minDate: String? = null
    private var maxDate: String? = null

    private var textDateFormat: DateFormat? = null

    private var date: Calendar? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        handleAttributes(attrs)
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        handleAttributes(attrs)
        init()
    }

    private fun init() {
        inputType = InputType.TYPE_NULL
        onFocusChangeListener = this
        setOnClickListener(this)

        /*Set fragment manager*/
        manager = (context as AppCompatActivity).supportFragmentManager
    }

    private fun handleAttributes(attributeSet: AttributeSet) {
        try {
            val array = context.obtainStyledAttributes(attributeSet, R.styleable.DateTimePickerEditText)

            themeId = array.getResourceId(R.styleable.DateTimePickerEditText_theme, 0)

            dateFormat = array.getString(R.styleable.DateTimePickerEditText_dateFormat)
            minDate = array.getString(R.styleable.DateTimePickerEditText_minDate)
            maxDate = array.getString(R.styleable.DateTimePickerEditText_maxDate)

            array.recycle()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    override fun onFocusChange(view: View, isFocused: Boolean) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
        focusCount++

        if (isFocused && focusCount == 1) {
            showDatePicker()
        }

        if (null != onFocusChangedListener) {
            onFocusChangedListener!!.onFocusChange(view, isFocused)
        }
    }

    override fun onClick(v: View) {
        showDatePicker()
    }

    private fun showDatePicker() {
        val datePickerFragment = DatePickerFragment()

        datePickerFragment.date = date
        datePickerFragment.themeId = themeId
        datePickerFragment.minDate = minDate
        datePickerFragment.maxDate = maxDate
        datePickerFragment.onDateSetListener = this

        datePickerFragment.show(manager!!, TAG)
    }

    override fun onDateSet(datePicker: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance(Locale.getDefault())

        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        if (textDateFormat != null) {
            setText(textDateFormat!!.format(calendar.time))
        } else {
            setText(DateUtils.toDate(calendar.time, dateFormat))
        }

        date = calendar
    }

    fun getManager(): FragmentManager? {
        return manager
    }

    /*fun setManager(manager: FragmentManager): DatePickerEditText {
        this.manager = manager
        return this
    }*/

    fun getDate(): Calendar? {
        return date
    }

    fun setDate(date: Calendar): DatePickerEditText {
        this.date = date
        return this
    }

    fun getOnFocusChangedListener(): OnFocusChangeListener? {
        return onFocusChangedListener
    }

    fun setOnFocusChangedListener(onFocusChangedListener: OnFocusChangeListener): DatePickerEditText {
        this.onFocusChangedListener = onFocusChangedListener
        return this
    }

    fun getDateFormat(): String? {
        return dateFormat
    }

    fun setDateFormat(dateFormat: String): DatePickerEditText {
        this.dateFormat = dateFormat
        return this
    }

    fun setDateFormat(format: DateFormat): DatePickerEditText {
        this.textDateFormat = format
        return this
    }

    fun getThemeId(): Int? {
        return themeId
    }

    fun setThemeId(themeId: Int?): DatePickerEditText {
        this.themeId = themeId
        return this
    }

    fun getMaxDate(): String? {
        return maxDate
    }

    fun setMaxDate(maxDate: String): DatePickerEditText {
        this.maxDate = maxDate
        return this
    }

    fun getMinDate(): String? {
        return minDate
    }

    fun setMinDate(minDate: String): DatePickerEditText {
        this.minDate = minDate
        return this
    }

    companion object {
        private val TAG = "DatePickerEditText"
    }
}
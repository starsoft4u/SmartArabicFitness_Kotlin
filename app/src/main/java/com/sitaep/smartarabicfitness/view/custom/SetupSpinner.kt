package com.sitaep.smartarabicfitness.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import com.sitaep.smartarabicfitness.R
import kotlinx.android.synthetic.main.setup_spinner.view.*

class SetupSpinner : ConstraintLayout {
    var firstTime = true
    var minValue = 1
    var maxValue = 10

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        if (attrs == null) {
            return
        }

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.SetupSpinner)

        selectedValue = attributes.getInt(R.styleable.SetupSpinner_value, 0)
        unitText.text = attributes.getText(R.styleable.SetupSpinner_unit)
        minValue = attributes.getInt(R.styleable.SetupSpinner_minValue, 1)
        maxValue = attributes.getInt(R.styleable.SetupSpinner_maXValue, 10)

        // Spinner
        val items = (minValue..maxValue).map { it.toString() }
        spinner.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, items).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (firstTime) {
                    firstTime = false
                } else {
                    valueText.text = items[position]
                }
            }
        }

        attributes.recycle()
    }

    init {
        inflate(context, R.layout.setup_spinner, this)
    }

    var selectedValue: Int
        get() = valueText.text.toString().toInt()
        set(value) {
            valueText.text = value.toString()
        }
}
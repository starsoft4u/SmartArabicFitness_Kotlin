package com.sitaep.smartarabicfitness.view

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import com.sitaep.smartarabicfitness.*
import com.sitaep.smartarabicfitness.extensions.dismissKeyboard
import com.sitaep.smartarabicfitness.extensions.toPixel
import kotlinx.android.synthetic.main.activity_special_info.*
import kotlin.math.log10

class SpecialInfoActivity : BaseActivity() {

    private var activityValues = arrayOf(1.2, 1.375, 1.55, 1.725, 1.9)
    private var selectedActivity = 2
    private var boardScale = 1f
    private var boardDegree = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_special_info)

        setTitle(R.string.special_infos)

        collapseAll()
        container.children.forEach { layout ->
            if (layout !is ConstraintLayout) {
                return@forEach
            }

            layout.children.firstOrNull { it.tag == "0" }?.let { button ->
                val parent = button.parent as ConstraintLayout
                val radio = parent.children.first { it is RadioButton } as RadioButton
                button.setOnClickListener { togglePanel(button, !radio.isChecked) }
            }
        }

        // Age
        ageSaveButton.setOnClickListener {
            try {
                AGE = ageEdit.text.toString().toInt()
            } catch (e: Exception) {
                ageEdit.setText("")
            } finally {
                expandPanel(ageToggleButton, false)
            }
        }
        ageCancelButton.setOnClickListener {
            expandPanel(ageToggleButton, false)
        }

        // Weight
        weightSaveButton.setOnClickListener {
            try {
                WEIGHT = weightEdit.text.toString().toFloat()
            } catch (e: Exception) {
                weightEdit.setText("")
            } finally {
                expandPanel(weightToggleButton, false)
            }
        }
        weightCancelButton.setOnClickListener {
            expandPanel(weightToggleButton, false)
        }

        // Length
        lengthSaveButton.setOnClickListener {
            try {
                LENGTH = lengthEdit.text.toString().toFloat()
            } catch (e: Exception) {
                lengthEdit.setText("")
            } finally {
                expandPanel(lengthToggleButton, false)
            }
        }
        lengthCancelButton.setOnClickListener {
            expandPanel(lengthToggleButton, false)
        }

        // Calories Amount Calculate
        val physicalActivities = resources.getStringArray(R.array.physical_activity)
        physicalActivitySpinner.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, physicalActivities).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
        physicalActivitySpinner.measure(0, 0)
        physicalActivitySpinner.dropDownWidth = physicalActivitySpinner.measuredWidth
        physicalActivitySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedActivity = position
                    caloriesActivityView.text = physicalActivities[position]
                }
            }
        physicalActivitySpinner.setSelection(selectedActivity)
        caloriesButton.setOnClickListener {
            if (WEIGHT <= 0 || LENGTH <= 0 || AGE <= 0) {
                return@setOnClickListener
            }

            /*
             * Formula
             * Man: 66 + ( 13.7*weight in kg) + ( 5  length in cm) - (6.8 age in years) * activity
             * Woman: 655+ ( 9.6*weight in kg) + ( 1.8  length in cm) - (4.7 age in years) * activity
            */
            if (IS_MALE) {
                val calories =
                    66 + (13.7 * WEIGHT) + (5 * LENGTH) - (6.8 * AGE * activityValues[selectedActivity])
                caloriesResultView.text = String.format("%.2f", calories)
            } else {
                val calories =
                    655 + (9.6 * WEIGHT) + (1.8 * LENGTH) - (4.7 * AGE * activityValues[selectedActivity])
                caloriesResultView.text = String.format("%.2f", calories)
            }
        }

        // Body Fat
        fatButton.setOnClickListener {
            var abdomen: Double = 0.0
            var hip: Double = 0.0
            var neck: Double = 0.0
            var height: Double = 0.0

            try {
                abdomen = fatAbdomenText.text.toString().toDouble()
            } catch (_: Exception) {
                fatAbdomenText.setText("")
            }

            try {
                hip = fatHipText.text.toString().toDouble()
            } catch (_: Exception) {
                fatHipText.setText("")
            }

            try {
                neck = fatNeckText.text.toString().toDouble()
            } catch (_: Exception) {
                fatNeckText.setText("")
            }

            try {
                height = fatHeightText.text.toString().toDouble()
            } catch (_: Exception) {
                fatHeightText.setText("")
            }

            /*
             * Formula
             * Man  : %Fat = 96.010 * LOG(abdomen - neck) - 70.041 * LOG(height) + 30.30
             * Woman: %Fat = 163.205 * LOG(abdomen + hip - neck) - 97.684 * LOG(height) - 78.387
             */
            if (IS_MALE && abdomen > 0 && neck > 0 && height > 0) {
                val fat = 96.01 * log10(abdomen - neck) - 70.041 * log10(height) + 30.3
                fatResultLabel.text = String.format("%.2f", fat)
            } else if (!IS_MALE && abdomen > 0 && neck > 0 && height > 0 && hip > 0) {
                val fat = 163.205 * log10(abdomen + hip - neck) - 97.684 * log10(height) - 78.387
                fatResultLabel.text = String.format("%.2f", fat)
            }
        }

        // BMI
        bmiButton.setOnClickListener {
            if (WEIGHT <= 0 || LENGTH <= 0) {
                return@setOnClickListener
            }

            /*
             * Formula
             * Man:  Weight (kg)  /  2(Length in meters)
             * Woman: Same?
             */
            val bmi = WEIGHT * 100 / LENGTH
            boardDegree = when (bmi.toInt()) {
                in Int.MIN_VALUE..17 -> 0f
                in 18..24 -> 180f / 8f
                in 25..29 -> 3f * 180f / 8f
                in 30..39 -> 5f * 180f / 8f
                else -> 7f * 180f / 8f
            }
            rotateBoardHandle(boardDegree)
        }
        initialBoard()
    }


    private fun initialBoard() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val boardImageWidth = displayMetrics.widthPixels - boardImage.toPixel(72f)
        boardScale = boardImageWidth / boardImage.toPixel(1024f)
        val layoutParams = boardImageHandle.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.width = (boardImageWidth * 0.0425f).toInt()
        layoutParams.height = layoutParams.width * 4
        layoutParams.bottomMargin = (450 * boardScale).toInt()
        boardImageHandle.layoutParams = layoutParams
    }

    private fun rotateBoardHandle(degree: Float) {
        boardImageHandle.startAnimation(
            RotateAnimation(
                -90f,
                degree - 90f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                1f
            ).apply {
                duration = 1000
                fillAfter = true
                repeatCount = 0
                interpolator = LinearInterpolator()
            })
    }

    private fun togglePanel(item: View, show: Boolean) {
        if (show) {
            collapseAll()
            expandPanel(item, show)

            when (item) {
                ageToggleButton -> ageEdit.setText(if (AGE == 0) "" else AGE.toString())
                weightToggleButton -> weightEdit.setText(if (WEIGHT == 0f) "" else WEIGHT.toString())
                lengthToggleButton -> lengthEdit.setText(if (LENGTH == 0f) "" else LENGTH.toString())
                caloriesToggleButton -> {
                    caloriesSexView.setText(if (IS_MALE) R.string.man else R.string.woman)
                    caloriesAgeView.text = if (AGE == 0) "" else AGE.toString()
                    caloriesWeightView.text = if (WEIGHT == 0f) "" else "${WEIGHT}Kg"
                    caloriesLengthView.text = if (LENGTH == 0f) "" else "${LENGTH}cm"
                }
                bmiToggleButton -> {
                    bmiWeightView.text = if (WEIGHT == 0f) "" else "${WEIGHT}Kg"
                    bmiLengthView.text = if (LENGTH == 0f) "" else "${LENGTH}cm"
                    rotateBoardHandle(boardDegree)
                }
            }
        } else {
            expandPanel(item, show)
        }
    }

    private fun expandPanel(item: View, show: Boolean) {
        item.dismissKeyboard()

        val visible = if (show) View.VISIBLE else View.GONE
        val parent = item.parent as ConstraintLayout

        parent.background = if (!show) {
            ColorDrawable(0xfff)
        } else if (container.children.indexOf(parent) == 0) {
            resources.getDrawable(R.drawable.shadow_bottom, null)
        } else {
            resources.getDrawable(R.drawable.shadow_top_bottom, null)
        }

        parent.children
            .filterNot { it is RadioButton || it == item }
            .forEach { it.visibility = visible }
        (parent.children.first { it is RadioButton } as RadioButton).isChecked = show

        if (IS_MALE) {
            fatHipLabel.visibility = View.GONE
            fatHipText.visibility = View.GONE
        }
    }

    private fun collapseAll() {
        container.children.forEach { layout ->
            expandPanel((layout as ConstraintLayout).children.first { it.tag == "0" }, false)
        }
    }
}

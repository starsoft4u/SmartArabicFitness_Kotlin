package com.sitaep.smartarabicfitness.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.sitaep.smartarabicfitness.Constants
import com.sitaep.smartarabicfitness.Events
import com.sitaep.smartarabicfitness.IS_MALE
import com.sitaep.smartarabicfitness.R
import com.sitaep.smartarabicfitness.extensions.navigate
import kotlinx.android.synthetic.main.activity_gender.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class GenderActivity : BaseActivity() {
    var gender: Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gender)

        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        femaleButton.setOnClickListener {
            gender = 0
            updateButton()
        }

        maleButton.setOnClickListener {
            gender = 1
            updateButton()
        }

        startButton.setOnClickListener {
            when (gender) {
                2 -> {
                    AlertDialog.Builder(this)
                        .setMessage(R.string.please_select_gender)
                        .setPositiveButton(R.string.ok) { dlg, _ -> dlg.dismiss() }
                        .show()
                }
                else -> {
                    IS_MALE = gender == 1
                    navigate(MainActivity::class.java)
                }
            }
        }

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        super.onDestroy()
    }

    @Subscribe
    public fun onLanguageUpdated(event: Events.LanguageUpdated) {
        Log.d(Constants.APP_KEY, "new event: $event")
        recreate()
    }

    private fun updateButton() {
        maleImage.setImageResource(if (gender == 1) R.drawable.ic_male_d else R.drawable.ic_male)
        maleImage.setBackgroundResource(if (gender == 1) R.drawable.gender_bg_d else R.drawable.gender_bg)
        femaleImage.setImageResource(if (gender == 0) R.drawable.ic_female_d else R.drawable.ic_female)
        femaleImage.setBackgroundResource(if (gender == 0) R.drawable.gender_bg_d else R.drawable.gender_bg)
    }
}

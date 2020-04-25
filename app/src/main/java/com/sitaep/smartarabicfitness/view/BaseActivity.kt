package com.sitaep.smartarabicfitness.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.sitaep.smartarabicfitness.LANGUAGE
import java.util.*

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if ((intent.flags and Intent.FLAG_ACTIVITY_CLEAR_TASK) == Intent.FLAG_ACTIVITY_CLEAR_TASK &&
            (intent.flags and Intent.FLAG_ACTIVITY_NEW_TASK) == Intent.FLAG_ACTIVITY_NEW_TASK
        ) {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        } else {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun attachBaseContext(newBase: Context?) {
        val lang = newBase?.LANGUAGE ?: "en"
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = newBase?.resources?.configuration ?: Configuration(resources.configuration)
        val context = newBase?.createConfigurationContext(config.apply { this.setLocale(locale) })
        super.attachBaseContext(context)
    }
}
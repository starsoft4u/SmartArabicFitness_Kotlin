package com.sitaep.smartarabicfitness.view

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.sitaep.smartarabicfitness.Events
import com.sitaep.smartarabicfitness.IS_EN
import com.sitaep.smartarabicfitness.LANGUAGE
import com.sitaep.smartarabicfitness.R
import com.sitaep.smartarabicfitness.extensions.dismissKeyboard
import kotlinx.android.synthetic.main.activity_more_info.*
import org.greenrobot.eventbus.EventBus

class MoreInfoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more_info)

        setTitle(R.string.more)

        collapseAll()
        if (IS_EN) {
            langEnButton.background = ContextCompat.getDrawable(this, R.drawable.button_primary)
            langEnButton.setTextColor(Color.WHITE)
            langArButton.background = ContextCompat.getDrawable(this, R.drawable.button_secondary)
            langArButton.setTextColor(Color.BLACK)
        } else {
            langEnButton.background = ContextCompat.getDrawable(this, R.drawable.button_secondary)
            langEnButton.setTextColor(Color.BLACK)
            langArButton.background = ContextCompat.getDrawable(this, R.drawable.button_primary)
            langArButton.setTextColor(Color.WHITE)
        }

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

        langEnButton.setOnClickListener {
            LANGUAGE = "en"
            EventBus.getDefault().post(Events.LanguageUpdated())
            recreate()
        }

        langArButton.setOnClickListener {
            LANGUAGE = "ar"
            EventBus.getDefault().post(Events.LanguageUpdated())
            recreate()
        }
    }

    private fun togglePanel(item: View, show: Boolean) {
        if (show) {
            collapseAll()
            expandPanel(item, show)
            when (item) {
                evaluationToggleButton -> openUrl("https://www.google.com/search?rls=enName&q=evaluation&ie=UTF-8&oe=UTF-8")
                helpToggleButton -> openUrl("https://www.google.com/search?rls=enName&q=help&ie=UTF-8&oe=UTF-8")
                contactToggleButton -> openUrl("https://www.google.com/search?rls=enName&q=contact&ie=UTF-8&oe=UTF-8")
                facebookToggleButton -> openUrl("https://www.facebook.com")
                instagramToggleButton -> openUrl("https://www.instagram.com")
                twitterToggleButton -> openUrl("https://www.twitter.com")
                termsToggleButton -> openUrl("https://www.google.com/search?rls=enName&q=terms&ie=UTF-8&oe=UTF-8")
                privacyToggleButton -> openUrl("https://www.google.com/search?rls=enName&q=privacy&ie=UTF-8&oe=UTF-8")
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

        parent.children.filterNot { it is RadioButton || it == item }.forEach { it.visibility = visible }
        (parent.children.first { it is RadioButton } as RadioButton).isChecked = show
    }

    private fun collapseAll() {
        container.children.forEach { layout ->
            expandPanel((layout as ConstraintLayout).children.first { it.tag == "0" }, false)
        }
    }

    private fun openUrl(url: String) {
        val path = if (url.startsWith("http://") || url.startsWith("https://")) {
            url
        } else {
            "http://$url"
        }
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(path)))
    }
}

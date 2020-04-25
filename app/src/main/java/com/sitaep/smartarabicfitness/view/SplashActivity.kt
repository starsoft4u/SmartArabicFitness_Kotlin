package com.sitaep.smartarabicfitness.view

import android.animation.Animator
import android.os.Bundle
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.sitaep.smartarabicfitness.R
import com.sitaep.smartarabicfitness.extensions.navigateClear
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        YoYo.with(Techniques.FadeIn)
            .duration(3000)
            .playOn(logoImage)
        YoYo.with(Techniques.FadeIn)
            .withListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator?) {}

                override fun onAnimationEnd(p0: Animator?) {
                    navigateClear(GenderActivity::class.java)
                }

                override fun onAnimationCancel(p0: Animator?) {}

                override fun onAnimationStart(p0: Animator?) {}

            })
            .duration(3000)
            .playOn(sloganImage)
    }
}

package com.sitaep.smartarabicfitness.view

import android.graphics.PointF
import android.graphics.RectF
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.SizeF
import android.view.GestureDetector
import android.view.MenuItem
import android.view.MotionEvent
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.graphics.contains
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.sitaep.smartarabicfitness.Constants.Companion.APP_KEY
import com.sitaep.smartarabicfitness.Events
import com.sitaep.smartarabicfitness.IS_EN
import com.sitaep.smartarabicfitness.IS_MALE
import com.sitaep.smartarabicfitness.R
import com.sitaep.smartarabicfitness.extensions.navigate
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener,
    GestureDetector.OnGestureListener {

    private lateinit var gestureDetector: GestureDetectorCompat
    private lateinit var viewSize: SizeF

    private val buttonSize = SizeF(0.375f, 0.04455445545f)
    private val mapForMale = arrayOf(
        PointF(0.296875f, 0.8613861386f), // All exercises
        PointF(0.1015625f, 0.3217821782f), // Abs
        PointF(0.5703125f, 0.297029703f), // Back
        PointF(0.109375f, 0.7722772277f), // Cardio
        PointF(0.5703125f, 0.2425742574f), // Chest
        PointF(0.59375f, 0.5495049505f), // Lower Legs
        PointF(0.578125f, 0.1633663366f), // Shoulders
        PointF(0.578125f, 0.7722772277f), // Stretch
        PointF(0.0625f, 0.1881188119f), // Triceps
        PointF(0.078125f, 0.4851485149f), // Upper Legs
        PointF(0.0703125f, 0.2524752475f), // Biceps
        PointF(0.6484375f, 0.3613861386f) // Forearm
    )
    private val mapForFemale = arrayOf(
        PointF(0.296875f, 0.8613861386f), // All exercises
        PointF(0.1f, 0.297029703f), // Abs
        PointF(0.5703125f, 0.2673267327f), // Back
        PointF(0.109375f, 0.7722772277f), // Cardio
        PointF(0.5625f, 0.2079207921f), // Chest
        PointF(0.5546875f, 0.495049505f), // Lower Legs
        PointF(0.578125f, 0.1336633663f), // Shoulders
        PointF(0.578125f, 0.7623762376f), // Stretch
        PointF(0.078125f, 0.1584158416f), // Triceps
        PointF(0.1f, 0.4603960396f), // Upper Legs
        PointF(0.078125f, 0.2178217822f), // Biceps
        PointF(0.6171875f, 0.3267326733f), // Forearm
        PointF(0.1f, 0.3564356436f) // Glutes
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        gestureDetector = GestureDetectorCompat(this, this)
        bodyImage.setImageResource(
            when {
                IS_MALE -> if (IS_EN) R.drawable.body_man else R.drawable.body_man_ar
                else -> if (IS_EN) R.drawable.body_woman else R.drawable.body_woman_ar
            }
        )
        bodyImage.post { viewSize = SizeF(bodyImage.width.toFloat(), bodyImage.height.toFloat()) }
        bodyImage.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
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

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        loadPage(item.itemId)
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun loadPage(itemId: Int) {
        when (itemId) {
            R.id.menuSpecialInfos -> navigate(SpecialInfoActivity::class.java)
            R.id.menuMyPlan -> navigate(MyPlanActivity::class.java)
            R.id.menuWorkouts -> navigate(WorkoutActivity::class.java)
            R.id.menuLogs -> navigate(LogsActivity::class.java)
            R.id.menuMore -> navigate(MoreInfoActivity::class.java)
        }
    }

    @Subscribe
    public fun onLanguageUpdated(event: Events.LanguageUpdated) {
        Log.d(APP_KEY, "new event: $event")
        recreate()
    }

    override fun onSingleTapUp(event: MotionEvent?): Boolean {
        if (event != null) {
            // Device screen size
            val matrix = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(matrix)
            Log.d(APP_KEY, "Screen size: ${matrix.widthPixels} X ${matrix.heightPixels}")
            Log.d(APP_KEY, "View size: ${viewSize.width} X ${viewSize.height}")

            var width = viewSize.width
            var height = viewSize.height
            val screenRatio = width / height
            val imageRatio = 640f / 1010f

            var offset = PointF(0f, 0f)
            if (imageRatio > screenRatio) {
                height = width / imageRatio
                offset = PointF(0f, (viewSize.height - height) / 2f)
            } else if (imageRatio < screenRatio) {
                width = height * imageRatio
                offset = PointF((viewSize.width - width) / 2f, 0f)
            }

            // Touch point
            val touchPoint = PointF(
                (event.x - offset.x) / width,
                (event.y - offset.y) / height
            )
            Log.d(APP_KEY, "Touch location $touchPoint")

            // Find match
            val data = if (IS_MALE) mapForMale else mapForFemale
            for ((index, point) in data.iterator().withIndex()) {
                val rect = RectF(
                    point.x, point.y,
                    point.x + buttonSize.width,
                    point.y + buttonSize.height
                )
                if (rect.contains(touchPoint)) {
                    navigate(MuscleDetailActivity::class.java, "muscleId" to index)
                    return true
                }
            }
        }

        return false
    }

    override fun onShowPress(p0: MotionEvent?) {
        //
    }

    override fun onDown(p0: MotionEvent?): Boolean {
        return true
    }

    override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        return false
    }

    override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        return false
    }

    override fun onLongPress(p0: MotionEvent?) {
        //
    }

}


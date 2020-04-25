package com.sitaep.smartarabicfitness.view

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.view.get
import com.beust.klaxon.Klaxon
import com.sitaep.smartarabicfitness.Constants
import com.sitaep.smartarabicfitness.Events
import com.sitaep.smartarabicfitness.IS_MALE
import com.sitaep.smartarabicfitness.R
import com.sitaep.smartarabicfitness.extensions.intentForNavigate
import com.sitaep.smartarabicfitness.model.Exercise
import com.sitaep.smartarabicfitness.model.Muscle
import com.sitaep.smartarabicfitness.model.Plan
import com.sitaep.smartarabicfitness.model.PlanExercise
import kotlinx.android.synthetic.main.activity_exercise.*
import org.greenrobot.eventbus.EventBus
import org.litepal.LitePal
import pl.droidsonroids.gif.GifDrawable

class ExerciseActivity : BaseActivity() {

    var plan: Plan? = null
    var planExercise: PlanExercise? = null
    lateinit var exercise: Exercise

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        // Params
        intent.getStringExtra("plan")?.let { plan = Klaxon().parse<Plan>(it) }
        intent.getStringExtra("planExercise")?.let {
            planExercise = Klaxon().parse<PlanExercise>(it)
        }
        exercise = intent.getSerializableExtra("exercise") as Exercise

        // Title
        title = exercise.name(this)

        // Main Group
        val mainGroup = exercise.mainGroup(this)
        if (mainGroup != null) {
            mainMuscle.text = getString(R.string.main_muscle_, mainGroup)
        } else {
            val muscleName = LitePal
                .where("identifier = ?", exercise.muscleId.toString())
                .findFirst(Muscle::class.java)
                .name(this)
            mainMuscle.text = getString(R.string.main_muscle_, muscleName)
        }

        // Difficulty
        if (exercise.difficulty(this) != null) {
            difficulty.text = getString(R.string.difficulty_, exercise.difficulty(this))
        } else {
            difficulty.visibility = GONE
        }

        // Equipment
        equipment.text = getString(R.string.equipment_, exercise.equipment(this))

        // Exercise Gif
        exerciseImage.setImageDrawable(
            if (IS_MALE) GifDrawable(assets, exercise.maleImage) else GifDrawable(
                assets,
                exercise.femaleImage
            )
        )

        // Steps
        steps.text = exercise.howTo(this)

        // Other Group
        if (exercise.otherGroup(this) != null) {
            otherMuscle.text = getString(R.string.other_muscle_, exercise.otherGroup(this))
        } else {
            otherMuscle.visibility = GONE
        }

        // Setup only edit plan
        setupPanel.visibility = if (plan == null) GONE else VISIBLE

        // Default setup values
        planExercise?.let {
            repsSetup.selectedValue = it.reps
            setsSetup.selectedValue = it.sets
            secondsSetup.selectedValue = it.restTime
            kilogramSetup.selectedValue = it.weight
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (plan != null) {
            menu?.clear()
            menu?.add(0, Constants.MENU_ITEM_SAVE, 0, R.string.save)
            menu?.get(0)?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
            return true
        }

        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId != Constants.MENU_ITEM_SAVE) {
            return super.onOptionsItemSelected(item)
        }

        if (planExercise != null) {
            // Edit plan exercise
            planExercise!!.sets = setsSetup.selectedValue
            planExercise!!.reps = repsSetup.selectedValue
            planExercise!!.restTime = secondsSetup.selectedValue
            planExercise!!.weight = kilogramSetup.selectedValue

            val values = ContentValues()
            values.put("reps", repsSetup.selectedValue)
            values.put("sets", setsSetup.selectedValue)
            values.put("resttime", secondsSetup.selectedValue)
            values.put("weight", kilogramSetup.selectedValue)
            LitePal.updateAll(
                PlanExercise::class.java,
                values,
                "identifier = ?",
                planExercise!!.identifier.toString()
            )

            EventBus.getDefault().post(Events.EditPlanExercise(planExercise!!))

            finish()

        } else if (plan != null) {
            // Add exercise to plan
            val maxId = LitePal.max(PlanExercise::class.java, "identifier", Int::class.java) + 1
            planExercise = PlanExercise(
                identifier = maxId,
                planId = plan!!.identifier,
                exerciseId = exercise.identifier,
                sets = setsSetup.selectedValue,
                reps = repsSetup.selectedValue,
                restTime = secondsSetup.selectedValue,
                weight = kilogramSetup.selectedValue,
                exercise = exercise
            ).apply { save() }

            EventBus.getDefault().post(Events.AddPlanExercise(planExercise!!))

            val intent = intentForNavigate(PlanExerciseActivity::class.java, "plan" to plan!!)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        return true
    }
}

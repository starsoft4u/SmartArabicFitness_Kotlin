package com.sitaep.smartarabicfitness

import com.sitaep.smartarabicfitness.model.Plan
import com.sitaep.smartarabicfitness.model.PlanExercise

class Events {
    class AddPlan(val plan: Plan)
    class EditPlan(val plan: Plan)
    class AddPlanExercise(val planExercise: PlanExercise)
    class EditPlanExercise(val planExercise: PlanExercise)
    class LanguageUpdated
}
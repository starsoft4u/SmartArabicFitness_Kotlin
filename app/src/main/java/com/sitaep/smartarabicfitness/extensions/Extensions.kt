package com.sitaep.smartarabicfitness.extensions

import android.util.Patterns

val Boolean.intValue: Int
    get() = if (this) 1 else 0
val Int.isOdd: Boolean
    get() = this % 2 == 1
val Int.isEven: Boolean
    get() = this % 2 == 0

// Validate email
fun String.isValidEmail(): Boolean = this.isNotEmpty() and Patterns.EMAIL_ADDRESS.matcher(this).matches()

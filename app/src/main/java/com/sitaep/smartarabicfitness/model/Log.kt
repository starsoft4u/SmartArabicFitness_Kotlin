package com.sitaep.smartarabicfitness.model

import org.litepal.crud.LitePalSupport
import java.util.*

data class Log(
    val issuedAt: Date,
    val planId: Int
) : LitePalSupport()
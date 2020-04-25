package com.sitaep.smartarabicfitness.model

import com.beust.klaxon.JsonObject
import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport

data class Plan(
    @Column(unique = true)
    val identifier: Int,
    var name: String?,
    var note: String?,
    val isDefault: Boolean
) : LitePalSupport() {
    companion object {
        fun fromJson(json: JsonObject): Plan {
            return Plan(
                identifier = json.int("id")!!,
                name = json.string("name"),
                note = json.string("notes"),
                isDefault = true
            )
        }
    }
}
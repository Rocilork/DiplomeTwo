package com.example.dip.Class

import kotlinx.serialization.Serializable

@Serializable
data class ProfileStudent (val ID_студента: String = "", val id_группы: Int = 0, val Курс: Int = 0, val Факультет: String = "")
package com.example.dip.Class

import kotlinx.serialization.Serializable

@Serializable
data class ProfileTeacher (val ID_преподавателя: String = "", val Факультет: String = "", val Кафедра: String = "", val МестоРаботы: String = "")
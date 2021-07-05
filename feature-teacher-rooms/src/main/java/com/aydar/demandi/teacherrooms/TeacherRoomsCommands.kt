package com.aydar.demandi.teacherrooms

sealed class TeacherRoomsCommands {

    object ShowProgress : TeacherRoomsCommands()
    object HideProgress : TeacherRoomsCommands()
    object HasNoRooms : TeacherRoomsCommands()
}
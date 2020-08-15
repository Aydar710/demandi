package com.aydar.demandi.common

import com.aydar.demandi.data.model.Room

fun createDeviceName(room: Room): String = "$ROOM_NAME_PREFIX${room.name}/${room.subjectName}/"

fun getRoomNameFromDeviceName(deviceName: String): String =
    deviceName.split("/")[0].drop(ROOM_NAME_PREFIX.length)

fun getRoomNameFromFullRoomName(roomNameFull: String) = roomNameFull.split("/")[0]

fun getSubjectNameFromDeviceName(deviceName: String) = deviceName.split("/")[1]


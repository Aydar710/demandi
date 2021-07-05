package com.aydar.demandi.teacherrooms.presentation

import androidx.recyclerview.widget.DiffUtil
import com.aydar.demandi.data.model.Room

class RoomDiffCallback : DiffUtil.ItemCallback<Room>() {

    override fun areItemsTheSame(oldItem: Room, newItem: Room): Boolean =
        oldItem.name == newItem.name && oldItem.subjectName == newItem.subjectName


    override fun areContentsTheSame(oldItem: Room, newItem: Room): Boolean =
        oldItem == newItem
}
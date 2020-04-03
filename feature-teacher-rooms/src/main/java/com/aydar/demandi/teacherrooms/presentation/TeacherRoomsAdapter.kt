package com.aydar.demandi.teacherrooms.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aydar.demandi.data.model.Room
import com.aydar.demandi.teacherrooms.R
import kotlinx.android.synthetic.main.item_room.view.*

class TeacherRoomsAdapter :
    ListAdapter<Room, TeacherRoomsAdapter.RoomViewHolder>(RoomDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_room, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class RoomViewHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {

        fun bind(room: Room) {
            with(view) {
                tv_room_name.text = room.name
                tv_subject_name.text = room.subjectName
            }
        }

    }
}
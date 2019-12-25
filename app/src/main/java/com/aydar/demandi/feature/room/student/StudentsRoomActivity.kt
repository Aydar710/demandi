package com.aydar.demandi.feature.room.student

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProviders
import com.aydar.demandi.base.BaseViewModelFactory
import com.aydar.demandi.feature.join.SocketHolder
import com.aydar.demandi.feature.join.StudentsViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.bottom_sheet_ask_question.*

class StudentsRoomActivity : AppCompatActivity() {

    private lateinit var sheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private lateinit var studentsViewModel: StudentsViewModel

    private lateinit var bluetoothService: MyBluetoothService

    private lateinit var studentsThread: MyBluetoothService.ConnectedThread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.aydar.demandi.R.layout.activity_students_room)

        sheetBehavior = BottomSheetBehavior.from(bottom_sheet_ask)

        initClickListeners()

        initViewModel()

        initStudentsThread()
    }

    private fun initStudentsThread() {
        val handler = Handler()
        bluetoothService = MyBluetoothService(handler)
        studentsThread = bluetoothService
            .ConnectedThread(SocketHolder.teachersSocket)

        studentsThread.start()
    }

    private fun initClickListeners() {
        iv_bottom_sheet_header.setOnClickListener {
            toggleBottomSheet()
        }

        btn_send.setOnClickListener {
            val questionText = et_question.text.toString()
            sendQuestion(questionText)
        }
    }

    private fun sendQuestion(text: String) {
        studentsThread.write(text)
    }

    private fun initViewModel() {
        val teachersSocket = SocketHolder.teachersSocket
        studentsViewModel = ViewModelProviders.of(this, BaseViewModelFactory {
            StudentsViewModel(teachersSocket)
        })[StudentsViewModel::class.java]

    }

    private fun toggleBottomSheet() {
        if (sheetBehavior.state !== BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
        }
    }
}
package com.aydar.demandi.feature.room.student

import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProviders
import com.aydar.demandi.base.BaseBluetoothActivity
import com.aydar.demandi.base.BaseViewModelFactory
import com.aydar.demandi.base.ServiceHolder
import com.aydar.demandi.feature.join.StudentsViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.bottom_sheet_ask_question.*

class StudentRoomActivity : BaseBluetoothActivity() {

    private lateinit var sheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private lateinit var studentsViewModel: StudentsViewModel

    private lateinit var bluetoothService: StudentBluetoothService

    private lateinit var studentsThread: StudentBluetoothService.ConnectedThread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.aydar.demandi.R.layout.activity_students_room)

        sheetBehavior = BottomSheetBehavior.from(bottom_sheet_ask)

        initClickListeners()

        //initViewModel()

    }

    private fun initClickListeners() {
        iv_bottom_sheet_header.setOnClickListener {
            toggleBottomSheet()
        }

        btn_test_send.setOnClickListener {
            val questionText = et_question.text.toString()
            sendQuestion(questionText)
        }
    }

    private fun sendQuestion(text: String) {
        ServiceHolder.studentService.sendQuestion(text)
    }

    private fun initViewModel() {
        val teachersSocket = ServiceHolder.teachersSocket
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
package com.aydar.demandi.feature.room.student

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.aydar.demandi.base.BaseBluetoothActivity
import com.aydar.demandi.base.BaseViewModelFactory
import com.aydar.demandi.base.ServiceHolder
import com.aydar.demandi.data.Question
import com.aydar.demandi.feature.room.common.MESSAGE_WRITE
import com.aydar.demandi.feature.room.common.QuestionsAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.bottom_sheet_ask_question.*
import kotlinx.android.synthetic.main.content_students_room.*

class StudentRoomActivity : BaseBluetoothActivity() {

    private lateinit var sheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private lateinit var studentViewModel: StudentsViewModel

    private lateinit var adapter: QuestionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.aydar.demandi.R.layout.activity_students_room)

        sheetBehavior = BottomSheetBehavior.from(bottom_sheet_ask)

        sheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            var isDragging = false
            var isSettled = false

            @SuppressLint("SwitchIntDef")
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        isDragging = false
                        if (isSettled){
                            rv_questions.visibility = View.GONE
                        }
                        isSettled = false
                    }

                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        rv_questions.visibility = View.VISIBLE
                        isDragging = false
                    }

                    BottomSheetBehavior.STATE_DRAGGING -> {
                        isDragging = true
                    }

                    BottomSheetBehavior.STATE_SETTLING -> {
                        isSettled = true
                    }

                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        rv_questions.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(p0: View, p1: Float) {

            }
        })

        initClickListeners()

        initHandler()
        initRecycler()
        initViewModel()
        initObservers()
    }

    private fun initHandler() {
        ServiceHolder.studentService.handler = Handler {
            when (it.what) {
                MESSAGE_WRITE -> {
                    val question = Question(it.obj as String)
                    studentViewModel.addQuestion(question)
                    true
                }
                else -> false
            }
        }
    }

    private fun initObservers() {
        studentViewModel.questionsLiveData.observe(this, Observer {
            adapter.submitList(it)
        })
    }

    private fun initRecycler() {
        adapter = QuestionsAdapter()
        rv_questions.adapter = adapter
    }

    private fun initClickListeners() {
        iv_bottom_sheet_header.setOnClickListener {
            toggleBottomSheet()
        }

        btn_test_send.setOnClickListener {
            val questionText = et_question.text.toString()
            sendQuestion(questionText)
            toggleBottomSheet()
            et_question.text?.clear()
        }
    }

    private fun sendQuestion(text: String) {
        ServiceHolder.studentService.sendQuestion(text)
    }

    private fun initViewModel() {
        studentViewModel = ViewModelProviders.of(this, BaseViewModelFactory {
            StudentsViewModel()
        })[StudentsViewModel::class.java]

    }

    private fun toggleBottomSheet() {
        if (sheetBehavior.state !== BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        } else {
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }
}
package com.aydar.demandi.featurerooms.student

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.aydar.demandi.common.base.BaseBluetoothActivity
import com.aydar.demandi.common.base.BaseViewModelFactory
import com.aydar.demandi.common.base.EXTRA_ROOM_NAME
import com.aydar.demandi.common.base.MESSAGE_WRITE
import com.aydar.demandi.common.base.bluetooth.ServiceHolder
import com.aydar.demandi.data.model.Question
import com.aydar.demandi.featurerooms.R
import com.aydar.demandi.featurerooms.common.QuestionsAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_teachers_room.*
import kotlinx.android.synthetic.main.bottom_sheet_ask_question.*

class StudentRoomActivity : BaseBluetoothActivity() {

    private lateinit var sheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private lateinit var studentViewModel: StudentsViewModel

    private lateinit var adapter: QuestionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_students_room)

        tv_send.visibility = View.INVISIBLE
        initToolbar()

        sheetBehavior = BottomSheetBehavior.from(bottom_sheet_ask)

        sheetBehavior.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            var isDragging = false

            @SuppressLint("SwitchIntDef")
            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        tv_send.visibility = View.VISIBLE
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        if (isDragging) {
                            tv_send.visibility = View.INVISIBLE
                        }
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                        isDragging = true
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        initClickListeners()

        initHandler()
        initRecycler()
        initViewModel()
        initObservers()
    }

    private fun initToolbar() {
        setSupportActionBar(inc_toolbar as Toolbar)
        val roomName = intent.getStringExtra(EXTRA_ROOM_NAME)
        supportActionBar?.title = roomName
    }

    private fun initHandler() {
        ServiceHolder.studentService.handler = Handler {
            when (it.what) {
                MESSAGE_WRITE -> {
                    val question =
                        Question(it.obj as String)
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

        tv_send.setOnClickListener {
            val questionText = et_question.text.toString()
            if (questionText.isNotEmpty()) {
                hideKeyboard {
                    toggleBottomSheet()
                }
                sendQuestion(questionText)
                et_question.text?.clear()
            }
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
            tv_send.visibility = View.VISIBLE
        } else {
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            tv_send.visibility = View.INVISIBLE
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = currentFocus
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun hideKeyboard(hidden: () -> Unit) {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = currentFocus
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
        Handler().postDelayed({
            hidden.invoke()
        }, 100)
    }
}
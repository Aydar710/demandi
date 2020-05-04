package com.aydar.demandi.featurestudentroom.presentation

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.amitshekhar.DebugDB
import com.aydar.demandi.common.base.*
import com.aydar.demandi.common.base.bluetooth.ServiceHolder
import com.aydar.demandi.common.base.bluetoothcommands.CommandDeleteQuestion
import com.aydar.demandi.data.model.Like
import com.aydar.demandi.data.model.Question
import com.aydar.demandi.data.model.Room
import com.aydar.demandi.featurestudentroom.R
import com.aydar.demandi.featurestudentroom.presentation.adapter.QuestionsAdapter
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeRecyclerView
import com.ernestoyaquello.dragdropswiperecyclerview.listener.OnItemSwipeListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.bottom_sheet_ask_question.*
import kotlinx.android.synthetic.main.content_students_room.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class StudentRoomActivity : BaseBluetoothActivity() {

    private lateinit var sheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private val viewModel: StudentRoomViewModel by viewModel()

    private lateinit var adapter: QuestionsAdapter

    private val user: FirebaseUser by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_room)

        tv_send.visibility = View.INVISIBLE
        initToolbar()

        sheetBehavior = BottomSheetBehavior.from(bottom_sheet_ask)

        setBottomSheetCallback()

        initClickListeners()

        initHandler()
        initRecycler()
        initObservers()

        val addressLog = DebugDB.getAddressLog()
        Log.d("address", addressLog)
    }

    private fun setBottomSheetCallback() {
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
    }

    private fun initToolbar() {
        val roomNameFull = intent.getStringExtra(EXTRA_ROOM_NAME)
        val roomName = getRoomNameFromFullRoomName(roomNameFull)
        val toolbar = inc_toolbar as Toolbar
        toolbar.setBackgroundColor(Color.WHITE)
        toolbar.setTitleTextColor(Color.BLACK)
        toolbar.title = roomName
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun initHandler() {
        ServiceHolder.studentService.handler = Handler {
            when (it.what) {
                MESSAGE_WRITE -> {
                    val question = it.obj as Question
                    viewModel.addReceivedQuestion(question)
                    true
                }
                MESSAGE_RECEIVED_ROOM_INFO -> {
                    val room = it.obj as Room
                    viewModel.handleReceivedRoom(room)
                    true
                }
                MESSAGE_RECEIVED_QUESTION -> {
                    val question = it.obj as Question
                    viewModel.onQuestionReceived(question)
                    true
                }
                MESSAGE_RECEIVED_LIKE -> {
                    val like = it.obj as Like
                    viewModel.handleReceivedLike(like)
                    true
                }
                MESSAGE_COMMAND_DELETE_QUESTION -> {
                    val command = it.obj as CommandDeleteQuestion
                    viewModel.handleReceivedCommandDeleteQuestion(command.question)
                    true
                }
                else -> false
            }
        }
    }

    private fun initObservers() {
        viewModel.questionsLiveData.observe(this, Observer {
            adapter.submitList(it)
        })
    }

    private fun initRecycler() {
        adapter = QuestionsAdapter(onAnswerClickListener = {
            viewModel.sendQuestion(it)
        }, onLikeClicked = {
            viewModel.handleLike(it.id)
        }, userId = user.uid)
        val recycler = findViewById<DragDropSwipeRecyclerView>(R.id.rv_questions)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
        recycler.orientation =
            DragDropSwipeRecyclerView.ListOrientation.VERTICAL_LIST_WITH_VERTICAL_DRAGGING

        recycler.disableSwipeDirection(DragDropSwipeRecyclerView.ListOrientation.DirectionFlag.RIGHT)

        val onItemSwipeListener = object : OnItemSwipeListener<Question> {
            override fun onItemSwiped(
                position: Int,
                direction: OnItemSwipeListener.SwipeDirection,
                item: Question
            ): Boolean {
                viewModel.onItemSwipedLeft(item)
                return false
            }
        }
        recycler.swipeListener = onItemSwipeListener
    }

    private fun initClickListeners() {
        iv_bottom_sheet_header.setOnClickListener {
            toggleBottomSheet()
        }

        tv_send.setOnClickListener {
            val question = Question(
                id = Date().time.toString(),
                text = et_question.text.toString(),
                visibleToOthers = cb_visible_to_others.isChecked
            )
            if (question.text.isNotEmpty()) {
                hideKeyboard {
                    toggleBottomSheet()
                }
                viewModel.sendQuestion(question)
                et_question.text?.clear()
            }
        }
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
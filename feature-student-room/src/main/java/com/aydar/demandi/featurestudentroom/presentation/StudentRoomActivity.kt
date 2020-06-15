package com.aydar.demandi.featurestudentroom.presentation

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.amitshekhar.DebugDB
import com.aydar.demandi.common.base.*
import com.aydar.demandi.common.base.bluetooth.teacher.StudentServiceFacade
import com.aydar.demandi.common.base.bluetoothmessages.*
import com.aydar.demandi.data.model.AnswerLike
import com.aydar.demandi.data.model.Message
import com.aydar.demandi.data.model.Question
import com.aydar.demandi.featurestudentroom.R
import com.aydar.demandi.featurestudentroom.presentation.adapter.QuestionsAdapter
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeRecyclerView
import com.ernestoyaquello.dragdropswiperecyclerview.listener.OnItemSwipeListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_student_room.*
import kotlinx.android.synthetic.main.bottom_sheet_ask_question.*
import kotlinx.android.synthetic.main.content_students_room.*
import kotlinx.android.synthetic.main.item_question_student.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class StudentRoomActivity : BaseBluetoothActivity() {

    private val studentServiceFacade: StudentServiceFacade by inject()
    private lateinit var sheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private val viewModel: StudentRoomViewModel by viewModel()
    private lateinit var adapter: QuestionsAdapter
    private val user: FirebaseUser by inject()
    private lateinit var snackbarLostConnection: Snackbar
    private val sharedPref: SharedPrefWrapper by inject()
    private lateinit var progressDialog: ProgressDialog

    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    Log.d("Bl", "Device found")
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val teacherAddress = sharedPref.getLastConnectedTeacherAddress()
                    if (device?.address == teacherAddress) {
                        connectToDevice(device!!)
                    }
                }
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {

                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {

                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_room)

        tv_send.visibility = View.INVISIBLE
        initToolbar()

        sheetBehavior = BottomSheetBehavior.from(bottom_sheet_ask)

        setBottomSheetCallback()

        initClickListeners()

        val handler = initHandler()
        studentServiceFacade.setHandler(handler)
        initRecycler()
        initObservers()

        val addressLog = DebugDB.getAddressLog()
        Log.d("address", addressLog)
        initLostConnectionSnackbar()

        initProgress()
    }

    private fun initProgress() {
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Переподключение")
        progressDialog.setMessage("Пожалуйста, подождите")
        progressDialog.isIndeterminate = true
    }

    private fun registerFoundReceiver() {
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver, filter)
    }

    private fun connectToDevice(device: BluetoothDevice) {
        val handler = initHandler()
        studentServiceFacade.startConnecting(device, handler)
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

    private fun initHandler(): Handler {
        return Handler {
            if (it.what == 12345) {
                when (val message = it.obj as Message) {
                    is MessageSendQuestion -> {
                        viewModel.onQuestionReceived(message.question)
                        true
                    }
                    is MessageSendRoomInfo -> {
                        viewModel.handleReceivedRoom(message.room)
                        true
                    }
                    is MessageSendQuestionLike -> {
                        viewModel.handleReceivedQuestionLike(message.questionLike)
                        true
                    }
                    is MessageDeleteQuestion -> {
                        viewModel.handleReceivedCommandDeleteQuestion(message.question)
                        true
                    }
                    is MessageSendAnswer -> {
                        adapter.addAnswer(message.answer)
                        true
                    }
                    is MessageSendAnswerLike -> {
                        viewModel.handleReceivedAnswerLike(message.answerLike)
                        true
                    }
                    else -> false
                }
            } else {
                when (it.what) {
                    MESSAGE_WRITE -> {
                        val question = (it.obj as MessageSendQuestion).question
                        viewModel.addReceivedQuestion(question)
                        true
                    }
                    MESSAGE_SOCKET_DISCONNECTED -> {
                        snackbarLostConnection.show()
                        true
                    }
                    MESSAGE_SHOW_DIALOG -> {
                        showProgress()
                        true
                    }
                    MESSAGE_CONNECTED_TO_ROOM -> {
                        hideProgress()
                        true
                    }
                    else -> false
                }
            }
/*
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
                MESSAGE_RECEIVED_QUESTION_LIKE -> {
                    val like = it.obj as QuestionLike
                    viewModel.handleReceivedQuestionLike(like)
                    true
                }
                MESSAGE_COMMAND_DELETE_QUESTION -> {
                    val command = it.obj as MessageDeleteQuestion
                    viewModel.handleReceivedCommandDeleteQuestion(command.question)
                    true
                }
                MESSAGE_ANSWER -> {
                    val answer = it.obj as Answer
                    adapter.addAnswer(answer)
                    true
                }
                MESSAGE_RECEIVED_ANSWER_LIKE -> {
                    val answerLike = it.obj as AnswerLike
                    viewModel.handleReceivedAnswerLike(answerLike)
                    true
                }
                MESSAGE_SOCKET_DISCONNECTED -> {
                    snackbarLostConnection.show()
                    true
                }
                MESSAGE_SHOW_DIALOG -> {
                    showProgress()
                    true
                }
                MESSAGE_CONNECTED_TO_ROOM -> {
                    hideProgress()
                    true
                }
                else -> false
            }
*/
        }
    }

    private fun initObservers() {
        viewModel.questionsLiveData.observe(this, Observer {
            adapter.submitList(it)
        })
    }

    private fun initRecycler() {
        adapter = QuestionsAdapter(onAnswerClickListener = {
            viewModel.sendAnswer(it)
        }, onQuestionLikeClickListener = {
            viewModel.handleQuestionLike(it.id)
        }, userId = user.uid,
            onQuestionClickListener = { constraintAnswer, constraintQuestion ->
                if (constraintAnswer.visibility === View.GONE) {
                    expandAnswers(constraintAnswer, constraintQuestion)
                } else {
                    collapseAnswers(constraintAnswer, constraintQuestion)
                }
            }, onAnswerLikeClickListener = {
                viewModel.handleAnswerLike(AnswerLike(it.id, user.uid))
            })
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

    private fun initLostConnectionSnackbar() {
        snackbarLostConnection = Snackbar.make(
            cl_student_room,
            getString(R.string.lost_connection),
            Snackbar.LENGTH_INDEFINITE
        ).setAction(getString(R.string.reconnect)) {
            //viewModel.reconnect()
            registerFoundReceiver()
            bluetoothAdapter.startDiscovery()
            showProgress()
        }
    }

    private fun collapseAnswers(
        constraintAnswer: View,
        constraintQuestion: View
    ) {
        constraintAnswer.visibility = View.GONE
        TransitionManager.beginDelayedTransition(
            ll_question_answer,
            AutoTransition()
        )
        constraintQuestion.background =
            getDrawable(R.drawable.rounded_rectangle_question)
    }

    private fun expandAnswers(
        constraintAnswer: View,
        constraintQuestion: View
    ) {
        constraintAnswer.visibility = View.VISIBLE
        TransitionManager.beginDelayedTransition(
            ll_question_answer,
            AutoTransition()
        )
        constraintQuestion.background =
            getDrawable(R.drawable.rounded_rectangle_question_expanded)
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

    private fun showProgress() {
        progressDialog.show()
    }

    private fun hideProgress() {
        progressDialog.dismiss()
    }

}
package com.aydar.featureteacherroom.presentation

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable
import com.airbnb.lottie.LottieTask
import com.aydar.demandi.common.base.*
import com.aydar.demandi.common.base.bluetooth.TeacherBluetoothService
import com.aydar.demandi.data.model.Question
import com.aydar.demandi.data.model.QuestionLike
import com.aydar.demandi.data.model.Room
import com.aydar.featureteacherroom.R
import com.aydar.featureteacherroom.presentation.adapter.QuestionsAdapter
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeRecyclerView
import com.ernestoyaquello.dragdropswiperecyclerview.listener.OnItemSwipeListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_teacher_room.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*


class TeacherRoomActivity : BaseBluetoothActivity() {

    private val teacherService: TeacherBluetoothService by inject()
    private val viewModel: TeacherRoomViewModel by viewModel()
    private lateinit var adapter: QuestionsAdapter
    private lateinit var room: Room

    private var animateBluetoothIcon: LottieDrawable? = null
    private var bluetoothMenuItem: MenuItem? = null
    private lateinit var tvCountDownTimer: TextView
    private lateinit var timerDiscovering: CountDownTimer
    private lateinit var snackbarBluetoothOff: Snackbar
    private lateinit var receiver: BroadcastReceiver
    private var doubleBackToExitPressedOnce = false

    private val onItemSwipeListener = object : OnItemSwipeListener<Question> {
        override fun onItemSwiped(
            position: Int,
            direction: OnItemSwipeListener.SwipeDirection,
            item: Question
        ): Boolean {
            if (direction == OnItemSwipeListener.SwipeDirection.RIGHT_TO_LEFT) {
                viewModel.deleteQuestion(item)
                return false
            }
            return true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_room)

        initToolbar()

        initRecycler()
        initObservers()
        initHandler()
        initBluetoothTurnedOffSnackbar()
        initBroadcastReceiver()
        viewModel.saveSession()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_teacher_room, menu)
        val lottieTask: LottieTask<LottieComposition> =
            LottieCompositionFactory.fromAsset(this, "bluetooth_discoverability_animation.json")

        lottieTask.addListener {
            animateBluetoothIcon = LottieDrawable()
            animateBluetoothIcon?.composition = it
            animateBluetoothIcon?.repeatCount = LottieDrawable.INFINITE
            animateBluetoothIcon?.scale = 0.07f
            val bluetoothItem = menu?.findItem(R.id.action_discover)
            bluetoothItem?.icon = animateBluetoothIcon
            animateBluetoothIcon?.playAnimation()
            bluetoothMenuItem = bluetoothItem
        }

        val timerItem = menu?.findItem(R.id.action_timer)
        tvCountDownTimer = timerItem?.actionView as TextView

        initCountDownTimer()
        timerDiscovering.start()

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_discover -> {
                requestDiscoverable()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_DISCOVERABLE) {
            if (resultCode == DISCOVERABLE_DURATION_SEC) {
                animateBluetoothIcon?.playAnimation()
                timerDiscovering.start()
                changeDeviceName(room)
                viewModel.openRoomConnection()
            } else {
                if (!bluetoothAdapter.isEnabled) {
                    snackbarBluetoothOff.show()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
        } else {
            doubleBackToExitPressedOnce = true
            Toast.makeText(
                this,
                getString(R.string.press_back_again_to_close_room),
                Toast.LENGTH_SHORT
            ).show()

            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        }

    }

    private fun initToolbar() {
        val room = intent.getSerializableExtra(EXTRA_ROOM) as Room
        val toolbar = inc_toolbar as androidx.appcompat.widget.Toolbar
        toolbar.setBackgroundColor(Color.WHITE)
        toolbar.title = getRoomNameFromFullRoomName(room.name)
        toolbar.setTitleTextColor(Color.BLACK)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        this.room = room
        viewModel.room = room
    }

    private fun initObservers() {
        viewModel.questionsLiveData.observe(this, Observer {
            adapter.submitList(it)
        })
    }

    private fun initRecycler() {
        adapter =
            QuestionsAdapter(
                onAnswerClickListener = {
                },
                onLikeClicked = {})
        rv_questions.adapter = adapter
        rv_questions.swipeListener = onItemSwipeListener
        rv_questions.disableSwipeDirection(DragDropSwipeRecyclerView.ListOrientation.DirectionFlag.RIGHT)
    }

    private fun initHandler() {
        teacherService.handler = Handler {
            when (it.what) {
                MESSAGE_READ -> {
                    val question = it.obj as Question
                    viewModel.addQuestion(question)
                    true
                }
                MESSAGE_RECEIVED_QUESTION_LIKE -> {
                    val like = it.obj as QuestionLike
                    viewModel.handleLike(like)
                    true
                }
                else -> false
            }
        }
    }

    private fun initCountDownTimer() {
        timerDiscovering = object : CountDownTimer(DISCOVERABLE_DURATION_MILLIS.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val mTimeFormat = SimpleDateFormat("mm:ss")
                tvCountDownTimer.text = mTimeFormat.format(Date(millisUntilFinished))
            }

            override fun onFinish() {
                cancelTimer()
            }
        }
    }

    private fun cancelTimer() {
        timerDiscovering.cancel()
        tvCountDownTimer.text = ""
        animateBluetoothIcon?.endAnimation()
        animateBluetoothIcon?.progress = 0f
    }

    private fun initBluetoothTurnedOffSnackbar() {
        snackbarBluetoothOff =
            Snackbar.make(
                activity_teacher_room,
                getString(R.string.bluetooth_turned_off),
                Snackbar.LENGTH_INDEFINITE
            ).setAction(getString(R.string.turn_on)) {
                requestDiscoverable()
            }
    }

    private fun initBroadcastReceiver() {

        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                    val state = intent.getIntExtra(
                        BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR
                    )

                    when (state) {
                        BluetoothAdapter.STATE_OFF -> {
                            snackbarBluetoothOff.show()
                            cancelTimer()

                        }
                        BluetoothAdapter.STATE_ON -> {
                            snackbarBluetoothOff.dismiss()
                        }
                    }
                }
            }

        }

        val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        registerReceiver(receiver, filter)
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        bluetoothAdapter.name = sharedPrefWrapperDeviceName.getDeviceName()
        sharedPrefWrapperDeviceName.deleteDeviceName()
        super.onDestroy()
    }
}

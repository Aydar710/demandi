package com.aydar.demandi.featuremain

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val router: MainRouter by inject()
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tv_rooms.setOnClickListener {
            router.moveToTeacherRoomsActivity(this)
        }

        tv_join.setOnClickListener {
            router.moveToJoinRoomActivity(this)
        }

        initToolbar()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mnu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_logout -> {
                showLogoutDialog()
            }
        }
        return true
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.exit_account))
            setMessage(getString(R.string.accept_exit))
            setPositiveButton(
                getString(R.string.yes)
            ) { dialog, _ ->
                logout()
                dialog.dismiss()
            }
            setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
        }.create().show()
    }

    private fun logout() {
        firebaseAuth.signOut()
        router.moveToLoginActivity(this)
    }

    private fun initToolbar() {
        val toolbar = inc_toolbar as Toolbar
        toolbar.setBackgroundColor(Color.WHITE)
        toolbar.title = getString(R.string.app_name)
        toolbar.setTitleTextColor(Color.BLACK)
        setSupportActionBar(toolbar)
    }
}

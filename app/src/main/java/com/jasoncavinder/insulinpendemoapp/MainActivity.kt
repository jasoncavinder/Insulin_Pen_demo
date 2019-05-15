/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.jasoncavinder.insulinpendemoapp.utilities.AUTHORIZE_USER
import com.jasoncavinder.insulinpendemoapp.utilities.Result
import com.jasoncavinder.insulinpendemoapp.utilities.UpdateToolbarListener
import com.jasoncavinder.insulinpendemoapp.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.custom_toolbar.*


class MainActivity : AppCompatActivity(), UpdateToolbarListener {

    val TAG: String by lazy { this::class.java.simpleName }

    private lateinit var _mainViewModel: MainViewModel

    private lateinit var navController: NavController

    private var titleText = ""
    private var leftMenu = R.menu.menu_home_left
    private var rightMenu = R.menu.menu_home_right
    private var menuActions = mapOf(
        Pair(R.id.menu_item_profile_settings, R.id.action_nav_fail_safe),
        Pair(R.id.menu_item_dose_history, R.id.action_nav_fail_safe)
    )

    private inner class VerifyLogin : Observer<Result<String>> {
        override fun onChanged(it: Result<String>?) {
            when (it) {
                is Result.Error -> {
                    Log.d(TAG, "No login detected. Redirecting to Login")
                    startActivityForResult(
                        Intent(this@MainActivity, LoginActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            .addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION),
                        AUTHORIZE_USER
                    )
                    _mainViewModel.loginResult.removeObservers(this@MainActivity)
                }
                is Result.Success -> {
                    Log.d(TAG, "Valid login detected. Continuing")
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        _mainViewModel = ViewModelProviders
            .of(this)
            .get(MainViewModel::class.java)

        // require login to access
        _mainViewModel.loginResult.observe(this, VerifyLogin())

        navController = findNavController(R.id.nav_host_main)

    }


    override fun onResume() {
        super.onResume()

        _mainViewModel.loginResult.observe(this, VerifyLogin())

    }

    override fun onPause() {
        super.onPause()

        _mainViewModel.loginResult.removeObservers(this)
    }


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        when (requestCode) {
//            AUTHORIZE_USER -> when (resultCode) {
//                Activity.RESULT_OK -> {
//                } // TODO
//                else -> {
//                } // TODO
//            }
//        }
//    }

    override fun onResumeFragments() {
        super.onResumeFragments()

        _mainViewModel.verifyLogin()
    }

    private fun attachAction(menuItem: MenuItem) {
        menuItem.setOnMenuItemClickListener {

            menuActions[menuItem.itemId]?.let {
                navController.navigate(it)
                return@setOnMenuItemClickListener true
            }
            return@setOnMenuItemClickListener false
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {

        text_title.text = titleText
        menu_left.menu.clear()
        menuInflater.inflate(leftMenu, menu_left.menu)
        menu_left.menu.forEach { attachAction(it) }
        menu_right.menu.clear()
        menuInflater.inflate(rightMenu, menu_right.menu)
        menu_right.menu.forEach { attachAction(it) }


//        menu_left.menu.forEach { menuItem ->
//            menuItem.setOnMenuItemClickListener { item ->
//                onMenuItemClicked(item)
//            }
//        }

        return true
    }

    override fun updateToolbar(title: String, left_menu: Int, right_menu: Int, menu_actions: Map<Int, Int>) {
        titleText = title
        leftMenu = left_menu
        rightMenu = right_menu
        menuActions = menu_actions

        onPrepareOptionsMenu(menu = null)
    }

}
//view.back_button.setOnClickListener(
//Navigation.createNavigateOnClickListener(
//R.id.action_dest_createUserFragment_pop, null))
//view.button_create_user.setOnClickListener(
//Navigation.createNavigateOnClickListener(
//R.id.action_global_home,null))
//
// setup toolbar
//nav_right.visibility = View.GONE
//if (action_bar_nav_left.hasOnClickListeners()) action_bar_nav_left.setOnClickListener { null }
//action_bar_nav_left.setImageDrawable(R.drawable.ic_arrow_back_black_24dp)
////        view.button_fingerprint.setOnClickListener(
////            Navigation.createNavigateOnClickListener(
////                R.id.action_global_home, null))
//
// Setup toggle listeners
//view.findViewById<ImageView>(R.id.icon_message_toggle)
//.setOnClickListener {
//    toggleVisibility(
//        group_message_display,
//        R.drawable.more_unfold_24dp,
//        R.drawable.less_unfold_24dp,
//        it as ImageView )}
//view.findViewById<ImageView>(R.id.icon_report_toggle)
//.setOnClickListener {
//    toggleVisibility(
//        group_report_long,
//        R.drawable.more_unfold_24dp,
//        R.drawable.less_unfold_24dp,
//        it as ImageView)
//    toggleVisibility(
//        group_report_short,null,null,null) }

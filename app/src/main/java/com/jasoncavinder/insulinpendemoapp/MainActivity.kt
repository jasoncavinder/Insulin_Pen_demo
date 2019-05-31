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
import com.jasoncavinder.insulinpendemoapp.ui.main.HomeFragment
import com.jasoncavinder.insulinpendemoapp.utilities.AUTHORIZE_USER
import com.jasoncavinder.insulinpendemoapp.utilities.MenuSafeClick
import com.jasoncavinder.insulinpendemoapp.utilities.Result
import com.jasoncavinder.insulinpendemoapp.utilities.UpdateToolbarListener
import com.jasoncavinder.insulinpendemoapp.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.custom_toolbar.*


class MainActivity : AppCompatActivity(), UpdateToolbarListener, HomeFragment.OnMessageSummaryInteractionListener {

    val TAG: String by lazy { this::class.java.simpleName }

    private lateinit var viewModel: MainViewModel

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
            when (it ?: return) {
                is Result.Error -> {
                    Log.d(TAG, "No login detected. Redirecting to Login")
                    startActivityForResult(
                        Intent(this@MainActivity, LoginActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            .addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION)
                            .putExtra("fromMain", true),
                        AUTHORIZE_USER
                    )
                }
                is Result.Success -> {
                    Log.d(TAG, "Valid login detected. Continuing")
                }
            }
//            viewModel.loginResult.removeObservers(this@MainActivity)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders
            .of(this)
            .get(MainViewModel::class.java)

        viewModel.loginResult.observe(this, VerifyLogin())

        navController = findNavController(R.id.nav_host_main)

    }


    override fun onResume() {
        super.onResume()

        // require login to access
        viewModel.loginResult.observe(this, VerifyLogin())

    }

    override fun onPause() {
        super.onPause()

        viewModel.loginResult.removeObservers(this)
    }


    override fun onResumeFragments() {
        super.onResumeFragments()

        viewModel.verifyLogin()
    }

    private fun attachAction(menuItem: MenuItem) {
        menuItem.setOnMenuItemClickListener(MenuSafeClick {
            menuActions[menuItem.itemId]?.let { navController.navigate(it) }
        })

    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {

        text_title.text = titleText
        menu_left.menu.clear()
        menuInflater.inflate(leftMenu, menu_left.menu)
        menu_left.menu.forEach { attachAction(it) }
        menu_right.menu.clear()
        menuInflater.inflate(rightMenu, menu_right.menu)
        menu_right.menu.forEach { attachAction(it) }

        return true
    }

    override fun onMessageSummaryInteraction() {
        navController.navigate(R.id.action_homeFragment_to_messageFragment)
    }

    override fun updateToolbar(title: String, left_menu: Int, right_menu: Int, menu_actions: Map<Int, Int>) {
        titleText = title
        leftMenu = left_menu
        rightMenu = right_menu
        menuActions = menu_actions

        onPrepareOptionsMenu(menu = null)
    }

}

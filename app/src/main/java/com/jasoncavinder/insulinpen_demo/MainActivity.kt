package com.jasoncavinder.insulinpen_demo

import android.os.Bundle
//import android.support.v7.app.AppCompatActivity
//import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_toolbar.*


class MainActivity : AppCompatActivity(),

    HomeFragment.updateToolbarListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(mainToolbar)
        setupActionBarWithNavController(findNavController(R.id.nav_host_main))
//        mainToolbar.setNavigationIcon(R.drawable.ic_account_circle_foreground_24dp)

        supportActionBar?.setHomeButtonEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)
//        actionBar?.setDisplayShowHomeEnabled(false)
//        actionBar?.setDisplayShowTitleEnabled(false)

        // Setup Toolbar
        val customToolbarView = LayoutInflater.from(this)
            .inflate(R.layout.custom_toolbar, null)
        customToolbarView.findViewById<TextView>(R.id.action_bar_title_text).text = getString(R.string.title_activity_main)
//        customToolbarView.findViewById<ImageView>(R.id.action_bar_nav_left)
//            .setOnClickListener { goLeft() }
//        customToolbarView.findViewById<ImageView>(R.id.nav_right)
//            .setOnClickListener { goRight() }
        supportActionBar?.customView = customToolbarView
        supportActionBar?.setDisplayShowCustomEnabled(true)

        // Setup Fast Action Button
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Ask me about this later!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

//        (nav_host_main.view?.let { Navigation.findNavController(it) })
//            .addOnDestinationChangedListener { controller, destination, arguments ->  }

//        Toast.makeText(this, supportActionBar?.displayOptions.toString(), Toast.LENGTH_LONG).show()

    }

    fun goLeft() {
        TODO()
    }
    fun goRight() {
        TODO()
    }

    override fun updateToolbar(title: String, leftIcon: Int?, leftAction: Int?, rightIcon: Int?, rightAction: Int?) {
        fun swapIn(icon: ImageView, src: Int?, action: Int?) {
            if (icon.hasOnClickListeners()) icon.setOnClickListener {  }
            src?.let { icon.setImageResource(it) }
            action?.let {
//                var navController = findNavController(R.id.nav_host_main)
                icon.setOnClickListener ( Navigation.createNavigateOnClickListener(action))
                icon.isClickable = true
            }
            icon.visibility = if (icon.hasOnClickListeners()) View.VISIBLE else View.INVISIBLE
        }

//        action_bar_title_text.text = title
//        swapIn(action_bar_nav_left, leftIcon, leftAction)
//        swapIn(action_bar_nav_right, rightIcon, rightAction)
    }

}
//view.back_button.setOnClickListener(
//Navigation.createNavigateOnClickListener(
//R.id.action_dest_createUserFragment_pop, null))
//view.button_create_user.setOnClickListener(
//Navigation.createNavigateOnClickListener(
//R.id.action_global_home,null))

// setup toolbar
//nav_right.visibility = View.GONE
//if (action_bar_nav_left.hasOnClickListeners()) action_bar_nav_left.setOnClickListener { null }
//action_bar_nav_left.setImageDrawable(R.drawable.ic_arrow_back_black_24dp)
////        view.button_fingerprint.setOnClickListener(
////            Navigation.createNavigateOnClickListener(
////                R.id.action_global_home, null))

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

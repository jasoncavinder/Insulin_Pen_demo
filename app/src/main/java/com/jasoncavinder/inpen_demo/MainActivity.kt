package com.jasoncavinder.inpen_demo

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.jasoncavinder.inpen_demo.ui.UserViewModel
import com.jasoncavinder.inpen_demo.ui.UserViewModel.AuthenticationState.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val contentView: View = findViewById(android.R.id.content)

        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)


        navController = findNavController(R.id.nav_host_main)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        findViewById<Toolbar>(R.id.toolbar)
            .setupWithNavController(navController, appBarConfiguration)
//        val toolbar = findViewById<Toolbar>(R.id.toolbar)
//        setSupportActionBar(toolbar)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment -> redecorate(DecorStyle.FULL_SCREEN_AMBER)
                R.id.createUserFragment -> redecorate(DecorStyle.FULL_SCREEN_AMBER)
                else -> redecorate(DecorStyle.NORMAL)
            }
        }
        userViewModel.authenticationState.observe(this, Observer {
            //            Snackbar.make(contentView, "Auth: ${it.toString()}", Snackbar.LENGTH_LONG).show()
            when (it) {
                AUTHENTICATED -> {
                    navController.popBackStack(R.id.loginFragment, true)
                }
                INVALID_AUTHENTICATION -> {
//                    userViewModel.getProfile().observe().
//                    Snackbar.make(contentView, userViewModel.getProfile(), Snackbar.LENGTH_LONG).show()
                }
                UNAUTHENTICATED -> {
                    navController.navigate(R.id.loginFragment)
                }
                REGISTERING -> {
//                    navController.navigate(R.id.createUserFragment)
                }
                ATTEMPTING_AUTHENTICATION -> {
                }
                else -> {
                    navController.navigate(R.id.loginFragment)
                }
            }
        })
//        userViewModel.user.observe(this, Observer {
//            Snackbar.make(contentView, "User: ${userViewModel.user.value.toString()}", Snackbar.LENGTH_LONG).show()
//        })

//        setSupportActionBar(toolbar)
//        setupActionBarWithNavController(findNavController(R.id.nav_host_main))
//        toolbar.setNavigationIcon(R.drawable.ic_account_circle_foreground_24dp)

//        supportActionBar?.setHomeButtonEnabled(false)
//        supportActionBar?.setDisplayShowHomeEnabled(false)
//        supportActionBar?.setDisplayShowTitleEnabled(false)
//        actionBar?.setDisplayShowHomeEnabled(false)
//        actionBar?.setDisplayShowTitleEnabled(false)

        // Setup Toolbar
//        val customToolbarView = LayoutInflater.from(this)
//            .inflate(R.layout.custom_toolbar, null)
//        customToolbarView.findViewById<TextView>(R.id.action_bar_title_text).text = getString(R.string.title_activity_main)
//        customToolbarView.findViewById<ImageView>(R.id.action_bar_nav_left)
//            .setOnClickListener { goLeft() }
//        customToolbarView.findViewById<ImageView>(R.id.nav_right)
//            .setOnClickListener { goRight() }
//        supportActionBar?.customView = customToolbarView
//        supportActionBar?.setDisplayShowCustomEnabled(true)

        // Setup Fast Action Button
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Ask me about this later!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

//        (nav_host_main.view?.let { Navigation.findNavController(it) })
//            .addOnDestinationChangedListener { controller, destination, arguments ->  }

//        Toast.makeText(this, supportActionBar?.displayOptions.toString(), Toast.LENGTH_LONG).show()

    }

    enum class DecorStyle { NORMAL, FULL_SCREEN_AMBER, NORMAL_NO_FAB }

    private fun redecorate(style: DecorStyle) {
        when (style) {
            DecorStyle.FULL_SCREEN_AMBER -> {
                toolbar.visibility = View.GONE
                fab.hide()
//                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    window.statusBarColor = ContextCompat.getColor(this, R.color.accentColor)
                } else {
                    window.statusBarColor = ContextCompat.getColor(this, R.color.secondaryColor_900)
                }
            }
            DecorStyle.NORMAL_NO_FAB -> {
                toolbar.visibility = View.VISIBLE
                fab.hide()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    window.decorView.systemUiVisibility =
                        window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                }
                window.statusBarColor = ContextCompat.getColor(this, R.color.primaryDarkColor)
            }
            DecorStyle.NORMAL -> {
                toolbar.visibility = View.VISIBLE
                fab.show()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    window.decorView.systemUiVisibility =
                        window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                }
                window.statusBarColor = ContextCompat.getColor(this, R.color.primaryDarkColor)
            }
        }
    }


//    override fun updateToolbar(title: String, leftIcon: Int?, leftAction: Int?, rightIcon: Int?, rightAction: Int?) {
//        fun swapIn(icon: ImageView, src: Int?, action: Int?) {
//            if (icon.hasOnClickListeners()) icon.setOnClickListener {  }
//            src?.let { icon.setImageResource(it) }
//            action?.let {
////                var navController = findNavController(R.id.nav_host_main)
//                icon.setOnClickListener ( Navigation.createNavigateOnClickListener(action))
//                icon.isClickable = true
//            }
//            icon.visibility = if (icon.hasOnClickListeners()) View.VISIBLE else View.INVISIBLE
//        }
//
////        action_bar_title_text.text = title
////        swapIn(action_bar_nav_left, leftIcon, leftAction)
////        swapIn(action_bar_nav_right, rightIcon, rightAction)
//    }

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

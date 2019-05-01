package com.jasoncavinder.inpen.demo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ActionMenuView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.forEach
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.jasoncavinder.inpen.demo.data.UserProfileViewModel
import com.jasoncavinder.inpen.demo.utilities.AppAccess
import com.jasoncavinder.inpen.demo.utilities.UpdateToolbarListener
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), UpdateToolbarListener {

    val AUTHORIZE_USER = 65535

    private lateinit var userProfileViewModel: UserProfileViewModel

    private lateinit var navController: NavController

    private val appAccess = AppAccess()

    lateinit var leftMenuView: ActionMenuView
    lateinit var rightMenuView: ActionMenuView
    lateinit var titleTextView: TextView
    lateinit var toolbar: ConstraintLayout
    private var leftMenu = R.menu.menu_home_left
    private var rightMenu = R.menu.menu_home_right
    private var title = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userProfileViewModel = UserProfileViewModel(application)

        navController = findNavController(R.id.nav_host_main)

        leftMenuView = findViewById(R.id.menu_left)
        rightMenuView = findViewById(R.id.menu_right)
        titleTextView = findViewById(R.id.title)
        toolbar = findViewById(R.id.toolbar)

        /* Couldn't get two ActionMenuViews working inside one toolbar... So much lost sleep on Google
        // Setup toolbar
//        val toolbar = findViewById<Toolbar>(R.id.toolbar)
//        setSupportActionBar(toolbar)
//
//        supportActionBar?.setDisplayShowHomeEnabled(false)
//        supportActionBar?.setDisplayShowTitleEnabled(false)
//        supportActionBar?.setHomeButtonEnabled(false)

//        val inflater = LayoutInflater.from(this)
//        val customView = inflater.inflate(R.layout.custom_toolbar, toolbar)
//        supportActionBar?.customView = customView
//        supportActionBar?.setDisplayShowCustomEnabled(true)

//        toolbarTitleView = findViewById(R.id.toolbar_title)
//        leftMenuView = findViewById(R.id.menu_left)
//        rightMenuView = findViewById(R.id.menu_right)
//        toolbarTitle = customView.findViewById<TextView>(R.id.toolbar_title).text
//        leftMenu = customView.findViewById<ActionMenuView>(R.id.menu_left).menu
//        rightMenu = customView.findViewById<ActionMenuView>(R.id.menu_right).menu


//        toolbar.setupWithNavController(navController, AppBarConfiguration(navController.graph))
        */

/*        // Setup data
        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

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
        })*/

/* TODO: Eliminate programmatic styling

        // Setup navigation
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment -> redecorate(DecorStyle.FULL_SCREEN_AMBER)
                R.id.createUserFragment -> redecorate(DecorStyle.FULL_SCREEN_AMBER)
                else -> redecorate(DecorStyle.NORMAL)
            }
        }
 */


        // Setup Fast Action Button
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Ask me about this later!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            AUTHORIZE_USER -> when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.getStringExtra("userID")?.let {
                        userProfileViewModel.loadUser(it)
                    }
                }
            }
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()

        checkAuthentication()
    }

/*
    override fun onResume() {
        super.onResume()

        checkAuthentication()
    }
*/

    private fun checkAuthentication() {
        if (!userProfileViewModel.isAuthenticated()) {
            startActivityForResult(
                Intent(this, LoginActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    .addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION)
                    .putExtra("userID", userProfileViewModel.user.value?.userID ?: "")
                , AUTHORIZE_USER
            )
        }
    }

    private fun onMenuItemClicked(item: MenuItem?): Boolean {
        navController.navigate(
            // TODO: Add remaining menuItem/navigation combinations
            when (item?.itemId) {
                R.id.profile_settings -> R.id.action_homeFragment_to_profile
                else -> R.id.action_nav_fail_safe
            }
        )
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
//        updateToolbar(title, leftMenu, rightMenu)

        titleTextView.text = title
        leftMenuView.menu.clear()
        menuInflater.inflate(leftMenu, leftMenuView.menu)
        rightMenuView.menu.clear()
        menuInflater.inflate(rightMenu, rightMenuView.menu)

        leftMenuView.menu.forEach { menuItem ->
            menuItem.setOnMenuItemClickListener { item ->
                onMenuItemClicked(item)
            }
        }

        return true
    }

    private fun verifyUser() {
        return
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        updateToolbar(title, leftMenu, rightMenu)
//
//        return true
//    }


/* TODO: Eliminate programmatic styling

    enum class DecorStyle { NORMAL, FULL_SCREEN_AMBER, NORMAL_NO_FAB }

    private fun redecorate(style: DecorStyle) {
        when (style) {
            DecorStyle.FULL_SCREEN_AMBER -> {
                toolbar.visibility = View.GONE
                fab.hide()
//                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    window.statusBarColor = ContextCompat.getColor(this, R.color.accentColor)
            }
            DecorStyle.NORMAL_NO_FAB -> {
                toolbar.visibility = View.VISIBLE
                fab.hide()
                    window.decorView.systemUiVisibility =
                        window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                window.statusBarColor = ContextCompat.getColor(this, R.color.primaryDarkColor)
            }
            DecorStyle.NORMAL -> {
                toolbar.visibility = View.VISIBLE
                fab.show()
                    window.decorView.systemUiVisibility =
                        window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                window.statusBarColor = ContextCompat.getColor(this, R.color.primaryDarkColor)
            }
        }
    }
*/

    override fun updateToolbar(title: String, left_menu: Int, right_menu: Int) {
        this.title = title
        this.leftMenu = left_menu
        this.rightMenu = right_menu

        onPrepareOptionsMenu(menu = null)
//        titleTextView.text = title
//        leftMenuView.menu.clear()
//        menuInflater.inflate(left_menu, leftMenuView.menu)
//        rightMenuView.menu.clear()
//        menuInflater.inflate(right_menu, rightMenuView.menu)
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

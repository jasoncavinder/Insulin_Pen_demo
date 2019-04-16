package com.jasoncavinder.insulinpen_demo

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import android.util.TypedValue.COMPLEX_UNIT_DIP
import android.util.TypedValue.applyDimension
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.Window
import androidx.navigation.NavController
import androidx.navigation.NavHost
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_home_create_user.view.*


class LoginActivity : AppCompatActivity(), NavHost,
    FingerprintLoginFragment.OnFragmentInteractionListener,
    LoginMainFragment.OnFragmentInteractionListener,
    LoginUserPassFragment.OnFragmentInteractionListener,
    CreateUserFragment.OnFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // IMPORTANT - Clears Splash Screen Theme
        setTheme(R.style.AppTheme_LoginTheme)
        setContentView(R.layout.activity_login)

        Window.FEATURE_ACTION_BAR

        supportFragmentManager
            .registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
                    super.onFragmentViewCreated(fm, f, v, savedInstanceState)

                    if (v.context.theme == this@LoginActivity.theme) setCardViewOff() else {
                        setCardViewOn()
                        v.button_create_user.setBackgroundColor(resources.getColor(R.color.primaryColor, theme))
//                        v.button_create_user.setBackgroundColor(resources.getColor(R.color.primaryColor))
                    } } }, true)

//        val host: NavHostFragment = supportFragmentManager
//            .findFragmentById(R.id.nav_host_login) as NavHostFragment? ?: return
//        host.navController.addOnDestinationChangedListener { _, destination, _ ->
//                if (destination.id == R.id.dest_createUserFragment) {
//                    setCardViewOn()
//                }
//                else {setCardViewOff()} }
    }

    override fun getNavController(): NavController {
        return NavController(this@LoginActivity)

    }

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented")
    }

    fun setCardViewOn() {
        fragmentCard.layoutParams.width = MATCH_PARENT
        fragmentCard.layoutParams.height = MATCH_PARENT
        fragmentCard.cardElevation = getPixelsFromDPs(4)
    }

    fun setCardViewOff() {
        fragmentCard.layoutParams.width = WRAP_CONTENT
        fragmentCard.layoutParams.height = WRAP_CONTENT
        fragmentCard.cardElevation = getPixelsFromDPs(0)
    }

    protected fun getPixelsFromDPs(dps: Int): Float {
        return applyDimension(COMPLEX_UNIT_DIP, dps.toFloat(), this.resources.getDisplayMetrics())
    }

}

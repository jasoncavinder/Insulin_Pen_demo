package com.jasoncavinder.inpen_demo

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat


class ProfileFragment : PreferenceFragmentCompat() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.profile_settings, rootKey)
    }

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_profile, container, false)
//    }


}

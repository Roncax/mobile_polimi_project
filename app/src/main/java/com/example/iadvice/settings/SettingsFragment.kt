package com.example.iadvice.settings

import android.os.Bundle
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.example.iadvice.PersistenceUtils
import com.example.iadvice.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_fragment, rootKey)

        displayCurrentValues()





    }

    private fun displayCurrentValues(){

        //Display text values
        val preferences = arrayOf("Name", "Nickname", "Age")
        for (p in preferences)
            displayCurrentText(p)

        //Display list value (radio buttons)
        val gender = findPreference<ListPreference>("Gender")
       /// gender!!.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()


    }

    private fun displayCurrentText(preference: String) {
        val namePref = findPreference<EditTextPreference>(preference)
        when (preference) {
            "Name" -> namePref!!.text = PersistenceUtils.currentUser.username
            "Nickname" -> namePref!!.text = PersistenceUtils.currentUser.username
            "Age" -> namePref!!.text = PersistenceUtils.currentUser.age.toString()
        }
    }
}

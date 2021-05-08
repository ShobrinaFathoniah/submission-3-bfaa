package com.dicoding.picodiploma.githubusers.ui.setting

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.dicoding.picodiploma.githubusers.R
import com.dicoding.picodiploma.githubusers.receiver.AlarmReceiver

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting_preferences, rootKey)
        alarmReceiver = AlarmReceiver()

        val alarmOnOff = findPreference<Preference>(this.resources.getString(R.string.sp_key_on_off_random_color))!! as SwitchPreference

        alarmOnOff.onPreferenceChangeListener =
                Preference.OnPreferenceChangeListener { _, _ ->
                    if (alarmOnOff.isChecked) {
                        this.activity?.let {
                            alarmReceiver.cancelAlarm(requireContext(), AlarmReceiver.TYPE_REPEATING)
                            alarmReceiver.isAlarmSet(requireContext(), AlarmReceiver.TYPE_REPEATING)
                        }
                        alarmOnOff.setChecked(false)
                    } else {
                        this.activity?.let {
                            alarmReceiver.setRepeatingAlarm(requireContext(), AlarmReceiver.TYPE_REPEATING, "09:00", "Searching Github\'s User")
                            alarmReceiver.isAlarmSet(requireContext(), AlarmReceiver.TYPE_REPEATING)
                        }
                        alarmOnOff.setChecked(true)
                    }
                    true
                }
    }
}
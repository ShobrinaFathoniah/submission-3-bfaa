package com.dicoding.picodiploma.githubusers.ui.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.githubusers.R

class SettingContainer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.setting_container)

        val mFragmentManager = supportFragmentManager
        val mSettingsFragment = SettingsFragment()
        val fragment = mFragmentManager.findFragmentByTag(SettingsFragment::class.java.simpleName)
        if (fragment !is SettingsFragment) {
            mFragmentManager
                    .beginTransaction()
                    .add(R.id.frame_container, mSettingsFragment, SettingsFragment::class.java.simpleName)
                    .commit()
        }
    }
}
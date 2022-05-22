package com.ekochkov.appfstr.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ekochkov.appfstr.R
import com.ekochkov.appfstr.view.fragment.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        openLoginFragment()
    }

    fun openLoginFragment() {

        val fragment = LoginFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun openRegisterFragment() {

        val fragment = RegisterFragment()

        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
    }

    fun openHomeFragment() {

        val fragment = HomeFragment()

        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
    }

    fun openAddMountainFragment() {

        val fragment = AddMountainFragment()

        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
    }

    fun openCameraFragment() {
        val fragment = CameraFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount <= 1) {
            finish()
        }
        super.onBackPressed()
    }
}
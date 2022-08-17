package com.sesac.week3.activities

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import androidx.core.view.WindowCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.sesac.week3.Constant
import com.sesac.week3.R
import com.sesac.week3.adapters.ViewPagerAdapter
import com.sesac.week3.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setLightStatusBar()
        initViews()
    }

    private fun initViews() {
        //TODO ViewPager2 스와이프할 때 BottomNavigationView 연동
        binding.fragmentContainerView.isUserInputEnabled = false
        binding.fragmentContainerView.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        binding.fragmentContainerView.registerOnPageChangeCallback(createViewPagerChangeCallback())

        //TODO BottomNavigationView 메뉴 클릭할 때 ViewPager2 연동
        binding.bottomNavView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.home_menu -> binding.fragmentContainerView.setCurrentItem(0, false)
                R.id.neighborhood_life_menu -> binding.fragmentContainerView.setCurrentItem(1, false)
                R.id.near_me_menu -> binding.fragmentContainerView.setCurrentItem(2, false)
                R.id.chatting_menu -> binding.fragmentContainerView.setCurrentItem(3, false)
                R.id.my_danggn_menu -> binding.fragmentContainerView.setCurrentItem(4, false)
            }
            true
        }
    }

    private fun createViewPagerChangeCallback(): ViewPager2.OnPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)

            binding.bottomNavView.selectedItemId = when(position) {
                0 -> R.id.home_menu
                1 -> R.id.neighborhood_life_menu
                2 -> R.id.near_me_menu
                3 -> R.id.chatting_menu
                4 -> R.id.my_danggn_menu
                else -> R.id.home_menu
            }
        }
    }

    private fun setLightStatusBar() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val wic = window.decorView.windowInsetsController
            wic?.setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS)
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
    }

}
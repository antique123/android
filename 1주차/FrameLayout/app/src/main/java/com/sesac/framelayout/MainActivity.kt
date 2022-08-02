package com.sesac.framelayout

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.WindowCompat
import androidx.viewpager2.widget.ViewPager2
import com.sesac.framelayout.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setFullScreen()
        initViews()
    }

    private fun setFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.statusBarColor = Color.TRANSPARENT
            WindowCompat.setDecorFitsSystemWindows(window, false)
            window.decorView.windowInsetsController?.apply {
                hide(WindowInsets.Type.navigationBars())
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }

        } else {
            window.statusBarColor = Color.TRANSPARENT
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
    }

    private fun initViews() {
        val urls = mutableListOf<String>(
            "https://cdn.spotvnews.co.kr/news/photo/202002/345794_431934_2210.jpg",
            "https://img.marieclairekorea.com/2021/02/mck_602b7a63a46c7-scaled.jpg",
            "https://image.newdaily.co.kr/site/data/img/2019/12/30/2019123000202_0.jpg"
        )

        val adapter = ViewPagerAdapter(urls)

        binding.fullScreenImageViewpager.adapter = adapter
        binding.fullScreenImageViewpager.registerOnPageChangeCallback(getViewPagerCallback())
    }

    private fun getViewPagerCallback() = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)

            binding.firstViewpagerIndicator.setImageDrawable(AppCompatResources.getDrawable(this@MainActivity, R.drawable.shape_indicator_gray))
            binding.secondViewpagerIndicator.setImageDrawable(AppCompatResources.getDrawable(this@MainActivity, R.drawable.shape_indicator_gray))
            binding.thirdViewpagerIndicator.setImageDrawable(AppCompatResources.getDrawable(this@MainActivity, R.drawable.shape_indicator_gray))

            when(position) {
                0 -> binding.firstViewpagerIndicator.setImageDrawable(AppCompatResources.getDrawable(this@MainActivity, R.drawable.shape_indicator_purple))
                1 -> binding.secondViewpagerIndicator.setImageDrawable(AppCompatResources.getDrawable(this@MainActivity, R.drawable.shape_indicator_purple))
                2 -> binding.thirdViewpagerIndicator.setImageDrawable(AppCompatResources.getDrawable(this@MainActivity, R.drawable.shape_indicator_purple))
            }
        }
    }

}
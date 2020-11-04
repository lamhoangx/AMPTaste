package com.lamhx.amptaste.viewpager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.lamhx.amptaste.R
import com.lamhx.amptaste.databinding.ActivityViewPagerBinding

class ViewPagerActivity : AppCompatActivity() {
    lateinit var binding: ActivityViewPagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter =
            WebPartSlidePagerAdapter(this)
        binding.viewPager.adapter = sectionsPagerAdapter

        binding.viewPager.setPageTransformer(ZoomOutPageTransformer())
        binding.viewPager.offscreenPageLimit = 1

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
        }.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
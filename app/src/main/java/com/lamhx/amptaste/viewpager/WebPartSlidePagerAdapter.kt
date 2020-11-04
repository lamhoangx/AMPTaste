package com.lamhx.amptaste.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lamhx.amptaste.entity.DummyData
import com.lamhx.amptaste.viewpager.PlaceholderWebFragment

class WebPartSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = DummyData.dummyData.size

    override fun createFragment(position: Int): Fragment = PlaceholderWebFragment.newInstance(position)
}
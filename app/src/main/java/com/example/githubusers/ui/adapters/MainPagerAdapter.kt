package com.example.githubusers.ui.adapters

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubusers.ui.fragment.FollowFragment

class MainPagerAdapter(activity: AppCompatActivity, username: String) : FragmentStateAdapter(activity){

    private var query = username
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = FollowFragment()
        fragment.arguments = Bundle().apply {
            putInt(FollowFragment.ARG_POSITION, position + 1)
            putString(FollowFragment.EXTRA_USERNAME, query)
        }
        return fragment
    }
}
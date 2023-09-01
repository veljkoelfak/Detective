package com.veljko.detective

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.veljko.detective.ui.CluesFragment
import com.veljko.detective.ui.LeaderboardFragment
import com.veljko.detective.ui.MapFragment
import com.veljko.detective.ui.ProfileFragment


class ViewpageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->{
                MapFragment()
            }
            1->{
                LeaderboardFragment()
            }
            2->{
                ProfileFragment()
            }

            3->{
                CluesFragment()
            }
            else->{
                Fragment()
            }
        }
    }
}
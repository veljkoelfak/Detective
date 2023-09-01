package com.veljko.detective

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.veljko.detective.ui.LeaderboardFragment
import com.veljko.detective.ui.MapFragment
import com.veljko.detective.ui.ProfileFragment

class MainActivity : AppCompatActivity() {

    private val viewModel: UserDataViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)



        val adapter = ViewpageAdapter(supportFragmentManager,lifecycle)
        viewPager.adapter = adapter
        viewPager.setUserInputEnabled(false)

        TabLayoutMediator(tabLayout,viewPager){tab,position->
            when(position){
                0->{
                    tab.text ="Map"
                }
                1->{
                    tab.text="Leaderboard"
                }
                2->{
                    tab.text="Profile"
                }
                3-> {
                    tab.text="Clues"
                }

            }
        }.attach()

    }
}
package com.mikail.chatme.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mikail.chatme.ui.ChatFragment
import com.mikail.chatme.ui.UsersFragment

class ViewpagerAdapter (fm: FragmentManager?, lifecycle: Lifecycle) : FragmentStateAdapter(fm!!, lifecycle) {
    private val int_items = 2
    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = UsersFragment()
            1 -> fragment = ChatFragment()

        }
        return fragment!!
    }
    override fun getItemCount(): Int {
        return int_items
    }
}
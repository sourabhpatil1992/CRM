package com.venter.crm.empMang

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class CandDetViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 2;
    }
    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> {
                return RawCandetDetFragemtn()
            }
            1 -> {
                return RawCanCommentFragment()
            }

            else -> {
                return RawCandetDetFragemtn()
            }
        }
    }
    override fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            0 -> {
                return "Candidate Details"
            }
            1 -> {
                return "Comments"
            }

        }
        return super.getPageTitle(position)
    }
}
package com.venter.regodigital.Candidate

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.venter.regodigital.utils.Constans.TAG


class CandidateViewPager(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 2;
    }

    override fun getItem(position: Int): Fragment {
        try {
            when (position) {
                0 -> {
                    return CandidateCertDoc()
                }
                1 -> {
                    return CandidateIdDoc()
                }

                else -> {
                    return CandidateCertDoc()
                }
            }
        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in CandidateViewPagerAdapter.kt getItem() is "+e.message)
            return CandidateCertDoc()
        }
    }
    override fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            0 -> {
                return "Certificate"
            }
            1 -> {
                return "ID Doc"
            }
        }
        return super.getPageTitle(position)
    }
}
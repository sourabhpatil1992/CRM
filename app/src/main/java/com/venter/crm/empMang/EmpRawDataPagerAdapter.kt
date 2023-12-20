package com.venter.crm.empMang

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.venter.crm.utils.Constans

class EmpRawDataPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        try {
            return when (position) {
                0 -> {
                    EmpCampRawDataFragment("Cold")
                }

                1 -> {
                    EmpCampRawDataFragment("Warm")
                }

                2 -> {
                    EmpCampRawDataFragment("Hot")
                }

                else -> {
                    EmpCampRawDataFragment("Cold")
                }
            }
        }
        catch (e:Exception)
        {
            Log.d(Constans.TAG,"Error in EmpRawDataPagerAdapter.kt getItem() is "+e.message)
            return EmpCampRawDataFragment("Cold")
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            0 -> {
                return "Cold Data"
            }
            1 -> {
                return "Warm Data"
            }
            2 -> {
                return "Hot Data"
            }


        }
        return super.getPageTitle(position)
    }
}
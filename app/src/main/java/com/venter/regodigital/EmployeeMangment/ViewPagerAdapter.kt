package com.venter.regodigital.EmployeeMangment

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.venter.regodigital.Dashboard.EmpDashboard
import com.venter.regodigital.utils.Constans

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 7;
    }

    override fun getItem(position: Int): Fragment {
        try {
            when (position) {
                0 -> {
                    return EmpRawDataFragment()
                }
                1 -> {
                    return EmpProsFragement()
                }
                2 -> {
                    return EmpFolloupCandFragment()
                }
                3 -> {
                    return IncomingLeads()
                }
                4 -> {
                    return EmpNotRespondingData()
                }
                5 -> {
                    return ColdDataFragment()
                }
                6 -> {
                    return EmpReportFragment()
                }
                else -> {
                    return EmpRawDataFragment()
                }
            }
        }
        catch (e:Exception)
        {
            Log.d(Constans.TAG,"Error in ViewPagerAdapter.kt getItem() is "+e.message)
            return EmpRawDataFragment()
        }
    }
    override fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            0 -> {
                return "Raw Data"
            }
            1 -> {
                return "Prospect Data"
            }
            2 -> {
                return "Today's Follow"
            }
            3 -> {
                return "Incoming Leads"
            }
            4 -> {
                return "Not Responding"
            }
            5 -> {
                return "Cold Data"
            }
            6 -> {
                return "Report"
            }
        }
        return super.getPageTitle(position)
    }
}
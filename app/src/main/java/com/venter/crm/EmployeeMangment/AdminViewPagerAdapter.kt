package com.venter.crm.EmployeeMangment

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.venter.crm.empMang.ColdDataFragment
import com.venter.crm.empMang.EmpFolloupCandFragment
import com.venter.crm.empMang.EmpNotRespondingData
import com.venter.crm.empMang.EmpProsFragement
import com.venter.crm.empMang.IncomingLeads
import com.venter.crm.empMang.StalDataFragment
import com.venter.crm.utils.Constans



class AdminViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 8;
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
                7 -> {
                    return StalDataFragment()
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
                return "Follow Up"
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
            7 -> {
                return "Stale Data"
            }
        }
        return super.getPageTitle(position)
    }
}
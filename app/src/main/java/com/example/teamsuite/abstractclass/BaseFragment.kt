package com.example.teamsuite.abstractclass

import android.view.View
import androidx.fragment.app.Fragment
import com.example.teamsuite.R

abstract class BaseFragment: Fragment() {
    private fun getLoader(): View?{
        return activity?.findViewById(R.id.globalLoader)
    }

    fun showLoader(){
        getLoader()?.visibility = View.VISIBLE
    }

    fun hideLoader(){
        getLoader()?.visibility = View.GONE
    }
}
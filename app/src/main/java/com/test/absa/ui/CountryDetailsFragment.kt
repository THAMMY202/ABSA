package com.test.absa.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.absa.R

private const val ARG_ALPHA_CODE = "param1"

class CountryDetailsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_country_details, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(alphaCode: String) =
            CountryDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ALPHA_CODE, alphaCode)
                }
            }
    }
}
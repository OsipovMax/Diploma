package com.example.maapp.RootFragments

import android.os.Bundle
import android.view.View
import com.example.maapp.R
import com.example.maapp.presentation.ui.base.BaseFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_about.*

class HelpFragment : BaseFragment() {
    override val layoutRes: Int = R.layout.fragment_about
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_g.setOnClickListener {
            fragmentNavigation.isRootFragment()
            activity?.bottom_nav?.setCurrentItem(0)

        }
    }


}

package com.example.maapp.RootFragments

import android.os.Bundle
import android.view.View
import com.example.android.roomdevice.DeviceRepository
import com.example.maapp.CustomTestsLogic.Tests
import com.example.maapp.R
import com.example.maapp.presentation.ui.base.BaseFragment
import com.example.maapp.presentation.ui.tests.bandwidthTest.BandwidthTestFragment
import com.example.maapp.presentation.ui.tests.searchTest.SearchTestFragment
import com.example.maapp.presentation.ui.tests.wallTest.WallTestFragment
import kotlinx.android.synthetic.main.fragment_tests.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class TestsFragment : BaseFragment() {
    override val layoutRes: Int = R.layout.fragment_tests

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)


    private var repository: DeviceRepository? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_test_1.setOnClickListener {
            fragmentNavigation.pushFragment(WallTestFragment())
        }


        btn_test_2.setOnClickListener {
            fragmentNavigation.pushFragment(BandwidthTestFragment())
        }
        btn_test_3.setOnClickListener{
            fragmentNavigation.pushFragment(SearchTestFragment())
        }

    }
}

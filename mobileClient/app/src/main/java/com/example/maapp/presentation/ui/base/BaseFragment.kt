package com.example.maapp.presentation.ui.base

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.maapp.R
import com.example.maapp.presentation.App
import com.ncapdevi.fragnav.FragNavController
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


abstract class BaseFragment : Fragment() {

    interface FragmentNavigation {
        fun pushFragment(fragment: BaseFragment)
        fun replaceFragment(fragment: BaseFragment)
        fun popFragment()
        fun isRootFragment(): Boolean
    }

    abstract val layoutRes: Int

    protected val appComponent by lazy { (requireContext().applicationContext as App).appComponent }

    private var compositeDisposable: CompositeDisposable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(layoutRes, container, false)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setHasOptionsMenu(true)
        if (context is FragmentNavigation) {
            fragmentNavigation = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        compositeDisposable?.dispose()
        compositeDisposable = CompositeDisposable()
    }

    override fun onDestroyView() {
        compositeDisposable?.dispose()
        super.onDestroyView()
    }

    protected fun Disposable.disposeView() {
        compositeDisposable?.add(this)
    }

    lateinit var fragmentNavigation: FragmentNavigation
    lateinit var navController: FragNavController

    fun configureToolbar(toolbar : Toolbar) {
        (context as AppCompatActivity).setSupportActionBar(toolbar)
        (context as AppCompatActivity).supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.setDisplayShowTitleEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }
        toolbar.setNavigationOnClickListener {
            (context as AppCompatActivity).onBackPressed()
        }
    }
}
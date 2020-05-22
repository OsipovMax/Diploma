package com.example.maapp.RootFragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.fragment.app.Fragment
import android.view.View
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.example.maapp.*
import com.example.maapp.ServerConnection.ConnectionService
import com.example.maapp.presentation.ui.about.AboutFragment
import com.example.maapp.presentation.ui.base.BaseFragment
import com.example.maapp.presentation.ui.leaderboard.LeaderboardFragment
import com.ncapdevi.fragnav.FragNavController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(),
    FragNavController.TransactionListener,
    FragNavController.RootFragmentListener,
    BaseFragment.FragmentNavigation
{

    lateinit var navController: FragNavController

    override val numberOfRootFragments = 3

    override fun getRootFragment(index: Int): Fragment {
        when (index) {
            0 -> {
                return TestsFragment()
            }
            1 -> {
                return LeaderboardFragment()
            }
            2 -> return AboutFragment()

        }
        throw IllegalStateException("Undefined index value")
    }

    override fun onTabTransaction(fragment: Fragment?, index: Int) {

    }

    override fun pushFragment(fragment: BaseFragment) {
        navController.pushFragment(fragment)
    }

    override fun replaceFragment(fragment: BaseFragment) {
        navController.pushFragment(fragment)
    }

    override fun popFragment() {
        navController.popFragment()
    }

    override fun isRootFragment(): Boolean {
        return navController.isRootFragment
    }

    override fun onFragmentTransaction(fragment: Fragment?, transactionType: FragNavController.TransactionType) {

    }

    override fun onBackPressed() {
        if (navController.isRootFragment || !navController.popFragment()) {
            super.onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        navController.onSaveInstanceState(outState)
    }

    var connectionService: ConnectionService? = null
    var isBound = false

    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        navController = FragNavController(supportFragmentManager, R.id.main_container).also {
            it.transactionListener = this
            it.rootFragmentListener = this
            it.initialize(0, savedInstanceState) // ???
        }
        bottom_nav.setOnTabSelectedListener { position, wasSelected ->
            if (wasSelected) {
                navController.clearStack()
            } else {
                navController.switchTab(position)
            }
            true
        }


        val intent = Intent(this, ConnectionService::class.java)
        bindService(intent,myConnection, Context.BIND_AUTO_CREATE)

        val item1 = AHBottomNavigationItem("Тесты",
            R.drawable.ic_assessment_dev,
            R.color.colorPrimary
        )
        val item2 = AHBottomNavigationItem("Лидеры",
            R.drawable.ic_leadre_board,
            R.color.colorPrimary
        )
        val item3 = AHBottomNavigationItem("О приложении",
            R.drawable.ic_help,
            R.color.colorPrimary
        )
        bottom_nav.addItem(item1)
        bottom_nav.addItem(item2)
        bottom_nav.addItem(item3)


    }

    private val myConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName,
                                        service: IBinder) {
            val binder = service as ConnectionService.MyLocalBinder
            connectionService = binder.getService()
            isBound = true

            //onConn()
        }
        override fun onServiceDisconnected(name: ComponentName) {
            isBound = false

        }

    }

    fun onConn() {
        GlobalScope.launch {
            val asyncVal = async {
                connectionService?.conn()
            }
            println(asyncVal.await())
            if (asyncVal.await() == true) {
                val job1 = launch {
                    connectionService?.getDeviceList()
                }
                job1.join()
            }
            else{
                println("No connection")
            }
        }
    }


    fun getD ():String? {
       val str =  connectionService?.localStorage
        //println(str)
       return str
    }


    override fun onDestroy() {
        unbindService(myConnection)
        super.onDestroy()
    }


}

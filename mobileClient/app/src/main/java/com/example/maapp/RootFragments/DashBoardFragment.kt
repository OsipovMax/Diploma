package com.example.maapp.RootFragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.roomdevice.DeviceListAdapter
import com.example.android.roomdevice.DeviceViewModel
import com.example.maapp.R
import com.example.maapp.ServerConnection.ConnectionService
import com.example.maapp.presentation.ui.base.BaseFragment
import kotlinx.android.synthetic.main.activity_leader_board.*
import kotlinx.android.synthetic.main.content_main.*

class DashBoardFragment : BaseFragment() {
    override val layoutRes: Int = R.layout.activity_leader_board

    private lateinit var deviceViewModel: DeviceViewModel



    var pp : String? = ""
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: DeviceListAdapter
    var connectionService: ConnectionService? = null


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menu?.clear()
        inflater?.inflate(R.menu.search_menu, menu)
        val searchView = SearchView((context as MainActivity).supportActionBar?.themedContext ?: context)
        menu!!.findItem(R.id.action_search).apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
            actionView = searchView
        }
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                adapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })
        searchView.setOnClickListener {view ->  }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> return false
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mA: MainActivity = activity as MainActivity
        setHasOptionsMenu(true)
        configureToolbar(toolbar_dashboard)

        pp = mA.getD()
        pp = pp!!.substringAfterLast(']')
        println("&&&&&&&&&&&&&&& ${pp}")
        //pp = "1/ASUS UX430U/21682320/45171.5/Intel Core i7-8550U, 16 GB RAM/2/Nokia 6.1/195000/406.25/Qualcomm Snapdragon 630, 3 GB RAM/3/MSI CX61/13795320/28749.3/Intel Core i5-4200M, 8 GB RAM/4/HP 430 G4/18627720/38807.8/Intel Core i7-7500U, 8 GB RAB/5/Blue Gene P/3936240/8200.5/PowerPC 450, 2 GB RAM/6/NULL/0/0.0/NULL/18/Google Android SDK built for x86/222720/464.0/0 GB RAM/19/Google Android SDK built for x86/226440/471.75/0 GB RAM/20/Xiaomi Redmi 6/360840/751.75/0 GB RAM/21/HMD Global Nokia 6.1/338880/706.0/0 GB RAM/22/HMD Global Nokia 6.1/342960/714.5/0 GB RAM/"
        recyclerView = recyclerview

        recyclerview.addItemDecoration(DividerItemDecoration(activity!!.applicationContext, DividerItemDecoration.VERTICAL))
        adapter = DeviceListAdapter(activity!!.applicationContext)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        deviceViewModel = ViewModelProviders.of(this).get(DeviceViewModel::class.java)
        deviceViewModel.firstLoadData(activity!!.application,pp!!)
        deviceViewModel.allDevices?.observe(this, Observer { words ->
            words?.let { adapter.setWords(it) }
        })

//        editText3.setOnClickListener {
//            editText3.text.clear()
//        }


//        res_title_text_view.setOnClickListener{
//            val toast = Toast.makeText(activity,"Длина построенного участка стены в метрах", Toast.LENGTH_LONG)
//            toast.setGravity(Gravity.CENTER,0,0)
//            toast.show()
//        }



    }

    fun searchDevices(view: View) {

        val inputManager: InputMethodManager = layoutInflater.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, InputMethodManager.SHOW_FORCED)

//        var searchDevices = editText3.text.toString()
//        if (searchDevices != "") {
//            recyclerView = recyclerview
//            adapter = DeviceListAdapter(activity!!.applicationContext)
//            recyclerView.adapter = adapter
//            recyclerView.layoutManager = LinearLayoutManager(activity)
//            deviceViewModel = ViewModelProviders.of(this).get(DeviceViewModel::class.java)
//            deviceViewModel.showSearchResults(activity!!.application, searchDevices)
//            deviceViewModel.allDevices?.observe(this, Observer { words ->
//                words?.let { adapter.setWords(it) }
//            })
//        }
//        else {
//            recyclerView = recyclerview
//            adapter = DeviceListAdapter(activity!!.applicationContext)
//            recyclerView.adapter = adapter
//            recyclerView.layoutManager = LinearLayoutManager(activity)
//            deviceViewModel = ViewModelProviders.of(this).get(DeviceViewModel::class.java)
//            deviceViewModel.showLocalData(activity!!.application)
//            deviceViewModel.allDevices?.observe(this, Observer { words ->
//                words?.let { adapter.setWords(it) }
//            })
//        }
    }
}

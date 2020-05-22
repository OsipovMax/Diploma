package com.example.android.roomdevice

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.example.maapp.R


class DeviceListAdapter internal constructor(
        context: Context
) : RecyclerView.Adapter<DeviceListAdapter.DeviceViewHolder>(), Filterable {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var device = emptyList<Device>()
    private var deviceSearch: List<Device> = emptyList()


    private var b = 0

    inner class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val deviceIdItemViuew: TextView = itemView.findViewById(R.id.pos_text_view)
        val deviceItemView: TextView = itemView.findViewById(R.id.dev_text_view)
        val lengthPerfomanceItemView: TextView = itemView.findViewById(R.id.res_text_view)
        //val brickPerfomanceItemView: TextView = itemView.findViewById(R.id.textView3)

        //val systemParametrItemView: TextView = itemView.findViewById(R.id.textView12)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return DeviceViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val current = deviceSearch[position]
        holder.deviceIdItemViuew.text = (position + 1).toString()
        holder.deviceItemView.text = current.device
        holder.lengthPerfomanceItemView.text = current.lengthPerfomance.toString()

        /*if (position == 0){
            holder.deviceIdItemViuew.text = "№"
            holder.deviceItemView.text = "Модель уст-ва"
           // holder.brickPerfomanceItemView.text = "Число кирпичей"
            //holder.lengthPerfomanceItemView.text = "Длина стены"

        }
        else {
            val current = device[position - 1]
            holder.deviceIdItemViuew.text = (position).toString()
            holder.deviceItemView.text = current.device
           // holder.brickPerfomanceItemView.text = current.brickPerfomance.toString()
            //holder.lengthPerfomanceItemView.text = current.lengthPerfomance.toString()
            //holder.systemParametrItemView.text = current.systemParameter
        }*/
    }



    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val charString = charSequence.toString()
                if(charString.isEmpty()){
                    deviceSearch = device
                } else {
                    val filteredList = ArrayList<Device>()
                    for (dev in device) {
                        if (dev.device!!.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(dev)
                        }
                    }
                    deviceSearch = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = deviceSearch
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence?, filterReults: FilterResults?) {
                deviceSearch = filterReults?.values as ArrayList<Device>
                notifyDataSetChanged()
            }
        }
    }

    internal fun setWords(device: List<Device>) {
        this.device = device
        this.deviceSearch = device
        notifyDataSetChanged()
    }

    override fun getItemCount() = deviceSearch.size
}



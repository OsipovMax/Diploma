package com.example.maapp.CustomTestsFragments

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.maapp.R
import com.example.maapp.presentation.system.argument
import kotlinx.android.synthetic.main.res_bottom_sheet.*

class ResultsTestDialog : BottomSheetDialogFragment() {


    companion object {
        private const val ARG_RESULT = "TEG_RESULT"

        fun create(result: ArrayList<String?>) =
            ResultsTestDialog().apply {
                arguments = Bundle().apply {
                    putStringArrayList(ARG_RESULT, result)
                }
            }
    }

    val result by argument(ARG_RESULT, ArrayList<String>(4))


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.res_bottom_sheet, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (result.size >= 6) {
            bestDev.text = "   " + result[0]
            bestVal.text = result[1]
            worstDevice.text = "   " + result[2]
            worstVal.text = result[3]
            yourDev.text = "   " + result[result.size - 2]
            yourVal.text = result[result.size - 1]
        }
        else if (result.size == 2) {
            bestDev.text = "   No data"
            bestVal.text = "-"
            worstDevice.text = "   Node data"
            worstVal.text = "-"
            yourDev.text = "   " + result[result.size - 2]
            yourVal.text = result[result.size - 1]
        }
    }

}
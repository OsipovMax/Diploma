package com.example.maapp.CustomTestsFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.maapp.R
import com.example.maapp.presentation.system.argument
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.wall_bottom_sheet.*

class DescriptionTestDialog: BottomSheetDialogFragment() {

    companion object {
        private const val ARG_RESULT = "Tag"

        fun create(result: ArrayList<String?>) =
            DescriptionTestDialog().apply {
                arguments = Bundle().apply {
                    putStringArrayList(ARG_RESULT, result)
                }
            }
    }

    val result by argument(ARG_RESULT, ArrayList<String>(1))


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.wall_bottom_sheet, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        descriptionTest.text = resources.getString(R.string.bottom_sheet_title)
        technicalDescription.text = resources.getString(R.string.wall_test_tech_topic)
        low_sheet_content2.text = resources.getString(R.string.technical_description)

        if (result[0] == "1"){ //wallTest
            titleTest.text = resources.getString(R.string.wall_test)
            low_sheet_content.text = resources.getString(R.string.bottom_wall_sheet_content)
        }
        else if (result[0] == "2"){ //bandwidthTest
            titleTest.text = resources.getString(R.string.bandwidth_test)
            low_sheet_content.text = resources.getString(R.string.bottom_bandwidth_sheet_content)

        }
        else if (result[0] == "3"){ //searchTest
            titleTest.text = resources.getString(R.string.search_test)
            low_sheet_content.text = resources.getString(R.string.bottom_search_sheet_content)
        }
    }

}
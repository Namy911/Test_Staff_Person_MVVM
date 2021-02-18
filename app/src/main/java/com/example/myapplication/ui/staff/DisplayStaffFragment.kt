package com.example.myapplication.ui.staff

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.myapplication.R
import com.example.myapplication.data.model.StaffAndPersons
import com.example.myapplication.databinding.DisplayStaffFragmentBinding
import com.example.myapplication.ui.util.MyUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DisplayStaffFragment : Fragment() {


    lateinit var binding: DisplayStaffFragmentBinding
    private val args: DisplayStaffFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?) =
        DisplayStaffFragmentBinding.inflate(layoutInflater, container, false)
            .apply { binding = this }
            .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.txtPerson.text = setFullText(args.model).toString()
    }

    // Convert data to display info about Staff
    private fun setFullText(model: StaffAndPersons): StringBuilder {
        val str = StringBuilder()
         if (model.persons.count() > 0) {
            for (person in model.persons) {
                str.append(person.name)
                    .append("\t\t")
                    .append(person.surName)
                    .append("\n")
                    .append(MyUtil().convertBtnText(person.date.time))
                    .append("\n")
            }
        } else { str.append(getString(R.string.msg_no_staff_display)) }
        return str
    }
}

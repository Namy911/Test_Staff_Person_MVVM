package com.example.myapplication.ui.person

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myapplication.R
import com.example.myapplication.data.entity.Person
import com.example.myapplication.data.model.PersonAndStaff
import com.example.myapplication.data.entity.Staff
import com.example.myapplication.databinding.DisplayFragmentBinding
import com.example.myapplication.ui.util.MyUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

private const val TAG = "DisplayPersonFragment"
@AndroidEntryPoint
class DisplayPersonFragment: Fragment() {

    lateinit var binding: DisplayFragmentBinding
    private val args: DisplayPersonFragmentArgs by navArgs()
    private val viewModel: PersonViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            DisplayFragmentBinding
                    .inflate(layoutInflater, container, false)
                    .apply { binding = this }
                    .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getPersonAndStaff(args.personId)
        lifecycleScope.launchWhenStarted {
            viewModel.personAndStaff.collect {
                binding.apply {
                    stuff = it.staff
                    person = it.person
                    txtDate.text = MyUtil().convertBtnText(it.person.date.time)
                    menuBotDisplay.setOnNavigationItemSelectedListener { setupBottomMenu(it.itemId) }
                }
            }
        }
    }

    private fun setupBottomMenu(id: Int): Boolean {
        val model = PersonAndStaff(binding.person as Person, binding.stuff as Staff)
         return when(id){
            R.id.menu_update_person -> {
                findNavController().navigate(DisplayPersonFragmentDirections.updatePersonFragment(model))
                true
            }
            R.id.menu_delete_person -> {
                findNavController().navigate(DisplayPersonFragmentDirections.dialogDeleteItem(model.person))
                true
            }
             else -> { false }
        }
    }
}
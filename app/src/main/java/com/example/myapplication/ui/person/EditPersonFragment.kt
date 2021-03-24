package com.example.myapplication.ui.person

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myapplication.R
import com.example.myapplication.data.entity.Person
import com.example.myapplication.data.entity.Staff
import com.example.myapplication.databinding.AddPersonFragmentBinding
import com.example.myapplication.ui.util.MyUtil
import com.example.myapplication.ui.util.dialog.DialogDatePiker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.util.*

private const val TAG = "AddPersonFragment"

@AndroidEntryPoint
class AddPersonFragment : Fragment(), AdapterView.OnItemSelectedListener {
    lateinit var binding: AddPersonFragmentBinding
    private val args: AddPersonFragmentArgs by navArgs()
    private val viewModel: PersonViewModel by viewModels()
    lateinit var staff: Staff
    private var imgUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = AddPersonFragmentBinding
            .inflate(layoutInflater, container, false)
            .apply { binding = this }
            .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState ?: viewModel.getAllStaff()

        if (args.model != null){
            binding.btnDate.text = MyUtil().convertBtnText(args.model?.person?.date?.time as Long)
        }else{
            binding.btnDate.text = MyUtil().convertBtnText(System.currentTimeMillis())
        }
        // Get Result from Date Piker Dialog
        parentFragmentManager.setFragmentResultListener(
            DialogDatePiker.DATE_PIKER_REQUEST_CODE,
            viewLifecycleOwner,
            { _, result: Bundle ->
                val date = result.get(DialogDatePiker.TIME_STAMP).toString().toLong()
                viewModel.savePikerPersonState(date)
                binding.btnDate.text =  MyUtil().convertBtnText(date)
            })

        binding.apply {
            // Restore State from view (model: Person)
            person = if (savedInstanceState != null) {
                viewModel.getPersonState()
            } else {
                args.model?.person
            }

            botMenuAdd.setOnNavigationItemSelectedListener { item ->  setupBottomMenu(item) }

            btnDate.setOnClickListener {
                // Create Listener
                if (args.model?.person == null) {
                    dialogPikerNavigate(System.currentTimeMillis())
                }else {
                    // Update Listener
                    viewModel.getPikerPersonState()?.let { dialogPikerNavigate(it) }
                        ?: dialogPikerNavigate(person!!.date.time)
                }
            }

            spinStaff.onItemSelectedListener = this@AddPersonFragment
            lifecycleScope.launchWhenStarted {
                viewModel.allStaff.collect { list ->
                    spinStaff.apply {
                        adapter = SpinnerArrayAdapter(requireContext(), list)
                        // Setup Spinner selection state(model: Staff)
                        savedInstanceState?.let { setSelection(list.indexOf(viewModel.getSpinnerSelection())) }
                            ?: setSelection(list.indexOf(args.model?.staff))
                    }
                }
            }
            // Update screen
            person?.let {
                botMenuAdd.menu.removeItem(R.id.menu_save_add)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.savePersonState(setupModel())
    }

    private fun setupModel(): Person {
        val newPerson = if (args.model == null) {
            // Case: Create Model
            Person(
                name = binding.edtName.text.toString(),
                surName = binding.edtSurName.text.toString(),
                date = Date(System.currentTimeMillis()),
                staffId = staff.id
            )
        } else {
            // Case: Update Model
            args.model!!.person.copy(
                name = binding.edtName.text.toString(),
                surName = binding.edtSurName.text.toString(),
                staffId = staff.id
            )
        }
        if (viewModel.getPikerPersonState() != null) {
            newPerson.date = Date(viewModel.getPikerPersonState()!!)
        }
        return newPerson
    }
    // Check if inputs is completed
    private fun validation(): Boolean{
        if (binding.edtName.text.isEmpty()){ binding.edtName.error = getString(R.string.empty_filed) }
        if (binding.edtSurName.text.isEmpty()){ binding.edtSurName.error = getString(R.string.empty_filed) }

        return  (binding.edtName.text.isNotEmpty() && binding.edtSurName.text.isNotEmpty())
    }
    private fun dialogPikerNavigate(stamp: Long) {
            findNavController().navigate(
                AddPersonFragmentDirections.dialogDatePikerFragment(stamp)
            )
    }

    private fun setupBottomMenu(item: MenuItem) = when (item.itemId) {
        R.id.menu_save_add -> {
            if (validation()) {
                viewModel.insertPerson(setupModel())
                viewModel.savePikerPersonState(System.currentTimeMillis())
                clearInputs()
                actionMessage()
            }
            true
        }
        R.id.menu_save_person -> {
            if (validation()) {
                viewModel.insertPerson(setupModel())
                findNavController().popBackStack()
                actionMessage()
            }
            true
        }
        else -> {
            false
        }
    }

    // Spinner setup
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        staff = parent?.getItemAtPosition(pos) as Staff
        viewModel.saveSpinnerSelection(staff)
    }
    override fun onNothingSelected(p0: AdapterView<*>?) { }

    private fun actionMessage() {
        Toast.makeText(requireContext(), getString(R.string.update_insert_msg_toast), Toast.LENGTH_SHORT)
            .show()
    }
    private fun clearInputs(){
        binding.apply {
            edtName.text.clear()
            edtSurName.text.clear()
            btnDate.text = MyUtil().convertBtnText(System.currentTimeMillis())
            spinStaff.setSelection(0)
        }
    }

    inner class SpinnerArrayAdapter(context: Context, items: List<Staff>) :
        ArrayAdapter<Staff>(context, 0, items) {
        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            return this.createView(position, convertView, parent)
        }
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            return this.createView(position, convertView, parent)
        }
        // Create view and associate data
        private fun createView(position: Int, recycledView: View?, parent: ViewGroup): View {
            val view = recycledView ?: layoutInflater.inflate(R.layout.staff_sppiner_row, parent, false)
            val field = view.findViewById<TextView>(R.id.txt_staff_office)
            field.text = getItem(position)!!.office
            return view
        }
    }
}
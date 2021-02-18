package com.example.myapplication.ui.util.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.icu.text.DateTimePatternGenerator.PatternInfo.OK
import android.icu.text.Normalizer.NO
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myapplication.R
import com.example.myapplication.ui.list.ListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogDeleteItem: DialogFragment() {

    private val viewModel: ListViewModel by viewModels()
    private val args: DialogDeleteItemArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.dialog_delete_title))
                    .setMessage(getString(R.string.dialog_delete_message, args.model.name, args.model.surName))
                    .setPositiveButton(getString(R.string.YES)){ _, _ ->
                        viewModel.deletePerson(args.model)
                        findNavController().navigate(DialogDeleteItemDirections.popUpDialogDeleteItem())
                    }
                    .setNegativeButton(getString(R.string.NO), null )
                    .create()
}
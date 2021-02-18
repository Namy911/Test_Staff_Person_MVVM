package com.example.myapplication.ui.util.dialog

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myapplication.R
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


private const val TAG = "DialogDatePiker"
@AndroidEntryPoint
class DialogDatePiker: DialogFragment(){
    private val args: DialogDatePikerArgs by navArgs()

    companion object{
        const val DATE_PIKER_REQUEST_CODE = "ui.util.dialog.piker.time_stamp"
        const val TIME_STAMP = "ui.util.dialog.piker.time_stamp"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c: Calendar = Calendar.getInstance()
        c.timeInMillis = args.timeStamp

        return DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                val date = convertDate(day, month, year)
                sendResult(date)
            },
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH),
            c.get(Calendar.DAY_OF_MONTH)
        )
    }


    private fun sendResult(date: Long?){
        parentFragmentManager.setFragmentResult(
            DATE_PIKER_REQUEST_CODE,
            bundleOf(TIME_STAMP to date)
        )
    }
    private fun convertDate(day: Int, month: Int, year: Int): Long {
        val c = Calendar.getInstance()
        c.set(year, month, day)
        return c.timeInMillis
    }
}
//       MaterialDatePicker.Builder.datePicker().build().apply {
//        addOnNegativeButtonClickListener {
//            Log.d(TAG, "Dialog Negative Button was clicked")
//        }
//        addOnPositiveButtonClickListener {
//            Log.d(TAG, ":: Date epoch value = ${it}")
//        }
//        addOnCancelListener {
//            Log.d(TAG, "Dialog was cancelled ")
//        }
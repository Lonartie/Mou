package com.team.app.service

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class DialogService(count : Int) : DialogFragment() {
    private val count = count
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder
                .setMessage("Du hast neue $count Coins erhalten")
                .setPositiveButton("Akzeptieren") {dialog, id ->
                    // ACCEPT
                }
                .setNegativeButton("Nicht annehmen") {dialog, id ->
                    //User canceled
                }
            builder.create()

        }?:throw IllegalStateException("Acctivity cant be null")
    }
}
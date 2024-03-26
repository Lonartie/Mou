package com.team.app.ui.common

import android.app.AlertDialog
import android.content.Context

class Dialog(private val context: Context) {
    fun showAlertDialog(
        title: String,
        message: String,
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { _, _ -> }
        builder.show()
    }

}
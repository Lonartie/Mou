package com.team.app.service

import android.app.AlertDialog
import android.content.Context

class DialogService(private val context: Context) {
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
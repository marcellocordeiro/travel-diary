package com.myapp.traveldiary

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import java.util.concurrent.Future

class PopupFragment(
    private val content: View,
    private val positiveCallback: () -> (Future<Unit>),
    private val negativeCallback: () -> (Unit)
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it).apply {
                setView(content)

                setPositiveButton(
                    "OK"
                ) { _, _ ->
                    positiveCallback()
                }

                setNegativeButton(
                    "Cancel"
                ) { _, _ ->
                    negativeCallback()
                }
            }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}
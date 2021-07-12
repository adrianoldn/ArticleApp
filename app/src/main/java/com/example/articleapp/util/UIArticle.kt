package com.example.articleapp.util

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.articleapp.R

object UIArticle {
    fun createLoadDialog(context: Context, show: Boolean): AlertDialog {
        val builder = AlertDialog.Builder(context)
        builder.setView(R.layout.dialog_load)
        builder.setCancelable(false)
        val modal = builder.create()
        modal.setCanceledOnTouchOutside(false)
        if (show) modal.show()
        return modal
    }
}
package com.joheba.hotelbediax.ui.common.contracts

import com.joheba.hotelbediax.R

interface SnackbarMessenger {
    fun resetSnackbar()
    fun updateSnackbar(show: Boolean = false, messageResId: Int = R.string.empty_message, isError: Boolean = false)
}
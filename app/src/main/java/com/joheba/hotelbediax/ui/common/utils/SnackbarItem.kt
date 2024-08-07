package com.joheba.hotelbediax.ui.common.utils

import com.joheba.hotelbediax.R

data class SnackbarItem (
    val show: Boolean = false,
    val messageResId: Int = R.string.empty_message,
    val isError: Boolean = false
)
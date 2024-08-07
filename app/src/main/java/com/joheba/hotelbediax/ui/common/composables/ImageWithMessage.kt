package com.joheba.hotelbediax.ui.common.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.joheba.hotelbediax.R

@Composable
fun ImageWithMessage(
    modifier: Modifier,
    imageResId: Int,
    messageResId: Int = R.string.empty_message,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .width(320.dp),
            painter = painterResource(imageResId),
            contentDescription = stringResource(id = messageResId),
            alignment = Alignment.BottomCenter,
            contentScale = ContentScale.Fit
        )
        Text(
            modifier = Modifier
                .padding(top = 12.dp)
                .align(Alignment.CenterHorizontally),
            text = stringResource(id = messageResId)
        )
    }
}

@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier,
) {
    CircularProgressIndicator(
        modifier = modifier
    )
}
package com.joheba.hotelbediax.ui.common.composables.topbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.joheba.hotelbediax.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior? = null,
    syncAction: () -> Unit,
    bottomSheetAction: () -> Unit,
) {
    TopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = stringResource(id = R.string.destinations)
            )
        },
        actions = {
            IconButton(
                onClick = {
                    bottomSheetAction()
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.FilterList,
                    contentDescription = Icons.Filled.FilterList.name
                )
            }
            IconButton(
                onClick = {
                    syncAction()
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Sync,
                    contentDescription = Icons.Filled.Sync.name
                )
            }
        }
    )
}
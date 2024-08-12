package com.joheba.hotelbediax.ui.common.composables.topbar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.joheba.hotelbediax.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior? = null,
    syncAction: () -> Unit,
    bottomSheetAction: () -> Unit,
    pendingOperations: Int
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
                Box {
                    Icon(
                        imageVector = Icons.Filled.Sync,
                        contentDescription = Icons.Filled.Sync.name
                    )
                    Canvas(
                        modifier = Modifier
                            .size(8.dp)
                            .align(Alignment.TopEnd)
                            .padding(
                                end = 2.dp,
                                top = 2.dp
                            ),
                        contentDescription = Icons.Filled.Sync.name
                    ) {
                        drawCircle(
                            color = if(pendingOperations > 0) Color.Red else Color.Green
                        )
                    }
                }
            }
        }
    )
}
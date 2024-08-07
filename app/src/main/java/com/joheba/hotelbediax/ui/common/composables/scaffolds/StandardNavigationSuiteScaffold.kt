package com.joheba.hotelbediax.ui.common.composables.scaffolds

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.joheba.hotelbediax.R
import com.joheba.hotelbediax.ui.navigation.MainAppScreens

@Composable
fun StandardNavigationSuiteScaffold(
    selectedNavigationIndex: Int,
    navigateToItemRoute: (String) -> Unit,
    content: @Composable () -> Unit
) {
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            NavigationItemList.list.forEachIndexed { index, navigationItem ->
                item(
                    selected = index == selectedNavigationIndex,
                    onClick = {
                        navigateToItemRoute(navigationItem.route)
                    },
                    icon = {
                        Icon(
                            imageVector = if (index == selectedNavigationIndex) navigationItem.selectedIcon else navigationItem.unselectedIcon,
                            contentDescription = navigationItem.labelResId?.let { stringResource(id = it) },
                        )
                    },
                    label = {
                        navigationItem.labelResId?.let {
                            Text(
                                text = stringResource(id = it)
                            )
                        }
                    },
                    alwaysShowLabel = true
                )
            }
        },
        content = content
    )
}

private object NavigationItemList {
    val list = listOf(
        NavigationItem(
            route = MainAppScreens.Home.route,
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            labelResId = R.string.home
        ),
        NavigationItem(
            route = MainAppScreens.Destinations.route,
            selectedIcon = Icons.Filled.Place,
            unselectedIcon = Icons.Outlined.Place,
            labelResId = R.string.destinations
        )
    )
}

private class NavigationItem(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val labelResId: Int? = null,
)
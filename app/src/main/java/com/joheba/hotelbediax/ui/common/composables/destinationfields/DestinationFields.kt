package com.joheba.hotelbediax.ui.common.composables.destinationfields

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.joheba.hotelbediax.R
import com.joheba.hotelbediax.domain.core.DestinationType
import com.joheba.hotelbediax.ui.common.composables.ColumnItemDivider
import com.joheba.hotelbediax.ui.theme.filterNotOkButton


@Composable
fun NameField(
    modifier: Modifier = Modifier,
    value: String?,
    onValueChange: (String?) -> Unit,
    isEnabled: Boolean = true
) {
    Row(
        modifier = modifier
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = value ?: "",
            onValueChange = { newValue ->
                if (newValue.isNotBlank()) {
                    onValueChange(newValue)
                } else onValueChange(null)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.TextFields,
                    contentDescription = Icons.Filled.TextFields.name
                )
            },
            label = {
                Text(
                    text = stringResource(id = R.string.filter_name)
                )
            },
            placeholder = {
                Text(
                    text = stringResource(id = R.string.filter_name_placeholder)
                )
            },
            trailingIcon = {
                value?.let {
                    if (it.isNotBlank() && isEnabled) {
                        IconButton(
                            onClick = {
                                onValueChange(null)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = Icons.Filled.Close.name
                            )
                        }
                    }
                }
            },
            enabled = isEnabled
        )
    }
}

@Composable
fun DescriptionField(
    modifier: Modifier = Modifier,
    value: String?,
    onValueChange: (String?) -> Unit,
    isEnabled: Boolean = true
) {
    Row(
        modifier = modifier
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = value ?: "",
            onValueChange = { newValue ->
                if (newValue.isNotBlank()) {
                    onValueChange(newValue)
                } else onValueChange(null)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.TextFields,
                    contentDescription = Icons.Filled.TextFields.name
                )
            },
            label = {
                Text(
                    text = stringResource(id = R.string.filter_description)
                )
            },
            placeholder = {
                Text(
                    text = stringResource(id = R.string.filter_description_placeholder)
                )
            },
            trailingIcon = {
                value?.let {
                    if (it.isNotBlank() && isEnabled) {
                        IconButton(
                            onClick = {
                                onValueChange(null)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = Icons.Filled.Close.name
                            )
                        }
                    }
                }
            },
            enabled = isEnabled
        )
    }
}

@Composable
fun CountryCodeField(
    modifier: Modifier = Modifier,
    value: String?,
    onValueChange: (String?) -> Unit,
    openCountryCodeSelector: () -> Unit,
    isEnabled: Boolean = true
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                if (isEnabled) {
                    openCountryCodeSelector()
                }
            }
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = value?.uppercase() ?: "",
            onValueChange = { newValue ->
                if (newValue.isNotBlank()) {
                    onValueChange(newValue)
                } else onValueChange(null)
            },
            enabled = false,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Flag,
                    contentDescription = Icons.Filled.Flag.name
                )
            },
            label = {
                Text(
                    text = stringResource(id = R.string.filter_country_code)
                )
            },
            placeholder = {
                Text(
                    text = stringResource(id = R.string.filter_country_code_placeholder)
                )
            },
            trailingIcon = {
                value?.let {
                    if (value.isNotBlank() && isEnabled) {
                        IconButton(
                            onClick = {
                                onValueChange(null)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = Icons.Filled.Close.name
                            )
                        }
                    }
                }
            },
            colors = if (isEnabled)
            TextFieldDefaults.colors(
                disabledContainerColor = TextFieldDefaults.colors().unfocusedContainerColor,
                disabledPrefixColor = TextFieldDefaults.colors().unfocusedPrefixColor,
                disabledSuffixColor = TextFieldDefaults.colors().unfocusedSuffixColor,
                disabledLabelColor = TextFieldDefaults.colors().unfocusedLabelColor,
                disabledTextColor = TextFieldDefaults.colors().unfocusedTextColor,
                disabledIndicatorColor = TextFieldDefaults.colors().unfocusedIndicatorColor,
                disabledPlaceholderColor = TextFieldDefaults.colors().unfocusedPlaceholderColor,
                disabledLeadingIconColor = TextFieldDefaults.colors().unfocusedLeadingIconColor,
                disabledTrailingIconColor = TextFieldDefaults.colors().unfocusedTrailingIconColor,
                disabledSupportingTextColor = TextFieldDefaults.colors().unfocusedSupportingTextColor,
            ) else TextFieldDefaults.colors()
        )
    }
}

@Composable
fun CountryCodeSelector(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    updateCountryCode: (String?) -> Unit,
) {
    val countryCodeList = java.util.Locale.getISOCountries()
    val readableCountryCodeList = countryCodeList.map {
        java.util.Locale("", it).displayCountry
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = .95f))
            .clickable {
                onDismiss()
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier
                .widthIn(
                    max = 300.dp
                )
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(8.dp))
        ) {
            items(
                count = countryCodeList.size,
                key = { index ->
                    countryCodeList[index]
                }
            ) { index ->
                Column(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(6.dp)
                        .clickable {
                            updateCountryCode(countryCodeList[index])
                            onDismiss()
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = readableCountryCodeList[index] + " (${countryCodeList[index]})",
                        textAlign = TextAlign.Center
                    )
                    ColumnItemDivider()
                }
            }
        }
    }
}

@Composable
fun TypeField(
    modifier: Modifier = Modifier,
    value: DestinationType?,
    onValueChange: (DestinationType?) -> Unit,
    selectedFieldColor: Color,
    unSelectedIcon: ImageVector? = null,
    selectedIcon: ImageVector? = null,
    isEnabled: Boolean = true
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val isCountrySelected = value == DestinationType.COUNTRY
        val isCitySelected = value == DestinationType.CITY

        ElevatedButton(
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = if (isCountrySelected) selectedFieldColor else Color.Unspecified,
                disabledContainerColor = if (isCountrySelected) selectedFieldColor.copy(alpha = .5f) else Color.Unspecified,
            ),
            onClick = {
                onValueChange(if (isCountrySelected) null else DestinationType.COUNTRY)
            },
            enabled = isEnabled
        ) {
            Text(
                text = stringResource(id = DestinationType.COUNTRY.nameResId)
            )

            unSelectedIcon?.let {
                selectedIcon?.let {
                    Icon(
                        imageVector = if (isCountrySelected) selectedIcon else unSelectedIcon,
                        contentDescription = if (isCountrySelected) selectedIcon.name else unSelectedIcon.name
                    )
                }
            }
        }
        ElevatedButton(
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = if (isCitySelected) selectedFieldColor else Color.Unspecified,
                disabledContainerColor = if (isCitySelected) selectedFieldColor else Color.Unspecified,
            ),
            onClick = {
                onValueChange(if (isCitySelected) null else DestinationType.CITY)
            },
            enabled = isEnabled
        ) {
            Text(
                text = stringResource(id = DestinationType.CITY.nameResId)
            )
            unSelectedIcon?.let {
                selectedIcon?.let {
                    Icon(
                        imageVector = if (isCountrySelected) selectedIcon else unSelectedIcon,
                        contentDescription = if (isCountrySelected) selectedIcon.name else unSelectedIcon.name
                    )
                }
            }
        }
    }
}
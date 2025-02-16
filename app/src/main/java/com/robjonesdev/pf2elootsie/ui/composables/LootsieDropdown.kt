package com.robjonesdev.pf2elootsie.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T: Enum<*>> LootsieDropdown(
    isExpanded: Boolean = false,
    selected: List<T> = listOf(),
    onSelection: (T) -> Unit,
    dropdownLabel: String,
    items: Iterable<T>
){

    var listExpanded by remember { mutableStateOf(isExpanded) }

    ExposedDropdownMenuBox(
        expanded = listExpanded,
        onExpandedChange = { listExpanded = !listExpanded }
    ) {
        OutlinedTextField(
            value = selected.joinToString { it.name },
            onValueChange = {},
            readOnly = true,
            label = { Text(dropdownLabel) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = listExpanded)
            },
            modifier = Modifier.fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = listExpanded,
            onDismissRequest = { listExpanded = false }
        ) {
            for (item in items) {
                DropdownMenuItem(
                    onClick = {
                        onSelection(item)
                    },
                    modifier = Modifier.background(
                        color = if(selected.contains(item)) {
                            MaterialTheme.colors.primary
                        } else {
                            MaterialTheme.colors.surface
                        }
                    )
                ) {
                    Text(text = item.toString())
                }
            }
        }
    }
}
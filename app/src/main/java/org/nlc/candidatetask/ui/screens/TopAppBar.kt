package org.nlc.candidatetask.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.nlc.candidatetask.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookTopBar(
    topBarTitle: Int,
    onAddItem: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = topBarTitle),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = MaterialTheme.colorScheme.surface),
        actions = {
            IconButton(onClick = onAddItem) {
                Icon(Icons.Filled.Add, contentDescription = "Add Item")
            }
        }
    )
}

@Preview
@Composable
fun BookTopBarPreview() {
    BookTopBar(topBarTitle = R.string.app_name,
        onAddItem = {})
}
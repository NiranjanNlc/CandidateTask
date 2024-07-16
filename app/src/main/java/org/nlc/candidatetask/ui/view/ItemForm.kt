package org.nlc.candidatetask.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.nlc.candidatetask.data.Item

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemForm(
    item: Item,
    onSave: (Item) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Item Form") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            TextField(
                value = item.title,
                onValueChange = { item.copy(title = it) },
                label = { Text("Title") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = item.description ?: "",
                onValueChange = { item.copy(description = it) },
                label = { Text("Description") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = item.imageUrl ?: "",
                onValueChange = { item.copy(imageUrl = it) },
                label = { Text("Image URL") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onSave(item) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
        }
    }
}
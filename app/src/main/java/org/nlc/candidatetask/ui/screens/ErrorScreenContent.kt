package org.nlc.candidatetask.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.nlc.candidatetask.ui.theme.CandidateTaskTheme

@Composable
fun ErrorScreenContent(
    modifier: Modifier = Modifier,
    title: String ="Error Occurred!",
    subTitle: String ="Try Again",
    onClickRetry: () -> Unit = { }
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = modifier
                .padding(16.dp)
                .fillMaxSize()
                .testTag("mifos:empty"),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = subTitle,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
            )

            Button(
                modifier = Modifier
                    .width(150.dp)
                    .padding(top = 16.dp),
                onClick = onClickRetry
            ) {
                Text(text ="Retry")
            }
        }
    }
}

@Preview(device = "id:pixel_5")
@Composable
fun ErrorContentScreenDrawableImagePreview() {
    CandidateTaskTheme {
        ErrorScreenContent(
            modifier = Modifier,
            title = "Error Occurred!",
            subTitle = "Please check your connection or try again",
            onClickRetry = { }
        )
    }
}

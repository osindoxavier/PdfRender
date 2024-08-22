package com.xavier.pdfreader.presentation

import android.graphics.Bitmap
import android.graphics.RectF
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.xavier.pdfreader.data.SearchResults

@Composable
fun PDFViewScreen(
    modifier: Modifier = Modifier,
    onPDFSelectorClicked: () -> Unit,
    pdfUri: Uri?,
    renderedPages: List<Bitmap>,
    searchText: String,
    searchResults: List<SearchResults>,
    onSearchTextChanged: (String) -> Unit,
    clearSearchText: () -> Unit,
) {
    if (pdfUri == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Button(onClick = { onPDFSelectorClicked() }) {
                Text(text = "Choose PDF")

            }
        }
    } else {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                itemsIndexed(renderedPages) { index, page ->
                    PDFPage(
                        page = page,
                        searchResults = searchResults.find { it.page == index }
                    )

                }
            }

            if (Build.VERSION.SDK_INT == 35) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = searchText,
                    onValueChange = { newSearchText ->
                        onSearchTextChanged(newSearchText)
                    },
                    trailingIcon = {
                        if (searchText.isNotEmpty()) {
                            IconButton(onClick = {
                                clearSearchText()
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Clear,
                                    contentDescription = "Clear search"
                                )
                            }
                        }
                    },
                    placeholder = {
                        Text(text = "Search Words.....")
                    }
                )
            }

        }

    }
}


@Composable
fun PDFPage(
    modifier: Modifier = Modifier,
    page: Bitmap,
    searchResults: SearchResults? = null
) {

    AsyncImage(
        model = page,
        contentDescription = null,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(page.width.toFloat() / page.height.toFloat())
            .drawWithContent {
                drawContent()

                val scaleFactorX = size.width / page.width
                val scaleFactorY = size.height / page.height

                searchResults?.results?.forEach { rect ->
                    val adjustedRect = RectF(
                        rect.left * scaleFactorX,
                        rect.top * scaleFactorY,
                        rect.right * scaleFactorX,
                        rect.bottom * scaleFactorY
                    )

                    drawRoundRect(
                        color = Color.Yellow.copy(alpha = 0.5f),
                        topLeft = Offset(
                            x = adjustedRect.left,
                            y = adjustedRect.top
                        ),
                        size = Size(
                            width = adjustedRect.width(),
                            height = adjustedRect.height()
                        ),
                        cornerRadius = CornerRadius(5.dp.toPx())
                    )

                }

            }
    )

}
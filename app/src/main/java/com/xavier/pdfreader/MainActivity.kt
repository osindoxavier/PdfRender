package com.xavier.pdfreader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.xavier.pdfreader.presentation.PDFViewScreen
import com.xavier.pdfreader.presentation.PDFViewViewModel
import com.xavier.pdfreader.ui.theme.PdfReaderTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val viewModel: PDFViewViewModel by viewModel()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PdfReaderTheme {
                val choosePDFLauncher =
                    rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
                        uri?.let { viewModel.onPdfSelected(it) }
                    }
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PDFViewScreen(
                        modifier = Modifier.padding(innerPadding),
                        onPDFSelectorClicked = {
                            choosePDFLauncher.launch("application/pdf")

                        },
                        pdfUri = viewModel.pdfUri,
                        renderedPages = viewModel.renderedPages,
                        searchText = viewModel.searchText,
                        searchResults = viewModel.searchResults,
                        onSearchTextChanged = { value: String ->
                            viewModel.onSearchTextChanged(value)
                        },
                        clearSearchText = {
                            viewModel.clearSearch()
                        }
                    )
                }
            }
        }
    }
}


package com.xavier.pdfreader.presentation

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xavier.pdfreader.data.SearchResults
import com.xavier.pdfreader.domain.PDFConverterRepository
import kotlinx.coroutines.launch

class PDFViewViewModel(
    private val repository: PDFConverterRepository
) : ViewModel() {

    var pdfUri by mutableStateOf<Uri?>(null)
        private set

    var renderedPages by mutableStateOf<List<Bitmap>>(emptyList())
        private set

    var searchText by mutableStateOf("")
        private set

    var searchResults by mutableStateOf(emptyList<SearchResults>())
        private set


    fun onPdfSelected(uri: Uri) {
        pdfUri = uri
        viewModelScope.launch {
            renderedPages = repository.pdfToBitmap(uri)
        }
    }

    fun onSearchTextChanged(text: String) {
        searchText = text
        viewModelScope.launch {
            searchResults = repository.searchInPdf(query = text)
        }
    }

    fun clearSearch() {
        searchText = ""
        searchResults = emptyList()
    }


}
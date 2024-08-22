package com.xavier.pdfreader.domain

import android.app.appsearch.SearchResult
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import com.xavier.pdfreader.data.SearchResults

interface PDFConverterRepository {
    suspend fun pdfToBitmap(contentUri:Uri):List<Bitmap>
    suspend fun searchInPdf(query: String): List<SearchResults>
}
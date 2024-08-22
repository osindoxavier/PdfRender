package com.xavier.pdfreader.domain

import android.app.appsearch.SearchResult
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import androidx.annotation.RequiresApi
import com.xavier.pdfreader.data.SearchResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

class PDFConverterRepositoryImpl(private val context: Context) :
    PDFConverterRepository {

    private var renderer: PdfRenderer? = null
    override suspend fun pdfToBitmap(contentUri: Uri): List<Bitmap> {

        return withContext(Dispatchers.IO) {
            renderer?.close()
            context.contentResolver.openFileDescriptor(
                contentUri,
                "r"
            )?.use { descriptor ->
                with(PdfRenderer(descriptor)) {
                    renderer = this

                    return@withContext (0 until pageCount).map { index ->
                        async {
                            openPage(index).use { page ->
                                val bitMap = Bitmap.createBitmap(
                                    page.width,
                                    page.height,
                                    Bitmap.Config.ARGB_8888
                                )
                                val canvas = android.graphics.Canvas(bitMap).apply {
                                    drawColor(Color.WHITE)
                                    drawBitmap(bitMap, 0f, 0f, null)
                                }
                                page.render(
                                    bitMap,
                                    null,
                                    null,
                                    PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                                )

                                bitMap
                            }
                        }
                    }.awaitAll()
                }

            }
            return@withContext emptyList()

        }
    }

    @RequiresApi(35)
    override suspend fun searchInPdf(query: String): List<SearchResults> {
        return withContext(Dispatchers.Default) {
            renderer?.let { renderer ->
                (0 until renderer.pageCount).map { index ->
                    async {
                        renderer.openPage(index).use { page ->
                            val results = page.searchText(query)

                            val matchedRects = results.map {
                                it.bounds.first()
                            }

                            SearchResults(
                                page = index,
                                results = matchedRects
                            )
                        }
                    }
                }.awaitAll()
            } ?: emptyList()
        }
    }

}
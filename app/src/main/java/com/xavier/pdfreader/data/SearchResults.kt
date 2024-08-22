package com.xavier.pdfreader.data

import android.graphics.RectF

data class SearchResults(
    val page: Int,
    val results: List<RectF>
)

package com.xavier.pdfreader.di

import com.xavier.pdfreader.domain.PDFConverterRepository
import com.xavier.pdfreader.domain.PDFConverterRepositoryImpl
import com.xavier.pdfreader.presentation.PDFViewViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

object AppModule {
    val appModule = module {
        single<PDFConverterRepository> { PDFConverterRepositoryImpl(androidContext()) }
        viewModel {
            PDFViewViewModel(get())
        }
    }
}
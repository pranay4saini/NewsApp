package com.pranay.newsapp.ui

import androidx.lifecycle.ViewModel
import com.pranay.newsapp.database.NewsRepository

class NewsViewModel(val newsRepository: NewsRepository):ViewModel() {
}
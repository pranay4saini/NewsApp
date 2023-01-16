package com.pranay.newsapp.model

import com.pranay.newsapp.model.Article

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)
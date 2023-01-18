package com.pranay.newsapp.model

import com.pranay.newsapp.model.Article

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)
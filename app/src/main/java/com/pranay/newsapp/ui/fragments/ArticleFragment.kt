package com.pranay.newsapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.pranay.newsapp.R
import com.pranay.newsapp.databinding.FragmentArticleBinding
import com.pranay.newsapp.databinding.FragmentBreakingNewsBinding
import com.pranay.newsapp.ui.MainActivity
import com.pranay.newsapp.ui.NewsViewModel

class ArticleFragment : Fragment(R.layout.fragment_article) {
    lateinit var viewModel: NewsViewModel
    val args: ArticleFragmentArgs by navArgs()
    private var binding: FragmentArticleBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArticleBinding.bind(view)
        viewModel = (activity as MainActivity).viewModel

        val article = args.article
        binding?.webView?.apply {
            webViewClient = WebViewClient()
            article.url?.let { loadUrl(it) }
        }

        binding?.fab?.setOnClickListener{

            viewModel.saveArticle(article)
            Snackbar.make(view,"Article saved successfully",Snackbar.LENGTH_SHORT).show()

        }
    }
}
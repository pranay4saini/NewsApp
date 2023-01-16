package com.pranay.newsapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.pranay.newsapp.R
import com.pranay.newsapp.ui.MainActivity
import com.pranay.newsapp.ui.NewsViewModel

class BreakingNewsFragment:Fragment(R.layout.fragment_breaking_news) {

    lateinit var viewModel:NewsViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }
}
package com.pranay.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pranay.newsapp.R
import com.pranay.newsapp.adapter.NewsAdapter
import com.pranay.newsapp.databinding.FragmentSearchNewsBinding
import com.pranay.newsapp.ui.MainActivity
import com.pranay.newsapp.ui.NewsViewModel
import com.pranay.newsapp.util.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.pranay.newsapp.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment:Fragment(R.layout.fragment_search_news) {
    lateinit var viewModel: NewsViewModel
    val TAG = "searchNewsFragment"
    lateinit var newsAdapter: NewsAdapter
    private var binding: FragmentSearchNewsBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchNewsBinding.bind(view)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(R.id.action_searchNewsFragment_to_articleFragment,bundle)
        }

        //CREATING DELAY IN SEARCH QUERY
        var job: Job? = null
        binding?.editTextSearch?.addTextChangedListener {editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let{
                    if (editable.toString().isNotEmpty()){
                        viewModel.searchNews(editable.toString())
                    }
                }
            }
        }


        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An error occured: $message ")
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }

        })
    }


    private fun hideProgressBar() {
        binding?.searchPaginationProgressBar?.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding?.searchPaginationProgressBar?.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding?.rcSearchNews?.apply {          //#######################IMP fragment binding
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }


    }
}
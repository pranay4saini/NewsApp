package com.pranay.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pranay.newsapp.R
import com.pranay.newsapp.adapter.NewsAdapter
import com.pranay.newsapp.databinding.FragmentSearchNewsBinding
import com.pranay.newsapp.ui.MainActivity
import com.pranay.newsapp.ui.NewsViewModel
import com.pranay.newsapp.util.Constants
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
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.searchNewsPage == totalPages
                        if(isLastPage){
                            binding?.rcSearchNews?.setPadding(0,0,0,0)
                        }
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
        isLoading = false
    }

    private fun showProgressBar() {
        binding?.searchPaginationProgressBar?.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener(){

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount


            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastPage = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastPage && isNotAtBeginning &&
                    isTotalMoreThanVisible &&isScrolling
            if(shouldPaginate){
                viewModel.searchNews(binding?.editTextSearch?.text.toString())
                isScrolling = false
            }
        }
    }
    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding?.rcSearchNews?.apply {          //#######################IMP fragment binding
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)

            addOnScrollListener(this@SearchNewsFragment.scrollListener)
        }


    }
}
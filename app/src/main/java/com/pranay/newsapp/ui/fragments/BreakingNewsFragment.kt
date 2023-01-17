package com.pranay.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View


import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pranay.newsapp.R
import com.pranay.newsapp.adapter.NewsAdapter
import com.pranay.newsapp.databinding.FragmentBreakingNewsBinding
import com.pranay.newsapp.ui.MainActivity
import com.pranay.newsapp.ui.NewsViewModel
import com.pranay.newsapp.util.Resource

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    val TAG = "breakingnewsfragment"
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    private var binding: FragmentBreakingNewsBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBreakingNewsBinding.bind(view)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(R.id.action_breakingNewsFragment_to_articleFragment,bundle)
        }
        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
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
        binding?.paginationProgressBar?.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding?.paginationProgressBar?.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding?.rcBreakingNews?.apply {          //#######################IMP fragment binding
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }


    }
}
package com.pranay.newsapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pranay.newsapp.R
import com.pranay.newsapp.adapter.NewsAdapter
import com.pranay.newsapp.databinding.FragmentSavedNewsBinding
import com.pranay.newsapp.databinding.FragmentSearchNewsBinding
import com.pranay.newsapp.ui.MainActivity
import com.pranay.newsapp.ui.NewsViewModel

class SavedNewsFragment:Fragment(R.layout.fragment_saved_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    private var binding: FragmentSavedNewsBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        binding = FragmentSavedNewsBinding.bind(view)
        setupRecyclerView()


        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(R.id.action_savedNewsFragment_to_articleFragment,bundle)
        }


        val itemTouchHelperCallback  = object: ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return  true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            }
        }
        viewModel.getSavedNews().observe(viewLifecycleOwner, Observer { articles ->
        newsAdapter.differ.submitList(articles)

        })
    }


    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding?.rvSavedNews?.apply {          //#######################IMP fragment binding
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }


    }
}
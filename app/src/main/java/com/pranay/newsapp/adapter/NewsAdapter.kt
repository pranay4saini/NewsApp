package com.pranay.newsapp.adapter

import android.view.LayoutInflater

import android.view.ViewGroup

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.pranay.newsapp.databinding.ItemArticlePreviewBinding
import com.pranay.newsapp.model.Article

class NewsAdapter: RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(val binding :ItemArticlePreviewBinding):RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {

        val binding = ItemArticlePreviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ArticleViewHolder(binding)

            }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {

        val article = differ.currentList[position]  //gives us current article
        with(holder){
            holder.itemView.apply {
                Glide.with(this).load(article.urlToImage).into(binding.ivArticle)
                binding.textViewSource.text = article.source?.name
                binding.tvTitle.text = article.title
                binding.tvDescription.text = article.description
                binding.tvPublishAt.text = article.publishedAt
                setOnClickListener {
                    onItemClickListener?.let{
                        it(article)
                    }
                }
            }

        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }



    private val differCallback = object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallback)


    private var onItemClickListener: ((Article) -> Unit)? = null

    fun setOnItemClickListener(listener:(Article) -> Unit){
        onItemClickListener = listener
    }
}
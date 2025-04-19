package com.example.urtisi

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NewsAdapter(private val newsList: List<NewsItem>) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    // Оптимизированный ViewHolder с View Binding
    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.newsTitle)
        val date: TextView = itemView.findViewById(R.id.newsDate)

        // Добавляем обработчик клика в ViewHolder
        fun bind(news: NewsItem, position: Int, onClick: (NewsItem) -> Unit) {
            title.text = news.title.ifEmpty { "Нет заголовка" }
            date.text = news.date.ifEmpty { "Нет даты" }

            itemView.setOnClickListener {
                Log.d("NewsAdapter", "Clicked item $position: ${news.title}")
                onClick(news)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.news_item, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = newsList[position]
        holder.bind(news, position) { clickedNews ->
            openNewsDetail(holder.itemView.context, clickedNews)
        }
    }

    override fun getItemCount() = newsList.size

    // Вынесенная логика открытия новости
    private fun openNewsDetail(context: android.content.Context, news: NewsItem) {
        try {
            val intent = Intent(context, NewsDetailActivity::class.java).apply {
                putExtra("url", news.link) // Теперь передаем только URL
                // Дополнительные данные можно получить через парсинг в NewsDetailActivity
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e("NewsAdapter", "Error opening NewsDetailActivity", e)
            // Fallback: открываем в браузере
            try {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(news.link))
                context.startActivity(browserIntent)
            } catch (e: Exception) {
                Log.e("NewsAdapter", "Error opening in browser", e)
                // Можно показать Toast с ошибкой
            }
        }
    }

    // Опционально: метод для обновления списка
    fun updateNews(newNewsList: List<NewsItem>) {
        (this as? androidx.recyclerview.widget.ListAdapter<NewsItem, NewsViewHolder>)?.submitList(newNewsList)
    }
}
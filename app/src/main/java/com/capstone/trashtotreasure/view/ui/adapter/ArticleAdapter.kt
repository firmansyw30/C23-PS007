package com.capstone.trashtotreasure.view.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.trashtotreasure.databinding.ItemArticleBinding
import com.capstone.trashtotreasure.model.data.remote.response.ArticlesItem
import com.capstone.trashtotreasure.view.ui.home.HomeFragment
import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import java.util.*

class ArticleAdapter(private val context: HomeFragment, private var list: List<ArticlesItem?>?) :  RecyclerView.Adapter<ArticleAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount() = list?.size ?: 0

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val news = list?.get(position)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        dateFormat.timeZone = TimeZone.getTimeZone("ID")
        val time = dateFormat.parse(news?.publishedAt!!)?.time
        val prettyTime = PrettyTime(Locale.getDefault())
        val date = prettyTime.format(time?.let { Date(it) })

        holder.binding.tvItemTitle.text = news?.title
        holder.binding.tvItemPublishedDate.text = date


        Glide.with(holder.itemView.context)
            .load(news?.urlToImage)
            .into(holder.binding.imgPoster)
    }

    inner class MyViewHolder(val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root)

    fun updateData(newList: List<ArticlesItem?>?) {
        list = newList
        notifyDataSetChanged()
    }
}
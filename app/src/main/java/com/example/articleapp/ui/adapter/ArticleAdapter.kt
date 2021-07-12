package com.example.articleapp.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.articleapp.R
import com.example.articleapp.data.db.entity.ArticleEntity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ArticleAdapter(val context: Context): RecyclerView.Adapter<ArticleAdapter.ViewHolderArticle>(),
    Filterable {


    private var articles : MutableList<ArticleEntity> = mutableListOf()
    private var articlesFull = mutableListOf<ArticleEntity>()
    var onClickListener: ItemClickListener? = null

    interface ItemClickListener {
        fun itemClick(view: View, article: ArticleEntity)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderArticle {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        return ViewHolderArticle(view)
    }

    override fun getItemCount(): Int {
       return articles.size
    }

    override fun onBindViewHolder(holder: ViewHolderArticle, position: Int) {
        val article = articles[position]
        holder.bindView(article)
        holder.let {
                onClickListener!!.itemClick(it.itemView, article)
        }
    }

    fun setArticleList(articlesList: List<ArticleEntity>){
        articles.clear()
        articles.addAll(articlesList)
        articlesFull = ArrayList<ArticleEntity>(articles)
        notifyDataSetChanged()
    }

   inner class ViewHolderArticle(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindView(article: ArticleEntity){
            val title = itemView.findViewById<TextView>(R.id.txt_title)
            val img = itemView.findViewById<ImageView>(R.id.img_article)
            val description = itemView.findViewById<TextView>(R.id.txt_description)

            title.text = article.title
            Glide.with(context).load(article.urlToImage).into(img);
            description.text = article.description
        }

    }

    override fun getFilter(): Filter {
        return filterArticle
    }

    private val filterArticle = object : Filter() {
        override fun performFiltering(p0: CharSequence?): FilterResults {
            Log.i("palavra digitada", p0.toString())
            val filtroList = mutableListOf<ArticleEntity>()
            if (p0 == null || p0.length == 0) {
                filtroList.addAll(articlesFull)
            }else{
                val fitro = p0.toString().toLowerCase().trim()
                for (item in articlesFull) {
                    if (item.title!!.toLowerCase().contains(fitro)) {
                        filtroList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filtroList

            return results
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {

            articles.clear()
            articles.addAll(p1!!.values as List<ArticleEntity>)
            notifyDataSetChanged()
        }

    }
}
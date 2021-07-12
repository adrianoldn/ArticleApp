package com.example.articleapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.articleapp.R
import com.example.articleapp.data.db.entity.ArticleEntity
import com.example.articleapp.util.UtilArticle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity() {

    lateinit var article: ArticleEntity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle()
        setContentView(R.layout.activity_detail)
        setBackButton()
        getExtras()
        setFieldsArticle()
        setBunttonShare()
        setButtonDownload()

    }

    private fun setTitle() {
        title = "Article Detail"
    }

    private fun setBackButton() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true);
    }

    override fun onSupportNavigateUp(): Boolean {
        finish();
        return true;
    }

    private fun setFieldsArticle() {
        txt_title.text = article.title
        txt_author.text = "Author: ${article.author}"
        val date = UtilArticle.formatDate(article.publishedAt!!)
        txt_data.text =  "Date: ${date}"
        txt_description.text = article.description
        Glide.with(this).load(article.urlToImage).into(img_article)
    }



    private fun getExtras() {
        article = intent.extras?.get("Article") as ArticleEntity
    }

    private fun setBunttonShare(
    ) {
        fab_shared.setOnClickListener {
            UtilArticle.shareImage(this, article.urlToImage!!)
        }
    }

    private fun setButtonDownload(
    ) {
        fab_download.setOnClickListener {
            GlobalScope.launch {
                val image = Glide.with(this@DetailActivity)
                    .asBitmap()
                    .load(article.urlToImage)
                    .submit()
                    .get()
                UtilArticle.saveImage(image, article.id, this@DetailActivity)
            }
        }
    }


}
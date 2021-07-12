package com.example.articleapp.ui

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.articleapp.R
import com.example.articleapp.data.db.AppDataBase
import com.example.articleapp.data.db.dao.ArticleDao
import com.example.articleapp.data.db.entity.ArticleEntity
import com.example.articleapp.factory.ArticleViewModelFactory
import com.example.articleapp.network.AppRetrofit
import com.example.articleapp.repository.ArticleRepository
import com.example.articleapp.ui.adapter.ArticleAdapter
import com.example.articleapp.util.UIArticle
import com.example.articleapp.viewModel.ArticleViewModel
import kotlinx.android.synthetic.main.activity_list_articles.*
import kotlinx.coroutines.*

class ArticleListActivity : AppCompatActivity() {

    private lateinit var viewModel: ArticleViewModel
    private lateinit var dao: ArticleDao
    private lateinit var api: AppRetrofit
    private lateinit var adapter: ArticleAdapter
    private lateinit var loading: AlertDialog
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_articles)
        val booleanValue = setPreferences()
        verifyModeTheme(booleanValue)
        haveStoragePermission()
        loading = UIArticle.createLoadDialog(this, false)
        setDependecies()
        setViewModel()
        getArticles()
        setupRecyclerView()
        setArticleList()
        setupSearchField()
        setSwitch()


    }

    private fun setSwitch() {
        switchCompat.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchCompat.isChecked = true
                val editor = sharedPreferences.edit()
                editor.putBoolean("night_mode", true)
                editor.commit()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchCompat.isChecked = false
                val editor = sharedPreferences.edit()
                editor.putBoolean("night_mode", false)
                editor.commit()
            }
        }
    }

    private fun verifyModeTheme(booleanValue: Boolean) {
        if (booleanValue) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            switchCompat.isChecked = true
        }
    }

    private fun setPreferences(): Boolean {
        sharedPreferences = getSharedPreferences("night", 0)
        val booleanValue = sharedPreferences.getBoolean("night_mode", false)
        return booleanValue
    }

    private fun setupSearchField() {

        sv_article.isIconifiedByDefault = false
        sv_article.clearFocus()
        sv_article.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            View.OnFocusChangeListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                adapter.filter.filter(p0)
                return true
            }

            override fun onFocusChange(p0: View?, p1: Boolean) {

            }


        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_drawer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

       when(id){
           R.id.refresh ->{
               getArticles()
           }

       }

        return super.onOptionsItemSelected(item)
    }

    private fun setArticleList() {
        viewModel.articlesLiveData.observe(this, Observer { articles ->
            if (!articles.isNullOrEmpty()) {
                txt_result.visibility = View.GONE
                adapter.setArticleList(articles)
            } else {
                adapter.setArticleList(listOf())
                txt_result.visibility = View.VISIBLE
            }
        })
    }

    fun setupRecyclerView() {
        rv_articles.setHasFixedSize(true)
        rv_articles.layoutManager = LinearLayoutManager(this)
        rv_articles.adapter = adapter
        onClickArticleItem()
    }

    private fun onClickArticleItem() {
        adapter.onClickListener = object : ArticleAdapter.ItemClickListener {
            override fun itemClick(
                view: View,
                article: ArticleEntity
            ) {
                view.setOnClickListener {
                    goToDatail(article)
                }
            }
        }
    }

    private fun goToDatail(article: ArticleEntity) {
        val intent = Intent(this@ArticleListActivity, DetailActivity::class.java)
        intent.putExtra("Article", article)
        startActivity(intent)
    }



    private fun getArticles() {
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                try {
                   loading.show()
                    delay(3000)
                    viewModel.fetchArticles()
                } finally {
                  loading.cancel()

                }
            }
        }

    }

    private fun setViewModel() {
        viewModel =
            ViewModelProvider(this, ArticleViewModelFactory(ArticleRepository(dao, api))).get(
                ArticleViewModel::class.java
            )
    }

    private fun setDependecies() {
        api = AppRetrofit()
        dao = AppDataBase.getDataBase(this).articleDao()
        adapter = ArticleAdapter(this)
    }


    fun haveStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                Log.e("Permission error", "You have permission")
                true
            } else {
                Log.e("Permission error", "You have asked for permission")
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
                false
            }
        } else { //you dont need to worry about these stuff below api level 23
            Log.e("Permission error", "You already have the permission")
            true
        }
    }

}
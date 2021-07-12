package com.example.articleapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.articleapp.data.db.dao.ArticleDao
import com.example.articleapp.data.db.entity.ArticleEntity


@Database(entities = [ArticleEntity::class], version = 4)
abstract class AppDataBase : RoomDatabase() {


    abstract fun articleDao (): ArticleDao

    companion object{
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDataBase(context: Context): AppDataBase {
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance  =  Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "article_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
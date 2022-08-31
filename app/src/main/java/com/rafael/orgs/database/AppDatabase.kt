package com.rafael.orgs.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rafael.orgs.database.converter.Converters
import com.rafael.orgs.database.dao.ProdutoDao
import com.rafael.orgs.model.Produto

@Database(entities = [Produto::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun produtoDao(): ProdutoDao

    companion object {

        @Volatile
        private lateinit var db: AppDatabase

        fun instancia(context: Context): AppDatabase {

            if (::db.isInitialized) return db
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "orgs.db"
            ).allowMainThreadQueries().build().also {
                db = it
            }
        }
    }

}
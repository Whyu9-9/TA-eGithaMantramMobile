package com.example.ekidungmantram.database.setup

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.database.dao.YadnyaDao
import com.example.ekidungmantram.database.data.Yadnya
import com.example.ekidungmantram.user.DetailYadnyaActivity
import com.example.ekidungmantram.user.fragment.ListYadnyaFragment

@Database(
    entities     = [Yadnya::class],
    version      = Constant.DATABASE_VERSION,
    exportSchema = false
)

abstract class YadnyaDb : RoomDatabase() {
    abstract fun yadnyaDao(): YadnyaDao

//    companion object {
//        @Volatile private var instance : YadnyaDb? = null
//        private val LOCK = Any()
//
//        operator fun invoke(context: DetailYadnyaActivity) = instance ?: synchronized(LOCK){
//            instance ?: buildDatabase(context).also {
//                instance = it
//            }
//        }
//
//        private fun buildDatabase(context: Context) = Room.databaseBuilder(
//            context.applicationContext,
//            YadnyaDb::class.java,
//            "yadnyabookmarked.db"
//        ).build()
//    }
}
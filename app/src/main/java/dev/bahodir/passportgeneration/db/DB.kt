package dev.bahodir.passportgeneration.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.bahodir.passportgeneration.user.User
import dev.bahodir.passportgeneration.userdao.UserDao

@Database(entities = [User::class], version = 1)
abstract class DB : RoomDatabase() {

    abstract fun userDB(): UserDao

    companion object {
        private var instance: DB? = null

        @Synchronized
        fun getInstance(context: Context): DB {
            if (instance == null) {
                instance = Room.databaseBuilder(context, DB::class.java, "db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }
}
package com.example.android.roomdevice

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Device::class], version = 1)
abstract class DeviceRoomDatabase : RoomDatabase() {

    abstract fun deviceDao(): DeviceDao

    companion object {
        @Volatile
        private var INSTANCE: DeviceRoomDatabase? = null

        fun getLocalDatabase(
            context: Context,
            scope: CoroutineScope
        ): DeviceRoomDatabase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DeviceRoomDatabase::class.java,
                    "dv_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        fun getDatabase(
                context: Context,
                scope: CoroutineScope,
                str: String
        ): DeviceRoomDatabase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        DeviceRoomDatabase::class.java,
                        "dv_db"
                )
                        .fallbackToDestructiveMigration()
                        .addCallback(WordDatabaseCallback(scope,str))
                        .build()
                INSTANCE = instance
                instance
            }
        }

        private class WordDatabaseCallback(
                private val scope: CoroutineScope,
                private val str: String
        ) : RoomDatabase.Callback() {

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.deviceDao(),str)
                    }
                }
            }
        }


        data class Record (var id: String = "", var dev: String = "", var per: String = "")


        fun populateDatabase(deviceDao: DeviceDao,str: String) {
            var dList : ArrayList<Record> = ArrayList(0)
            println("OK")
            if (!str.isEmpty()){
                println("lll")
                deviceDao.deleteAll()
            }
            var tempBuf: String = ""
            var localId = ""
            var localDev = ""
            var localBrickPer = ""
            var localLengthPer = ""
            var localSystemPar = ""
            var cnt = 0
            var autoIncrement = 0
            for (char in str) {
                if (char != '/') {
                    tempBuf += char
                } else {
                    println(tempBuf)
                    when (cnt) {
                        0 -> localId = tempBuf
                        1 -> localDev = tempBuf
                        2 -> localBrickPer = tempBuf
                        3 -> localLengthPer = tempBuf
                        4 -> localSystemPar = tempBuf
                    }
                    tempBuf = ""
                    cnt++
                    if (cnt == 5) {
                        print(localId.toInt())
                        print(localDev)
                        print(localBrickPer.toInt())
                        print(localLengthPer.toDouble())
                        print(localSystemPar)
                        autoIncrement++
                        var device = Device(autoIncrement, localDev, localBrickPer.toInt(),
                            localLengthPer.toDouble(),localSystemPar)
                        deviceDao.insert(device)
                        cnt = 0
                    }
                }
            }
            INSTANCE = null
        }
    }

}

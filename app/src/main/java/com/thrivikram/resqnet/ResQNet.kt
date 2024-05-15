package com.thrivikram.resqnet

import android.app.Application
import androidx.room.Room

class ResQNet : Application() {

    companion object {
        lateinit var database: ReportDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()

        // Initialize Room database
        database = Room.databaseBuilder(applicationContext, ReportDatabase::class.java, "report_database")
            .fallbackToDestructiveMigration()
            .build()
    }
}

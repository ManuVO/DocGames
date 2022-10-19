package com.example.docgames

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null,
    DATABASE_VERSION) {
    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "MySQLDataBase"
    }
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE Usuario (" +
                "USUARIO_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "USUARIO_EMAIL TEXT NOT NULL, " +
                "USUARIO_NOMBRE TEXT NOT NULL," +
                "USUARIO_PASS TEXT NOT NULL)")

        /* CREAMOS OTRA TABLA
        db.execSQL("CREATE TABLE Usuario2 (" +
                "UID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "email TEXT NOT NULL, " +
                "password TEXT NOT NULL)")
        */


    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS Usuario")
        //db.execSQL("DROP TABLE IF EXISTS Usuario2")
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
}
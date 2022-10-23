package com.example.docgames

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null,
    DATABASE_VERSION) {
    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "MySQLDataBase"

        private val USUARIO = "usuario"
        // User Table Columns names
        private val USUARIO_ID = "id"
        private val USUARIO_EMAIL = "email"
        private val USUARIO_NOMBRE = "nombre"
        private val USUARIO_PASS = "pass"
    }

    private val CREAR_TABLA_USUARIO = ("CREATE TABLE " + USUARIO + "(" +
            USUARIO_ID + "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            USUARIO_EMAIL + "TEXT NOT NULL, " +
            USUARIO_NOMBRE + "TEXT NOT NULL," +
            USUARIO_PASS + "TEXT NOT NULL)")

    private val DROP_TABLA_USUARIO = "DROP TABLE IF EXISTS USUARIO"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREAR_TABLA_USUARIO)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //Drop User Table if exist
        db.execSQL(DROP_TABLA_USUARIO)
        // Create tables again
        onCreate(db)
    }

    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */
    fun getAllUser(): List {
        // array of columns to fetch
        val columns = arrayOf(USUARIO_ID, USUARIO_EMAIL, USUARIO_NOMBRE, USUARIO_PASS)
        // sorting orders
        val sortOrder = "$USUARIO_NOMBRE ASC"
        val userList = ArrayList()
        val db = this.readableDatabase
        // query the user table
        val cursor = db.query(USUARIO, //Table to query
            columns,            //columns to return
            null,     //columns for the WHERE clause
            null,  //The values for the WHERE clause
            null,      //group the rows
            null,       //filter by row groups
            sortOrder)         //The sort order
        if (cursor.moveToFirst()) {
            do {
                val usuario = Usuario(id = cursor.getString(cursor.getColumnIndex(USUARIO_ID)).toInt(),
                    nombre = cursor.getString(cursor.getColumnIndex(USUARIO_NOMBRE)),
                    email = cursor.getString(cursor.getColumnIndex(USUARIO_EMAIL)),
                    pass = cursor.getString(cursor.getColumnIndex(USUARIO_PASS)))
                userList.add(usuario)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return userList
    }
    /**
     * This method is to create user record
     *
     * @param user
     */
    fun addUser(usuario: Usuario) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(USUARIO_NOMBRE, usuario.nombre)
        values.put(USUARIO_EMAIL, usuario.email)
        values.put(USUARIO_PASS, usuario.pass)
        // Inserting Row
        db.insert(USUARIO, null, values)
        db.close()
    }
    /**
     * This method to update user record
     *
     * @param user
     */
    fun updateUser(usuario: Usuario) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(USUARIO_NOMBRE, usuario.nombre)
        values.put(USUARIO_EMAIL, usuario.email)
        values.put(USUARIO_PASS, usuario.pass)
        // updating row
        db.update(USUARIO, values, "$USUARIO_ID = ?",
            arrayOf(usuario.id.toString()))
        db.close()
    }
    /**
     * This method is to delete user record
     *
     * @param user
     */
    fun deleteUser(usuario: Usuario) {
        val db = this.writableDatabase
        // delete user record by id
        db.delete(USUARIO, "$USUARIO_ID = ?",
            arrayOf(usuario.id.toString()))
        db.close()
    }
    /**
     * This method to check user exist or not
     *
     * @param email
     * @return true/false
     */
    fun checkUser(email: String): Boolean {
        // array of columns to fetch
        val columns = arrayOf(USUARIO_ID)
        val db = this.readableDatabase
        // selection criteria
        val selection = "$USUARIO_EMAIL = ?"
        // selection argument
        val selectionArgs = arrayOf(email)
        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        val cursor = db.query(USUARIO, //Table to query
            columns,        //columns to return
            selection,      //columns for the WHERE clause
            selectionArgs,  //The values for the WHERE clause
            null,  //group the rows
            null,   //filter by row groups
            null)  //The sort order
        val cursorCount = cursor.count
        cursor.close()
        db.close()
        if (cursorCount > 0) {
            return true
        }
        return false
    }
    /**
     * This method to check user exist or not
     *
     * @param email
     * @param password
     * @return true/false
     */
    fun checkUser(email: String, password: String): Boolean {
        // array of columns to fetch
        val columns = arrayOf(USUARIO_ID)
        val db = this.readableDatabase
        // selection criteria
        val selection = "$USUARIO_EMAIL = ? AND $USUARIO_PASS = ?"
        // selection arguments
        val selectionArgs = arrayOf(email, password)
        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        val cursor = db.query(USUARIO, //Table to query
            columns, //columns to return
            selection, //columns for the WHERE clause
            selectionArgs, //The values for the WHERE clause
            null,  //group the rows
            null, //filter by row groups
            null) //The sort order
        val cursorCount = cursor.count
        cursor.close()
        db.close()
        if (cursorCount > 0)
            return true
        return false
    }
}
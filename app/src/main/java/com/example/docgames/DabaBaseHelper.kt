package com.example.docgames

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.docgames.Converters as Converters

class DataBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null,
    DATABASE_VERSION) {
    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 2
        const val DATABASE_NAME = "MySQLDataBase"

        private val converters =  Converters()

        private val USUARIO = "usuario"
        // User Table Columns names
        private val USUARIO_ID = "id"
        private val USUARIO_EMAIL = "email"
        private val USUARIO_NOMBRE = "nombre"
        private val USUARIO_PASS = "pass"
        private val USUARIO_IMG = "img"

        private val VIDEOJUEGO = "videojuego"
        // User Table Columns names
        private val VIDEOJUEGO_ID = "id"
        private val VIDEOJUEGO_NOMBRE = "nombre"
        private val VIDEOJUEGO_SINOPSIS = "sinopsis"
        private val VIDEOJUEGO_IMG = "img"
}

    private val DROP_TABLA_USUARIO = "DROP TABLE IF EXISTS USUARIO"
    private val DROP_TABLA_VIDEOJUEGO = "DROP TABLE IF EXISTS VIDEOJUEGO"

    override fun onCreate(db: SQLiteDatabase) {
        //db.execSQL(CREAR_TABLA_USUARIO)
        db.execSQL("CREATE TABLE " + USUARIO + " (" +
                USUARIO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                USUARIO_EMAIL + " TEXT NOT NULL," +
                USUARIO_NOMBRE + " TEXT NOT NULL," +
                USUARIO_PASS + " TEXT NOT NULL," +
                USUARIO_IMG + " BLOB NULL" + ");")

        db.execSQL("CREATE TABLE " + VIDEOJUEGO + " (" +
                VIDEOJUEGO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                VIDEOJUEGO_NOMBRE + " TEXT NOT NULL," +
                VIDEOJUEGO_SINOPSIS + " TEXT NOT NULL," +
                VIDEOJUEGO_IMG + " BLOB NULL" + ");")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //Drop User Table if exist
        db.execSQL(DROP_TABLA_USUARIO)
        db.execSQL(DROP_TABLA_VIDEOJUEGO)
        // Create tables again
        onCreate(db)
    }

    //**************PARTE DE USUARIO**************//

    /**
     * Este metodo reune y devuelve una lista con todos los usuarios
     *
     * @return list
     */
    @SuppressLint("Range")
    fun getAllUser(): List<Usuario> {
        // array of columns to fetch
        val columns = arrayOf(USUARIO_ID, USUARIO_EMAIL, USUARIO_NOMBRE, USUARIO_PASS, USUARIO_IMG)
        // sorting orders
        val sortOrder = "$USUARIO_NOMBRE ASC"
        val userList = ArrayList<Usuario>()
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
                    pass = cursor.getString(cursor.getColumnIndex(USUARIO_PASS)),
                    img = converters.toBitmap(cursor.getBlob(cursor.getColumnIndex(USUARIO_IMG))))
                userList.add(usuario)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return userList
    }
    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */
    @SuppressLint("Range")
    fun getUserByEmail(email: String): Usuario? {
        val db = this.readableDatabase
        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        val cursor = db.query(USUARIO, //Table to query
            null,        //columns to return
            "usuario.email == '" + email + "'",      //columns for the WHERE clause
            null,  //The values for the WHERE clause
            null,  //group the rows
            null,   //filter by row groups
            null)  //The sort order
        with(cursor) {
            if (moveToFirst()){
                val itemID = getInt(getColumnIndexOrThrow("id"))
                val itemNombre = getString(getColumnIndexOrThrow("nombre"))
                val itemEmail = getString(getColumnIndexOrThrow("email"))
                val itemPass = getString(getColumnIndexOrThrow("pass"))
                val itemImg = converters.toBitmap(getBlob(getColumnIndexOrThrow("img")))
                var user : Usuario = Usuario(itemID, itemNombre, itemEmail, itemPass, itemImg)
                cursor.close()
                db.close()
                return user
            }
            else{
                return null;
            }
        }
    }
    /**
     * Este método agrega usuarios
     *
     * @param usuario
     */
    fun addUser(usuario: Usuario) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(USUARIO_NOMBRE, usuario.nombre)
        values.put(USUARIO_EMAIL, usuario.email)
        values.put(USUARIO_PASS, usuario.pass)
        values.put(USUARIO_IMG, converters.fromBitmap(usuario.img))
        // Se inserta la fila
        db.insert(USUARIO, null, values)
        db.close()
    }
    /**
     * Este método actualiza usuarios
     *
     * @param usuario
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
     * Este método elimina usuarios
     *
     * @param usuario
     */
    fun deleteUser(usuario: Usuario) {
        val db = this.writableDatabase
        // delete user record by id
        db.delete(USUARIO, "$USUARIO_ID = ?",
            arrayOf(usuario.id.toString()))
        db.close()
    }
    /**
     * Este método verifica si el usuario existe o no
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
         * Utilizamos el siguiente método como si fuera una query de SQL normal.
         * La SQL query equivalente a este metodo es
         * SELECT id FROM usuario WHERE email = 'ejemplo@ejemplo.com';
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
     * Este método verifica si el usuario existe o no
     *
     * @param email
     * @param password
     * @return true/false
     */
    fun checkUser(email: String, password: String): Boolean {
        // array of columns to fetch
        val columns = arrayOf(USUARIO_ID)
        val db = this.readableDatabase
        // criterio de selección
        val selection = "$USUARIO_EMAIL = ? AND $USUARIO_PASS = ?"
        // selection arguments
        val selectionArgs = arrayOf(email, password)
        // query user table with conditions
        /**
         * Utilizamos el siguiente método como si fuera una query de SQL normal.
         * La SQL query equivalente a este metodo es
         * SELECT id FROM usuario WHERE email = 'ejemplo@ejemplo.com' AND pass = 'ejemplo';
         */
        if(columns!=null){
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
        }
        db.close()
        return false
    }
    //Funcion para retornar el usuario loggeado
    fun getUser(email: String) : Usuario{
        val query = "SELECT * FROM $USUARIO WHERE $USUARIO_EMAIL = \"$email\""
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        var usuario: Usuario? = null;
        if(cursor.moveToFirst()){
            cursor.moveToFirst()

            usuario = Usuario(id = Integer.parseInt(cursor.getString(0)),
                nombre = cursor.getString(2),
                email = cursor.getString(1),
                pass = cursor.getString(3),
                img = converters.toBitmap(cursor.getBlob(4)))
        }
        else usuario = Usuario(1,"","","", null)
        return usuario
    }

    //**************PARTE DE VIDEOJUEGO**************//

}
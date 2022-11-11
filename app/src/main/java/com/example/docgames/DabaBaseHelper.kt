package com.example.docgames

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class DataBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null,
    DATABASE_VERSION) {
    companion object {
        const val DATABASE_VERSION = 3
        const val DATABASE_NAME = "MySQLDataBase"

        private val converters =  Converters()

        private val USUARIO = "usuario"
        // Nombre de las columnas de la tabla usuario
        private val USUARIO_ID = "id"
        private val USUARIO_EMAIL = "email"
        private val USUARIO_NOMBRE = "nombre"
        private val USUARIO_PASS = "pass"
        private val USUARIO_IMG = "img"
        private val USUARIO_BIO = "bio"

        private val VIDEOJUEGO = "videojuego"
        // Nombre de las columnas de la tabla videojuego
        private val VIDEOJUEGO_ID = "id"
        private val VIDEOJUEGO_NOMBRE = "nombre"
        private val VIDEOJUEGO_SINOPSIS = "sinopsis"
        private val VIDEOJUEGO_IMG = "img"

        private val USERGAME = "usergame"
        // Nombre de las columnas de la tabla usergame
        private val USERGAME_USUARIO = "idUsuario"
        private val USERGAME_VIDEOJUEGO = "idVideojuego"
        private val USERGAME_ESTADO = "estado"
}

    private val DROP_TABLA_USUARIO = "DROP TABLE IF EXISTS USUARIO"
    private val DROP_TABLA_VIDEOJUEGO = "DROP TABLE IF EXISTS VIDEOJUEGO"
    private val DROP_TABLA_USERGAME = "DROP TABLE IF EXISTS USERGAME"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE " + USUARIO + " (" +
                USUARIO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                USUARIO_EMAIL + " TEXT NOT NULL," +
                USUARIO_NOMBRE + " TEXT NOT NULL," +
                USUARIO_PASS + " TEXT NOT NULL," +
                USUARIO_IMG + " BLOB NULL," +
                USUARIO_BIO + " TEXT" + ");")

        db.execSQL("CREATE TABLE " + VIDEOJUEGO + " (" +
                VIDEOJUEGO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                VIDEOJUEGO_NOMBRE + " TEXT NOT NULL," +
                VIDEOJUEGO_SINOPSIS + " TEXT NOT NULL," +
                VIDEOJUEGO_IMG + " BLOB NULL" + ");")
        db.execSQL("CREATE TABLE " + USERGAME + " (" +
                USERGAME_USUARIO + " INTEGER NOT NULL, " +
                USERGAME_VIDEOJUEGO + " INTEGER NOT NULL, " +
                USERGAME_ESTADO + " TEXT NOT NULL, " +
                "FOREIGN KEY (" + USERGAME_USUARIO + ") REFERENCES " + USUARIO + "(" + USUARIO_ID + "), " +
                "FOREIGN KEY (" + USERGAME_VIDEOJUEGO + ") REFERENCES " + VIDEOJUEGO + "(" + VIDEOJUEGO_ID + "));")


        GlobalScope.launch {
            //***********INSERT VIDEOJUEGOS***************//
            db.execSQL("INSERT INTO " + VIDEOJUEGO + " (" +
                    "VALUES (-1, " +
                    "Pokemon Soulsilver, " +
                    "Prepárate para una emocionante aventura con Pokémon SoulSilver para la consola Nintendo DS. " +
                    "Ponte en la piel de un entrenador o entrenadora Pokémon y lucha en la región de Johto con tus " +
                    "Pokémon para conseguir la victoria., " +
                    getBitmap("https://m.media-amazon.com/images/I/517ZggTk5PL._AC_.jpg") + ");")
        }


    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //Drop User Table if exist
        db.execSQL(DROP_TABLA_USUARIO)
        db.execSQL(DROP_TABLA_VIDEOJUEGO)
        db.execSQL(DROP_TABLA_USERGAME)
        // Vuelve a crear las tablas
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
        // Array de las columnas a añadir
        val columns = arrayOf(USUARIO_ID, USUARIO_EMAIL, USUARIO_NOMBRE, USUARIO_PASS, USUARIO_IMG)
        // Tipo de orden
        val sortOrder = "$USUARIO_NOMBRE ASC"
        val userList = ArrayList<Usuario>()
        val db = this.readableDatabase
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
                    img = converters.toBitmap(cursor.getBlob(cursor.getColumnIndex(USUARIO_IMG))),
                    biografia = cursor.getString(cursor.getColumnIndex(USUARIO_BIO)))
                userList.add(usuario)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return userList
    }
    /**
     * Este metodo busca y devuelve a un usuario por su email
     *
     * @return list
     */
    @SuppressLint("Range")
    fun getUserByEmail(email: String): Usuario? {
        val db = this.readableDatabase
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
                val itemBio = getString(getColumnIndexOrThrow("bio"))
                var user : Usuario = Usuario(itemID, itemNombre, itemEmail, itemPass, itemImg, itemBio)
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
        values.put(USUARIO_BIO,usuario.biografia)
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
        values.put(USUARIO_IMG, converters.fromBitmap(usuario.img))
        values.put(USUARIO_BIO,usuario.biografia)
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
        // Elimina un usuario por id
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
        val columns = arrayOf(USUARIO_ID)
        val db = this.readableDatabase
        val selection = "$USUARIO_EMAIL = ?"
        val selectionArgs = arrayOf(email)
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
        val columns = arrayOf(USUARIO_ID)
        val db = this.readableDatabase
        val selection = "$USUARIO_EMAIL = ? AND $USUARIO_PASS = ?"
        val selectionArgs = arrayOf(email, password)
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
                img = converters.toBitmap(cursor.getBlob(4)),
                biografia = cursor.getString(5))
        }
        else usuario = Usuario(1,"","","", null, null)
        return usuario
    }

    //**************PARTE DE VIDEOJUEGO**************//

    /**
     * Este metodo reune y devuelve una lista con todos los usuarios
     *
     * @return list
     */
    @SuppressLint("Range")
    fun getAllGames(): List<Videojuego> {
        val columns = arrayOf(VIDEOJUEGO_ID, VIDEOJUEGO_NOMBRE, VIDEOJUEGO_SINOPSIS, VIDEOJUEGO_IMG)
        val sortOrder = "$VIDEOJUEGO_NOMBRE ASC"
        val gameList = ArrayList<Videojuego>()
        val db = this.readableDatabase
        val cursor = db.query(VIDEOJUEGO, //Table to query
            columns,            //columns to return
            null,     //columns for the WHERE clause
            null,  //The values for the WHERE clause
            null,      //group the rows
            null,       //filter by row groups
            sortOrder)         //The sort order
        if (cursor.moveToFirst()) {
            do {
                val videojuego = Videojuego(id = cursor.getString(cursor.getColumnIndex(VIDEOJUEGO_ID)).toInt(),
                    nombre = cursor.getString(cursor.getColumnIndex(VIDEOJUEGO_NOMBRE)),
                    sinopsis = cursor.getString(cursor.getColumnIndex(VIDEOJUEGO_SINOPSIS)),
                    img = converters.toBitmap(cursor.getBlob(cursor.getColumnIndex(VIDEOJUEGO_IMG))))
                gameList.add(videojuego)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return gameList
    }
    /**
     * Este metodo obtiene todos los juegos en funcion de un nombre
     *
     * @return list
     */
    @SuppressLint("Range")
    fun getGameByName(nombre: String): List<Videojuego>? {
        val columns = arrayOf(VIDEOJUEGO_ID, VIDEOJUEGO_NOMBRE, VIDEOJUEGO_SINOPSIS, VIDEOJUEGO_IMG)
        val sortOrder = "$VIDEOJUEGO_NOMBRE ASC"
        val gameList = ArrayList<Videojuego>()
        val db = this.readableDatabase
        /**
         * La SQL query equivalente a este metodo es
         * SELECT * FROM videojuego WHERE nombre = 'ejemplo';
         */
        val cursor = db.query(
            VIDEOJUEGO, //Table to query
            columns,        //columns to return
            "videojuego.nombre LIKE '%" + nombre + "%'",      //columns for the WHERE clause
            null,  //The values for the WHERE clause
            null,  //group the rows
            null,   //filter by row groups
            sortOrder)  //The sort order
        if (cursor.moveToFirst()) {
            do {
                val videojuego = Videojuego(id = cursor.getString(cursor.getColumnIndex(VIDEOJUEGO_ID)).toInt(),
                    nombre = cursor.getString(cursor.getColumnIndex(VIDEOJUEGO_NOMBRE)),
                    sinopsis = cursor.getString(cursor.getColumnIndex(VIDEOJUEGO_SINOPSIS)),
                    img = converters.toBitmap(cursor.getBlob(cursor.getColumnIndex(VIDEOJUEGO_IMG))))
                gameList.add(videojuego)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return gameList
    }
    /**
     * Este método agrega videojuegos
     *
     * @param videojuego
     */
    fun addGame(videojuego: Videojuego) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(VIDEOJUEGO_NOMBRE, videojuego.nombre)
        values.put(VIDEOJUEGO_SINOPSIS, videojuego.sinopsis)
        values.put(VIDEOJUEGO_IMG, converters.fromBitmap(videojuego.img))
        // Se inserta la fila
        db.insert(VIDEOJUEGO, null, values)
        db.close()
    }
    /**
     * Este método actualiza videojuegos
     *
     * @param videojuego
     */
    fun updateGame(videojuego: Videojuego) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(VIDEOJUEGO_NOMBRE, videojuego.nombre)
        values.put(VIDEOJUEGO_SINOPSIS, videojuego.sinopsis)
        values.put(VIDEOJUEGO_IMG, converters.fromBitmap(videojuego.img))
        // updating row
        db.update(VIDEOJUEGO, values, "$VIDEOJUEGO_ID = ?",
            arrayOf(videojuego.id.toString()))
        db.close()
    }
    /**
     * Este método elimina videojuegos
     *
     * @param videojuego
     */
    fun deleteGame(videojuego: Videojuego) {
        val db = this.writableDatabase
        db.delete(VIDEOJUEGO, "$VIDEOJUEGO_ID = ?",
            arrayOf(videojuego.id.toString()))
        db.close()
    }
    /**
     * Este método verifica si el videojuego existe o no
     *
     * @param nombre
     * @return true/false
     */
    fun checkGame(nombre: String): Boolean {
        // array of columns to fetch
        val columns = arrayOf(VIDEOJUEGO_ID)
        val db = this.readableDatabase
        // selection criteria
        val selection = "$VIDEOJUEGO_NOMBRE = ?"
        // selection argument
        val selectionArgs = arrayOf(nombre)
        // query user table with condition
        /**
         * La SQL query equivalente a este metodo es
         * SELECT id FROM videojuego WHERE nombre = 'ejemplo';
         */
        val cursor = db.query(
            VIDEOJUEGO, //Table to query
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

    //Aqui empieza la tabla de union entre usuarios y videojuegos

    /**
     * Este método agrega el estado de un videojuego a un usuario
     *
     * @param videojuego
     */
    fun addUserGame(userGame: UserGame) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(USERGAME_USUARIO, userGame.idUsuario)
        values.put(USERGAME_VIDEOJUEGO, userGame.idVideojuego)
        values.put(USERGAME_ESTADO, userGame.estado)
        // Se inserta la fila
        db.insert(USERGAME, null, values)
        db.close()
    }
    /**
     * Este método actualiza el estado de un videojuego a un usuario
     *
     * @param videojuego
     */
    fun updateUserGame(userGame: UserGame) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(USERGAME_USUARIO, userGame.idUsuario)
        values.put(USERGAME_VIDEOJUEGO, userGame.idVideojuego)
        values.put(USERGAME_ESTADO, userGame.estado)
        // Se actualiza la fila
        db.update(USERGAME, values, "$USERGAME_USUARIO = ? AND $USERGAME_VIDEOJUEGO = ?",
            arrayOf(userGame.idUsuario.toString(), userGame.idVideojuego.toString()))
        db.close()
    }
    /**
     * Este método elimina el estado de un videojuego a un usuario
     *
     * @param videojuego
     */
    fun deleteUserGame(userGame: UserGame) {
        val db = this.writableDatabase
        db.delete(USERGAME, "$USERGAME_USUARIO = ? AND $USERGAME_VIDEOJUEGO = ?",
            arrayOf(userGame.idUsuario.toString(), userGame.idVideojuego.toString()))
        db.close()
    }
    /**
     * Este metodo reune y devuelve una lista con todos los videojuegos relacionados con los usuarios
     *
     * @return list
     */
    @SuppressLint("Range")
    fun getAllUserGame(): List<UserGame> {
        // array of columns to fetch
        val columns = arrayOf(USERGAME_USUARIO, USERGAME_VIDEOJUEGO, USERGAME_ESTADO)
        val userGameList = ArrayList<UserGame>()
        val db = this.readableDatabase
        // query the user table
        val cursor = db.query(VIDEOJUEGO, //Table to query
            columns,            //columns to return
            null,     //columns for the WHERE clause
            null,  //The values for the WHERE clause
            null,      //group the rows
            null,       //filter by row groups
            null)         //The sort order
        if (cursor.moveToFirst()) {
            do {
                val userGame = UserGame(idUsuario = cursor.getString(cursor.getColumnIndex(USERGAME_USUARIO)).toInt(),
                    idVideojuego = cursor.getString(cursor.getColumnIndex(USERGAME_VIDEOJUEGO)).toInt(),
                    estado = cursor.getString(cursor.getColumnIndex(USERGAME_ESTADO)))
                userGameList.add(userGame)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return userGameList
    }

    //************CONVERTIR URL EN BITMAP****************//
    private suspend fun getBitmap(url : String): Bitmap? {
        try {
            val url = URL(url)
            val connection: HttpURLConnection =
                withContext(Dispatchers.IO) {
                    url.openConnection()
                } as HttpURLConnection
            connection.doInput = true
            withContext(Dispatchers.IO) {
                connection.connect()
            }
            val input: InputStream = connection.inputStream
            val myBitmap : Bitmap = BitmapFactory.decodeStream(input)
            return myBitmap
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}
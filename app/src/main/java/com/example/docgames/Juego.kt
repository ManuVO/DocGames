package com.example.docgames

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import coil.load

class Juego : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_juego)

        //sonido boton pacman
        val mediaPlayer : MediaPlayer = MediaPlayer.create(this, R.raw.pacman)

        val dataBaseHelper = DataBaseHelper(applicationContext)
        val intent : Intent = intent
        val email = intent.getStringExtra("email")
        val nombreJuego = intent.getStringExtra("Nombrejuego")
        val juegoShow : Videojuego = dataBaseHelper.getJuego(nombreJuego.toString())

        val nombreJuegoShow : TextView = findViewById(R.id.txtNombreJuego)
        nombreJuegoShow.text = juegoShow.nombre
        val imageView : ImageView = findViewById(R.id.ivImagenJuego)
        imageView.load(juegoShow.img)

        val textViewSinopsis : TextView = findViewById(R.id.txtSinopsisJuego)
        textViewSinopsis.text = juegoShow.sinopsis

        val botonAñadirColeccion : Button = findViewById(R.id.btnAñadirAColeccion)
        botonAñadirColeccion.setOnClickListener {
            if(dataBaseHelper.checkUserGame(juegoShow.id)){
                val userLogged : Usuario = getUsrLogged()
                val userGame : UserGame = UserGame(userLogged.id, juegoShow.id)
                dataBaseHelper.addUserGame(userGame)
                Toast.makeText(this@Juego, "Juego añadido correctamente al usuario.", Toast.LENGTH_LONG)
                    .show()
            }else
                Toast.makeText(this@Juego, "Ya has añadido el juego previamente a tu coleccion.", Toast.LENGTH_LONG)
                    .show()
        }

        val logoMenu : TextView = findViewById(R.id.logo_menu)
        logoMenu.setOnClickListener {
            showMenu(logoMenu)
        }
    }
    public fun showMenu(v: View){

        //sonido boton pacman
        val mediaPlayer : MediaPlayer = MediaPlayer.create(this, R.raw.pacman)

        if(getSonido()){
            mediaPlayer.start()
        }

        val menuPopup = PopupMenu(this,v)
        menuPopup.inflate(R.menu.menu_home)  // importo el menu : menu_home.xml
        menuPopup.setOnMenuItemClickListener {  // establece funcionalidad a los botones del menu
            when(it.itemId){
                R.id.id_menu_mhome -> {
                    if(getSonido()){
                        mediaPlayer.start()
                    }
                    val intent = Intent(this, Home::class.java)
                    startActivity(intent)
                    true
                }
                R.id.id_menu_perfil -> {
                    if(getSonido()){
                        mediaPlayer.start()
                    }
                    val intent = Intent(this, Perfil::class.java)
                    startActivity(intent)
                    true
                }
                R.id.id_menu_juegos -> {
                    if(getSonido()){
                        mediaPlayer.start()
                    }
                    val intent = Intent(this, MisJuegos::class.java)
                    startActivity(intent)
                    true
                }
                R.id.id_menu_ajustes -> {
                    if(getSonido()){
                        mediaPlayer.start()
                    }
                    val intent = Intent(this, Ajustes::class.java)
                    startActivity(intent)
                    true

                }else -> true
            }
        }
        menuPopup.show()
    }
}
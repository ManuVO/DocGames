package com.example.docgames

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore.Video
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MisJuegos : AppCompatActivity(),RecyclerViewInterface {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_juegos)
        val dataBaseHelper = DataBaseHelper(applicationContext)

        val mediaPlayer : MediaPlayer = MediaPlayer.create(this, R.raw.pacman)

        val intent : Intent = intent
        val email = intent.getStringExtra("email")
        println(email)

        val logoMenu : TextView = findViewById(R.id.logo_menu)
        logoMenu.setOnClickListener {
            showMenu(logoMenu)
        }

        val rvListaJuegos : RecyclerView = findViewById(R.id.rvListaJuegos)
        val listaJuegosUsuario: List<Videojuego> = dataBaseHelper.getAllGamesByUserId(getUsrLogged().id)
        //val listaJuegosUsuario: List<Videojuego>  = dataBaseHelper.getAllGames()
        val adapter = V_RecyclerViewAdapter(this, listaJuegosUsuario, this)
        rvListaJuegos.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvListaJuegos.adapter = adapter

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

    private fun listaJuegos() : List<Videojuego>{
        val dataBaseHelper = DataBaseHelper(applicationContext)
        val listaJuegosUsuario: List<Videojuego> = dataBaseHelper.getAllGamesByUserId(getUsrLogged().id)
        return listaJuegosUsuario
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this, Juego::class.java)
        intent.putExtra("Nombrejuego", listaJuegos().get(position).nombre)
        startActivity(intent)
    }
}
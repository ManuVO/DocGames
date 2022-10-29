package com.example.docgames

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import coil.load
import android.widget.Toast

class Perfil : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        var usr : Usuario = getUsrLogged()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)
        val nombreUsr : TextView = findViewById(R.id.txtNombreUsuario)
        nombreUsr.text = usr.nombre
        val imgUsr : ImageView = findViewById(R.id.ivImagenUsuario)
        imgUsr.load(usr.img)

        val dataBaseHelper = DataBaseHelper(applicationContext)
        val intent : Intent = intent
        val email = intent.getStringExtra("email")
        println(email)

        val logoMenu : TextView = findViewById(R.id.logo_menu)
        logoMenu.setOnClickListener {
            showMenu(logoMenu)
        }
    }
    public fun showMenu(v: View){
        val menuPopup = PopupMenu(this,v)
        menuPopup.inflate(R.menu.menu_home)  // importo el menu : menu_home.xml
        menuPopup.setOnMenuItemClickListener {  // establece funcionalidad a los botones del menu
            when(it.itemId){
                R.id.id_menu_mhome -> {
                    val intent = Intent(this, Home::class.java)
                    startActivity(intent)
                    true
                }
                R.id.id_menu_perfil -> {
                    val intent = Intent(this, Perfil::class.java)
                    startActivity(intent)
                    true
                }
                R.id.id_menu_juegos -> {
                    val intent = Intent(this, MisJuegos::class.java)
                    startActivity(intent)
                    true
                }
                R.id.id_menu_ajustes -> {
                    val intent = Intent(this, Ajustes::class.java)
                    startActivity(intent)
                    true

                }else -> true
            }
        }
        menuPopup.show()

    }
}
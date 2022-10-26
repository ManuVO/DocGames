package com.example.docgames

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

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
                R.id.id_menu_perfil -> {
                    val intentPerfil = Intent(this,Perfil::class.java)
                    startActivity(intentPerfil)
                    true
                }
                R.id.id_menu_juegos -> {
                    Toast.makeText(this, "Menu Popup", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.id_menu_ajustes -> {
                    Toast.makeText(this, "Menu Popup", Toast.LENGTH_SHORT).show()
                    true
                }else -> true
            }
        }
    }
}
package com.example.docgames

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast

private var sonidoActivado : Boolean = true

class Ajustes : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajustes)

        val intent : Intent = intent
        val email = intent.getStringExtra("email")
        println(email)

        val logoMenu : TextView = findViewById(R.id.logo_menu)
        logoMenu.setOnClickListener {
            showMenu(logoMenu)
        }

        val switchSonidoNotif : Switch = findViewById(R.id.switchSonidoNotif)  //switch
        switchSonidoNotif.setOnCheckedChangeListener { compoundButton, b ->
            if(switchSonidoNotif.isChecked)
            {
                sonidoActivado = true       //sonido activado
            } else{
                sonidoActivado = false      //sonido desactivado
            }
        }
    }

    public fun showMenu(v: View) {
        val menuPopup = PopupMenu(this, v)
        menuPopup.inflate(R.menu.menu_home)  // importo el menu : menu_home.xml
        menuPopup.setOnMenuItemClickListener {  // establece funcionalidad a los botones del menu
            when (it.itemId) {
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

                }
                else -> true
            }
        }
        menuPopup.show()
    }

}
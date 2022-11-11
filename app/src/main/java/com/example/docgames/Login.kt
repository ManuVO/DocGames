package com.example.docgames

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

private var usrLogged : Usuario = Usuario(1,"","","", null, null)

fun getUsrLogged():Usuario{
    return usrLogged
}
fun setUsrLogged(usr:Usuario){
    usrLogged = usr
}

private var sonidoActivado : Boolean = true  //sonido

fun getSonido() : Boolean{
    return sonidoActivado
}
fun setSonido(Sonido : Boolean){
    sonidoActivado = Sonido
}

class Login : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializamos la variable de la BBDD
        val dataBaseHelper = DataBaseHelper(applicationContext)

        //sonido boton pacman
        val mediaPlayer : MediaPlayer = MediaPlayer.create(this, R.raw.pacman)

        //BOTON DE INICIO DE SESIÓN
        val btnIniciarSesion : Button = findViewById(R.id.btnLogin)
        btnIniciarSesion.setOnClickListener {
            if(getSonido()){
                mediaPlayer.start()
            }
            val email : EditText = findViewById(R.id.ptEmail)
            val pass : EditText = findViewById(R.id.ptPass)
            //USUARIO por defecto: admin CONTRASEÑA por defecto: admin
            if(dataBaseHelper.checkUser(email.text.toString(), pass.text.toString())) {
                //if(editTextEmail.text.toString().equals("admin") && editTextPass.text.toString().equals("admin")) {
                val intent = Intent(this, Home::class.java)
                intent.putExtra("email", email.text.toString())
                intent.putExtra("pass", pass.text.toString())
                usrLogged = dataBaseHelper.getUser(email.text.toString())
                startActivity(intent)
            }
            else{
                //ENTRA A HOME SIEMPRE (CAMBIAR MAS ADELANTE)
                /*val intent = Intent(this, Home::class.java)
                intent.putExtra("email", editTextEmail.text.toString())
                intent.putExtra("pass", editTextEmail.text.toString())
                startActivity(intent)*/
                Toast.makeText(this, "No se han introducido los datos correctamente", Toast.LENGTH_LONG).show()
            }
        }

        val tvRegistro : TextView = findViewById(R.id.tvIrRegistrarse)
        tvRegistro.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }
}
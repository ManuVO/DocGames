package com.example.docgames

import android.R.attr.src
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.regex.*


class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //sonido boton pacman
        val mediaPlayer : MediaPlayer = MediaPlayer.create(this, R.raw.pacman)

        // Inicializamos la variable de la BBDD
        val dataBaseHelper = DataBaseHelper(applicationContext)

        val btnRegistrarse : Button = findViewById<Button>(R.id.btnRegistrarse)
        val nombreUsuario : EditText = findViewById(R.id.ptUsuario)
        val email : EditText = findViewById(R.id.ptEmail)
        val pass : EditText = findViewById(R.id.ptPass)
        val confirmPass : EditText = findViewById(R.id.ptConfirPass)

        //Presionas el boton registrarse
        btnRegistrarse.setOnClickListener {

            //Minimo 8 caracteres, una letra minuscula y otra mayuscula
            val regexEmail = "^[^\\s@]+@([^\\s@.,]+\\.)+[^\\s@.,]{2,}$".toRegex()
            val regexPass = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$".toRegex()


            //Verifica el email
            if(!regexEmail.matches(email.text.toString())){
                println("---Email: " + email.text.toString())
                Toast.makeText(this, "El email tiene que tener un formato ejemplo@ejemplo.com", Toast.LENGTH_LONG).show()
            }
            //Verifica la contraseña
            else if(!regexPass.matches(pass.text.toString())){
                println("---Pass: " + pass.text.toString())
                Toast.makeText(this, "La contraseña debe tener un mínimo de 8 caracteres, una letra minuscula, una mayuscula y un dígito", Toast.LENGTH_LONG).show()
            }
            //Si ya existe el email
            else if(dataBaseHelper.checkUser(email.text.toString())) {
                Toast.makeText(this, "El email introducido ya existe", Toast.LENGTH_LONG).show()
            }
            //Si las contraseñas no coinciden
            else if(!pass.text.toString().equals(confirmPass.text.toString())){
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show()
            }
            //Si las contraseñas coinciden
            else{
                GlobalScope.launch {
                    val usuario : Usuario = Usuario(-1,nombreUsuario.text.toString(),email.text.toString(),confirmPass.text.toString(), getBitmap(), "")
                    dataBaseHelper.addUser(usuario)
                }
                Toast.makeText(this, "Te has registado correctamente", Toast.LENGTH_LONG).show()
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            }
        }

        val tvLogin : TextView = findViewById(R.id.tvIrLogin)
        tvLogin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }

    private suspend fun getBitmap(): Bitmap {
        val loader = ImageLoader(this)
        val request = ImageRequest.Builder(this)
            .data("https://avatars.githubusercontent.com/u/14994036?s=400&u=2832879700f03d4b37ae1c09645352a352b9d2d0&v=4.jpg")
            .allowHardware(false) // Disable hardware bitmaps.
            .build()

        val result = (loader.execute(request) as SuccessResult).drawable
        //val result = loader.execute(request).drawable
        val bitmap = (result as BitmapDrawable).bitmap
        return bitmap
    }

}
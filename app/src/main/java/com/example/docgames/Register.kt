package com.example.docgames

import android.R.attr.src
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
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
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inicializamos la variable de la BBDD
        val dataBaseHelper = DataBaseHelper(applicationContext)

        val btnRegistrarse : Button = findViewById<Button>(R.id.btnRegistrarse)
        val nombreUsuario : EditText = findViewById(R.id.ptUsuario)
        val email : EditText = findViewById(R.id.ptEmail)
        val pass : EditText = findViewById(R.id.ptPass)
        val confirmPass : EditText = findViewById(R.id.ptConfirPass)
        btnRegistrarse.setOnClickListener {
            if(dataBaseHelper.checkUser(email.text.toString())) {
                Toast.makeText(this, "El email introducido ya existe", Toast.LENGTH_LONG).show()
            }
            else if(pass.text.toString().equals(confirmPass.text.toString())){
                GlobalScope.launch {
                    val usuario : Usuario = Usuario(-1,nombreUsuario.text.toString(),email.text.toString(),confirmPass.text.toString(), getBitmap(), "Introduce los datos: ")
                    dataBaseHelper.addUser(usuario)
                }
                Toast.makeText(this, "Te has registado correctamente", Toast.LENGTH_LONG).show()
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show()
            }

        }

        val tvLogin : TextView = findViewById(R.id.tvIrLogin)
        tvLogin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }

    private suspend fun getBitmap(): Bitmap {
        /*
        val loading = ImageLoader(this)
        val request = ImageRequest.Builder(this)
            .data("https://avatars.githubusercontent.com/u/14994036?s=400&u=2832879700f03d4b37ae1c09645352a352b9d2d0&v=4")
            .build()
        val result = (loading.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
        */

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
package com.example.docgames

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


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
                val usuario : Usuario = Usuario(-1,nombreUsuario.text.toString(),email.text.toString(),confirmPass.text.toString())
                dataBaseHelper.addUser(usuario)
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
}
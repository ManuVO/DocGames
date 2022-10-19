package com.example.docgames

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class Login : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializamos la variable de la BBDD
        val dataBaseHelper = DataBaseHelper(applicationContext)
        // Obtienemos la BBDD en modo lectura
        val db_reader = dataBaseHelper.readableDatabase
        // Obtienemos la BBDD en modo escritura
        val db_writer = dataBaseHelper.writableDatabase


        //BOTON DE INICIO DE SESIÓN
        val btnIniciarSesion : Button = findViewById(R.id.btnIniciarSesion)
        btnIniciarSesion.setOnClickListener {
            val editTextEmail : EditText = findViewById(R.id.ptEmail)
            val editTextPass : EditText = findViewById(R.id.ptPass)
            if(editTextEmail.text.toString().equals("Manuel") && editTextPass.text.toString().equals("1234")) {
                val intent = Intent(this, Home::class.java)
                intent.putExtra("email", editTextEmail.text.toString())
                intent.putExtra("pass", editTextEmail.text.toString())
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "No se han introducido los datos correctamente", Toast.LENGTH_LONG).show()
            }
        }
    }
}
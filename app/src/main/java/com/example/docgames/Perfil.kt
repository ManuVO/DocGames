package com.example.docgames

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.media.Image
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings.Global
import android.view.View
import android.widget.*
import coil.load
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class Perfil : AppCompatActivity() {

    private lateinit var imageView: ImageView
    companion object{
        val IMAGE_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        var usr : Usuario = getUsrLogged()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        //sonido boton pacman
        val mediaPlayer : MediaPlayer = MediaPlayer.create(this, R.raw.pacman)

        val nombreUsr : TextView = findViewById(R.id.txtNombreUsuario)
        nombreUsr.text = usr.nombre
        imageView = findViewById(R.id.ivImagenUsuario)
        imageView.load(usr.img)

        val editText : EditText = findViewById(R.id.txtBibliografiaUsuario)
        editText.setText(usr.biografia)
        imageView.setOnClickListener{
            GlobalScope.launch {
               async{ pickImageGallery() }
            }
        }

        val dataBaseHelper = DataBaseHelper(applicationContext)
        val intent : Intent = intent
        val email = intent.getStringExtra("email")
        println(email)

        val logoMenu : TextView = findViewById(R.id.logo_menu)
        logoMenu.setOnClickListener {
            showMenu(logoMenu)
        }
        val btnSave : Button = findViewById(R.id.btnGuardarPerfil)
        btnSave.setOnClickListener{
            val drawable : BitmapDrawable = imageView.drawable as BitmapDrawable
            usr.img = drawable.bitmap
            var getText : String = editText.text.toString()
            usr.biografia = getText
            dataBaseHelper.updateUser(usr)
            setUsrLogged(usr)
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

    suspend private fun pickImageGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK){

            imageView.setImageBitmap(MediaStore.Images.Media.getBitmap(this.contentResolver, data?.data))
        }
    }

}
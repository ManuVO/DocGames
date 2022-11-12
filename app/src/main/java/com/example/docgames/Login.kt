package com.example.docgames

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

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

        insertVideojuegos(dataBaseHelper)

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
                Toast.makeText(this, "No se han introducido los datos correctamente", Toast.LENGTH_LONG).show()
            }
        }

        val tvRegistro : TextView = findViewById(R.id.tvIrRegistrarse)
        tvRegistro.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }

    //************CONVERTIR URL EN BITMAP****************//
    private suspend fun getBitmap(url : String): Bitmap {
        val loader = ImageLoader(this)
        val request = ImageRequest.Builder(this)
            .data(url)
            .allowHardware(false) // Disable hardware bitmaps.
            .build()

        val result = (loader.execute(request) as SuccessResult).drawable
        val bitmap = (result as BitmapDrawable).bitmap
        return bitmap
    }

    private fun insertVideojuegos(dataBaseHelper : DataBaseHelper){

        //Insertamos juegos en la Base de Datos

        //***********INSERT VIDEOJUEGOS***************//
        GlobalScope.launch(Dispatchers.Main) {
            var text : String
            var image : Bitmap
            var videojuego : Videojuego

            text = "Prepárate para una emocionante aventura con Pokémon SoulSilver para la consola Nintendo DS. " +
                    "Ponte en la piel de un entrenador o entrenadora Pokémon y lucha en la región de Johto con tus " +
                    "Pokémon para conseguir la victoria."
            image = getBitmap("https://m.media-amazon.com/images/I/517ZggTk5PL._AC_.jpg")
            videojuego = Videojuego(-1, "Pokemon Soulsilver", text,  image)
            if(!dataBaseHelper.checkGame(videojuego.nombre)){
                dataBaseHelper.addGame(videojuego)
            }

            text = "Una de las sagas más populares de los últimos años llega a la 3DS dando un giro total en experiencias " +
                    "y jugabilidad. En esta ocasión el jugador se convertirá en el alcalde y tendrá en sus manos " +
                    "un gran número de diseños y posibilidades de personalización."
            image = getBitmap("https://m.media-amazon.com/images/I/61bdAphKt7L._AC_.jpg")
            videojuego = Videojuego(-1, "Animal Crossing New Leaf", text, image)
            if(!dataBaseHelper.checkGame(videojuego.nombre)){
                dataBaseHelper.addGame(videojuego)
            }

            text = "¡Abajo los cables...es la hora de realizar una carrera en toda libertad! MarioKart, la serie de gran " +
                    "éxito permite ahora a los jugadores competir demanera simultánea en los circuitos." +
                    "que se añaden a este título de éxito."
            image = getBitmap("https://m.media-amazon.com/images/I/51CIc6ggYtL._AC_.jpg")
            videojuego = Videojuego(-1, "Mario Kart DS", text, image)
            if(!dataBaseHelper.checkGame(videojuego.nombre)) {
                dataBaseHelper.addGame(videojuego)
            }

            text = "Los controles táctiles y los puzles sorprendentes toman la saga Zelda. En The Legend of Zelda: Phantom " +
                    "Hourglass el jugador encontrará a un LInk perdido surcando los mares."
            image  = getBitmap("https://m.media-amazon.com/images/I/617qj3dhb-L._AC_.jpg")
            videojuego = Videojuego(-1, "The legend of Zelda: Phantom Hourglass", text,  image)
            if(!dataBaseHelper.checkGame(videojuego.nombre)) {
                dataBaseHelper.addGame(videojuego)
            }

            text = "Un comienzo nuevo. Su venganza contra los dioses del Olimpo ha quedado atrás y Kratos ahora " +
                    "vive como un hombre en las tierras de los dioses y monstruos nórdicos."
            image = getBitmap("https://m.media-amazon.com/images/I/714+Tko1dWL._AC_SL1500_.jpg")
            videojuego = Videojuego(-1, "God of War", text, image)
            if(!dataBaseHelper.checkGame(videojuego.nombre)) {
                dataBaseHelper.addGame(videojuego)
            }

            text = "¡Bienvenidos a un nuevo mundo! En Monster Hunter: World, la última entrega de la serie, podrás " +
                    "disfrutar de la mejor experiencia de juego, usando todos los recursos a tu alcance para " +
                    "acechar monstruos en un nuevo mundo rebosante de emociones y sorpresas."
            image = getBitmap("https://m.media-amazon.com/images/I/71x4zX3dOOL._AC_SL1000_.jpg")
            videojuego = Videojuego(-1, "Monster Hunter World", text, image)
            if(!dataBaseHelper.checkGame(videojuego.nombre)) {
                dataBaseHelper.addGame(videojuego)
            }

            text = "Te espera una aventura a través de la galaxia en Star Wars Jedi: Fallen Order, un título de acción " +
                    "en tercera persona de Respawn Entertainment."
            image = getBitmap("https://m.media-amazon.com/images/I/81Tl8Y78CuL._AC_SL1500_.jpg")
            videojuego = Videojuego(-1, "Star Wars: Jedi Fallen Order", text, image)
            if(!dataBaseHelper.checkGame(videojuego.nombre)) {
                dataBaseHelper.addGame(videojuego)
            }

            text = "La Orden Dorada está rota. Álzate, Sinluz, y que la gracia te guíe para abrazar el poder del " +
                    "Círculo de Elden y encumbrarte como señor del Círculo en las Tierras Intermedias."
            image = getBitmap("https://m.media-amazon.com/images/I/81cY7DwfO+S._AC_SL1500_.jpg")
            videojuego = Videojuego(-1, "Elden Ring", text, image)
            if(!dataBaseHelper.checkGame(videojuego.nombre)) {
                dataBaseHelper.addGame(videojuego)
            }
        }
    }
}
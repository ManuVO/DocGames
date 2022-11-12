package com.example.docgames

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.security.AccessController.getContext

class Home : AppCompatActivity(),RecyclerViewInterface {

    // creating variables for listview
    lateinit var juegosListView : ListView

    // creating array adapter for listview
    lateinit var listAdapter: ArrayAdapter<String>


    // creating array list for listview
    lateinit var juegosList: ArrayList<String>;
    // creating variable for searchview
    lateinit var searchView: SearchView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val dataBaseHelper = DataBaseHelper(applicationContext)
        //sonido boton pacman
        val mediaPlayer: MediaPlayer = MediaPlayer.create(this, R.raw.pacman)
        var cerrado: Boolean = true
        val intent: Intent = intent
        val email = intent.getStringExtra("email")
        println(email)

        //video de novedades
        val videoView: VideoView = findViewById(R.id.videoView)
        videoView.setVideoURI(Uri.parse("android.resource://" + packageName + "/" + R.raw.novedadpokemon))
        videoView.start()

        val logoMenu: TextView = findViewById(R.id.logo_menu)
        logoMenu.setOnClickListener {
            showMenu(logoMenu)
        }
        val relativeLayout: RelativeLayout = findViewById(R.id.cl_buscarjuego)
        // initializing variables of list view with their ids.
        juegosListView = findViewById(R.id.idListaJuegos)
        searchView = findViewById(R.id.id_buscarjuegos)
        searchView.setOnQueryTextFocusChangeListener(object : View.OnFocusChangeListener{
            override fun onFocusChange(p0: View?, p1: Boolean) {
                if (cerrado) {
                    val scale: Float = applicationContext.resources.displayMetrics.density;
                    val pixels = (150 * scale + 0.5f).toInt()
                    relativeLayout.layoutParams.height = pixels
                    cerrado = false
                } else {
                    val scale: Float = applicationContext.resources.displayMetrics.density;
                    val pixels = (40 * scale + 0.5f).toInt()
                    relativeLayout.layoutParams.height = pixels
                    cerrado = true
                }
            }
        })
        // Inicializamos la lista y le cargamos los nombres de todos los
        // juegos que hay en la base de datos en orden alfabetico
        juegosList = ArrayList()
        val listaTemporal: List<String>  = dataBaseHelper.getAllGamesName()

            for(juego in listaTemporal){
                juegosList.add(juego)
            }

        // initializing list adapter and setting layout
        // for each list view item and adding array list to it.
        listAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            juegosList
        )

        // on below line setting list
        // adapter to our list view.
        juegosListView.adapter = listAdapter

        // on below line we are adding on query
        // listener for our search view.
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // on below line we are checking
                // if query exist or not.
                if (juegosList.contains(query)) {
                    // if query exist within list we
                    // are filtering our list adapter.
                    listAdapter.filter.filter(query)
                } else {
                    // if query is not present we are displaying
                    // a toast message as no  data found..
                    Toast.makeText(this@Home, "No hay ningún juego con ese nombre.", Toast.LENGTH_LONG)
                        .show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //Si cambia el texto de la busqueda
                //filtramos de nuevo para ver si hay alguna coincidencia
                listAdapter.filter.filter(newText)
                return false
            }
        })
        juegosListView.setOnItemClickListener { parent, view, position, id ->
            val element = listAdapter.getItem(position) // The item that was clicked
            val intent = Intent(this, Juego::class.java)
            intent.putExtra("Nombrejuego", element)
            startActivity(intent)
        }

        val rvListaJuegos : RecyclerView = findViewById(R.id.rvJuegosHome)
        val listaJuegosUsuario: List<Videojuego>  = dataBaseHelper.getAllGames()
        val adapter = V_RecyclerViewAdapter(this, listaJuegosUsuario, this)
        rvListaJuegos.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvListaJuegos.adapter = adapter

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

    private fun listaJuegos() : List<Videojuego>{
        val dataBaseHelper = DataBaseHelper(applicationContext)
        val listaJuegosUsuario: List<Videojuego>  = dataBaseHelper.getAllGames()
        return listaJuegosUsuario
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this, Juego::class.java)
        intent.putExtra("Nombrejuego", listaJuegos().get(position).nombre)
        startActivity(intent)
    }

}
package com.example.docgames

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load

class V_RecyclerViewAdapter(private val context: Context, VideojuegoList: List<Videojuego>, recyclerViewInterface : RecyclerViewInterface) :
    RecyclerView.Adapter<V_RecyclerViewAdapter.MyViewHolder>() {
    private val recyclerViewInterface : RecyclerViewInterface
    private val VideojuegoList: List<Videojuego>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): V_RecyclerViewAdapter.MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_row, parent, false)
        return MyViewHolder(view, recyclerViewInterface)
    }

    override fun onBindViewHolder(holder: V_RecyclerViewAdapter.MyViewHolder, position: Int) {
        val modelo: Videojuego = VideojuegoList[position]
        holder.imagen.load(modelo.img)
        holder.nombre.setText(modelo.nombre)
    }

    override fun getItemCount(): Int {
        return VideojuegoList.size
    }

    class MyViewHolder(itemView: View, recyclerViewInterface: RecyclerViewInterface) : RecyclerView.ViewHolder(itemView) {
        val imagen : ImageView
        val nombre: TextView
        init {
            imagen = itemView.findViewById(R.id.ivPortadaJuego)
            nombre = itemView.findViewById(R.id.tvNombreJuego)

            itemView.setOnClickListener {
                if(recyclerViewInterface != null){
                    val pos : Int = adapterPosition

                    if(pos != RecyclerView.NO_POSITION){
                        recyclerViewInterface.onItemClick(pos)
                    }
                }
            }

        }
    }

    init {
        this.VideojuegoList = VideojuegoList
        this.recyclerViewInterface = recyclerViewInterface
    }

}
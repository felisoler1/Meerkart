package com.example.meerkart40.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.meerkart40.Productos
import com.example.meerkart40.R

class ProductViewHolder(view:View):ViewHolder(view){

    val nombre=view.findViewById<TextView>(R.id.nombreProducto)
    val cantidad=view.findViewById<TextView>(R.id.cantidadProducto)
    val precio=view.findViewById<TextView>(R.id.precioProducto)

    fun render(productos: Productos) {
        nombre.text = productos.nombre
        cantidad.text = productos.cantidad.toString()
        precio.text = productos.precio.toString()

    }
}
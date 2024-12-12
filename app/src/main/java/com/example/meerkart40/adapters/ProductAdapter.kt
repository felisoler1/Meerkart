package com.example.meerkart40

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.meerkart40.Productos
import com.example.meerkart40.R
import com.example.meerkart40.adapters.ProductViewHolder

class ProductAdapter(private val productList:MutableList<Productos>): RecyclerView.Adapter<ProductViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ProductViewHolder(layoutInflater.inflate(R.layout.item_producto,parent,false))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        val item = productList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = productList.size

}
package com.example.meerkart40

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ListaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lista)
        initRecyclerView()
//       ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//           val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//           v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//          insets
//      }
   }

    override fun onResume() {
        super.onResume()
        // Actualiza la lista cuando la actividad se reanuda
        val adapter = ProductAdapter(ProductProvider.productList)
        adapter.notifyDataSetChanged()
    }

    private fun initRecyclerView(){
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerProductos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ProductAdapter(ProductProvider.productList)

    }

}
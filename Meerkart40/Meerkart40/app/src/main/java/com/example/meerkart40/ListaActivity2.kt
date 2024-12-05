package com.example.meerkart40

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ListaActivity2 : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)

        val productListContainer: LinearLayout = findViewById(R.id.productListContainer)
        val subtotalText: TextView = findViewById(R.id.subtotalText)

        // Función para agregar productos dinámicamente
        fun addProduct(name: String, quantity: Int, price: Double) {
            // Inflar la vista del producto desde el archivo XML (product_item.xml)
            val productView = layoutInflater.inflate(R.layout.item_product, productListContainer, false)

            // Configurar los TextViews para mostrar los datos del producto
            val nameText: TextView = productView.findViewById(R.id.productName)
            val quantityText: TextView = productView.findViewById(R.id.productQuantity)
            val priceText: TextView = productView.findViewById(R.id.productPrice)

            nameText.text = name
            quantityText.text = quantity.toString()
            priceText.text = "${price} €"

            // **Configurar accesibilidad para el producto**
            productView.contentDescription = "Producto: $name, Cantidad: $quantity, Precio: ${String.format("%.2f", price)} €"

            // Agregar la vista del producto al contenedor
            productListContainer.addView(productView)

            // Actualizar subtotal
            val currentSubtotal = subtotalText.text.toString().replace("Subtotal: ", "").replace(" €", "").toDouble()
            val newSubtotal = currentSubtotal + (price * quantity)
            subtotalText.text = "Subtotal: ${String.format("%.2f", newSubtotal)} €"
        }

        // Ejemplo de productos añadidos
        addProduct("Legía MM", 1, 1.35)
        addProduct("Cápsulas café", 2, 10.0)
    }
}
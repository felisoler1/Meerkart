package com.example.meerkart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.meerkart.ui.theme.MeerKartTheme
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

val supabase = createSupabaseClient(
    supabaseUrl = "https://zdgpnziviiipmsyvupoh.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InpkZ3Bueml2aWlpcG1zeXZ1cG9oIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzEzNDQzMzcsImV4cCI6MjA0NjkyMDMzN30.z_21oHpHaOBLLR_ndfUzw3lyLIAlD06Wfq1j_-XuXXU"
) {
    install(Auth)
    install(Postgrest)
    //install other modules
}


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MeerKartTheme {
                ObtenerDatosClientes()//Executam la funcio

            }

        }

    }

}

@Serializable
data class cliente(
    val email: String,
    val nomCli: String,
    val apellido: String,
    val password: String,
    val pag1: String,
    val pag2: String,
)

@Composable
fun ObtenerDatosClientes(){

    //codi que funciona, amb taula de prova
    val clientes = remember { mutableStateListOf<cliente>() }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO){
            val results = supabase.from("CLIENTE").select().decodeList<cliente>()
            clientes.addAll(results)
        }
    }
    LazyColumn {
        items(clientes){
            cliente -> ListItem(headlineContent = { Text(text = cliente.password) })
        }
    }
}


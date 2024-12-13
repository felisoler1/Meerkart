package com.example.meerkart40

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.runBlocking
import androidx.recyclerview.widget.RecyclerView

class LectorNFC: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        //val nfcAdapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(this)ç


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var ref: String=""
        var nomProd: String = ""
        var precio: Double
        Log.d("Funciona", "onCreate")
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        
        var nfcId = handleIntent(intent)
        Log.d("nfcID", nfcId.toString())

        runBlocking {
            try {
                ref = ObtenerRef(nfcId)
                nomProd = ObtenerProducto(ref)
                precio = ObtenerPrecio(ref).toDouble()

               // ProductProvider.productList.add(Productos(nomProd, 1, precio.toDouble()))


                Log.d("referencia", "referencia: " + ref)
                Log.d("referencia","nom producte: "+ nomProd)
                Log.d("referencia", "PREU: " +precio)

            } catch (e:Exception){

            }
        }

    }

    fun handleIntent(intent: Intent): String {
        var nfcId: String = ""
        Log.d("Funciona", "handleIntent")
        val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
        val idTag = tag?.id
        Log.d("idTag", "EL IDTAG(HeloNFC); $idTag")
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
            Log.d("Funciona", "primer_if")
            val rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            if (rawMsgs != null) {
                val msgs = arrayOfNulls<NdefMessage>(rawMsgs.size)
                for (i in rawMsgs.indices) {
                    msgs[i] = rawMsgs[i] as NdefMessage
                    Log.d("msgs", msgs[i].toString())
                }
                Log.d("idMsg", readNdefMessage(msgs))
                nfcId = readNdefMessage(msgs)
            }
        }
        return nfcId
    }

    private fun readNdefMessage(msgs: Array<NdefMessage?>): String {

        var nfcId: String = ""
        for (msg in msgs) {
            msg?.let {
                for (record in it.records) {
                    val payload = record.payload
                    val text = String(payload, Charsets.UTF_8)
                    nfcId = text.takeLast(5)
                }
            }
        }
        return nfcId
    }

    private suspend fun ObtenerRef(nfcId: String): String {
        val ref =supabase.from("UNIDAD"). select(columns = Columns.list("referencia")){
            filter {
                eq("idUnidad", nfcId)
            }

        }
        return ref.data.dropLast(2).takeLast(1)
    }

    private suspend fun ObtenerProducto(ref: String): String{
        val nomProd = supabase.from("PRODUCTO").select (columns = Columns.list("nomProd")){
            filter {
                eq ("referencia", ref)
            }
        }
        return nomProd.data.dropLast(3).drop(13)
    }

    private suspend fun ObtenerPrecio(ref: String): String {
        val precio = supabase.from("PRODUCTO").select (columns = Columns.list("precio")){
            filter {
                eq ("referencia", ref)
            }
        }
        return precio.data.dropLast(2).drop(10)
    }

    override fun onNewIntent(intent: Intent) {
        //val nfcAdapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(this)
        super.onNewIntent(intent)
        Log.d("Funciona", "Intent")
        val tag: Tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)!!
        val idTag = tag!!.id
        Log.d("Funciona", "EL IDTAG; $idTag")

        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
            Log.d("Funciona", "ifIntent")
            Log.d("NfcAdapter", "ID: ${NfcAdapter.EXTRA_ID}")
            val rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            if (rawMsgs != null) {
                val msgs = arrayOfNulls<NdefMessage>(rawMsgs.size)
                for (i in rawMsgs.indices) {
                    msgs[i] = rawMsgs[i] as NdefMessage
                }
                // Procesar los mensajes NDEF
                val record = msgs[0]?.records?.get(0)
                val payload = record?.payload?.let { String(it) }
                Log.d("NFC_TAG", "Texto leído: $payload")
            }
        }
    }
}

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

class ListaActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        //val nfcAdapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(this)ç
        Log.d("Funciona", "onCreate")
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        handleIntent(intent)

    }

    private fun handleIntent(intent: Intent) {
        Log.d("Funciona", "handleIntent")
        val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
        val idTag = tag?.id
        Log.d("Funciona", "EL IDTAG(HeloNFC); $idTag")
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
            Log.d("Funciona", "primer_if")
            val rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            if (rawMsgs != null) {
                val msgs = arrayOfNulls<NdefMessage>(rawMsgs.size)
                for (i in rawMsgs.indices) {
                    msgs[i] = rawMsgs[i] as NdefMessage
                }
                readNdefMessage(msgs)
            }
        }
    }

    private fun readNdefMessage(msgs: Array<NdefMessage?>) {
        for (msg in msgs) {
            msg?.let {
                for (record in it.records) {
                    val payload = record.payload
                    val text = String(payload, Charsets.UTF_8)
                    Log.d("Funciona","Hello_NDEF: $text")
                }
            }
        }
    }

//    override fun onResume() {
//        val nfcAdapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(this)
//        super.onResume()
//        Log.d("Funciona", "onResume")
//        val intentFilter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED).apply {
//            addCategory(Intent.CATEGORY_DEFAULT)
//            try {
//                addDataType("text/plain")
//            } catch (e: IntentFilter.MalformedMimeTypeException) {
//                throw RuntimeException("Error al agregar tipo MIME", e)
//            }
//        }
//
//        val intentFiltersArray = arrayOf(intentFilter)
//        val techListsArray = arrayOf(arrayOf<String>(NfcF::class.java.name))
//
//        nfcAdapter?.enableForegroundDispatch(this, PendingIntent.getActivity(
//            this, 0, Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
//            PendingIntent.FLAG_IMMUTABLE), intentFiltersArray, techListsArray)
//    }

//    override fun onPause() {
//        Log.d("Funciona", "onPause")
//        val nfcAdapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(this)
//        super.onPause()
//        nfcAdapter?.disableForegroundDispatch(this)
//    }



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
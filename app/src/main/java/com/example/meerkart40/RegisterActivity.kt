package com.example.meerkart40

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Count
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.w3c.dom.Text

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        send_data_register()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun send_data_register(){
        var nombre_registre: EditText = findViewById(R.id.register_name)
        var apellido_registre: EditText = findViewById(R.id.register_surname)
        var email_registre: EditText = findViewById(R.id.register_email)
        var contra_registre: EditText = findViewById(R.id.register_contra)
        var contra_repe_registre: EditText = findViewById(R.id.register_contra_repe)
        var textViewAlert: TextView = findViewById(R.id.contra_alerta)
        val pago_principal: RadioGroup = findViewById(R.id.pago_principal)
        val g_pay_principal: RadioButton = findViewById(R.id.g_pay_principal)
        val apple_pay_principal: RadioButton = findViewById(R.id.apple_pay_principal)
        val ppal_principal: RadioButton = findViewById(R.id.ppal_principal)
        val pago_secundario: RadioGroup = findViewById(R.id.pago_secundario)
        val g_pay_secundario: RadioButton = findViewById(R.id.g_pay_secundario)
        val apple_pay_secundario: RadioButton = findViewById(R.id.apple_pay_secundario)
        val ppal_secundario: RadioButton = findViewById(R.id.ppal_secundario)

        var pago1: String = ""
        var pago2: String = ""

        pago_principal.setOnCheckedChangeListener {group, checkedId -> when (checkedId) {
            R.id.g_pay_principal -> {pago1 = "Google Pay"}
            R.id.apple_pay_principal -> {pago1 = "Apple Pay"}
            R.id.ppal_principal -> {pago1 = "PayPal"}
        } }

        pago_secundario.setOnCheckedChangeListener {group, checkedId -> when (checkedId) {
            R.id.g_pay_secundario -> {pago2 = "Google Pay"}
            R.id.apple_pay_secundario -> {pago2 = "Apple Pay"}
            R.id.ppal_secundario -> {pago2 = "PayPal"}
        } }


        val botoSendRegister: Button =findViewById<Button>(R.id.register_send_button)
        botoSendRegister.setOnClickListener {


            var nombre = nombre_registre.text.toString()
            var apellido = apellido_registre.text.toString()
            var correo = email_registre.text.toString()
            var contra = contra_registre.text.toString()
            var contrarepe = contra_repe_registre.text.toString()



            runBlocking {

                if (correo != "" && nombre != "" && apellido !="" && contra == contrarepe && emailValid(correo) && validarContra(contra) && pago1 != pago2){
                    try {

                        val nuevoCliente = MainActivity.cliente(
                            email = correo,
                            nomCli = nombre,
                            apellido = apellido,
                            pag1 = pago1,
                            pag2 = pago2,
                        )
                        authNewUser(correo, contra)
                        supabase.from("CLIENTE").insert(nuevoCliente)
                        val go = Intent(this@RegisterActivity, MainActivity::class.java)
                        go.putExtra("ALERT_TITLE", "Registro exitoso")
                        go.putExtra("ALERT_MESSAGE", "Usuario registrado correctamente")
                        go.putExtra("SHOW_ALERT", true)
                        setResult(RESULT_OK, go)
                        startActivity(go)


                    } catch (e:Exception){
                        MainActivity.alerta(this@RegisterActivity, "Error en el correo", "Usuario ya registrado", 1000)
                    }


                } else if (pago1 == pago2){
                    MainActivity.alerta(this@RegisterActivity, "Error en el metodo de pago", "Los dos metodos no pueden ser el mismo", 1000)
                }

            }
        }
    }
    fun emailValid(email:String): Boolean{
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    suspend fun authNewUser(correo: String, contraseña: String){
        supabase.auth.signUpWith(Email) {
            email = correo
            password = contraseña
        }
    }

    fun validarContra(contraseña: String): Boolean{
        val minLon = 8
        val mayus = contraseña.any { it.isUpperCase() }
        val minus = contraseña.any { it.isLowerCase() }
        val numeros = contraseña.any { it.isDigit() }
        val CharEspecial = contraseña.any { !it.isLetterOrDigit() }
        val LongitudMinima = contraseña.length >= minLon

        return mayus && minus && numeros && CharEspecial && LongitudMinima
    }

}
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

        contra_registre.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()
                if (!validarContra(password)){
                    textViewAlert.visibility = TextView.VISIBLE
                    textViewAlert.text = "La contraseña debe contener al menos: \n"+
                            "- 8 caracteres\n"+
                            "- Una letra minúscula \n" +
                            "- Una letra mayúscula \n" +
                            "- Un número\n" +
                            "- Un carácter especial"
                } else{
                    textViewAlert.visibility = TextView.GONE
                }
            }
        })
        //runBlocking {
        //    try{
        //        authNewUser("clarascc98@gmail.com", "holaHola1.")

        //    } catch (e: Exception){
        //        MainActivity.alerta(this@RegisterActivity, "Error de autenticación", "", 4000)
        //        Log.d("ActRegister", e.toString())
        //    }
        //}
        val botoSendRegister: Button =findViewById<Button>(R.id.register_send_button)
        botoSendRegister.setOnClickListener {


            var nombre = nombre_registre.text.toString()
            var apellido = apellido_registre.text.toString()
            var correo = email_registre.text.toString()
            var contra = contra_registre.text.toString()
            var contrarepe = contra_repe_registre.text.toString()

            runBlocking {

                if (correo != "" && nombre != "" && apellido !="" && contra == contrarepe && emailValid(correo) && validarContra(contra)){
                    try {

                        val nuevoCliente = MainActivity.cliente(
                            email = correo,
                            nomCli = nombre,
                            apellido = apellido,
                            pag1 = "NULL",
                            pag2 = "NULL"
                        )
                        supabase.from("CLIENTE").insert(nuevoCliente)
                        authNewUser(correo, contra)
                        val go = Intent(this@RegisterActivity, MainActivity::class.java)
                        go.putExtra("ALERT_TITLE", "Registro exitoso")
                        go.putExtra("ALERT_MESSAGE", "Usuario registrado correctamente")
                        go.putExtra("SHOW_ALERT", true)
                        setResult(RESULT_OK, go)
                        startActivity(go)


                    } catch (e:Exception){
                        MainActivity.alerta(this@RegisterActivity, "Error en el correo", "Usuario ya registrado", 1000)
                    }


                }

            }
        }
    }
    fun emailValid(email:String): Boolean{
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    suspend fun authNewUser(correo: String, contraseña: String){
        auth.signUpWith(Email) {
            email = "clarascc98@gmail.com"
            password = "holaHola1."
        }

    }

    fun validarContra(contraseña: String): Boolean{
        val regex = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*[\\d])(?=.*[@\$!%*?&])[A-Za\\d@\$!%*?&]{8,}$")
        return regex.matches(contraseña)
    }

}
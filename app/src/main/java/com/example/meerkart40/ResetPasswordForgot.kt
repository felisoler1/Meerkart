package com.example.meerkart40

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import io.github.jan.supabase.auth.auth

class ResetPasswordForgot : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.forgot_password_reset)
        setContentView(R.layout.forgot_password_reset)

        val passwordInput: EditText = findViewById(R.id.passwordInput)
        val updateButton: Button = findViewById(R.id.updatePasswordButton)
        val data: Uri? = intent?.data
        data?.let {
            val token = it.getQueryParameter("token")
            }

        updateButton.setOnClickListener {
            val newPassword = passwordInput.text.toString()
            if (newPassword.isNotEmpty()) {
                updatePassword(newPassword)
            } else {
                Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updatePassword(newPassword: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                supabase.auth.updateUser{password = newPassword}
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@ResetPasswordForgot, "Unexpected error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
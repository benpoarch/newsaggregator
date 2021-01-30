package com.example.news_aggregator

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.signUpBtn
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class LoginActivity : AppCompatActivity(){

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginBtn.setOnClickListener {
            doLogin()
        }


        signUpBtn.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
            finish()
        }





        auth = FirebaseAuth.getInstance()
    }



    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser != null) {
            if(currentUser.isEmailVerified) {
                Toast.makeText(baseContext, "Authentication successful.",
                    Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
            else {
                Toast.makeText(baseContext, "Please verify your email.",
                    Toast.LENGTH_SHORT).show()
            }
        }
        else {
            Toast.makeText(baseContext, "Authentication failed.",
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun doLogin() {
        if(enterEmail.text.toString().isEmpty()) {
            enterEmail.error = "Please enter email"
            enterEmail.requestFocus()
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(enterEmail.text.toString()).matches()) {
            enterEmail.error = "Please enter valid email"
            enterEmail.requestFocus()
        }

        if(enterPassword.text.toString().isEmpty()) {
            enterEmail.error = "Please enter password"
            enterEmail.requestFocus()
        }

        auth.signInWithEmailAndPassword(enterEmail.text.toString(), enterPassword.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    updateUI(user)
                    Toast.makeText(baseContext, "Authentication successful.",
                        Toast.LENGTH_SHORT).show()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }
}


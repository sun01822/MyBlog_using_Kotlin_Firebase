package com.example.authentication

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.authentication.databinding.ActivityAuthenticaitonBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*


class AuthenticaitonActivity : AppCompatActivity() {
    private var isPasswordVisible = true
    private var signUp = false
    private var login = true
    private lateinit var binding : ActivityAuthenticaitonBinding
    private lateinit var firebaseAuth: FirebaseAuth
    val formatter = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a")
    val time = formatter.format(Date())
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticaitonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkPref()
        firebaseAuth = FirebaseAuth.getInstance()
        togglePassVisability()
        binding.loginBT.setOnClickListener {
            binding.signupBT.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.white)));
            binding.signupBT.setTextColor(Color.BLACK)
            binding.loginBT.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.purple_200)));
            binding.loginBT.setTextColor(Color.WHITE)
            binding.signupLayout.visibility = View.GONE
            binding.submitButton.text = "Log In"
            login = true
        }
        binding.signupBT.setOnClickListener {
            binding.nameEditText.requestFocus()
            binding.emailEditText.text?.clear()
            binding.passwordEditText.text?.clear()
            binding.loginBT.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.white)));
            binding.loginBT.setTextColor(Color.BLACK)
            binding.signupBT.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.purple_200)));
            binding.signupBT.setTextColor(Color.WHITE)
            binding.signupLayout.visibility = View.VISIBLE
            binding.submitButton.text = "Sign Up"
            signUp = true
            login = false
        }

        binding.submitButton.setOnClickListener {
            if(login){
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()
                if(email.length > 0 && password.length > 0){
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                        if(it.isSuccessful) {
                            val sharedPreference =  getSharedPreferences("secret", Context.MODE_PRIVATE)
                            var editor = sharedPreference.edit()
                            editor.putBoolean("isLoggedIn", true)
                            editor.putString("email", email)
                            editor.commit()
                            binding.submitButton.visibility = View.GONE
                            binding.progressBar.visibility = View.VISIBLE
                            Toast.makeText(this, "Log in successfully", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("email", email.toString())
                            startActivity(intent)
                            this.finish()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(this, "Enter valid email and password", Toast.LENGTH_SHORT).show()
                }
            }
            if(signUp){
                val image: String = ""
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()
                val name = binding.nameEditText.text.toString()
                val address = binding.addressEditText.text.toString()
                if(email.length > 0 && password.length > 0){
                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                        if(it.isSuccessful){
                            startActivity(Intent(this, DemoActivity::class.java))
                            Toast.makeText(this,"Sign up successfully", Toast.LENGTH_SHORT).show()
                            saveToDB(image, name, email, address, time)
                            this.finish()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(this, "Enter valid email and password", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun saveToDB(
        image: String,
        name: String,
        email: String,
        address: String,
        time: String
    ) {
        val model = ProfileModel(email,image, name, email, address, time)
        db.collection("Profile").document(email).set(model).addOnCompleteListener {

        }
    }

    private fun togglePassVisability() {
        if (isPasswordVisible) {
            val pass: String = binding.passwordEditText.getText().toString()
            binding.passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance())
            binding.passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
            binding.passwordEditText.setText(pass)
            binding.passwordEditText.setSelection(pass.length)
        } else {
            val pass: String = binding.passwordEditText.getText().toString()
            binding.passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
            binding.passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT)
            binding.passwordEditText.setText(pass)
            binding.passwordEditText.setSelection(pass.length)
        }
        isPasswordVisible = !isPasswordVisible
    }
    private fun checkPref(){
        val sharedPreference =  getSharedPreferences("secret", Context.MODE_PRIVATE)
        val value = sharedPreference.getBoolean("isLoggedIn", false)
        if(value){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
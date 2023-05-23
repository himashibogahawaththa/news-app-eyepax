package com.example.newsapp.view.auth

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.newsapp.database.PrefManager
import com.example.newsapp.R
import com.example.newsapp.database.AppDatabase
import com.example.newsapp.model.User
import com.example.newsapp.view.home.HomeScreenActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity() {
    private lateinit var prefManager: PrefManager
    private lateinit var etFName: EditText
    private lateinit var etLName: EditText
    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var signupBtn: Button

    private lateinit var progressDialog: ProgressDialog
    private lateinit var auth: FirebaseAuth

    private var emailPattern: Regex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        init()

        signupBtn.setOnClickListener{
            performAuth()
        }

        val loginText = findViewById<TextView>(R.id.tv_loginHere_sc)
        loginText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    fun performAuth(
//        firstName: String,
//        lastName: String,
//        user: String,
//        email: String,
//        password: String,
//        confirmPassword: String
        firstName: String = etFName.text.toString(),
        lastName: String = etLName.text.toString(),
        user: String = etUsername.text.toString(),
        email: String = etEmail.text.toString(),
        password: String = etPassword.text.toString(),
        confirmPassword: String = etConfirmPassword.text.toString()

        ) : Boolean{

        if (firstName.isEmpty()){
            etUsername.error = "Please enter your First Name"
            return false
        }
        else if (lastName.isEmpty()){
            etUsername.error = "Please enter your Last Name"
            return false
        }
        else if (user.isEmpty()){
            etUsername.error = "Please enter username"
            return false
        }
        else if (!email.matches(emailPattern)){
            etEmail.error = "Enter Correct Email"
            return false
        }
        else if (password.isEmpty() || password.length<6){
            etPassword.error = "Input proper password"
            return false
        }
        else if(password != confirmPassword){
            etConfirmPassword.error = "Password not matched"
            return false
        }
        else{
            progressDialog.setMessage("Wait until Register")
            progressDialog.setTitle("Registration")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        prefManager.setLogin(true)
                        prefManager.setFName(firstName)
                        prefManager.setLName(lastName)
                        prefManager.setUsername(user)
                        prefManager.setEmail(email)
                        prefManager.setPassword(password)

                        insertUser(firstName, lastName, user, email, password)

                        progressDialog.dismiss()
                        sendUserToNextActivity()
                        Toast.makeText(baseContext, "Authentication Successful.",Toast.LENGTH_SHORT).show()
                    } else {
                        progressDialog.dismiss()
                        Toast.makeText(baseContext, "Authentication failed.",Toast.LENGTH_SHORT).show()
                    }
                }
            return true
        }
    }

    private fun insertUser(fName: String, lName: String, userName: String, email: String, password: String){
        lifecycleScope.launch{
            val user = User(
                firstName = fName,
                lastName = lName,
                userName = userName,
                userEmail = email,
                gender = password
            )
            AppDatabase(this@SignupActivity).getUserDao().addUser(user)
        }
    }

    private fun sendUserToNextActivity() {
        val intent = Intent(this, HomeScreenActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK+Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun init(){
        prefManager = PrefManager(this)
        auth = Firebase.auth
        etFName = findViewById(R.id.et_FName)
        etLName = findViewById(R.id.et_LName)
        etUsername = findViewById(R.id.et_userName)
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        etConfirmPassword = findViewById(R.id.et_confirmPassword)
        signupBtn = findViewById(R.id.signupBtn_sc)
        progressDialog = ProgressDialog(this@SignupActivity)
    }
}
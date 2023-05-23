package com.example.newsapp.view.auth

import android.widget.Toast

class signup {
    private var emailPattern: Regex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()

    fun performAuth(
        firstName: String,
        lastName: String,
        user: String,
        email: String,
        password: String,
        confirmPassword: String
//        firstName: String = etFName.text.toString(),
//        lastName: String = etLName.text.toString(),
//        user: String = etUsername.text.toString(),
//        email: String = etEmail.text.toString(),
//        password: String = etPassword.text.toString(),
//        confirmPassword: String = etConfirmPassword.text.toString()

    ) : Boolean{

        if (firstName.isEmpty()){
            //etUsername.error = "Please enter your First Name"
            return false
        }
        else if (lastName.isEmpty()){
            //etUsername.error = "Please enter your Last Name"
            return false
        }
        else if (user.isEmpty()){
            //etUsername.error = "Please enter username"
            return false
        }
        else if (!email.matches(emailPattern)){
            //etEmail.error = "Enter Correct Email"
            return false
        }
        else if (password.isEmpty() || password.length<6){
            //etPassword.error = "Input proper password"
            return false
        }
        else if(password != confirmPassword){
            //etConfirmPassword.error = "Password not matched"
            return false
        }
        else{
            return true
        }
    }
}
package com.example.newsapp.database

import android.content.Context
import android.content.SharedPreferences

class PrefManager(context: Context) {
    val PRIVATE_MODE = 0

    private val PREF_NAME = "SharedPreferences"
    private val IS_LOGIN = "is_login"

    private val pref: SharedPreferences? = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    private val editor: SharedPreferences.Editor? = pref?.edit()

    //Save true or false as IS_LOGIN value
    fun setLogin(isLogin : Boolean){
        editor?.putBoolean(IS_LOGIN, isLogin)
        editor?.commit()
    }

    fun setFName(firstName : String){
        editor?.putString("firstName", firstName)
        editor?.commit()
    }

    fun setLName(lastName : String){
        editor?.putString("lastName", lastName)
        editor?.commit()
    }

    //Save currently logged in user name as user name
    fun setUsername(username : String){
        editor?.putString("username", username)
        editor?.commit()
    }

    //Save currently logged in users' password
    fun setEmail(email : String){
        editor?.putString("email", email)
        editor?.commit()
    }

    fun setPassword(password : String){
        editor?.putString("password", password)
        editor?.commit()
    }

    fun setCategory(category : String){
        editor?.putString("category", category)
        editor?.commit()
    }

    fun setFavoriteCategory(favCategory : String){
        editor?.putString("favCategory", favCategory)
        editor?.commit()
    }

    fun setHeading(heading : String){
        editor?.putString("heading", heading)
        editor?.commit()
    }

    //Check the boolean value to identify user logged in or not (true mean logged in and false mean not)
    fun isLogin() : Boolean?{
        return pref?.getBoolean(IS_LOGIN, false)
    }

    fun getFName() : String{
        return pref?.getString("firstName", "").toString()
    }

    fun getLName() : String{
        return pref?.getString("lastName", "").toString()
    }

    fun getUsername() : String{
        return pref?.getString("username", "").toString()
    }

    fun getPassword() : String{
        return pref?.getString("password", "").toString()
    }

    fun getEmail() : String{
        return pref?.getString("email", "").toString()
    }

    fun getCategory() : String{
        return pref?.getString("category", "").toString()
    }

    fun getFavoriteCategory() : String{
        return pref?.getString("favCategory", "").toString()
    }

    fun getHeading() : String{
        return pref?.getString("heading", "").toString()
    }

    fun removeData(){
        setLogin(isLogin = false)
        editor?.commit()
    }
}
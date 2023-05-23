package com.example.newsapp.view.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.example.newsapp.R
import com.example.newsapp.database.PrefManager
import com.example.newsapp.model.User
import com.example.newsapp.view.home.HomeScreenActivity
import com.example.newsapp.view.splash.MainActivity

class UserProfileFragment : Fragment() {
    private lateinit var backBtn : ImageButton
    private lateinit var signOut : Button
    private lateinit var name : TextView
    private lateinit var uEmail : TextView
    private lateinit var uName : TextView
    private lateinit var password : TextView
    private lateinit var fullName : String
    private lateinit var email : String
    private lateinit var userName : String
    private lateinit var gender : String
    private lateinit var prefManager: PrefManager
    private var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(R.layout.fragment_user_profile, container, false)

        name = view.findViewById(R.id.u_nameInput)
        uEmail = view.findViewById(R.id.u_emailInput)
        uName = view.findViewById(R.id.u_userNameInput)
        password = view.findViewById(R.id.u_passwordInput)

        signOut = view.findViewById(R.id.signOut_up)
        backBtn = view.findViewById(R.id.uBack_btn)
        backBtn.setOnClickListener {
            activity?.let{
                val intent = Intent (it, HomeScreenActivity::class.java)
                it.startActivity(intent)
            }
        }

        prefManager = PrefManager(this.requireContext())

        fullName = name.text.toString()
        email = uEmail.text.toString()
        userName = uName.text.toString()
        gender = password.text.toString()

        getUser()

        val fullName = prefManager.getFName() + prefManager.getLName()
        name.text = fullName
        uEmail.text = prefManager.getEmail()
        uName.text = prefManager.getUsername()
        password.text = prefManager.getPassword()

        signOut.setOnClickListener{
            prefManager.removeData()
            val intent = Intent (activity, MainActivity::class.java)
            activity?.startActivity(intent)
        }

        return view

    }

    private fun getUser() {
        val user = User(
            firstName = prefManager.getFName(),
            lastName = prefManager.getLName(),
            userName = prefManager.getUsername(),
            userEmail = prefManager.getEmail(),
            gender = prefManager.getPassword()
        )
    }

//    private fun getUser(){
//        lifecycleScope.launch{
//                val user = User(
//                    firstName = fullName,
//                    lastName = fullName,
//                    userName = userName,
//                    userEmail = email,
//                    gender = gender
//                )
//                AppDatabase(this@UserProfileFragment.requireContext()).getUserDao().getUser(user)
////                activity?.let { AppDatabase(it.applicationContext).getUserDao().getUser() }
////            AppDatabase(this@SignupActivity).getUserDao().getAllUsers()
//
//        }
//    }

//    private fun getUser(userDetails: User) {
//        lifecycleScope.launch {
//            userDetails.forEach {
//                val user = User(
//                    firstName = it.firstName,
//                    lastName = it.lastName,
//                    userEmail = it.userEmail,
//                    userName = it.userName,
//                    gender = it.gender
//                )
//                AppDatabase(this).getUserDao().getUser()
//            }
//        }
//    }
}
package com.example.newsapp.view.auth

import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.newsapp.R
import com.example.newsapp.database.PrefManager
import com.example.newsapp.view.home.HomeScreenActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class LoginActivity : AppCompatActivity() {
    private lateinit var prefManager: PrefManager
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var username: String
    private lateinit var password: String
    private lateinit var loginBtn: Button
    private lateinit var googleBtn: LottieAnimationView
    private lateinit var signupText: TextView
    private var emailPattern: Regex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()

    private lateinit var progressDialog: ProgressDialog
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var googleSignInClient: GoogleSignInClient
    private companion object{
        private const val RC_SIGN_IN = 100
        private const val TAG = "GOOGLE_SIGN_IN_TAG"
    }

    private lateinit var callbackManager: CallbackManager
    private lateinit var buttonFacebookLogin: LoginButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FacebookSdk.sdkInitialize(getApplicationContext())
        setContentView(R.layout.activity_login)
        init()

        loginBtn = findViewById(R.id.loginBtn_lc)
        loginBtn.setOnClickListener{
            performLogin()
        }

        prefManager = PrefManager(this)
        firebaseAuth = FirebaseAuth.getInstance()

        signupText = findViewById<TextView>(R.id.tv_signup_lc)
        signupText.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        googleBtn.setOnClickListener{
            progressDialog = ProgressDialog(this@LoginActivity)
            progressDialog.setMessage("Google Sign In...")
            progressDialog.show()

            Log.d(TAG, "onCreate: begin Google SignIn")
            val intent = googleSignInClient.signInIntent
            startActivityForResult(intent, RC_SIGN_IN)
        }

//        Facebook Login
//         [START initialize_fblogin]
//         Initialize Facebook Login button
        callbackManager = CallbackManager.Factory.create()

        buttonFacebookLogin = findViewById(R.id.facebook_login)
        buttonFacebookLogin.setReadPermissions("email", "public_profile")
        buttonFacebookLogin.registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d(TAG, "facebook:onSuccess:$loginResult")
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d(TAG, "facebook:onCancel")
            }

            override fun onError(error: FacebookException) {
                Log.d(TAG, "facebook:onError", error)
            }
        })
    }


    private fun init(){
        prefManager = PrefManager(this)
        etEmail = findViewById(R.id.et_loginEmail)
        etPassword = findViewById(R.id.et_password)
        googleBtn = findViewById(R.id.google_login)
        firebaseAuth = Firebase.auth
        progressDialog = ProgressDialog(this@LoginActivity)
    }

    private fun performLogin() {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        if (!email.matches(emailPattern)){
            etEmail.error = "Enter Correct Email"
        }
        else if (password.isEmpty() || password.length<6){
            etPassword.error = "Input proper password"
        }
        else {
            progressDialog.setMessage("Wait until Login")
            progressDialog.setTitle("Login")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()

            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        prefManager.setLogin(true)
                        prefManager.setUsername(username)
                        progressDialog.dismiss()
                        sendUserToNextActivity()
                        Toast.makeText(baseContext, "Authentication Successful.", Toast.LENGTH_SHORT).show()
                    } else {
                        progressDialog.dismiss()
                        Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun sendUserToNextActivity() {
        val intent = Intent(this, HomeScreenActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK+Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {

                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogleAccount(account.idToken!!)
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)

            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    // [START auth_with_facebook]
    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this@LoginActivity, HomeScreenActivity::class.java))
                    prefManager.setLogin(true)
                    finish()
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun firebaseAuthWithGoogleAccount(account: String) {
        Log.d(TAG, "firebaseAuthWithGoogleAccount: begin firebase auth with google account")

        val credential = GoogleAuthProvider.getCredential(account, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                Log.d(TAG, "firebaseAuthWithGoogleAccount: LoggedIn")

                val firebaseUser = firebaseAuth.currentUser
                val uid = firebaseUser!!.uid
                val email = firebaseUser.email
                val name = firebaseUser.displayName

                Log.d(TAG, "firebaseAuthWithGoogleAccount: Uid: $uid")
                Log.d(TAG, "firebaseAuthWithGoogleAccount: Email: $email")

                if (authResult.additionalUserInfo!!.isNewUser){
                    progressDialog.dismiss()
                    Log.d(TAG, "firebaseAuthWithGoogleAccount: Account created... \n$email")
                    Toast.makeText(this@LoginActivity, "LoggedIn... \n$email", Toast.LENGTH_SHORT).show()
                }
                else{
                    progressDialog.dismiss()
                    Log.d(TAG, "firebaseAuthWithGoogleAccount: Existing created... \n$email")
                    Toast.makeText(this@LoginActivity, "LoggedIn... \n$email", Toast.LENGTH_SHORT).show()
                }
                startActivity(Intent(this@LoginActivity, HomeScreenActivity::class.java))
                prefManager.setLogin(true)
                prefManager.setFName(name.toString())
                prefManager.setEmail(email.toString())
                prefManager.setUsername(username)
                finish()
            }
            .addOnFailureListener{ e ->
                progressDialog.dismiss()
                Log.d(TAG, "firebaseAuthWithGoogleAccount: Logging Failed due to \n${e.message}")
                Toast.makeText(this@LoginActivity, "Logging Failed due to \n${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
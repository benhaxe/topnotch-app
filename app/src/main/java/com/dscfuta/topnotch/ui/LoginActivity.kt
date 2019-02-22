package com.dscfuta.topnotch.ui

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import com.dscfuta.topnotch.App
import com.dscfuta.topnotch.MainActivity
import com.dscfuta.topnotch.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private  lateinit var auth: FirebaseAuth
    private lateinit var dialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        dialog = ProgressDialog(this)

        btn_login.setOnClickListener {
            signIn(field_email.text.toString(), field_password.text.toString())
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        Log.d("TAG", "Current User ${currentUser.toString()}")
        updateUI(currentUser)
    }

    private fun validateForm() : Boolean{
        var valid = true

        val email = field_email.text.toString()
        val password = field_password.text.toString()

        if (TextUtils.isEmpty(email)){
            field_email.error = "Field Required"
            valid = false
        }else{
            field_email.error = null
        }

        if (TextUtils.isEmpty(password)){
            field_password.error = "Password Field is Required"
            valid = false
        }else{
            field_password.error = null
        }

        return valid
    }

    private fun signIn(email: String, password: String){
        Log.d("TAG", "signIN: $email")
        if(!validateForm()){
            return
        }
        showProgressDialog()

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        val user = auth.currentUser
                        updateUI(user)
                    }else{
                        Log.d("TAG", "signInWithEmail: failure", it.exception)
                        makeText(baseContext, "${it.exception}", LENGTH_SHORT).show()
                        updateUI(null)
                    }
                    /*if(!it.isSuccessful){
                        makeText(baseContext, "Something went wrong", LENGTH_SHORT).show()
                    }*/
                }

        dialog.dismiss()
    }
    private fun updateUI(user: FirebaseUser?){
        if(user != null){
            startActivity(Intent(baseContext, MainActivity::class.java))
            finish()
        }
    }

    private fun showProgressDialog(){
        dialog.setMessage("Authenticating User")
        dialog.show()
    }
}

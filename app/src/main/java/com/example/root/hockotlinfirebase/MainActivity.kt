package com.example.root.hockotlinfirebase

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var save: SharedPreferences
    private val KEY_EMAIL = "email"
    private val KEY_PASSWORD = "password"
    private val KEY_CHECK = "check"
    override fun onClick(p0: View?) {
        when(p0){
            btnLogin -> {
                shortToast("LOGIN")
                if(!kiemtra()){
                    return
                }
                Login()
            }
            btnRegister -> {
                shortToast("REGISTER")
                if(!kiemtra()){
                    return
                }
                Register()
            }
        }
    }

    private fun kiemtra(): Boolean {
        var chk = true
        val email = edtemail.text.toString().trim()
        val password = edtpassword.text.toString().trim()
        if(TextUtils.isEmpty(email)){
            edtemail.setError("Please enter email!")
            chk = false
        }
        if(TextUtils.isEmpty(password)){
            edtpassword.setError("Please enter password")
            chk = false
        }
        return chk
    }

    private fun Register() {
        val email = edtemail.text.toString().trim()
        val password = edtpassword.text.toString().trim()
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        shortToast("Success")
                    }else{
                        shortToast("Fail")
                    }
                }
    }

    private fun Login() {
        val email = edtemail.text.toString().trim()
        val password = edtpassword.text.toString().trim()
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        shortToast("Success")
                        route()
                        if(checkboxSave.isChecked){
                            val editor: SharedPreferences.Editor = save.edit()
                            editor.putString(KEY_EMAIL,email)
                            editor.putString(KEY_PASSWORD,password)
                            editor.putBoolean(KEY_CHECK,true)
                            editor.apply()
                        }else{
                            val editor: SharedPreferences.Editor = save.edit()
                            editor.remove(KEY_EMAIL)
                            editor.remove(KEY_PASSWORD)
                            editor.remove(KEY_CHECK)
                            editor.apply()
                        }
                    }else{
                        shortToast("Fail")
                    }
                }
    }

    private fun route() {
        startActivity(Intent(this@MainActivity,Main2Activity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolbar()
        addControls()

        save = getSharedPreferences("data", Context.MODE_PRIVATE)
        edtemail.setText(save.getString(KEY_EMAIL,""))
        edtpassword.setText(save.getString(KEY_PASSWORD,""))
        checkboxSave.isChecked = save.getBoolean(KEY_CHECK,false)
        mAuth = FirebaseAuth.getInstance()
    }

    private fun initToolbar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle("MAIN")
        toolbar.setTitleTextColor(Color.WHITE)
    }

    private fun addControls() {
        btnLogin.setOnClickListener(this)
        btnRegister.setOnClickListener(this)
    }

    private fun shortToast(s: String, length: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this@MainActivity,s,length).show()
    }
    //TODO: this@MainActivity = MainActivity.this

    //TODO: MainActivity::class.java = MainActivity.class
}

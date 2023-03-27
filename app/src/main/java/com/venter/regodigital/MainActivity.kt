package com.venter.regodigital


import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.venter.regodigital.Dashboard.AdminDashboard
import com.venter.regodigital.Dashboard.EmpDashboard
import com.venter.regodigital.Login.LogInActivity

import com.venter.regodigital.utils.Constans.TAG
import com.venter.regodigital.utils.TokenManger
import dagger.hilt.android.AndroidEntryPoint

import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var tokenManger: TokenManger

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        supportActionBar?.hide()


        Handler().postDelayed(
            {


                try {

                    if (tokenManger.getToken() != null) {

                        val intents = if (tokenManger.getUserType() == "Admin") {
                            Intent(this, AdminDashboard::class.java)
                        } else {
                            Intent(this, EmpDashboard::class.java)
                        }


                        startActivity(intents)
                        this.finish()

                    } else {


                        val intent = Intent(this, LogInActivity::class.java)
                        startActivity(intent)
                        this.finish()
                    }


                } catch (e: Exception) {
                    Log.d(TAG, "Error in MainActivity.kt Handler().postDelayed is  " + e.message)
                }

            }, 3000
        )
    }


}
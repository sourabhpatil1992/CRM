package com.venter.regodigital

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.venter.regodigital.Dashboard.AdminDashboard
import com.venter.regodigital.Dashboard.EmpDashboard
import com.venter.regodigital.Login.LogInActivity

import com.venter.regodigital.utils.Constans.TAG
import com.venter.regodigital.utils.TokenManger
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
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

                        var intents =Intent()

                        if(tokenManger.getUserType()=="Admin")
                         intents = Intent(this, AdminDashboard::class.java)
                        else
                            intents = Intent(this, EmpDashboard::class.java)
                        startActivity(intents)
                        this.finish()

                    } else {


                        val intent = Intent(this, LogInActivity::class.java)
                        startActivity(intent)
                        this.finish()
                    }


                } catch (e: Exception) {
                    Log.d(TAG, "Error in MainActivty.kt Handler().postDelayed is  " + e.message)
                }

            }, 2000
        )
    }
}
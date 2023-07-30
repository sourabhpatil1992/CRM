package com.venter.regodigital.userledger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.NetworkOnMainThreadException
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.venter.regodigital.Dashboard.AdminDashboard
import com.venter.regodigital.databinding.ActivityUserDetBinding
import com.venter.regodigital.models.UserListRes
import com.venter.regodigital.utils.Constans.TAG
import com.venter.regodigital.utils.NetworkResult
import com.venter.regodigital.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UserDetActivity : AppCompatActivity() {
    private var _binding: ActivityUserDetBinding? = null
    private val binding: ActivityUserDetBinding
        get() = _binding!!
    private lateinit var user: UserListRes

    private val candidateViewModel by viewModels<CandidateViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityUserDetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = intent.getParcelableExtra("user")!!

        binding.whatsButton.setOnClickListener {
            whatsAppInitialization()
        }
        binding.dataSwapping.setOnClickListener {
            val intent = Intent(this, DataTransActivity::class.java)
            intent.putExtra("userId", user.id)
            intent.putExtra("userName", user.user_name)
            startActivity(intent)
        }
        binding.userReport.setOnClickListener {
            val intent = Intent(this, UserReport::class.java)
            intent.putExtra("userId", user.id)
            intent.putExtra("userName", user.user_name)
            startActivity(intent)
        }

        binding.delAcc.setOnClickListener {
            delAcc()
        }

    }

    private fun delAcc() {
        try {
            val message = TextView(this)
            message.text ="Are you sure delete this account? \n Please check raw data is transferred or not. "
            val layout = LinearLayout(this)
            layout.setPadding(20, 20, 20, 20)
            layout.addView(message)
            val builders = AlertDialog.Builder(this)
            builders.setTitle("Account Remove")
            builders.setView(layout)

            builders.setPositiveButton("Yes"){_,_->
                try {
                   candidateViewModel.delAcc(user.id)
                    candidateViewModel.stringResData.observe(this){
                        when(it)
                        {
                            is NetworkResult.Loading ->{}
                            is NetworkResult.Error -> Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT)
                                .show()
                            is NetworkResult.Success ->{
                                Toast.makeText(this, it.data.toString(), Toast.LENGTH_SHORT)
                                    .show()
                                startActivity(Intent(this,AdminDashboard::class.java))
                                this.finish()
                            }
                        }
                    }
                }
                catch (e:Exception)
                {
                    Log.d(TAG,"Error in UserDetActivity.kt delAcc() is ${e.message}")
                }

            }
            builders.setNegativeButton("No"){_,_->

            }

            builders.show()
        } catch (e: Exception) {
            Log.d(TAG, "Error in UserDetActivity.kt delAcc() is ${e.message}")
        }
    }

    private fun whatsAppInitialization() {
        try {
            val intent = Intent(this, WhatsAppActivity::class.java)
            intent.putExtra("userId", user.id)
            intent.putExtra("userName", user.user_name)
            startActivity(intent)


        } catch (e: Exception) {
            Log.d(TAG, "Error in UserDetActivity.kt whatsAppInitialization() is " + e.message)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
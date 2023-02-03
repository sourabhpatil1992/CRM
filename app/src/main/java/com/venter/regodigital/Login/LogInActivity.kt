package com.venter.regodigital.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.venter.regodigital.databinding.ActivityLogInBinding
import com.venter.regodigital.models.SignInReq
import com.venter.regodigital.utils.Constans.TAG
import com.venter.regodigital.utils.NetworkResult
import com.venter.regodigital.utils.TokenManger
import com.venter.regodigital.viewModelClass.logViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class LogInActivity : AppCompatActivity() {
    private var _binding:ActivityLogInBinding? = null
    private val binding:ActivityLogInBinding
    get() = _binding!!

    private val logViewModel by viewModels<logViewModel>()

    @Inject
    lateinit var tokenManger: TokenManger

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLogInBinding.inflate(layoutInflater)

        if(tokenManger.getDevToken()==null)
        {
            tokenManger.saveDevToken()
        }

        setContentView(binding.root)

        binding.btnOtp.setOnClickListener {

            logViewModel.logInReq(SignInReq(binding.txtmobno.text.toString(),tokenManger.getDevToken().toString()))

            logViewModel.logInReqRes.observe(this) {
                binding.progressbar.visibility = View.GONE
                when (it) {
                    is NetworkResult.Success -> {
                        val intent = Intent(this, OtpActivity::class.java)
                        intent.putExtra("otp",it.data!!.mob_otp.toString())
                        intent.putExtra("userId",it.data.userId.toString())
                        startActivity(intent)
                    }
                    is NetworkResult.Error -> {
                       Toast.makeText(this,"Something went wrong.Please contact the System Administrator.",Toast.LENGTH_SHORT).show()
                    }
                    is NetworkResult.Loading -> {
                        binding.progressbar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}
package com.venter.crm.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import com.venter.crm.Dashboard.AdminDashboard
import com.venter.crm.Dashboard.EmployeeDash
import com.venter.crm.databinding.ActivityOtpBinding
import com.venter.crm.models.SignIn
import com.venter.crm.utils.NetworkResult
import com.venter.crm.utils.TokenManger
import com.venter.crm.viewModelClass.logViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OtpActivity : AppCompatActivity() {
    private var _binding: ActivityOtpBinding? = null
    private val binding: ActivityOtpBinding
        get() = _binding!!

    private var otp: String = ""
    private var userId: String = ""

    private val logViewModel by viewModels<logViewModel>()

    @Inject
    lateinit var tokenManger: TokenManger

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        otp = intent.getStringExtra("otp").toString()
        userId = intent.getStringExtra("userId").toString()

        //Toast.makeText(this,otp.toString(),Toast.LENGTH_SHORT).show()

        resenTime()

        otp_set()

        binding.btnconfirm.setOnClickListener {
            val userOtp =
                binding.edtOtp1.text.toString() + binding.edtOtp2.text.toString() + binding.edtOtp3.text.toString() + binding.edtOtp4.text.toString()
            if (userOtp == otp) {
                logViewModel.logIn(
                    SignIn(
                        userId,
                        tokenManger.getDevToken().toString(),
                        tokenManger.getFirebaseToken().toString()
                    )
                )

                logViewModel.logInResData.observe(this) {
                    binding.progressbar.visibility = View.GONE
                    when (it) {
                        is NetworkResult.Loading -> {
                            binding.progressbar.visibility = View.VISIBLE
                        }

                        is NetworkResult.Error -> {
                            Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                        }

                        is NetworkResult.Success -> {
                            //Log.d(TAG,it.data.toString())
                            tokenManger.saveToken(it.data!!.devToken.toString())
                            tokenManger.saveUserDet(it.data.user_type, it.data.user_id,it.data.institute_id)

                            var intent = Intent()
                            if (it.data!!.user_type == "Admin")
                                intent = Intent(this, AdminDashboard::class.java)
                            else
                            //intent = Intent(this, EmpDashboard::class.java)
                                intent = Intent(this, EmployeeDash::class.java)
                            startActivity(intent)
                            this.finish()
                        }
                    }
                }
            }


            // val intent = Intent(this, AdminDashboard::class.java)
            // startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun resenTime() {
        Thread(Runnable {
            for (i in 0..45) {

                this.runOnUiThread {
                    binding.txtResendotp.text = (45 - i).toString() + " Sec"
                }
                Thread.sleep(1000)
            }
            this.runOnUiThread {
                binding.txtResendotp.text = "Resend OTP"
            }


        }).start()
    }

    fun otp_set() {
        val edit =
            arrayOf<EditText>(binding.edtOtp1, binding.edtOtp2, binding.edtOtp3, binding.edtOtp4)



        binding.edtOtp1.addTextChangedListener(GenericTextWatcher(binding.edtOtp1, edit))
        binding.edtOtp2.addTextChangedListener(GenericTextWatcher(binding.edtOtp2, edit))
        binding.edtOtp3.addTextChangedListener(GenericTextWatcher(binding.edtOtp3, edit))
        binding.edtOtp4.addTextChangedListener(GenericTextWatcher(binding.edtOtp4, edit))
    }
}
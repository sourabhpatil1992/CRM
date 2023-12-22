package com.venter.crm.integration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.venter.crm.databinding.ActivityEmailAccountIntegrationBinding
import com.venter.crm.databinding.LayoutEmailaccconfigBinding
import com.venter.crm.utils.Constans.TAG
import dagger.hilt.android.AndroidEntryPoint
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.venter.crm.R
import com.venter.crm.models.ConfigMailModel
import com.venter.crm.utils.NetworkResult
import com.venter.crm.viewModelClass.CandidateViewModel

@AndroidEntryPoint
class EmailAccountIntegrationActivity : AppCompatActivity(),EmailConfInterFace {
    private var _binding: ActivityEmailAccountIntegrationBinding? = null
    private val binding: ActivityEmailAccountIntegrationBinding
        get() = _binding!!

    private val candidateViewModel by viewModels<CandidateViewModel>()

    private lateinit var adapter:EmailListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEmailAccountIntegrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = EmailListAdapter(this,this)
        getData()
        binding.floatingActionButton.setOnClickListener {
            mailConfig()
        }
        Toast.makeText(this,"For update any account, please long press on account.",Toast.LENGTH_SHORT).show()
    }

    private fun getData() {
        try {
            candidateViewModel.getEmailList()
            candidateViewModel.emailListDataLiveData.observe(this)
            {
                binding.progressbar.visibility = View.GONE
                when (it) {
                    is NetworkResult.Loading -> binding.progressbar.visibility =
                        View.VISIBLE

                    is NetworkResult.Error -> {
                        Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT)
                            .show()
                       // candidateViewModel.emailListDataLiveData.removeObservers(this)
                    }

                    is NetworkResult.Success -> {
                        setData(it.data)
                       // candidateViewModel.emailListDataLiveData.removeObservers(this)

                    }
                }
            }
        }
        catch (e: Exception) {
            Log.d(TAG, "Error in EmailAccountIntegrationActivity.kt getData() is : ${e.message}")
        }
    }

    private fun setData(data: List<ConfigMailModel>?) {
        try {

            adapter.submitList(data)
            val layoutManager = LinearLayoutManager(this)
            binding.rcView.layoutManager = layoutManager

            binding.rcView.adapter = adapter
        }
        catch (e: Exception) {
            Log.d(TAG, "Error in EmailAccountIntegrationActivity.kt setData() is : ${e.message}")
        }

    }

    private fun mailConfig() {
        try {


            val emailView = LayoutEmailaccconfigBinding.inflate(layoutInflater)

            val alertDialog = AlertDialog.Builder(this)
                .setView(emailView.root)

            emailView.btnTogglePassword.setOnClickListener {
                // Toggle the password visibility using XOR
                emailView.password.inputType = emailView.password.inputType xor
                        (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)

                // Set the appropriate image resource based on the current input type
                val imageResource =
                    if (emailView.password.transformationMethod == PasswordTransformationMethod.getInstance()) {
                        R.drawable.baseline_remove_red_eye_24
                    } else {
                        R.drawable.baseline_visibility_off_24
                    }
                emailView.btnTogglePassword.setImageResource(imageResource)

                // Set the cursor position to the end of the text
                emailView.password.setSelection(emailView.password.length())
            }


            val domainRegex = """^([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.[a-zA-Z]{2,}$""".toRegex()


            alertDialog.setPositiveButton("Create Email Acc") { _, _ ->
                if (isEmailValid(emailView.email.text) && domainRegex.matches(emailView.host.text.toString()) &&
                    emailView.port.text.isNotEmpty() && emailView.password.text.isNotEmpty()
                ) {

                    val mailConf = ConfigMailModel(
                        0,
                        emailView.host.text.toString(),
                        emailView.port.text.toString().toInt(),
                        emailView.email.text.toString(),
                        emailView.password.text.toString(), 1
                    )
                    candidateViewModel.configEmail(mailConf)
                    candidateViewModel.stringResData.observe(this)
                    {
                        binding.progressbar.visibility = View.GONE
                        when (it) {
                            is NetworkResult.Loading -> binding.progressbar.visibility =
                                View.VISIBLE

                            is NetworkResult.Error -> {
                                Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT)
                                    .show()
                                candidateViewModel.stringResData.removeObservers(this)
                            }

                            is NetworkResult.Success -> {
                                Toast.makeText(this, it.data.toString(), Toast.LENGTH_SHORT).show()
                                getData()
                                candidateViewModel.stringResData.removeObservers(this)

                            }
                        }
                    }
                    //dialog.dismiss()
                } else {
                    Toast.makeText(this, "Please Fill all details correctly.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
                .setNegativeButton("Cancel") { dialog, _ ->
                    // Handle negative button click
                    dialog.dismiss()
                }
                .create()
            alertDialog.show()
        } catch (e: Exception) {
            Log.d(TAG, "Error in EmailAccountIntegrationActivity.kt mailConfig() is : ${e.message}")
        }
    }

    private fun isEmailValid(email: CharSequence): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun updateEmailConf(data: ConfigMailModel) {
        try {


            val emailView = LayoutEmailaccconfigBinding.inflate(layoutInflater)

            val alertDialog = AlertDialog.Builder(this)
                .setView(emailView.root)

            emailView.btnTogglePassword.setOnClickListener {
                // Toggle the password visibility using XOR
                emailView.password.inputType = emailView.password.inputType xor
                        (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)

                // Set the appropriate image resource based on the current input type
                val imageResource =
                    if (emailView.password.transformationMethod == PasswordTransformationMethod.getInstance()) {
                        R.drawable.baseline_remove_red_eye_24
                    } else {
                        R.drawable.baseline_visibility_off_24
                    }
                emailView.btnTogglePassword.setImageResource(imageResource)

                // Set the cursor position to the end of the text
                emailView.password.setSelection(emailView.password.length())
            }


            val domainRegex = """^([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.[a-zA-Z]{2,}$""".toRegex()

            emailView.host.setText(data.host)
            emailView.port.setText(data.port.toString())
            emailView.email.setText(data.emailId)
            emailView.password.setText(data.pass)

            alertDialog.setPositiveButton("Update Email Acc") { _, _ ->
                if (isEmailValid(emailView.email.text) && domainRegex.matches(emailView.host.text.toString()) &&
                    emailView.port.text.isNotEmpty() && emailView.password.text.isNotEmpty()
                ) {

                    val mailConf = ConfigMailModel(
                        data.id,
                        emailView.host.text.toString(),
                        emailView.port.text.toString().toInt(),
                        emailView.email.text.toString(),
                        emailView.password.text.toString(), 1
                    )
                    candidateViewModel.configEmail(mailConf)
                    candidateViewModel.stringResData.observe(this)
                    {
                        binding.progressbar.visibility = View.GONE
                        when (it) {
                            is NetworkResult.Loading -> binding.progressbar.visibility =
                                View.VISIBLE

                            is NetworkResult.Error -> {
                                Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT)
                                    .show()
                                candidateViewModel.stringResData.removeObservers(this)
                            }

                            is NetworkResult.Success -> {
                                Toast.makeText(this, it.data.toString(), Toast.LENGTH_SHORT).show()
                                getData()
                                candidateViewModel.stringResData.removeObservers(this)

                            }
                        }
                    }
                    //dialog.dismiss()
                } else {
                    Toast.makeText(this, "Please Fill all details correctly.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
                .setNegativeButton("Cancel") { dialog, _ ->
                    // Handle negative button click
                    dialog.dismiss()
                }
                .create()
            alertDialog.show()
        } catch (e: Exception) {
            Log.d(TAG, "Error in EmailAccountIntegrationActivity.kt updateEmailConf() is : ${e.message}")
        }
    }
}
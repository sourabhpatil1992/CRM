package com.venter.crm.whatsTemp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.venter.crm.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WhatsappMessageActivity : AppCompatActivity() {
   // var whatsId: Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_whatsapp_message)

      //  whatsId = intent.getIntExtra("accId",-1)
    }
}
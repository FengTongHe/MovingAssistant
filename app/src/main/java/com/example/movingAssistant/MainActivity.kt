package com.example.movingAssistant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)

            val customerTextView = findViewById<TextView>(R.id.ed_customerID)
            customerID = customerTextView.text.toString()

            if(customerID.isNotEmpty()){
                startActivity(intent)
            }

            else{
                Toast.makeText(this, "Please enter the customer ID", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object{
        lateinit var customerID: String
    }
}
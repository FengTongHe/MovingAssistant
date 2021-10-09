package com.example.movingAssistant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main4.*
import kotlinx.android.synthetic.main.activity_main4.view.*
import java.util.*

class MainActivity4 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)

        val dataBaseHelper = DataBaseHelper(this)

        rc_view.adapter = ItemAdapter(dataBaseHelper.everyone)
        rc_view.layoutManager = LinearLayoutManager(this)

        tv_total.text = dataBaseHelper.totalNumber.toString()
    }
}


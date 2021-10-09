package com.example.movingAssistant

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*

private  const val CAMERA_REQUEST_CDDE = 101

class MainActivity2 : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner
    private  lateinit var photoUri : String

    companion object{
        lateinit var codeNumber : String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        setupPermission()
        codeScanner()

        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)
        scannerView.setOnClickListener{
            codeScanner.startPreview()
        }

        val button2 = findViewById<Button>(R.id.button2)
        button2.setOnClickListener {
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
        }

        val button3 = findViewById<Button>(R.id.button3)
        button3.setOnClickListener {

            val dataBaseHelper = DataBaseHelper(this)
            val codeNumberList : List<String> = dataBaseHelper.barcodeNumbers

            try {
                if (codeNumber in codeNumberList){
                    Toast.makeText(this, "Item is already scanned", Toast.LENGTH_SHORT).show()
                }

                else{
                    photoUri = MainActivity3.photoUri.toString()

                    val movingItemsControl = MovingItemsControl(-1, codeNumber, true, photoUri, MainActivity.customerID)
                    val addOne = dataBaseHelper.addOne(movingItemsControl)

                    val txtview = findViewById<TextView>(R.id.tv_textView)
                    txtview.text = " "

                    Toast.makeText(this, "Success= $addOne", Toast.LENGTH_SHORT).show()
                }
            }catch (e: UninitializedPropertyAccessException){
                Toast.makeText(this, "Please scan the item first", Toast.LENGTH_SHORT).show()
            }
        }

        val button4 = findViewById<Button>(R.id.button4)
        button4.setOnClickListener {
            val intent = Intent(this, MainActivity4::class.java)

            startActivity(intent)
        }

        val button5 = findViewById<Button>(R.id.button5)
        button5.setOnClickListener {

            val dataBaseHelper = DataBaseHelper(this)
            val codeNumberList : List<String> = dataBaseHelper.barcodeNumbers

            val movingItem : MovingItemsControl

            try {
                if (codeNumber in codeNumberList){

                    movingItem = dataBaseHelper.findTheItem()

                    dataBaseHelper.deleteOne(movingItem)

                    Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()

                    val txtview = findViewById<TextView>(R.id.tv_textView)
                    txtview.text = " "
                }
                else{
                    Toast.makeText(this, "Code not exist", Toast.LENGTH_SHORT).show()
                }
            }catch (e: UninitializedPropertyAccessException){
                Toast.makeText(this, "No barcode scanned", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun setupPermission(){
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED){
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
        arrayOf(android.Manifest.permission.CAMERA),
        CAMERA_REQUEST_CDDE)
    }

    private  fun codeScanner(){
        val scv = findViewById<CodeScannerView>(R.id.scanner_view)
        val txtview = findViewById<TextView>(R.id.tv_textView)
        val toneGenerator = ToneGenerator(AudioManager.STREAM_ALARM, 100)

        codeScanner = CodeScanner(this, scv)
        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                runOnUiThread {
                    if(txtview.text != it.text){
                        toneGenerator.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200)
                    }
                    txtview.text = it.text
                    codeNumber = txtview.text.toString()
                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread{
                    Log.e("Main","Camera initialization error: ${it.message}")
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode){
            CAMERA_REQUEST_CDDE -> {
                if(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "The scanner needs camera permission", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}


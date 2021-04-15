package edu.bluejack20_2.chantuy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val goToTestBtn: Button = findViewById(R.id.goToTestBtn) //dapetin button dg id goToTestBtn
        goToTestBtn.setOnClickListener { // set click listener nya
            val testActivityIntent = Intent(this, TestActivity::class.java)
            startActivity(testActivityIntent) // pindah activity
        }
    }
}
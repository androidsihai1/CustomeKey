package com.yy.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        btn_drag?.setOnClickListener {
            startActivity(Intent(baseContext, DragActivity::class.java))
        }
        btn_wheel_view?.setOnClickListener {
            startActivity(Intent(baseContext, MergeCircleActivity::class.java))
        }
    }
}

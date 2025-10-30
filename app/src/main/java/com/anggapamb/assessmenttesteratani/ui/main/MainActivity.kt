package com.anggapamb.assessmenttesteratani.ui.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.anggapamb.assessmenttesteratani.databinding.ActivityMainBinding
import com.anggapamb.assessmenttesteratani.ui.animation.AnimationActivity
import com.anggapamb.assessmenttesteratani.ui.apiCalling.ApiCallingActivity
import com.anggapamb.assessmenttesteratani.ui.dataProcessing.DataProcessingActivity
import com.anggapamb.assessmenttesteratani.ui.wordSearch.WordSearchActivity
import com.anggapamb.assessmenttesteratani.utils.applySystemWindowInsetsPadding
import com.nuvyz.core.utils.ActivityExtension.openActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.main.applySystemWindowInsetsPadding()

        initListener()

    }

    private fun initListener() {
        binding.btnWordSearch.setOnClickListener {
            openActivity<WordSearchActivity>()
        }

        binding.btnDataProcessing.setOnClickListener {
            openActivity<DataProcessingActivity>()
        }

        binding.btnAnimation.setOnClickListener {
            openActivity<AnimationActivity>()
        }

        binding.btnApiCalling.setOnClickListener {
            openActivity<ApiCallingActivity>()
        }
    }
}
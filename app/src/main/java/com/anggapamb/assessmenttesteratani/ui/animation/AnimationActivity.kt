package com.anggapamb.assessmenttesteratani.ui.animation

import android.animation.Keyframe
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.anggapamb.assessmenttesteratani.R
import com.anggapamb.assessmenttesteratani.databinding.ActivityAnimationBinding
import com.anggapamb.assessmenttesteratani.utils.applySystemWindowInsetsPadding
import kotlin.math.roundToInt

class AnimationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnimationBinding

    private var heartbeatAnimator: ObjectAnimator? = null
    private var isPlaying = false

    private val minBpm = 40
    private val maxBpm = 180
    private var currentBpm = 72

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAnimationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.main.applySystemWindowInsetsPadding()

        setupToolbar()
        setupControls()
        applyBpm(currentBpm)
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupControls() = with(binding) {
        sbBpm.max = (maxBpm - minBpm)
        sbBpm.progress = (currentBpm - minBpm)
        tvBpm.text = "${currentBpm} BPM"

        sbBpm.setOnSeekBarChangeListener(object : android.widget.SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: android.widget.SeekBar?, progress: Int, fromUser: Boolean) {
                val bpm = minBpm + progress
                applyBpm(bpm)
                if (isPlaying) restartAnimator()
            }
            override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {}
        })

        btnToggle.setOnClickListener {
            if (isPlaying) {
                pauseAnimator()
            } else {
                startAnimator()
            }
        }
    }

    private fun applyBpm(bpm: Int) = with(binding) {
        currentBpm = bpm.coerceIn(minBpm, maxBpm)
        tvBpm.text = "${currentBpm} BPM"
    }

    private fun buildHeartbeatAnimator(target: View, bpm: Int): ObjectAnimator {
        val periodMs = (60_000f / bpm).roundToInt().toLong()
        
        val k0 = Keyframe.ofFloat(0f,   1.0f)
        val k1 = Keyframe.ofFloat(0.10f,1.28f)
        val k2 = Keyframe.ofFloat(0.20f,1.00f)
        val k3 = Keyframe.ofFloat(0.30f,1.18f)
        val k4 = Keyframe.ofFloat(0.40f,1.00f)
        val k5 = Keyframe.ofFloat(1f,   1.00f)

        val scaleX = PropertyValuesHolder.ofKeyframe(View.SCALE_X, k0, k1, k2, k3, k4, k5)
        val scaleY = PropertyValuesHolder.ofKeyframe(View.SCALE_Y, k0, k1, k2, k3, k4, k5)

        return ObjectAnimator.ofPropertyValuesHolder(target, scaleX, scaleY).apply {
            duration = periodMs
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
            interpolator = LinearInterpolator()
        }
    }

    private fun startAnimator() {
        heartbeatAnimator?.cancel()
        heartbeatAnimator = buildHeartbeatAnimator(binding.ivHeart, currentBpm).also { it.start() }
        isPlaying = true
        binding.btnToggle.text = getString(R.string.btn_pause)
    }

    private fun restartAnimator() {
        if (!isPlaying) return
        startAnimator()
    }

    private fun pauseAnimator() {
        heartbeatAnimator?.cancel()
        binding.ivHeart.animate().scaleX(1f).scaleY(1f).setDuration(120).start()
        isPlaying = false
        binding.btnToggle.text = getString(R.string.btn_start)
    }

    override fun onDestroy() {
        heartbeatAnimator?.cancel()
        super.onDestroy()
    }
}
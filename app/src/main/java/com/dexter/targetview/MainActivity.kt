package com.dexter.targetview

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.dexter.advancedmaterialtaptarget.MaterialTapTargetPrompt

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.textvw).setOnClickListener { startActivity(Intent(this@MainActivity, MainActivity2::class.java)) }

        val abc = MaterialTapTargetPrompt.Builder(this@MainActivity).setTarget(findViewById<View>(R.id.textvw))
                .setPrimaryText("No Auto Dismiss")
            .setPromptBackgroundClickable(true)
                .setSecondaryText("This prompt will only be removed after tapping the envelop")
                .setAnimationInterpolator(FastOutSlowInInterpolator())
                .setPromptStateChangeListener { prompt, state ->
                    if (state === MaterialTapTargetPrompt.STATE_BACK_BUTTON_PRESSED) {
                        Log.e("utkarsh ", "innside " + 1)
                    } else if (state === MaterialTapTargetPrompt.STATE_DISMISSED) {
                        Log.e("utkarsh ", "innside " + 2)
                    } else if (state === MaterialTapTargetPrompt.STATE_DISMISSING) {
                        Log.e("utkarsh ", "innside " + 3)
                    } else if (state === MaterialTapTargetPrompt.STATE_FINISHED) {
                        Log.e("utkarsh ", "innside " + 4)
                    } else if (state === MaterialTapTargetPrompt.STATE_FINISHING) {
                        Log.e("utkarsh ", "innside " + 5)
                    } else if (state === MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                        Log.e("utkarsh ", "innside " + 6)
                    } else if (state === MaterialTapTargetPrompt.STATE_REVEALED) {
                        Log.e("utkarsh ", "innside " + 7)
                    } else if (state === MaterialTapTargetPrompt.STATE_NOT_SHOWN) {
                        Log.e("utkarsh ", "innside " + 8)
                    } else if (state === MaterialTapTargetPrompt.STATE_REVEALING) {
                        Log.e("utkarsh ", "innside " + 9)
                    } else if (state === MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                        Log.e("utkarsh ", "innside " + 10)
                        startActivity(Intent(this, MainActivity2::class.java))
                    } else if (state === MaterialTapTargetPrompt.STATE_SHOW_FOR_TIMEOUT) {
                        Log.e("utkarsh ", "innside " + 11)
                    }
                }
            .show()
    }
}
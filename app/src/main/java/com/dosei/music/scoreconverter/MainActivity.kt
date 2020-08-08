package com.dosei.music.scoreconverter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dosei.music.scoreconverter.converter.ScoreConverterFragment

class MainActivity : AppCompatActivity() {

    private var scoreConverterFragment: ScoreConverterFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        savedInstanceState?.let {
            scoreConverterFragment = supportFragmentManager.findFragmentByTag(
                SCORE_CONVERTER_TAG
            ) as? ScoreConverterFragment
        } ?: initConverter()
    }

    private fun initConverter() {
        val fragment = ScoreConverterFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment, SCORE_CONVERTER_TAG)
            .commit()
        scoreConverterFragment = fragment
    }

    companion object {
        private const val SCORE_CONVERTER_TAG = "ScoreConverter"
    }
}

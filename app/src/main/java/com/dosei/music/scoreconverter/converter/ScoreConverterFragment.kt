package com.dosei.music.scoreconverter.converter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.dosei.music.scoreconverter.R
import com.dosei.music.scoreconverter.ui.view.ScoreFragment
import kotlinx.android.synthetic.main.fragment_score_converter.*
import org.koin.android.viewmodel.ext.android.viewModel

class ScoreConverterFragment : Fragment(),
    ScoreFragment.OnPositionChangedListener {

    private val viewModel by viewModel<ScoreConverterViewModel>()
    private lateinit var scoreFragment: com.dosei.music.scoreconverter.ui.view.ScoreFragment
    private lateinit var tablatureFragment: com.dosei.music.scoreconverter.ui.view.TablatureFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_score_converter, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initChildFragments()

        sharp_button.setOnClickListener { viewModel.onSharpClicked() }
        flat_button.setOnClickListener { viewModel.onFlatClicked() }
        scoreFragment.onPositionChangedListener = this

        viewModel.apply {
            lifecycle.addObserver(this)
            currentNote.observe(
                this@ScoreConverterFragment,
                Observer { updateNote(it) }
            )
            progressMax.observe(
                this@ScoreConverterFragment,
                Observer { scoreFragment.maxPosition = it }
            )
            savedInstanceState?.getInt(STATE_KEY_PROGRESS)?.let {
                onSavedIndexRetrieved(it)
            }
            sharpHighlight.observe(
                this@ScoreConverterFragment,
                Observer { sharp_button.setImageResource(if (it) R.drawable.ic_sharp_active else R.drawable.ic_sharp_black) }
            )
            flatHighlight.observe(
                this@ScoreConverterFragment,
                Observer { flat_button.setImageResource(if (it) R.drawable.ic_flat_active else R.drawable.ic_flat_black) }
            )
            noteDecoration.observe(
                this@ScoreConverterFragment,
                Observer { scoreFragment.noteDecoration = it }
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(STATE_KEY_PROGRESS, scoreFragment.notePosition ?: 0)
    }

    private fun initChildFragments() {
        scoreFragment = com.dosei.music.scoreconverter.ui.view.ScoreFragment()
        tablatureFragment =
            com.dosei.music.scoreconverter.ui.view.TablatureFragment()

        activity?.supportFragmentManager?.run {
            beginTransaction()
                .replace(R.id.notes_container, scoreFragment, "Score")
                .commit()

            beginTransaction()
                .replace(R.id.tablature_container, tablatureFragment, "Tablature")
                .commit()
        }
    }

    private fun updateNote(note: CurrentNote?) {
        note?.run {
            text_current_note.text = getString(R.string.current_note, name)
            scoreFragment.notePosition = scorePosition
            tablatureFragment.positions = tablaturePositions
        }
    }

    companion object {
        private const val STATE_KEY_PROGRESS = "progress"
    }

    override fun onScorePositionChanged(position: Int) {
        viewModel.onScorePositionUpdated(position)
    }
}
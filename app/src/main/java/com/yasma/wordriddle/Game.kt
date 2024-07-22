package com.yasma.wordriddle

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.yasma.wordriddle.Utils.WordleGame

class Game : AppCompatActivity() {
    private lateinit var wordleGame: WordleGame
    private var win:Boolean=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        wordleGame = WordleGame(this)
        wordleGame.setWord()
        setupKeyboardListeners()
    }

    private fun setupKeyboardListeners() {
        val keyIds = listOf(
            R.id.key_Q, R.id.key_W, R.id.key_E, R.id.key_R, R.id.key_T, R.id.key_Y, R.id.key_U,
            R.id.key_I, R.id.key_O, R.id.key_P, R.id.key_A, R.id.key_S, R.id.key_D, R.id.key_F,
            R.id.key_G, R.id.key_H, R.id.key_J, R.id.key_K, R.id.key_L, R.id.key_Z, R.id.key_X,
            R.id.key_C, R.id.key_V, R.id.key_B, R.id.key_N, R.id.key_M
        )

        for (id in keyIds) {
            findViewById<TextView>(id).setOnClickListener { view ->
                val letter = (view as TextView).text.toString()
                if(!win){
                wordleGame.addLetter(letter)
                handleLetterInput(letter)
                }
                else{
                    Toast.makeText(this, "You Win ", Toast.LENGTH_SHORT).show()
                }
            }
        }

        findViewById<TextView>(R.id.key_ENTER).setOnClickListener {
            handleEnterKey()
        }

        findViewById<ImageButton>(R.id.key_DELETE).setOnClickListener {
            handleDeleteKey()
        }
    }

    private fun handleLetterInput(letter: String) {
        println(" CLICK :$letter")
    }

    private fun handleEnterKey() {
        val alcrrect=  wordleGame.submitWord()
        if(alcrrect){
          win=true
        }else{

        }
        // Handle enter key press
    }

    private fun handleDeleteKey() {
        wordleGame.deleteLetter()
        // Handle delete key press
    }
}
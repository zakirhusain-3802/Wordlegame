package com.yasma.wordsriddles

import FireworksView
import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.yasma.wordsriddles.Utils.WordleGame

class Game : AppCompatActivity() {
    private lateinit var wordleGame: WordleGame
    private var win:Boolean=false
    private var attempts: Int = 0
    private var maxAttempts: Int = 6
    private val handler = Handler(Looper.getMainLooper())
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
        setupButtons()
    }
    private fun setupButtons() {
        findViewById<ImageButton>(R.id.restart).setOnClickListener {
            showRestartConfirmationDialog()
        }

        findViewById<ImageButton>(R.id.rules).setOnClickListener {
            showRulesBottomSheet()
        }
    }

    private fun showRestartConfirmationDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.restart_game)
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        val btnPlayAgain = dialog.findViewById<Button>(R.id.restart)
        val btnClose = dialog.findViewById<Button>(R.id.cancel)

        btnPlayAgain.setOnClickListener {
            dialog.dismiss()
            restartGame()
        }

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun restartGame() {
        attempts = 0
         maxAttempts = 7
        win = false
        wordleGame = WordleGame(this)
        wordleGame.setWord()
        // Clear the game board
        for (row in 0..5) {
            for (col in 0..4) {
                val tileId = resources.getIdentifier("tile_${row}_${col}", "id", packageName)
                findViewById<TextView>(tileId).apply {
                    text = ""
                    setBackgroundResource(R.drawable.tile) // Assuming you have a default tile background
                }
            }
        }
        for (col in 0..4) {
            val tileId = resources.getIdentifier("tile_${col}", "id", packageName)
            findViewById<TextView>(tileId).apply {
                text = ""
                setBackgroundResource(R.drawable.tile_guess) // Assuming you have a default tile background
            }
        }
        val keyIds = listOf(
            R.id.key_Q, R.id.key_W, R.id.key_E, R.id.key_R, R.id.key_T, R.id.key_Y, R.id.key_U,
            R.id.key_I, R.id.key_O, R.id.key_P, R.id.key_A, R.id.key_S, R.id.key_D, R.id.key_F,
            R.id.key_G, R.id.key_H, R.id.key_J, R.id.key_K, R.id.key_L, R.id.key_Z, R.id.key_X,
            R.id.key_C, R.id.key_V, R.id.key_B, R.id.key_N, R.id.key_M
        )

        for (id in keyIds) {
            findViewById<TextView>(id).setBackgroundResource(R.drawable.key_button)
        }
        // Reset keyboard colors if needed
        // ...
    }

    @SuppressLint("MissingInflatedId")
    private fun showRulesBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.activity_rules, null)
        bottomSheetDialog.setContentView(view)

        val backButton=view.findViewById<ImageButton>(R.id.backButton)

        backButton.setOnClickListener { bottomSheetDialog.dismiss() }
        bottomSheetDialog.show()

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

        val guessWord=wordleGame.getGuessWord()
        val isValid=wordleGame.isWordValid(guessWord)
        if(isValid){
        if (attempts < maxAttempts) {
            val allCorrect = wordleGame.submitWord()
            attempts++

            if (allCorrect) {
                win = true
                showWinDialog()
            } else if (attempts == maxAttempts) {
                showLoseDialog()
            }
        }
        }else{

            if(guessWord.length!=5){
                Toast.makeText(this, "Please enter 5 letter word", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Not a valid word ", Toast.LENGTH_SHORT).show()
            }

        }
        // Handle enter key press
    }

    private fun handleDeleteKey() {
        wordleGame.deleteLetter()
        // Handle delete key press
    }
    private fun showWinDialog() {

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_win)
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        val textView=dialog.findViewById<TextView>(R.id.guessWord)
        val textView1=dialog.findViewById<TextView>(R.id.guessWord1)
        val btnPlayAgain = dialog.findViewById<Button>(R.id.playAgain)
        val btnClose = dialog.findViewById<Button>(R.id.goBack)
        val word=wordleGame.getTargetWord()
        textView.text=word
        textView1.text=word
        btnPlayAgain.setOnClickListener {

            dialog.dismiss()
            restartGame()
        }

        btnClose.setOnClickListener {
            dialog.dismiss()

        }

        dialog.show()

    }

    private fun showLoseDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_lose)
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        val btnPlayAgain = dialog.findViewById<Button>(R.id.Save)
        val btnClose = dialog.findViewById<Button>(R.id.goBackLose)


        btnPlayAgain.setOnClickListener {
            dialog.dismiss()
            restartGame()
        }

        btnClose.setOnClickListener {
            dialog.dismiss()

        }
        dialog.show()
    }

}
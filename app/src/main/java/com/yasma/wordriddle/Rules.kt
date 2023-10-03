package com.yasma.wordriddle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.TextView

class Rules : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rules)

        val actionbar = supportActionBar
        var txt1 = "<b>F </b>is in the word and in the correct spot."
        val txt2 = "<b>R </b>is in the word but in the wrong spot"
        val txt3 = " <b> Any</b> letter are not in the word in any spot."

        val tx1 = findViewById<TextView>(R.id.textView5)
        val tx2: TextView = findViewById(R.id.textView6)
        val tx3: TextView = findViewById(R.id.textView7)
        tx1.setText(Html.fromHtml(txt1))
        tx2.setText(Html.fromHtml(txt2))
        tx3.setText(Html.fromHtml(txt3))

    }




    //set actionbar title



override fun onSupportNavigateUp(): Boolean {
    onBackPressed()
    return true
}
}
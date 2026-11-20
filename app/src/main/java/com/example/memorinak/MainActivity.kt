package com.example.memorinak

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var firstCard: ImageView? = null
    private var isBusy = false
    private var pairsFound = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        }

        val rowParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            0,
            1f
        )
        val cardParams = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.MATCH_PARENT,
            1f
        ).apply {
            setMargins(8, 8, 8, 8)
        }

        val cardImages = listOf(
            R.drawable.card_1,
            R.drawable.card_2,
            R.drawable.card_3,
            R.drawable.card_4,
            R.drawable.card_5,
            R.drawable.card_6,
            R.drawable.card_7,
            R.drawable.card_8
        )
        val deck = (cardImages + cardImages).shuffled()

        val cardViews = ArrayList<ImageView>()
        for (i in 0 until 16) {
            cardViews.add(
                ImageView(this).apply {
                    setImageResource(R.drawable.card_back)
                    layoutParams = cardParams
                    tag = deck[i]
                    setOnClickListener(colorListener)
                }
            )
        }

        val rows = Array(4) {
            LinearLayout(this).apply {
                layoutParams = rowParams
                orientation = LinearLayout.HORIZONTAL
            }
        }

        var count = 0
        for (view in cardViews) {
            val row = count / 4
            rows[row].addView(view)
            count++
        }

        for (row in rows) {
            layout.addView(row)
        }

        setContentView(layout)
    }

    private val colorListener = View.OnClickListener { v ->
        val card = v as? ImageView ?: return@OnClickListener
        if (isBusy || card.visibility != View.VISIBLE || card == firstCard) return@OnClickListener

        val resId = card.tag as? Int ?: return@OnClickListener
        card.setImageResource(resId)

        if (firstCard == null) {
            firstCard = card
        } else {
            val secondCard = card
            val first = firstCard!!
            isBusy = true

            lifecycleScope.launch(Dispatchers.Main) {
                delay(1000)
                if (first.tag == secondCard.tag) {
                    first.visibility = View.INVISIBLE
                    secondCard.visibility = View.INVISIBLE
                    first.isClickable = false
                    secondCard.isClickable = false
                    pairsFound++
                    if (pairsFound == 8) {
                        Toast.makeText(this@MainActivity, "Победа! Все пары найдены!", Toast.LENGTH_LONG).show()
                    }
                } else {
                    first.setImageResource(R.drawable.card_back)
                    secondCard.setImageResource(R.drawable.card_back)
                }
                firstCard = null
                isBusy = false
            }
        }
    }
}

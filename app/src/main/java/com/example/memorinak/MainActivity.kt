package com.example.memorinak

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

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

    private suspend fun openCard(v: View) {
        if (v !is ImageView) return
        val resId = v.tag as? Int ?: return
        v.isClickable = false
        v.setImageResource(resId)
        delay(1000)
        v.setImageResource(R.drawable.card_back)
        v.isClickable = true
    }

    private val colorListener = View.OnClickListener { v ->
        lifecycleScope.launch(Dispatchers.Main) {
            openCard(v)
        }
    }
}

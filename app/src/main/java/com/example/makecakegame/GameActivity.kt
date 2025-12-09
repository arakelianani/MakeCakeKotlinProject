package com.example.makecakegame

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.TextView

class GameActivity : AppCompatActivity() {
    private lateinit var bowl: ImageView
    private lateinit var cake: ImageView
    private lateinit var sad: ImageView
    private lateinit var milk: ImageView
    private lateinit var eggs: ImageView
    private lateinit var sugar: ImageView
    private lateinit var salt: ImageView
    private lateinit var broccoli: ImageView
    private lateinit var carrot: ImageView
    private lateinit var apple: ImageView
    private lateinit var fruits: ImageView
    private lateinit var ingredients: List<ImageView>
    private lateinit var cakeText: TextView
    private lateinit var restartButton: Button


    private var dx = 0f
    private var dy = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bowl = findViewById(R.id.bowl)
        cake = findViewById(R.id.cake)
        sad = findViewById(R.id.sad)
        milk = findViewById(R.id.milk)
        eggs = findViewById(R.id.eggs)
        sugar = findViewById(R.id.sugar)
        salt = findViewById(R.id.salt)
        broccoli = findViewById(R.id.broccoli)
        carrot = findViewById(R.id.carrot)
        apple = findViewById(R.id.apple)
        fruits = findViewById(R.id.fruits)
        cakeText = findViewById(R.id.cakeText)
        restartButton = findViewById(R.id.restartButton)


        //vorpisi 1 ingridient sharjvi
//        milk.x=-milk.width.toFloat()
//        milk.animate()
//            .x(screenWidth)
//            .setDuration(4000)
//            .start()

        val screenWidth = resources.displayMetrics.widthPixels.toFloat()
        ingredients = listOf(milk, eggs, sugar, salt, fruits, broccoli, carrot, apple)
        val wrongIngredients = listOf(broccoli, carrot, apple)
        val collected = mutableSetOf<View>()


        for ((index, ingredient) in ingredients.withIndex()) {
            ingredient.y = 50f + index * 50f
            val duration = 3000L + index * 300L
            moveIngredients(ingredient, screenWidth, duration)
        }

        for (ingredient in ingredients) {
            ingredient.setOnTouchListener { view, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        dx = view.x - event.rawX
                        dy = view.y - event.rawY
                        view.animate().cancel()
                    }

                    MotionEvent.ACTION_MOVE -> {
                        view.animate()
                            .x(event.rawX + dx)
                            .y(event.rawY + dy)
                            .setDuration(0)
                            .start()
                    }

                    MotionEvent.ACTION_UP -> {
                        if (isViewOverlapping(view, bowl)) {
                            view.x = bowl.x
                            view.y = bowl.y
                            collected.add(view)
                            if (wrongIngredients.contains(view)) {
                                gameOver()
                            }
                            if (collected.containsAll(ingredients - wrongIngredients)) {
                                showCake()
                            }
                        }
                    }
                }
                true
            }
        }
    }

    private fun moveIngredients(view: ImageView, screenWidth: Float, duration: Long) {
        if (view.visibility != View.VISIBLE) return
        view.x = -view.width.toFloat()   //drvuma ekrani dzax masum
        view.animate()
            .x(screenWidth) //minchev vortex gna
            .setDuration(duration)
            .withEndAction {
                // rekursia hasnelov sahmanin sksvuma noric
                moveIngredients(view, screenWidth, duration)
            }
            .start()
    }

    private fun isViewOverlapping(view1: View, view2: View): Boolean {
        val rect1 = Rect()
        val rect2 = Rect()
        view1.getHitRect(rect1) //stanuma viewi chapy uxxankyunov
        view2.getHitRect(rect2)
        return Rect.intersects(rect1, rect2) //stuguma hatumy
    }

    fun showCake() {
       stopMoving()
        bowl.visibility=View.GONE
        cake.alpha = 0f
        cake.scaleX = 0f
        cake.scaleY = 0f
        cake.visibility = View.VISIBLE
        cake.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(800)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
        cakeText.alpha = 0f
        cakeText.visibility = View.VISIBLE
        cakeText.animate()
            .alpha(1f)
            .setDuration(800)
            .start()
        restartButton.visibility = View.VISIBLE
        restartButton.setOnClickListener {
            restartGame()
        }
    }

    private fun gameOver() {
        bowl.visibility=View.GONE
        stopMoving()
        sad.alpha = 0f
        sad.scaleX = 0f
        sad.scaleY = 0f
        sad.visibility = View.VISIBLE
        sad.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(800)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
        cakeText.text = "Cake Is Spoiled"
        cakeText.visibility = View.VISIBLE
        cakeText.alpha = 0f
        cakeText.animate()
            .alpha(1f)
            .setDuration(1000)
            .start()
        restartButton.visibility = View.VISIBLE
        restartButton.setOnClickListener {
            restartGame()
        }
    }

    private fun restartGame() {
        val intent = intent
        finish()
        startActivity(intent)
    }
    private fun stopMoving(){
        for (ingredient in ingredients) {
            ingredient.animate().cancel()
            ingredient.visibility = View.GONE
        }
    }
}




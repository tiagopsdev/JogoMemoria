package br.com.tps.jogomemoria

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.MotionEvent
import kotlin.random.Random

class FirstScreen(val  game: Game) : Screen(game) {

    private val drawPaint = Paint()
    private val textPaint = Paint()
    private val rnd = Random
    private var changeTime = 0f
    private var x = canvas.width / 2f
    private var y = canvas.height / 2f
    private var w = 400f
    private var h = 400f
    private val boundingBox: Rect

    init {
        drawPaint.color = Color.BLACK
        textPaint.typeface = Fonts.chalkboard
        textPaint.color = Color.BLUE
        textPaint.textAlign = Paint.Align.CENTER
        boundingBox = Rect((x - w/2).toInt(), (y - h/2).toInt(), (x + w/2).toInt(), (y + h/2).toInt())
    }

    override fun checkIfWin(elapsedTime: Float) {

    }

    override fun update(elapsedTime: Float) {
        changeTime += elapsedTime
        if (changeTime > 1000) {
            changeTime = 0f
            drawPaint.color = Color.rgb(
                rnd.nextInt(256),
                rnd.nextInt(256),
                rnd.nextInt(256)
            )
        }
    }

    override fun draw() {
        canvas.drawRGB(255, 255, 255)
        canvas.drawRect(boundingBox, drawPaint)
        textPaint.textSize = 100f
        canvas.drawText("Jogo da Memória", canvas.width/2f, 120f, textPaint)
        textPaint.textSize = 50f
        canvas.drawText("(Toque no quadrado para Começar...)", canvas.width/2f, 200f, textPaint)
    }

    override fun handleEvent(event: Int, x: Float, y: Float) {
        if (event == MotionEvent.ACTION_DOWN) {
            if (boundingBox.contains(x.toInt(), y.toInt())) {
                game.actualScreen = SquareImagesScreen(game)
            }
        }
    }

    override fun onResume() {

    }

    override fun onPause() {

    }

    override fun backPressed() {

    }
}
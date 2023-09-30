package br.com.tps.jogomemoria

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import android.view.MotionEvent

class LastScreen(val  game: Game) : Screen(game) {

    private val btnRestartPaint = Paint()
    private val drawPaint = Paint()
    private val textPaint = Paint()
    private val btnRestartTextPaint = Paint()
    private val textPaintFixed = Paint()
    private val boundingBox: Rect
    private val btnRestart: Rect

    private var drawMainColor = Color.BLACK
    private var texColor = Color.RED
    private var changeTime = 0f
    private var x = canvas.width / 2f
    private var y = canvas.height / 2f
    private var w = 400f
    private var h = 400f



    init {
        textPaintFixed.typeface = Fonts.chalkboard
        textPaintFixed.color = Color.BLACK
        textPaintFixed.textAlign = Paint.Align.CENTER

        drawPaint.color = drawMainColor

        textPaint.typeface = Fonts.chalkboard
        textPaint.color = texColor
        textPaint.textAlign = Paint.Align.CENTER

        btnRestartTextPaint.typeface = Fonts.chalkboard
        btnRestartTextPaint.color = texColor
        btnRestartTextPaint.textAlign = Paint.Align.CENTER
        btnRestartTextPaint.textSize = 100f

        boundingBox = Rect((x - w/2).toInt(), (y - h/2).toInt(), (x + w/2).toInt(), (y + h/2).toInt())

        btnRestart = Rect((x - 200).toInt(), (canvas.height - 10), ((x + 200).toInt()), (canvas.height - 210))
        btnRestartPaint.color = Color.BLACK
    }

    override fun checkIfWin(elapsedTime: Float) {

    }

    override fun update(elapsedTime: Float) {
        changeTime += elapsedTime
        if (changeTime > 1000) {
            changeTime = 0f

            if (drawMainColor == Color.RED){
                drawMainColor = Color.BLACK
                texColor =  Color.RED
            }else{
                drawMainColor = Color.RED
                texColor = Color.BLACK
            }
            drawPaint.color = drawMainColor
            textPaint.color = texColor

        }
    }

    override fun draw() {
        canvas.drawRGB(255, 255, 255)
        canvas.drawRect(boundingBox, drawPaint)
        textPaintFixed.textSize = 100f
        canvas.drawText("Fim de Jogo", canvas.width/2f, canvas.height/2f - 320f, textPaintFixed)
        textPaintFixed.textSize = 100f
        canvas.drawText("Movimentos", canvas.width/2f, canvas.height/2f - 210f, textPaintFixed)
        textPaint.textSize = 300f
        var result =  String.format("%02d", GameStatistics.moves)
        canvas.drawText(result, canvas.width/2f, canvas.height/2f + 110f, textPaint)

        canvas.drawRect(btnRestart, btnRestartPaint)
        canvas.drawText("Voltar", w + 125, (canvas.height - 30).toFloat(), btnRestartTextPaint)
    }

    override fun handleEvent(event: Int, x: Float, y: Float) {
        if (event == MotionEvent.ACTION_DOWN) {
            Log.i("Toques", "X${x.toInt()}, y${y.toInt()}")
            Log.i("Toques", "B${btnRestart.bottom}, T${btnRestart.top}, L${btnRestart.left}, R${btnRestart.right}")
            Log.i("Toques", "${btnRestart.contains(x.toInt(), y.toInt())}")
            if (btnRestart.contains(x.toInt(), y.toInt()) || (btnRestart.left <= x.toInt() && x.toInt() > btnRestart.right || btnRestart.bottom < y.toInt() && y.toInt() <= btnRestart.top) ) {
                game.actualScreen = FirstScreen(game)
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
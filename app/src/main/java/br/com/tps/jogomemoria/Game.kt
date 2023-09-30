package br.com.tps.jogomemoria

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import kotlin.math.abs

class Game(val context: Context) {

    //Define o Bitmap onde vou desenhar
    var buffer: Bitmap
    var render: Render
    var actualScreen: Screen? = null
    //variavaie para calculo da tela

    //Novas Alturas e Larguras
    private var nWid = 0f
    private var nHei = 0f
    //espaço preto quando aplicavel
    private var hDist = 0f
    private var vDist = 0f
    //fator de escala
    private var sx = 0f
    private var sy = 0f

    init {
        //Define orientação
        val isLandScape = context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        //Define Largura e Altura
        val bufferWidth = if(isLandScape) 1800f else 1080f
        val bufferHeight = if(isLandScape) 1080f else 1800f
        //DAdo a altura e largura de acordo com a orientacao, cria o area da tela de jogo
        buffer = Bitmap.createBitmap(bufferWidth.toInt(), bufferHeight.toInt(), Bitmap.Config.ARGB_8888)
        //Obtem o Tamanho da tela do celular
        val screenWidth = context.resources.displayMetrics.widthPixels.toFloat()
        val screenHeight = context.resources.displayMetrics.heightPixels.toFloat()
        //aspecto da resololuçao Ex.: 3x4 6x9 16x9
        val aspectBuffer = bufferWidth / bufferHeight
        val aspectScreen = screenWidth / screenHeight
        /*
        Area    Largura	Altura	Aspecto
        Buffer	1080	1800	0,6
        Tela	1080	2220	0,4864864865
        Quanto maior, siginifica que Largura mais próximo da altura
        Assume-se que mais gordo então largura é a mesma e ajusta na altura
        Quanto menor significa que é mais fino
        então se ajusta a largura com  relação da altura
        */
        if (aspectBuffer > aspectScreen || abs(aspectScreen - aspectBuffer) < 0.01){
            this.nWid = screenWidth
            val fAsp = aspectScreen / aspectBuffer //Propporção entre aspectScrren e aspecrBuffer
            this.nHei = screenHeight * fAsp
            this.vDist = (screenHeight - nHei) / 2f // Calcula metade da obra que vai um para cima e outro para baixo
        }else{

            this.nHei = screenHeight
            this.nWid = nHei * aspectBuffer
            this.hDist = (screenWidth - nWid) / 2f
        }

        //Depois de feito os cauculos deve ser callcular o fator de escala que será
        //nova largura / tamanho do buffer
        sx = nWid / bufferWidth
        sy = nHei / bufferHeight
        ScreenLimits.limitWidth = this.nWid
        ScreenLimits.limitHeight = this.nHei
        render = Render(context, buffer)
        render.setOnTouchListener(render)
    }

    inner class Render(context: Context, private val buffer: Bitmap) : View(context),
        OnTouchListener {

        private var startTime = System.nanoTime()
        private var paint = Paint()

        init {
            paint.isAntiAlias = true
            paint.isFilterBitmap = true
            paint.isDither = true

        }

        override fun draw(canvas: Canvas?) {
            super.draw(canvas)

            val src = Rect(0, 0, buffer.width, buffer.height)
            val dst = Rect(hDist.toInt(), vDist.toInt(), (nWid + hDist).toInt(), (nHei + vDist).toInt())

            val deltaTime = (System.nanoTime() - startTime) / 1000000f


            startTime = System.nanoTime()

            actualScreen?.update(deltaTime)
            actualScreen?.draw()
            actualScreen?.checkIfWin(deltaTime)

            canvas?.let {
                it.drawRGB(0, 0, 0)
                it.drawBitmap(buffer, src, dst, paint)
            }

            invalidate()
        }

        override fun onTouch(v: View?, event: MotionEvent?): Boolean {

            event?.let {
                val x = (it.x - hDist) / sx
                val y = (it.y - vDist) / sy
                actualScreen?.handleEvent(it.action, x, y)
            }


            return true
        }


    }
    fun onResume(){

        actualScreen?.onResume()

    }

    fun onPause(){
        actualScreen?.onPause()
    }

    fun BackPressed(){

        actualScreen?.backPressed()


    }



}
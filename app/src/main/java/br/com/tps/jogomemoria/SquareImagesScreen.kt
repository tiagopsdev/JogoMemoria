package br.com.tps.jogomemoria

import android.util.Log
import android.view.MotionEvent

class SquareImagesScreen(val game: Game): Screen(game) {


    private var music: Music? = null
    private var squareImages = SquareImages(game.context, 20f, 40f)

    init {
        try {
            val descriptor = game.context.assets.openFd("metalgameon.mp3")
            music = Music(descriptor)
        }
        catch (e: Exception) {
            Log.d("Jogo2", e.localizedMessage)
        }
        music?.setLooping(true)
        music?.setVolume(1f)
        music?.play()
    }

    override fun checkIfWin(elapsedTime: Float){

        if (squareImages.checkIfWin(elapsedTime)){

            music?.pause()
            music?.dispose()
            game.actualScreen = LastScreen(game)

        }


    }
    override fun update(elapsedTime: Float) {
        squareImages.update(elapsedTime)
    }

    override fun draw() {
        canvas.drawRGB(255, 255, 255)
        squareImages.draw(canvas)
    }

    override fun handleEvent(event: Int, x: Float, y: Float) {
        if (event == MotionEvent.ACTION_DOWN) {
            squareImages.verifyWasCaught(x, y)
        }

    }

    override fun onResume() {

    }

    override fun onPause() {

        music?.pause()
        music?.dispose()

    }

    override fun backPressed() {

        music?.pause()
        music?.dispose()
        game.actualScreen = FirstScreen(game)

    }
}
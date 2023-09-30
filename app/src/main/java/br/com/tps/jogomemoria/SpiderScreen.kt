package br.com.tps.jogomemoria

import android.util.Log
import android.view.MotionEvent

class SpiderScreen(val game: Game): Screen(game) {


    private var music: Music? = null
    private var spider = Spider(game.context, 0f, 0f)
    //private var spider = Spider(game.context, canvas.width/2f, 100f)

    init {
        try {
            val descriptor = game.context.assets.openFd("music.mp3")
            music = Music(descriptor)
        }
        catch (e: Exception) {
            Log.d("Jogo2", e.localizedMessage)
        }
        music?.setLooping(true)
        music?.setVolume(1f)
        music?.play()
    }

    override fun checkIfWin(elapsedTime: Float) {

    }

    override fun update(elapsedTime: Float) {
        spider.update(elapsedTime)
    }

    override fun draw() {
        canvas.drawRGB(255, 255, 255)
        spider.draw(canvas)
    }

    override fun handleEvent(event: Int, x: Float, y: Float) {
        if (event == MotionEvent.ACTION_DOWN) {
            if (!spider.verifyWasCaught(x, y)) {
                spider.changeDir()
            }
        }
        else if (event == MotionEvent.ACTION_MOVE) {
            if (spider.caught) {
                spider.moveTo(x, y)
            }
        }
        else if (event == MotionEvent.ACTION_UP) {
            spider.wasReleased()
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
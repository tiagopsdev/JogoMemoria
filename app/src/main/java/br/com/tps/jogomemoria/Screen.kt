package br.com.tps.jogomemoria

import android.graphics.Canvas

abstract class Screen(game: Game) {

    protected val canvas = Canvas(game.buffer)

    abstract fun checkIfWin(elapsedTime: Float)
    abstract fun update(elapsedTime: Float)
    abstract fun draw()
    abstract fun handleEvent(event: Int, x: Float, y: Float)
    abstract fun onResume()
    abstract fun onPause()
    abstract fun backPressed()
}
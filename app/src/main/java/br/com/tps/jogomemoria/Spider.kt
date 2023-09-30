package br.com.tps.jogomemoria

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import android.util.Log

class Spider(context: Context, var x: Float, var y: Float) {

    companion object {
        const val WIDTH = 96
        const val HEIGHT = 96
    }

    private lateinit var image: Bitmap
    private val src = Rect()
    private val dst = Rect()
    private val vel = 220.0f
    private var frame = 0
    private var dir = 0
    private var animTime = 0f
    private var changeTime = 200.0f;

    var caught = false

    init {
        try {
            val inputStream = context.assets.open("spider.png")
            image = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
        }
        catch (e: Exception) {
            Log.d("Jogo!", e.localizedMessage)
        }
    }

    fun update(elapsedTime: Float) {
        animTime += elapsedTime
        if (animTime > changeTime) {
            animTime = 0f
            frame = if (frame == 3) 0 else frame + 1
            src.set(frame * WIDTH, dir * HEIGHT, frame * WIDTH + WIDTH, dir * HEIGHT + HEIGHT)
        }
        if (!caught) {
            when (dir) {
                0 -> y += vel * elapsedTime / 1000f
                1 -> x -= vel * elapsedTime / 1000f
                2 -> x += vel * elapsedTime / 1000f
                3 -> y -= vel * elapsedTime / 1000f
            }
        }
        dst.set((x - WIDTH/2 * 4).toInt(), (y - HEIGHT/2 * 4).toInt(), (x + WIDTH/2 * 4).toInt(), (y + HEIGHT/2 * 4).toInt())
    }

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(image, src, dst, null)
    }

    fun moveTo(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    fun verifyWasCaught(x: Float, y: Float): Boolean {
        if (dst.contains(x.toInt(), y.toInt())) {
            caught = true
            changeTime = 40f
            return true
        }
        return false
    }

    fun wasReleased() {
        caught = false
        changeTime = 200f
    }

    fun changeDir() {
        dir++
        if (dir > 3) {
            dir = 0
        }
    }
}
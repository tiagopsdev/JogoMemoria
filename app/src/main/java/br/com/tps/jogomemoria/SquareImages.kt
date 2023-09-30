package br.com.tps.jogomemoria

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import android.util.Log
import java.io.InputStream

class SquareImages(context: Context, var x: Float, var y: Float) {

    companion object {
        const val WIDTH = 250
        const val HEIGHT = 250
    }


    private val lines = 6
    private val columns = 4
    private  val numTiles = (lines * columns) / 2
    private var squareSizes = 0
    private val mI = 10 //margeInternal
    private val mET = 50 //margeExternalTop
    //private val mELR = 50 //margeExternalLeftRight
    private var tilesList = mutableListOf<Tiles>()
    private var tileA: Int = -1
    private var tileB: Int = -1
    private var scale = 1f
    private var scaleTeste = 1f
    private var dir = 0
    private var animTime = 0f
    private var animHideTime = 0f
    private var animCheckTime = 0f
    private var animCheckWinTime = 0f
    private var changeTime = 200.0f
    private var hideTime = 10000.0f
    private var checkTime = 750.0f
    private var checkWinTime = 200.0f
    private var tilesReveled = 0
    private lateinit var backCoverImage: Bitmap
    private var hideAllTile = true
    private var startGame = false

    var caught = false

    init {
        while (((lines * ((WIDTH  * scaleTeste) + mI))) > ScreenLimits.limitWidth){

            scaleTeste *= 0.99f

        }
        Log.d("Teste","Tamanho da linha ${((lines * ((WIDTH  * scaleTeste) + mI)))}")
        Log.d("Teste","Largura da linha ${ScreenLimits.limitWidth}")
        mountTileList(context)
        try {
            val auxInputStream = context.assets.open("backcover.jpg")
            backCoverImage = BitmapFactory.decodeStream(auxInputStream)
            auxInputStream.close()
        }
        catch (e: Exception) {
            Log.d("Jogo!", e.localizedMessage)
        }
        GameStatistics.moves = 0
    }

    fun update(elapsedTime: Float) {



        if (hideAllTile){
            animHideTime += elapsedTime
            if (animHideTime > hideTime){

                hideAllTile = false

                for (i in 0 until tilesList.count()){
                    tilesList[i].reveal = false
                    tilesList[i].keepRevealed = false



                }
                startGame = true

            }
        }

        if (tilesReveled == 2){
            animCheckTime += elapsedTime
            if (animCheckTime > checkTime){
                checkIfMatch()
                tilesReveled = 0
                tileA = -1
                tileB = -1
                animCheckTime = 0f

            }
        }



        animTime += elapsedTime
        if (animTime > changeTime) {

            animTime = 0f

        }

    }

    fun draw(canvas: Canvas) {


        canvas.drawRGB(100,0,0)
        for (i in 0 until tilesList.count()){


            if (tilesList[i].reveal || tilesList[i].keepRevealed) {

                canvas.drawBitmap(tilesList[i].image, null, tilesList[i].dst, null)
            }else{
                canvas.drawBitmap(backCoverImage, null, tilesList[i].dst, null)
            }

        }
    }

    fun moveTo(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    fun verifyWasCaught(x: Float, y: Float) {

        if (tilesReveled == 1 || tilesReveled == 0 ){

            for (i in 0 until tilesList.count()){

                if(tilesList[i].dst.contains(x.toInt(), y.toInt())){

                    tilesList[i].reveal = true
                    if(tilesReveled == 0) tileA = i else tileB = i
                    tilesReveled +=1
                    GameStatistics.moves++


                }

            }

        }


    }

    fun checkIfMatch(){

        if (tilesList[tileA].image.sameAs(tilesList[tileB].image)){
            tilesList[tileA].keepRevealed = true
            tilesList[tileB].keepRevealed = true
        }else{

            tilesList[tileA].reveal = false
            tilesList[tileB].reveal = false

        }

    }

    fun checkIfWin(elapsedTime: Float): Boolean {

        if (startGame){

            animCheckWinTime += elapsedTime
            if (animCheckWinTime > checkWinTime) {
                for (tile in tilesList){
                    if (!tile.keepRevealed){return false}
                }

                animCheckWinTime = 0f
                return true

            }

        }
        return false

    }


    private fun getNdstList(): ArrayList<Rect> {

        var auxPosX: Float
        var auxPosY = y+mET
        val ndst: ArrayList<Rect> = arrayListOf()

        for (l in 1..lines) {
            auxPosX = x
            for (c in 1..columns) {

                ndst += Rect((auxPosX).toInt(), (auxPosY).toInt(), (auxPosX + (WIDTH * scale)).toInt(), (auxPosY + (HEIGHT*scale)).toInt())
                auxPosX += (WIDTH * scale)+mI


            }
            auxPosY += (HEIGHT*scale)+mI
        }
        return ndst

    }

    private fun getImageList(context: Context): MutableList<Bitmap>{

        var image: Bitmap
        val imageList = mutableListOf<Bitmap>()
        try {
            var inputStream: InputStream
            var z: String
            for(i in 1..numTiles){
                if(i < 10){z = "0"}else{z = ""}
                inputStream = context.assets.open("${z}${i}.jpg")
                image = BitmapFactory.decodeStream(inputStream)
                imageList.add(image)
                imageList.add(image)
                imageList.shuffle()
                inputStream.close()
            }

        }
        catch (e: Exception) {
            Log.d("Jogo!", e.localizedMessage?.toString() ?: "inputStream")
        }
        return imageList
    }

    fun mountTile(imgTile: Bitmap, dst: Rect) = Tiles(
        imgTile,
        dst,
        reveal = true,
        keepRevealed = true
    )
    fun mountTileList(context: Context){

        val imageList = getImageList(context)
        val dstList = getNdstList()
        val count = imageList.count()

        if (imageList.count() != dstList.count()){ throw IllegalArgumentException() }
        for(i in 0 until count){

            tilesList += mountTile(imageList[i], dstList[i])

        }



    }




}
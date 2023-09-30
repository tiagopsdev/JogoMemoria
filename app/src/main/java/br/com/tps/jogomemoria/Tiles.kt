package br.com.tps.jogomemoria

import android.graphics.Bitmap
import android.graphics.Rect

data class Tiles(

    val image: Bitmap,
    val dst: Rect,
    var reveal: Boolean,
    var keepRevealed: Boolean,



    ){


}

package br.com.tps.jogomemoria

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager

class MainActivity : AppCompatActivity() {

    private var game: Game? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        Fonts.initializeFonts(this)
        game = Game(this).apply {
            setContentView(this.render)
            this.actualScreen = FirstScreen(this)
        }
    }

    override fun onResume() {
        super.onResume()

        game?.onResume()
    }

    override fun onPause() {
        super.onPause()

        game?.onPause()
    }

    override fun onBackPressed() {
        game?.BackPressed()
    }


}
package br.com.tps.jogomemoria

import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.util.Log

class Music(descriptor: AssetFileDescriptor) {

    private val mediaPlayer = MediaPlayer()
    private var isPrepared = false

    init {
        try {
            mediaPlayer.setDataSource(
                descriptor.fileDescriptor,
                descriptor.startOffset,
                descriptor.length
            )
            mediaPlayer.prepare()
            isPrepared = true
        }
        catch (e: Exception) {
            Log.d("Jogo2", e.localizedMessage)
        }
    }

    fun dispose() {
        if (mediaPlayer.isPlaying) mediaPlayer.stop()
        mediaPlayer.release()
    }

    fun pause() {
        if (mediaPlayer.isPlaying) mediaPlayer.pause()
    }

    fun play() {
        if (mediaPlayer.isPlaying) return
        try {
            if (!isPrepared) mediaPlayer.prepare()
            mediaPlayer.start()
        }
        catch (e: Exception) {
            Log.d("Jogo2", e.localizedMessage)
        }
    }

    fun stop() {
        mediaPlayer.stop()
        isPrepared = false
    }

    fun setLooping(isLooping: Boolean) {
        mediaPlayer.isLooping = isLooping
    }

    fun setVolume(volume: Float) {
        mediaPlayer.setVolume(volume, volume)
    }
}
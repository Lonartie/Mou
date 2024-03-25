package com.team.app.service

import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.RawRes

class SoundService(
    private val context: Context
) {
    private var mediaPlayer: MediaPlayer? = null

    fun play(@RawRes res: Int) {
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
        }
        mediaPlayer = MediaPlayer.create(context, res)
        mediaPlayer?.start()
    }
}
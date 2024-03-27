package com.team.app.utils

import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.RawRes

class SoundManager(
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
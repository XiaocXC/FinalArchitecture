package com.xiaoc.feature_fluid_music.service

import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Intent
import android.os.Build
import androidx.core.app.TaskStackBuilder
import androidx.media3.common.AudioAttributes
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.xiaoc.feature_fluid_music.service.callback.MediaLibrarySessionCallbackImpl
import com.xiaoc.feature_fluid_music.ui.FluidMusicMainActivity

/**
 * @author Xiaoc
 * @since 2022-07-27
 */
class FluidMusicService: MediaLibraryService() {

    /**
     * 播放器实例
     */
    private lateinit var player: ExoPlayer

    /**
     * 媒体会话对象
     */
    private lateinit var mediaLibrarySession: MediaLibrarySession

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession? {
        return mediaLibrarySession
    }

    override fun onCreate() {
        super.onCreate()
        initializeSessionAndPlayer()
    }

    private fun initializeSessionAndPlayer(){
        player = ExoPlayer.Builder(this)
            .setAudioAttributes(AudioAttributes.DEFAULT,true)
            .build()

        // 创建PendingIntent用于点击通知栏跳转
        val sessionActivityPendingIntent = TaskStackBuilder.create(this).run {
                addNextIntent(Intent(this@FluidMusicService, FluidMusicMainActivity::class.java))
//                addNextIntent(Intent(this@FluidMusicService, PlayerActivity::class.java))

                val immutableFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) FLAG_IMMUTABLE else 0
                getPendingIntent(0, immutableFlag or FLAG_UPDATE_CURRENT)
            }

        mediaLibrarySession =
            MediaLibrarySession.Builder(this, player, MediaLibrarySessionCallbackImpl(player, this))
                .setSessionActivity(sessionActivityPendingIntent!!)
                .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaLibrarySession.release()
    }
}